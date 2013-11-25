package city.restaurant.yixin;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.restaurant.yixin.gui.YixinWaiterGui;

public abstract class YixinWaiterRole extends Role {//implements Waiter{
	public YixinRestaurant restaurant;
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public YixinCookRole cook = null;
	public YixinHostRole host = null;
	public YixinCashierRole cashier = null;
	public YixinWaiterGui waiterGui = null;
	private Semaphore atTable = new Semaphore(0,true);
	//The four booleans below are for Gui purposes, they have NOTHING to do with agent design
	boolean breakRequest = false, backRequest = false;//Two booleans from gui to tell whether to go on break or to 
	boolean	OnBreak = false, breakEnabled = true; //Two booleans to tell gui what to show and whether to enable
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	private String name;

	public YixinWaiterRole(PersonAgent p, YixinRestaurant r, String name) {
		super(p);
		this.restaurant = r;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean getBreakStatus(){
		return OnBreak;
	}

	public boolean getBreakEnable(){
		return breakEnabled;
	}

	public void setHost(YixinHostRole h){
		this.host = h;
	}

	public void setCook(YixinCookRole c){
		this.cook = c;
	}

	public void setCashier(YixinCashierRole c){
		this.cashier = c;
	}

	public void releaseSemaphore(){
		atTable.release();
		stateChanged();
	}

	// Messages

	public void msgSitAtTable(YixinCustomerRole cust, int tablenumber, int count) {
		customers.add(new MyCustomer(cust, tablenumber, MyCustomer.CustomerState.waiting, count));
		stateChanged();
	}

	public void msgNoMoneyAndLeaving(YixinCustomerRole cust){
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.state = MyCustomer.CustomerState.noMoney;
				stateChanged();
			}
		}
	}

	public void msgReadyToOrder(YixinCustomerRole cust) {
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.state = MyCustomer.CustomerState.readyToOrder;
				stateChanged();
			}
		}
	}

	public void msgHereIsTheChoice(YixinCustomerRole cust, String choice) {
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.choice = choice;
				c.state = MyCustomer.CustomerState.orderGiven;
				atTable.release();
				stateChanged();
				return;
			}
		}
	}

	public void msgOrderIsReady(String choice, int tableNumber) {
		for (MyCustomer c: customers) {
			if (c.tableNumber == tableNumber) {
				c.state = MyCustomer.CustomerState.orderReady;
				stateChanged();
			}
		}
	}

	public void msgFoodRunsOut(String choice, int tableNumber) {
		for (MyCustomer c: customers) {
			if (c.tableNumber == tableNumber) {
				print("Got msg " + choice + " is running out.");
				c.state = MyCustomer.CustomerState.noFood;
				stateChanged();
			}
		}
	}

	public void msgDoneEating(YixinCustomerRole cust) {
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.state = MyCustomer.CustomerState.finishedEating;
				stateChanged();
			}
		}
	}

	public void msgHereIsTheCheck(double money, YixinCustomerRole cust){
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.state = MyCustomer.CustomerState.checkComputed;
				c.check = money;
				stateChanged();
			}
		}
	}

	public void msgLeavingRestaurant(YixinCustomerRole cust){
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.state = MyCustomer.CustomerState.leaving;
				stateChanged();
			}
		}
	}

	public void msgAskForBreak(){
		breakRequest = true;
		OnBreak = true;// for gui purpose
		breakEnabled = false;//for gui purpose
		stateChanged();
	}

	public void msgBreakGranted(){
		breakEnabled = true;//for gui purpose
		print("Break request granted.");
		stateChanged();
	}

	public void msgAskToComeBack(){
		backRequest = true;
		OnBreak = false; // for gui purpose
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			if (breakRequest){
				print("Tell host to break");
				host.msgWantToBreak(this);
				breakRequest = false;
				return true;
			}
			if (backRequest){
				print("Tell host I'm coming back");
				host.msgWantToComeBack(this);
				backRequest = false;
				return true;
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.finishedEating) {
					computeBill(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.noMoney) {
					clearCustomer(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.checkComputed) {
					giveCheck(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.leaving) {
					clearCustomer(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.waiting) {
					seatCustomer(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.readyToOrder){
					askForChoice(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.noFood){
					giveNewMenu(customer);
					return true;
				}
			}		
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.orderGiven) {
					processOrder(customer);
					return true;
				}
			}
			for (MyCustomer customer : customers) {
				if (customer.state == MyCustomer.CustomerState.orderReady) {
					giveOrderToCustomer(customer);
					return true;
				}
			}
			if (customers.size() == 0 && role_state == RoleState.WantToLeave){
				LeaveRestaurant();
				role_state = RoleState.none;
				active = false;
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			return false;
		}
		DoGoHome();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer customer) {
		waiterGui.DoFetchCustomer(customer.count);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = MyCustomer.CustomerState.none;
		customer.c.msgFollowMe(this, customer.tableNumber, new Menu());
		DoSeatCustomer(customer.c, customer.tableNumber);
	}

	private void askForChoice(MyCustomer customer){
		customer.state = MyCustomer.CustomerState.none;	
		DoGoToCustomer(customer.c, customer.tableNumber);
		customer.c.msgWhatWouldYouLike();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void giveNewMenu(MyCustomer customer){
		print("give new menu");
		DoGoToCustomer(customer.c, customer.tableNumber);
		customer.state = MyCustomer.CustomerState.none;
		Menu m = new Menu();
		m.remove(customer.choice);
		customer.c.msgNoFood(m);
	}

	protected abstract void processOrder(MyCustomer customer);

	private void giveOrderToCustomer(MyCustomer customer){
		DoFetchPlate();
		print("Give order to customer");
		customer.state = MyCustomer.CustomerState.none;
		DoGiveFoodToCustomer(customer.c, customer.tableNumber, customer.choice);
		customer.c.msgHereIsYourFood(customer.choice);
	}

	private void computeBill(MyCustomer customer){
		print("Ask Cashier to compute bill");
		cashier.msgComputeBill(this, customer.c, customer.choice);
		customer.state = MyCustomer.CustomerState.none;
	}

	private void giveCheck(MyCustomer customer){
		print("Give Customer the bill");
		customer.c.msgHereIsTheCheck(customer.check, cashier);
		customer.state = MyCustomer.CustomerState.none;
	}

	private void clearCustomer(MyCustomer customer){
		print("Clear customer");
		host.msgTableIsFree(customer.c, customer.tableNumber);
		customers.remove(customer);
	}

	private void DoSeatCustomer(YixinCustomerRole customer, int table){
		print("Seating " + customer + " at " + table);
		waiterGui.DoGoToTable(table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGoToCustomer(YixinCustomerRole customer, int table){
		print("Going to " + customer + " at " + table);
		waiterGui.DoGoToTable(table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGoHome(){
		waiterGui.DoLeaveCustomer();
	}

	protected void DoGoToCook(){
		print("Going to cook");
		waiterGui.DoGoToCook();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void DoFetchPlate(){
		print("Fetching the food.");
		waiterGui.DoFetchDish();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void DoGoToRevolvingStand(){
		print("Putting on the revolving stand.");
		waiterGui.DoGoToRevolvingStand();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGiveFoodToCustomer(YixinCustomerRole customer, int table, String food){
		print("Giving food to " + customer + " at " + table);
		waiterGui.DoBringFood(table, food);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void LeaveRestaurant(){
		waiterGui.LeaveRestaurant();
		try{
			atTable.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//utilities

	public void setGui(YixinWaiterGui gui) {
		waiterGui = gui;
	}

	public YixinWaiterGui getGui() {
		return waiterGui;
	}

	public static class MyCustomer {
		YixinCustomerRole c;
		int tableNumber, count;
		String choice = "";
		double check = 0;
		public enum CustomerState{none, waiting, noMoney, readyToOrder, 
			orderGiven, orderReady, noFood, finishedEating, checkComputed, leaving}
		public CustomerState state = CustomerState.none;

		public MyCustomer(YixinCustomerRole c, int tableNumber, CustomerState s, int count) {
			this.c = c;
			this.tableNumber = tableNumber;
			this.state = s;
			this.count = count;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}

