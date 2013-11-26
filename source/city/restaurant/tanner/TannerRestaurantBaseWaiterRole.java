package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.MyCustomer.customerState;
import city.restaurant.tanner.gui.TannerRestaurantWaiterRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public abstract class TannerRestaurantBaseWaiterRole extends Role implements TannerRestaurantWaiter 
{



//--------------------------------------------Data---------------------------------------------------------
	ArrayList<MyCustomer> myCustomers;
	String name_;
	Semaphore doingAction;
	boolean onBreak;
	boolean wantBreak;
	int[] menu;
	TannerRestaurantHost host;
	TannerRestaurantCook cook;
	TannerRestaurantCashier cashier;
	TannerRestaurantWaiterRoleGui myGUI;
	TannerRestaurant restaurant;
	Timer breakTimer;
	public utilities.EventLog log;
	
//----------------------------------------Constructor------------------------------------------------------
	public TannerRestaurantBaseWaiterRole(PersonAgent person, TannerRestaurant rest, String name) 
	{
		super(person);
		name_ = name;
		myCustomers = new ArrayList<MyCustomer>();
		doingAction = new Semaphore(0, true);
		onBreak = false;
		menu = new int[]{1, 2, 3, 4};
		log = new utilities.EventLog();
		
	}

//-----------------------------------------Accessors---------------------------------------------------------

	@Override
	public Place place()
	{
		return this.restaurant;		
	}
	
	public void setHost(TannerRestaurantHost h)
	{
		this.host = h;
	}
	
	public void setCook(TannerRestaurantCook c)
	{
		this.cook = c;
	}
	
	public void setCashier(TannerRestaurantCashier c)
	{
		this.cashier = c;
	}
	
	public void setGui(TannerRestaurantWaiterRoleGui g)
	{
		this.myGUI = g;
	}
//------------------------------------------Messages---------------------------------------------------------
	
	@Override
	public void msgHereIsANewCustomer(TannerRestaurantCustomer c, int tableNumber, TannerRestaurantHost h, TannerRestaurantCook co) 
	{
		MyCustomer mc = new MyCustomer(tableNumber, c, restaurant);
		myCustomers.add(mc);
		host = h;
		cook = co;
		mc.currentState = customerState.waitingToBeSeated;
		stateChanged();
	}

	@Override
	public void msgImReadyToOrder(TannerRestaurantCustomer c) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
		}
		myCustomers.get(index).currentState = customerState.waitingToOrder;
		stateChanged();
	}

	@Override
	public void msgHereIsMyOrder(TannerRestaurantCustomer c, int choice)
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
			
		}
		myCustomers.get(index).order = choice;
		myCustomers.get(index).currentState = customerState.orderPending;
		stateChanged();
	}

	@Override
	public void msgICantAffordAnything(TannerRestaurantCustomer c) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
			
		}
		myCustomers.get(index).currentState = customerState.leaving;
		stateChanged();
	}

	@Override
	public void msgThatChoiceIsOutOfStock(int choice, int tableNumber) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).tableNumber == tableNumber)
			{
				index = i;
				break;
			}
			
		}
		ArrayList<Integer> newMenu = new ArrayList<Integer>();
		for(int i = 0; i < myCustomers.get(index).menu.size(); i++)
		{
			if(myCustomers.get(index).menu.get(i) == choice)
			{
				continue;
			}
			else
			{
				newMenu.add(myCustomers.get(i).menu.get(i));
			}
		}
		myCustomers.get(index).currentState = customerState.waitingToReorder;
		stateChanged();
	}

	@Override
	public void msgHereIsMyReOrder(TannerRestaurantCustomer c, int choice) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
			
		}
		myCustomers.get(index).order = choice;
		myCustomers.get(index).currentState = customerState.orderPending;
		stateChanged();
	}

	@Override
	public void msgOrderIsReady(int choice, int tableNumber) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).table == restaurant.tableMap.get(tableNumber))
			{
				index = i;
				break;
			}
		}
		myCustomers.get(index).currentState = customerState.orderReady;
		stateChanged();
	}

	@Override
	public void msgIWouldLikeTheCheck(TannerRestaurantCustomer c, int choice) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).currentState == customerState.eating)
			{
				index = i;
				break;
			}
		}
		myCustomers.get(index).currentState = customerState.waitingForCheck;
		stateChanged();
	}

	@Override
	public void msgHereIsTheChek(double amount, TannerRestaurantCustomer c) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
		}
		myCustomers.get(index).billAmount = amount;
		myCustomers.get(index).currentState = customerState.paying;
		stateChanged();	
	}

	@Override
	public void msgGoodBye(TannerRestaurantCustomer c) 
	{
		int index = 0;
		for(int i = 0; i < myCustomers.size(); i++)
		{
			if(myCustomers.get(i).customer == c)
			{
				index = i;
				break;
			}
		}
		myCustomers.get(index).currentState = customerState.leaving;
		stateChanged();
	}
	
