package city.restaurant.eric;

import agent.Role;
import city.Place;
import city.interfaces.Person;
import city.restaurant.eric.interfaces.*;
import city.restaurant.eric.gui.EricWaiterGui;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class EricWaiterRole extends Role implements EricWaiter
{
	// ---------------------------------- DATA --------------------------------
	
	// State data:
	//TODO Will add WorkingState in when dealing with job switching.  This interaction will be very similar to the waiter going on break scenario.
	//protected enum WorkingState { AWAY, WORKING, WANT_TO_LEAVE, TOLD_HOST_WANT_TO_LEAVE, HOST_SAID_CANT_LEAVE }
	//protected WorkingState _workingState = WorkingState.AWAY;
	// BreakState is only relevant if _workingState == WorkingState.WORKING.
	protected enum BreakState { NOT_ON_BREAK, WANT_A_BREAK, TOLD_HOST_WANT_A_BREAK, HOST_SAID_NO_BREAK, HOST_SAID_GO_ON_BREAK, ON_BREAK, RETURNING_FROM_BREAK }
	protected BreakState _breakState = BreakState.NOT_ON_BREAK;
	
	// Correspondence:
	public EricWaiterGui _gui = null;
	protected EricHost _host = null;
	protected EricCook _cook;
	protected EricCashier _cashier;
	protected EricRestaurant _restaurant;
	
	// Semaphores:
	protected Semaphore _reachedDestination = new Semaphore(1, true); //TODO why is this initialized with a 1? Shouldn't it be a 0?
	protected Semaphore _customerOrdered = new Semaphore(0, true);
	
	// Agent data:
	// Customer:
	protected class MyCustomer
	{
		public EricCustomer agent;
		public int tableNumber = -1;
		public Order order = null;
		public CustomerState state;
		public Menu menu = null;
		public Check check = null;
	}
	protected enum CustomerState { WAITING_AT_FRONT, CHOOSING_ORDER, READY_TO_ORDER, ASKED_TO_ORDER, GAVE_ORDER, WAITING_FOR_FOOD, EATING, DONE_EATING, WAITING_FOR_CASHIER_TO_GIVE_ME_CHECK, CASHIER_GAVE_ME_CHECK, PAYING, LEFT }
	public List<MyCustomer> customers	= new ArrayList<MyCustomer>();
	// Order:
	protected class Order
	{
		public OrderState state;
		public String choice;
	}
	protected enum OrderState { TAKEN, GONE, COOKING, READY, AT_TABLE }
	// note: instead of a list of orders, we just iterate through the list of customers.
	
	
	
	// ---------------------------------------- CONSTRUCTOR & PROPERTIES --------------------------------------
	public EricWaiterRole(Person person, EricRestaurant restaurant)
	{
		super(person);
		
		_restaurant = restaurant;
	}
	public String name() { return _person.name(); }
	//public List getWaitingCustomers() { return waitingCustomers; }
	//public Collection getTables() { return tables; }
	public void setHost(EricHost host)
	{
		if(_host == null)
		{
			_host = host;
			_host.msgImOnDuty(this); //TODO should this be a thing?
		}
	}
	public void setCook(EricCook cook) { _cook = cook; }
	public void setCashier(EricCashier cashier) { _cashier = cashier; }
	public void setGui(EricWaiterGui gui) { _gui = gui; }
	public EricWaiterGui gui() { return _gui; }
	public boolean wantsBreak() { return _breakState == BreakState.WANT_A_BREAK || _breakState == BreakState.TOLD_HOST_WANT_A_BREAK || _breakState == BreakState.HOST_SAID_GO_ON_BREAK; }
	public boolean onBreak() { return _breakState == BreakState.ON_BREAK; }
	public Place place() { return _restaurant; }
	public boolean active() { return active; }
	
	
	
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
	
	public void msgSeatCustomer(EricCustomer ca, int tableNumber) // from Host
	{
		print(AlertTag.ERIC_RESTAURANT, _host.name() + " told me to seat " + ca.name());
		MyCustomer c = new MyCustomer();
		c.agent = ca;
		c.tableNumber = tableNumber;
		c.state = CustomerState.WAITING_AT_FRONT;
		customers.add(c);
		
		stateChanged();
	}
	
	public void msgReadyToOrder(EricCustomer sender)
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
	
	public void msgHeresMyChoice(EricCustomer sender, String choice)
	{
		print(AlertTag.ERIC_RESTAURANT, sender.name() + " chooses " + choice);
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
					print(AlertTag.ERIC_RESTAURANT,_cook.name() + " says that we're out of " + c.agent.name() + "'s order of " + c.order.choice);
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

	public void msgGiveMeCheck(EricCustomer sender) // from Customer
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
	
	public void msgLeaving(EricCustomer sender) // from Customer
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
		if(_breakState == BreakState.NOT_ON_BREAK)
		{
			_breakState = BreakState.WANT_A_BREAK;
			print(AlertTag.ERIC_RESTAURANT,"I want a break!");
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
		print(AlertTag.ERIC_RESTAURANT,"I will finish my customers then go on break");
		_breakState = BreakState.HOST_SAID_GO_ON_BREAK;
		stateChanged();
	}
	
	public void msgBreaksOver()
	{
		print(AlertTag.ERIC_RESTAURANT,"Break's over!");
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

	protected void actSeatCustomer(MyCustomer customer)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to front desk to get customer");
		_gui.doGoToFrontDesk();
		waitForGuiToReachDestination();
		
		print(AlertTag.ERIC_RESTAURANT,"Seating Customer " + customer.agent.name() + " at Table " + customer.tableNumber);
		
		// This order is important; i.e. setting customer.state to CHOOSING_ORDER first (fixes a concurrency clobbering error that arises from getting msgReadyToOrder from CustomerAgent before this line executes)
		customer.state = CustomerState.CHOOSING_ORDER;
		_gui.doBringCustomerToTable(customer.agent, customer.tableNumber);
		customer.menu = new Menu();
		customer.agent.msgFollowMeToTable(this, customer.menu);
		waitForGuiToReachDestination();
		
		_gui.doGoIdle();
	}
	
	protected void actTakeOrder(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to table " + c.tableNumber + " to take order from " + c.agent.name());
		
		_gui.doGoToTable(c.tableNumber);
		waitForGuiToReachDestination();
		
		c.agent.msgWhatDoYouWant();
		c.state = CustomerState.ASKED_TO_ORDER;
		
		try { _customerOrdered.acquire(); } catch(InterruptedException e) { e.printStackTrace(); }
		
		c.state = CustomerState.WAITING_FOR_FOOD;
		
		_gui.doGoIdle();
	}

	// Implemented differently for Normal waiter and SharedData waiter
	protected abstract void actGiveOrderToCook(MyCustomer c);
	
	protected void actTellCustomerOutOfChoice(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to tell Customer " + c.agent.name() + " that we're out of his/her order of " + c.order.choice);
		
		_gui.doGoToTable(c.tableNumber);
		waitForGuiToReachDestination();
		
		c.menu.removeEntree(c.order.choice);
		c.agent.msgOutOfChoice(c.menu);
		
		c.order = null;
		
		c.state = CustomerState.CHOOSING_ORDER;
	}

	protected void actBringFoodToCustomer(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to cook to pick up " + c.agent.name() + "'s order of " + c.order.choice);
		_gui.doGoToCook();
		waitForGuiToReachDestination();
		
		print(AlertTag.ERIC_RESTAURANT,"Delivering " + c.agent.name() + "'s order of " + c.order.choice);
		
		_gui.doDeliverFood(c.tableNumber, c.order.choice);
		waitForGuiToReachDestination();
		print(AlertTag.ERIC_RESTAURANT,"Giving " + c.order.choice + " to " + c.agent.name());
		
		c.agent.msgHeresYourFood(c.order.choice);
		c.order.state = OrderState.AT_TABLE;
		c.state = CustomerState.EATING;
		
		_gui.doGoIdle();
	}
	
	protected void actGetCheckForCustomer(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to get check for Customer " + c.agent.name());
		_gui.doGoToCashier();
		waitForGuiToReachDestination();
		
		print(AlertTag.ERIC_RESTAURANT,"Requesting check from " + _cashier.name());
		_cashier.msgGiveMeCheck(this, c.order.choice, c.tableNumber);
		
		c.state = CustomerState.WAITING_FOR_CASHIER_TO_GIVE_ME_CHECK;
		
		_gui.doGoIdle();
	}

	protected void actBringCheckToCustomer(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to bring check to Customer " + c.agent.name());
		
		_gui.doBringCheckToCustomer(c.agent, c.tableNumber);
		waitForGuiToReachDestination();

		print(AlertTag.ERIC_RESTAURANT,"Giving check to Customer " + c.agent.name());
		
		c.agent.msgHeresYourCheck(c.check, _cashier);
		
		c.state = CustomerState.PAYING;
		
		_gui.doGoIdle();
	}
	
	protected void actCustomerLeft(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Cleaning table number " + c.tableNumber);
		_gui.doCleanTable(c.tableNumber);
		waitForGuiToReachDestination();
		// Might have to add another semaphore or something here if there's a cleaning animation
		
		print(AlertTag.ERIC_RESTAURANT,"Done cleaning table");
		
		_gui.doGoIdle();
		
		print(AlertTag.ERIC_RESTAURANT,"Notifying " + _host.name() + " that table number " + c.tableNumber + " is now free.");
		_host.msgTableFree(this, c.tableNumber);
		customers.remove(c);
	}
	
	protected void actTellHostIWantABreak()
	{
		print(AlertTag.ERIC_RESTAURANT,"Telling host I want a break.");
		_host.msgIWantABreak(this);
		_breakState = BreakState.TOLD_HOST_WANT_A_BREAK;
	}

	protected void actNoBreak()
	{
		print(AlertTag.ERIC_RESTAURANT,"Not going on break.");
		_breakState = BreakState.NOT_ON_BREAK;
		_gui.doNoBreak();
	}
	
	protected void actGoOnBreak()
	{
		print(AlertTag.ERIC_RESTAURANT,"Going on break.");
		_breakState = BreakState.ON_BREAK;
		_gui.doGoOnBreak();
	}
	
	protected void actBackToWork()
	{
		print(AlertTag.ERIC_RESTAURANT,"Going back to work.");
		_gui.doBackToWork();
		_host.msgGoingBackToWork(this);
		_breakState = BreakState.NOT_ON_BREAK;
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

