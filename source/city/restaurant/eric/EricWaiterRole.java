package city.restaurant.eric;

import agent.Role;
import city.Place;
import city.interfaces.Person;
import city.restaurant.eric.interfaces.*;
import city.restaurant.eric.gui.EricWaiterGui;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

public class EricWaiterRole extends Role implements Waiter
{
	// ---------------------------------- DATA --------------------------------
	
	// Personal data:
	private String _name;
	private enum BreakState { WORKING, WANT_A_BREAK, TOLD_HOST_WANT_A_BREAK, HOST_SAID_NO_BREAK, HOST_SAID_GO_ON_BREAK, ON_BREAK, RETURNING_FROM_BREAK }
	private BreakState _breakState = BreakState.WORKING;
	
	// Correspondence:
	public EricWaiterGui _gui = null;
	private Host _host = null;
	private Cook _cook;
	private Cashier _cashier;
	private EricRestaurant _restaurant;
	
	// Semaphores:
	private Semaphore _reachedDestination = new Semaphore(1, true); //TODO why is this initialized with a 1? Shouldn't it be a 0?
	private Semaphore _customerOrdered = new Semaphore(0, true);
	
	// Agent data:
	// Customer:
	private class MyCustomer
	{
		public Customer agent;
		public int tableNumber = -1;
		public Order order = null;
		public CustomerState state;
		public Menu menu = null;
		public Check check = null;
	}
	private enum CustomerState { WAITING_AT_FRONT, CHOOSING_ORDER, READY_TO_ORDER, ASKED_TO_ORDER, GAVE_ORDER, WAITING_FOR_FOOD, EATING, DONE_EATING, WAITING_FOR_CASHIER_TO_GIVE_ME_CHECK, CASHIER_GAVE_ME_CHECK, PAYING, LEFT }
	public List<MyCustomer> customers	= new ArrayList<MyCustomer>();
	// Order:
	private class Order
	{
		public OrderState state;
		public String choice;
	}
	private enum OrderState { TAKEN, GONE, COOKING, READY, AT_TABLE }
	// note: instead of a list of orders, we just iterate through the list of customers.
	
	// ---------------------------------------- CONSTRUCTOR & PROPERTIES --------------------------------------
	public EricWaiterRole(Person person, String name, EricRestaurant restaurant)
	{
		super(person);
		
		_name = name;
		_restaurant = restaurant;
	}
	public String getName() { return _name; }
	//public List getWaitingCustomers() { return waitingCustomers; }
	//public Collection getTables() { return tables; }
	public void setHost(Host host)
	{
		if(_host == null)
		{
			_host = host;
			_host.msgImOnDuty(this);
		}
	}
	public void setCook(Cook cook) { _cook = cook; }
	public void setCashier(Cashier cashier) { _cashier = cashier; }
	public void setGui(EricWaiterGui gui) { _gui = gui; }
	public EricWaiterGui gui() { return _gui; }
	public boolean wantsBreak() { return _breakState == BreakState.WANT_A_BREAK || _breakState == BreakState.TOLD_HOST_WANT_A_BREAK || _breakState == BreakState.HOST_SAID_GO_ON_BREAK; }
	public boolean onBreak() { return _breakState == BreakState.ON_BREAK; }
	public Place place() { return _restaurant; }
	
	
	