//-----------------------------------------Scheduler---------------------------------------------------------
	
	@Override
	public boolean pickAndExecuteAnAction() 
	{	
		try {
			if(wantBreak)
			{
				AskForBreak();
				return true;
			}
			
			if(myCustomers.size() >= 1)
			{
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.waitingToBeSeated)
					{
						SeatNewCustomer(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.waitingToOrder)
					{
						GetCustomerOrder(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.waitingToReorder)
					{
						GetCustomerReorder(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.ordering)
					{
						TakeOrder(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.reordering)
					{
						TakeReorder(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.orderPending)
					{
						SubmitOrder(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.orderIn)
					{
						GiveOrderToCook(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.orderReady)
					{
						BringFood(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.recievedFood)
					{
						GiveOrderToCustomer(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.waitingForCheck)
					{
						GoGetCheck(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.paying)
					{
						BringCheckToCustomer(myCustomers.get(i));
						return true;
					}
				}
				
				for(int i = 0; i < myCustomers.size(); i++)
				{
					if(myCustomers.get(i).currentState == customerState.leaving)
					{
						ClearTable(myCustomers.get(i));
						return true;
					}
				}
			}
			else
			{
				if(onBreak)
				{
					TakeBreak();
					return false;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		return false;
	}
	
//-----------------------------------------Actions-----------------------------------------------------------

	private void SeatNewCustomer(MyCustomer c)
	{
		print("Seating new customer");
		myGUI.DoGoToFront();
		try {
			doingAction.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.customer.msgFollowMeToTable(c.tableNumber, c.menu, this);
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.currentState = customerState.lookingAtMenu;
		
	}
	
	private void GetCustomerOrder(MyCustomer c)
	{
		print("Getting customers order");
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.currentState = customerState.ordering;	
	}
	
	private void GetCustomerReorder(MyCustomer c)
	{
		print("Get customers reorder");
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.currentState = customerState.reordering;
	}
	
	private void TakeOrder(MyCustomer c)
	{
		c.customer.msgWhatWouldYouLike();
	}
	
	private void TakeReorder(MyCustomer c)
	{
		c.customer.msgYourChoiceIsOutOfStock(c.menu);
	}
	
	
	protected abstract void SubmitOrder(MyCustomer c);
	
	private void GiveOrderToCook(MyCustomer c)
	{
		print("Give order to cook");
		cook.msgHereIsANewOrder(c.order, c.tableNumber, this);
		c.currentState = customerState.eating;
	}
	
	private void BringFood(MyCustomer c)
	{
		print("Bring food to Customers");
		myGUI.DoGoToCook(cook.getPosition());
		try {
			doingAction.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.currentState = customerState.recievedFood;
	}
	
	private void GiveOrderToCustomer(MyCustomer c)
	{
		c.currentState = customerState.eating;
		c.customer.msgHereIsYourFood(c.order);
	}
	
	private void GoGetCheck(MyCustomer c)
	{
		print("Going to answer call for check");
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Going to get check from front");
		myGUI.DoGoToFront();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.msgComputeBill(c.order, c.customer, this);
		c.currentState = customerState.checkComputing;
	}
	
	private void BringCheckToCustomer(MyCustomer c)
	{
		print("Bringing check to customer");
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.currentState = customerState.paid;
		c.customer.msgHereIsYourCheck(c.billAmount);
	}
	
	private void ClearTable(MyCustomer c)
	{
		print("Clearing table");
		myGUI.DoGoToTable(c.tableNumber);
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgTableIsFree(c.tableNumber, this);
		myCustomers.remove(c);
	}
	
	private void AskForBreak()
	{
		myGUI.DoGoToFront();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Ask For break");
		//((TannerRestaurantHost) host).msgIWantToGoOnBreak(this);
		wantBreak = false;
	}
		
	private void TakeBreak()
	{
		myGUI.DoGoToFront();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		breakTimer.schedule(new TimerTask() {
			public void run()
			{
				GoBackOnDuty();
			}
		}, 60000);
	}
	
	private void GoBackOnDuty()
	{
		print("Back on Duty");
		onBreak = false;
		wantBreak = false;
		//((TannerRestaurantHost) host).msgBackOnDuty(this);
		//myGUI.setOnBreak(false);
	}
	
//----------------------------------------Commands-----------------------------------------------------------

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}
}
	
//---------------------------------------Utilities-----------------------------------------------------------

	class MyCustomer
	{
		TannerRestaurant restaurant;
		TannerRestaurantCustomer customer;
		Table table;
		int tableNumber;
		int order;
		ArrayList<Integer> menu;
		double billAmount;
		public enum customerState
		{
			none, checkingWaitTime, waitingToBeSeated, lookingAtMenu, waitingToOrder, waitingToReorder, ordering, reordering, orderPending, orderIn, orderReady, recievedFood, eating, waitingForCheck, checkComputing, paying, paid, leaving
		}
		customerState currentState;
		
		public MyCustomer(int tn, TannerRestaurantCustomer c, TannerRestaurant r)
		{
			restaurant = r;
			tableNumber = tn;
			table = restaurant.tableMap.get(tn);
			customer = c;
			menu = new ArrayList<Integer>();
			for(int i = 1; i <= 4; i++)
			{
				menu.add(i);
			}
			currentState = customerState.none;
		}
	}
	
