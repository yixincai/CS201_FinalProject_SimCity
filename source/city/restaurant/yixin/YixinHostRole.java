package city.restaurant.yixin;

import agent.Role;

import java.util.*;

import city.PersonAgent;
import city.Place;
import city.restaurant.yixin.gui.YixinHostGui;
import utilities.EventLog;

/**
 * Restaurant Host Agent
 */

public class YixinHostRole extends Role {//implements Host{
	public YixinRestaurant restaurant;
	public EventLog log = new EventLog();
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Collection<Table> tables;
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>()); 
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	
	private String name;
	int waiterNumber = 0;

	public YixinHostGui hostGui = null;

	public YixinHostRole(PersonAgent p, YixinRestaurant r, String name) {
		super(p);
		this.restaurant = r;
		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	
	public void addWaiter(YixinWaiterRole w){
		waiters.add(new MyWaiter(w));
		stateChanged();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void msgIWantFood(YixinCustomerRole cust, int count) {
		synchronized(waitingCustomers){
			waitingCustomers.add(new MyCustomer(cust, count));
			print("Got customer " + waitingCustomers.size());
			stateChanged();
		}
	}

	public void msgIAmLeaving(YixinCustomerRole cust) {
		synchronized(waitingCustomers){
			for (MyCustomer customer : waitingCustomers) {
				if (customer.customer == cust) {
					print(cust + " want to leave");
					waitingCustomers.remove(customer);
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgIWantToStay(YixinCustomerRole cust) {
		synchronized(waitingCustomers){
			for (MyCustomer customer : waitingCustomers) {
				if (customer.customer == cust) {
					print(cust + " want to stay");
					customer.state = MyCustomer.CustomerState.staying;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgTableIsFree(YixinCustomerRole cust, int tablenumber) {
		synchronized(tables){
			for (Table table : tables) {
				if (table.tableNumber == tablenumber) {
					print(cust + " leaving " + table);
					table.setUnoccupied();
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgWantToBreak(YixinWaiterRole w){
		synchronized(waiters){
			for (MyWaiter waiter : waiters) {
				if (waiter.w == w) {
					print(w + " want to break");
					waiter.state = MyWaiter.WaiterState.askingForBreak;
					stateChanged();
				}
			}
		}
	}
	
	public void msgWantToComeBack(YixinWaiterRole w){
		synchronized(waiters){
			waiters.add(new MyWaiter(w));
			stateChanged();
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			synchronized(waiters){
				for (MyWaiter waiter : waiters) {
					if (waiter.state == MyWaiter.WaiterState.askingForBreak && waiters.size() > 1) {
						print("Break granted");
						waiter.w.msgBreakGranted();
						waiters.remove(waiter);
						return true;
					}
				}
			}
			boolean hasEmptyTable = false;
			synchronized(tables){
				for (Table table : tables) {
					if (!table.isOccupied()) {
						hasEmptyTable = true;
						synchronized(waitingCustomers){
							for (MyCustomer customer : waitingCustomers) {
								if (waiters.size() != 0){
									if (customer.state == MyCustomer.CustomerState.waiting ||
											customer.state == MyCustomer.CustomerState.staying) {
										seatCustomer(customer.customer, table, customer.count);
										waitingCustomers.remove(customer);
										return true;
									}
								}
							}
						}
					}
				}
			}
			if (!hasEmptyTable){
				synchronized(waitingCustomers){
					for (MyCustomer customer : waitingCustomers){
						if(customer.state == MyCustomer.CustomerState.waiting){
							customer.customer.msgNoSeat();
							customer.state = MyCustomer.CustomerState.deciding;
							return true;
						}
					}
				}
			}
			if (waitingCustomers.size() == 0 && role_state == RoleState.WantToLeave){
				LeaveRestaurant();
				role_state = RoleState.none;
				active = false;
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			return false;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(YixinCustomerRole customer, Table table, int count) {
		if (waiterNumber < waiters.size() - 1)
			waiterNumber++;
		else
			waiterNumber = 0;
		print("Telling waiter " + waiterNumber + " " + waiters.get(waiterNumber).w + " to seat customer");
		waiters.get(waiterNumber).w.msgSitAtTable(customer, table.tableNumber, count);
		table.setOccupant(customer);
	}
	
	public void LeaveRestaurant(){
		hostGui.LeaveRestaurant();
	}

	//utilities

	public void setGui(YixinHostGui gui) {
		hostGui = gui;
	}

	public YixinHostGui getGui() {
		return hostGui;
	}

	private class Table {
		YixinCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(YixinCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	public static class MyWaiter{
		YixinWaiterRole w;
		public enum WaiterState
		{none, askingForBreak};
		private WaiterState state = WaiterState.none;
		
		MyWaiter(YixinWaiterRole w){
			this.w = w;
		}
	}
	
	public static class MyCustomer{
		YixinCustomerRole customer;
		public enum CustomerState
		{waiting, deciding, staying};
		private CustomerState state;
		int count;
		MyCustomer(YixinCustomerRole cust, int count){
			this.customer = cust;
			this.state = CustomerState.waiting;
			this.count = count;
		}
	}

	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}
	
	public Place place() {
		// TODO Auto-generated method stub
		return restaurant;
	}
}

