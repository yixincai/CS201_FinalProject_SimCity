package city.restaurant.ryan;

import agent.Agent;
import agent.Role;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.restaurant.ryan.gui.RyanHostGui;
import city.restaurant.yixin.YixinRestaurant;
/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class RyanHostRole extends Role {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	static final int NSEATS = 5;
	static int occupiedTables;
	public List<MyCustomer> waitingCustomers = new ArrayList<MyCustomer>();
	public List<RyanWaiterRole> breakWaiters = new ArrayList<RyanWaiterRole>();
	public List<Table> tables = new ArrayList<Table>(NTABLES);
	public List<Seat> seats = new ArrayList<Seat>();
	public List<MyWaiter> waiters = new ArrayList<MyWaiter>();
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	enum waiterState{working, askbreak, deciding, onbreak, offbreak};
	enum customerState{here, sitting, seated, served};
	
	static int waiterNumber = 0;
	
	final int startx = 150;
	final int starty = 150;
	
	RyanRestaurant _restaurant;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public RyanHostGui hostGui = null;

	public RyanHostRole(PersonAgent p, RyanRestaurant r, String name) {
		super(p);
		_restaurant = r;
		this.name = name;
		
		// make some tables
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		for (int s = 1; s <= NSEATS; s++) {
			seats.add(new Seat(s, (10+25*(s-1)), 5));//how you add to a collections
		}	
	}
	//Messages
	public void addWaiter(RyanWaiterRole waiter){
		waiters.add(new MyWaiter(waiter));
		stateChanged();
	}
	
	public void msgAskForBreak(RyanWaiterRole waiter){
		MyWaiter temp = doSearch(waiter);
		temp.wState = waiterState.askbreak;
		stateChanged();
	}
	
	public void msgOffBreak(RyanWaiterRole waiter){
		MyWaiter temp = doSearch(waiter);
		temp.wState = waiterState.offbreak;
		stateChanged();
	}
	
	public void msgIWantFood(RyanCustomerRole cust) {
		waitingCustomers.add(new MyCustomer(cust));
		stateChanged();
	}
	
	public void msgImHere(RyanCustomerRole cust){
		for(MyCustomer customer: waitingCustomers){
			if(customer.customer == cust){
				customer.cState = customerState.seated;
			}
			
		}
	}

	public void msgFreeTable(RyanCustomerRole cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgSeatUnoccupied(int seatNumber){
		for(Seat seat: seats){
			if(seat.seatNumber == seatNumber){
				seat.occupied = false;
			}
		}
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgGone(RyanCustomerRole customer){
		waitingCustomers.remove(customer);
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.********************************************************************************************************************************************
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized(waitingCustomers){
			if(tables.get(0).isOccupied() && tables.get(1).isOccupied() && tables.get(2).isOccupied()){
				if(!waitingCustomers.isEmpty()){
					for(int i = 0; i < waitingCustomers.size(); i++){
						RyanCustomerRole temp = waitingCustomers.get(i).customer;
						if(!temp.patient){
							TablesAreFull(temp);
							return true;
						}
					}
				}
			}
		}
		
		synchronized(waitingCustomers){
			synchronized(seats){
				for(Seat seat: seats){
					if(seat.occupied == false){
						for(MyCustomer customer: waitingCustomers){
							if(customer.cState == customerState.here){
								ShowWaitArea(customer, seat);
							}
						}
					}
				}
			}
		}
		
		synchronized(waitingCustomers){
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if(!waiters.isEmpty()){
						if(!waitingCustomers.isEmpty()){
							for(int i=0; i<waitingCustomers.size(); i++){
								MyCustomer customer = waitingCustomers.get(i);
								if(customer.cState == customerState.seated){
									getWaiter(customer, table);//the action
									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
						}
					}
				}
			}
		}
		synchronized(waiters){
			for(MyWaiter temp:waiters){
				if(temp.wState == waiterState.askbreak){
					DecideOnBreak(temp);
					return true;
				}
				if(temp.wState == waiterState.offbreak){
					WaiterOffBreak(temp);
					return true;
				}
			}
		}
		
		if(!waitingCustomers.isEmpty()){ //continues running if there are customers still in the queue
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions************************************************************************************************************************************************************************
	private void ShowWaitArea(MyCustomer customer, Seat seat){
		seat.occupied = true;
		customer.sNumber = seat.seatNumber;
		customer.cState = customerState.sitting;
		customer.sNumber = seat.seatNumber;
		customer.customer.msgSitAtWaitArea(seat.x, seat.y);
	}
	
	
	
	private void getWaiter(MyCustomer customer, Table table) {
		RyanWaiterRole chooseWaiter = null;  
		MyWaiter temp;
		
		synchronized(waiters){
			boolean chosen = false;
			while(!chosen){
				for(int i = 0; i < waiters.size(); i++){
					if(waiters.size() != 0){
						if(waiterNumber >= waiters.size()){
							waiterNumber = 0;
						}
					}
					temp = waiters.get(i);
					if(temp.onBreak == false && i == waiterNumber){
						chooseWaiter = temp.waiter;
						waiterNumber++;
						chosen = true;
						break;
					}
					else if(temp.onBreak == true && i == waiterNumber){
						waiterNumber++;
					}
				}
			}
		}
		for(Seat seat: seats){
			if(customer.sNumber == seat.seatNumber){
				print("Waiter " + chooseWaiter.getName() + " please seat customer " + customer.customer.getCustomerName() + 
						" at table " + table.getTableNumber());
				table.setOccupant(customer.customer);
				chooseWaiter.msgSeatCustomer(customer.customer, table.getTableNumber(), seat.seatNumber);
				waitingCustomers.remove(customer);
			}
		}

	}
	
	public void TablesAreFull(RyanCustomerRole customer){
		print(customer.getName() + ", the tables are full.");
		customer.patient = true;
		customer.msgTablesAreFull();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(RyanCustomerRole customer, Table table) {
		//TODO get rid of this function? since seating customers is done by the waiter.
		//Same with "table"
		print("Seating customer " + customer.getName() + " at " + table); //TODO note: I (Eric) changed this print statement a little in order to work with the new Role.toString() system. (delete this comment when you read)
		hostGui.DoBringToTable(customer, table.x, table.y); 

	}
	
	public void DecideOnBreak(MyWaiter waiter){
		synchronized(waiters){
			waiter.wState = waiterState.deciding;
			if(CheckOnBreak(waiter)){
				print("Yes, " + waiter.waiter.getName() + " can go on break.");
				waiter.wState = waiterState.onbreak;
				waiter.onBreak = true;
				waiter.waiter.msgGoOnBreak();
				breakWaiters.remove(waiter);
			}
			else if(!CheckOnBreak(waiter)){
				print("No, " + waiter.waiter.getName() + " can't go on break.");
				waiter.wState = waiterState.working;
				//waiter.waiter.msgNoBreak();
				breakWaiters.remove(waiter);
			}
		}
	}
	
	public void WaiterOffBreak(MyWaiter waiter){
		waiter.onBreak = false;
		waiter.wState = waiterState.working;
	}

	//utilities****************************************************************************************************************
	public MyWaiter doSearch(RyanWaiterRole waiter){
		synchronized(waiters){
			for(MyWaiter temp:waiters){
				if(temp.waiter == waiter){
					return temp; 
				}
			}
			return null;
		}
	}
	
	public boolean CheckOnBreak(MyWaiter waiter){
		for(MyWaiter temp: waiters){
			if(temp.onBreak == false && temp != waiter){
				return true;
			}
		}
		return false;
	}
	
	public void ChooseWaiter(RyanWaiterRole chooseWaiter){
		MyWaiter temp;
		
		synchronized(waiters){
			boolean chosen = false;
			while(!chosen){
				for(int i = 0; i < waiters.size(); i++){
					temp = waiters.get(i);
					if(waiters.size() != 0){
						if(waiterNumber >= waiters.size()){
							waiterNumber = 0;
						}
					}
					if(temp.onBreak == false && i == waiterNumber){
						chooseWaiter = temp.waiter;
						waiterNumber++;
						chosen = true;
					}
					else if(temp.onBreak == true && i == waiterNumber){
						waiterNumber++;
					}
				}
			}
		}
	}
	
	public int tableAmount(){
		return tables.size();
	}
	
	public void placeTable(int tableNumber, int x, int y){
		x = tables.get(tableNumber).x;
		y = tables.get(tableNumber).y;
	}
	
	public void findTable(int tableNumber, int x, int y){
		for(Table temp:tables){
			if(temp.tableNumber == tableNumber){
				x = tables.get(tableNumber).x;
				y = tables.get(tableNumber).y;
			}
		}
	}
	
	public void setGui(RyanHostGui gui) {
		hostGui = gui;
	}

	public RyanHostGui getGui() {
		return hostGui;
	}
	
	
	//Table Class
	private class Table {
		RyanCustomerRole occupiedBy;
		int tableNumber;
		int x;
		int y;
		int width;
		
		

		Table(int pTableNumber) {
			this.tableNumber = pTableNumber;
			if(tableNumber == 1){
				x = startx + 150;
				y = starty;
			}
			if(tableNumber == 2){
				x = startx + 150;
				y = starty + 150;
			}
			if(tableNumber == 3){
				x = startx;
				y = starty +150;
			}
			occupiedBy = null;
		}

		void setOccupant(RyanCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		RyanCustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		int getTableNumber(){
			return tableNumber;
		}
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	
	//MyWaiter Class
	class MyWaiter{
		RyanWaiterRole waiter;
		boolean onBreak = false;
		waiterState wState = waiterState.working;
		
		
		MyWaiter(RyanWaiterRole waiter){
			this.waiter = waiter;
		}
	}
	
	class MyCustomer{
		RyanCustomerRole customer;
		int sNumber;
		customerState cState = customerState.here;
		
		MyCustomer(RyanCustomerRole customer){
			this.customer = customer;
			cState = customerState.here;
		}
	}
	
	class Seat{
		int x;
		int y;
		boolean occupied = false;
		int seatNumber;
		
		Seat(int number, int x, int y){
			seatNumber= number;
			this.x = x;
			this.y = y;
		}
		
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return _restaurant;
	}
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
	
}