	// ---------------------------------- COMMANDS & MESSAGES -------------------------------------------

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
	}

	// This function is called multiple times, thus it's at the top
	public void msgReachedDestination() //from WaiterGui
	{
		_reachedDestination.release();
		// stateChanged(); // not necessary because this is just to release the semaphore.
	}
	
	public void msgSeatCustomer(Customer ca, int tableNumber) // from Host
	{
		print(_host.getName() + " told me to seat " + ca.getName());
		MyCustomer c = new MyCustomer();
		c.agent = ca;
		c.tableNumber = tableNumber;
		c.state = CustomerState.WAITING_AT_FRONT;
		customers.add(c);
		
		stateChanged();
	}
	
	public void msgReadyToOrder(Customer sender)
	{
		for(MyCustomer c : customers)
		{
			if(c.agent == sender)
			{
				c.state = CustomerState.READY_TO_ORDER;
				stateChanged();
			}
		}
	}
	
	public void msgHeresMyChoice(Customer sender, String choice)
	{
		print(sender.getName() + " chooses " + choice);
		for(MyCustomer c : customers)
		{
			if(c.agent == sender)
			{
				c.order = new Order();
				c.order.state = OrderState.TAKEN;
				c.order.choice = choice;
				c.state = CustomerState.GAVE_ORDER;
				_customerOrdered.release();
				// no stateChanged because this is just to call the customerOrdered.release().
			}
		}
	}
	
	public void msgOutOfOrder(String choice, int tableNumber) // from Cook
	{
		for(MyCustomer c : customers)
		{
			if(c.tableNumber == tableNumber && c.order != null)
			{
				if(c.order.choice.equals(choice))
				{
					print(_cook.getName() + " says that we're out of " + c.agent.getName() + "'s order of " + c.order.choice);
					c.order.state = OrderState.GONE;
					stateChanged();
				}
			}
		}
	}
	
	public void msgOrderReady(String choice, int tableNumber) // from Cook
	{
		for(MyCustomer c : customers)
		{
			if(c.tableNumber == tableNumber && c.order != null)
			{
				if(c.order.choice.equals(choice))
				{
					c.order.state = OrderState.READY;
					stateChanged();
				}
			}
		}
	}

	public void msgGiveMeCheck(Customer sender) // from Customer
	{
		for (MyCustomer c : customers)
		{
			if(c.agent == sender)
			{
				c.state = CustomerState.DONE_EATING;
				stateChanged();
			}
		}
	}

	public void msgHereIsCheck(Check ch) // from Cashier
	{
		for(MyCustomer c : customers)
		{
			if(c.tableNumber == ch.table())
			{
				c.check = ch;
				c.state = CustomerState.CASHIER_GAVE_ME_CHECK;
				stateChanged();
			}
		}
	}
	
	public void msgLeaving(Customer sender) // from Customer
	{
		for (MyCustomer c : customers)
		{
			if(c.agent == sender)
			{
				c.state = CustomerState.LEFT;
				stateChanged();
			}
		}
	}
	
	// Going on break stuff:
	public void msgWantABreak() // from RestaurantGui/RestaurantPanel
	{
		if(_breakState == BreakState.WORKING)
		{
			_breakState = BreakState.WANT_A_BREAK;
			print("I want a break!");
			stateChanged();
		}
	}
	
	public void msgNoBreak()
	{
		_breakState = BreakState.HOST_SAID_NO_BREAK;
		stateChanged();
	}
	
	public void msgFinishCustomersGoOnBreak()
	{
		print("I will finish my customers then go on break");
		_breakState = BreakState.HOST_SAID_GO_ON_BREAK;
		stateChanged();
	}
	
	public void msgBreaksOver()
	{
		print("Break's over!");
		_breakState = BreakState.RETURNING_FROM_BREAK;
		stateChanged();
	}
	
	
	
	// ---------------------------------- SCHEDULER -------------------------------
	
	public boolean pickAndExecuteAnAction()
	{
		try
		{
			if(_breakState == BreakState.ON_BREAK) {
				// do nothing, cause you're on break.
				return false;
			}
			if(_breakState == BreakState.WANT_A_BREAK) {
				actTellHostIWantABreak();
				return true;
			}
			if(_breakState == BreakState.HOST_SAID_NO_BREAK) {
				actNoBreak();
				return true;
			}
			if(_breakState == BreakState.HOST_SAID_GO_ON_BREAK && customers.isEmpty()) {
				actGoOnBreak();
				return true;
			}
			if(_breakState == BreakState.RETURNING_FROM_BREAK) {
				actBackToWork();
				return true;
			}
			for(MyCustomer c : customers) {
				if(c.state == CustomerState.WAITING_AT_FRONT) {
					actSeatCustomer(c);
					return true;
				}
			}
			for(MyCustomer c : customers) {
				if(c.order != null) {
					if(c.order.state == OrderState.GONE) {
						actTellCustomerOutOfChoice(c);
						return true;
					}
				}
			}
			for(MyCustomer c : customers) {
				if(c.order != null) {
					if(c.order.state == OrderState.READY) {
						actBringFoodToCustomer(c);
						return true;
					}
				}
			}
			for(MyCustomer c : customers) {
				if(c.order != null) {
					if(c.order.state == OrderState.TAKEN) {
						actGiveOrderToCook(c);
						return true;
					}
				}
			}
			for(MyCustomer c : customers) {
				if(c.state == CustomerState.CASHIER_GAVE_ME_CHECK) {
					actBringCheckToCustomer(c);
					return true;
				}
			}
			for(MyCustomer c : customers) {
				if(c.state == CustomerState.READY_TO_ORDER) {
					actTakeOrder(c);
					return true;
				}
			}
			for(MyCustomer c : customers) {
				if(c.state == CustomerState.DONE_EATING) {
					actGetCheckForCustomer(c);
					return true;
				}
			}
			for(MyCustomer c : customers) {
				if(c.state == CustomerState.LEFT) {
					actCustomerLeft(c);
					return true;
				}
			}
			return false;
		}
		catch(ConcurrentModificationException e) //TODO change to synchronized lists, those are better.
		{ return true; }
		// note: return true because if the ConcurrentModificationException results from an action modifying a list,
		// stateChanged won't have been called and therefore the scheduler won't re-run.
	}

	// ------------------------------------------ ACTIONS ---------------------------------------------------

	private void actSeatCustomer(MyCustomer customer)
	{
		print("Going to front desk to get customer");
		_gui.doGoToFrontDesk();
		waitForGuiToReachDestination();
		
		print("Seating Customer " + customer.agent.getName() + " at Table " + customer.tableNumber);
		
		// This order is important; i.e. setting customer.state to CHOOSING_ORDER first (fixes a concurrency clobbering error that arises from getting msgReadyToOrder from CustomerAgent before this line executes)
		customer.state = CustomerState.CHOOSING_ORDER;
		_gui.doBringCustomerToTable(customer.agent, customer.tableNumber);
		customer.menu = new Menu();
		customer.agent.msgFollowMeToTable(this, customer.menu);
		waitForGuiToReachDestination();
		
		_gui.doGoIdle();
	}
	
	private void actTakeOrder(MyCustomer c)
	{
		print("Going to table " + c.tableNumber + " to take order from " + c.agent.getName());
		
		_gui.doGoToTable(c.tableNumber);
		waitForGuiToReachDestination();
		
		c.agent.msgWhatDoYouWant();
		c.state = CustomerState.ASKED_TO_ORDER;
		
		try { _customerOrdered.acquire(); } catch(InterruptedException e) { e.printStackTrace(); }
		
		c.state = CustomerState.WAITING_FOR_FOOD;
		
		_gui.doGoIdle();
	}

	private void actGiveOrderToCook(MyCustomer c)
	{
		print("Going to cook to give Customer " + c.agent.getName() + "'s order");
		_gui.doTakeOrderToCook(c.order.choice);
		waitForGuiToReachDestination();
		
		print("Giving order of " + c.order.choice + " to cook.");
		
		_cook.msgHereIsOrder(this, c.order.choice, c.tableNumber);
		c.order.state = OrderState.COOKING;
		
		_gui.doGoIdle();
	}
	
	private void actTellCustomerOutOfChoice(MyCustomer c)
	{
		print("Going to tell Customer " + c.agent.getName() + " that we're out of his/her order of " + c.order.choice);
		
		_gui.doGoToTable(c.tableNumber);
		waitForGuiToReachDestination();
		
		c.menu.removeEntree(c.order.choice);
		c.agent.msgOutOfChoice(c.menu);
		
		c.order = null;
		
		c.state = CustomerState.CHOOSING_ORDER;
	}

	private void actBringFoodToCustomer(MyCustomer c)
	{
		print("Going to cook to pick up " + c.agent.getName() + "'s order of " + c.order.choice);
		_gui.doGoToCook();
		waitForGuiToReachDestination();
		
		print("Delivering " + c.agent.getName() + "'s order of " + c.order.choice);
		
		_gui.doDeliverFood(c.tableNumber, c.order.choice);
		waitForGuiToReachDestination();
		print("Giving " + c.order.choice + " to " + c.agent.getName());
		
		c.agent.msgHeresYourFood(c.order.choice);
		c.order.state = OrderState.AT_TABLE;
		c.state = CustomerState.EATING;
		
		_gui.doGoIdle();
	}
	
	private void actGetCheckForCustomer(MyCustomer c)
	{
		print("Going to get check for Customer " + c.agent.getName());
		_gui.doGoToCashier();
		waitForGuiToReachDestination();
		
		print("Requesting check from " + _cashier.getName());
		_cashier.msgGiveMeCheck(this, c.order.choice, c.tableNumber);
		
		c.state = CustomerState.WAITING_FOR_CASHIER_TO_GIVE_ME_CHECK;
		
		_gui.doGoIdle();
	}

	private void actBringCheckToCustomer(MyCustomer c)
	{
		print("Going to bring check to Customer " + c.agent.getName());
		
		_gui.doBringCheckToCustomer(c.agent, c.tableNumber);
		waitForGuiToReachDestination();

		print("Giving check to Customer " + c.agent.getName());
		
		c.agent.msgHeresYourCheck(c.check, _cashier);
		
		c.state = CustomerState.PAYING;
		
		_gui.doGoIdle();
	}
	
	private void actCustomerLeft(MyCustomer c)
	{
		print("Cleaning table number " + c.tableNumber);
		_gui.doCleanTable(c.tableNumber);
		waitForGuiToReachDestination();
		// Might have to add another semaphore or something here if there's a cleaning animation
		
		print("Done cleaning table");
		
		_gui.doGoIdle();
		
		print("Notifying " + _host.getName() + " that table number " + c.tableNumber + " is now free.");
		_host.msgTableFree(this, c.tableNumber);
		customers.remove(c);
	}
	
	private void actTellHostIWantABreak()
	{
		print("Telling host I want a break.");
		_host.msgIWantABreak(this);
		_breakState = BreakState.TOLD_HOST_WANT_A_BREAK;
	}

	private void actNoBreak()
	{
		print("Not going on break.");
		_breakState = BreakState.WORKING;
		_gui.doNoBreak();
	}
	
	private void actGoOnBreak()
	{
		print("Going on break.");
		_breakState = BreakState.ON_BREAK;
		_gui.doGoOnBreak();
	}
	
	private void actBackToWork()
	{
		print("Going back to work.");
		_gui.doBackToWork();
		_host.msgGoingBackToWork(this);
		_breakState = BreakState.WORKING;
	}
	
	
	
	// -------------------------------------- HELPERS ---------------------------------------------
	void waitForGuiToReachDestination()
	{
		try
		{
			_reachedDestination.acquire();
		}
		catch (InterruptedException e) { e.printStackTrace(); }
	}
}

