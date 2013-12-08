package city.restaurant.omar;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import agent.Role;

public class OmarHostRole extends Role {

	static final int NTABLES = 3;//a global for the number of tables.

	OmarRestaurant restaurant;
	public Collection<Table> tables; //note that tables is typed with Collection semantics.
	public List<OmarWaiterRole> waiters = Collections.synchronizedList(new ArrayList<OmarWaiterRole>());
	public List<OmarCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<OmarCustomerRole>());

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public OmarHostRole(PersonAgent p, OmarRestaurant r, String name) {
		super(p);
		this.restaurant = r;
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		synchronized(tables){
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, ix*100 + 100, 100));
		}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	// Messages
	public void msgIWantFood(OmarCustomerRole cust) { // 1 receives messages from hungry customers, puts them on list
		print(AlertTag.OMAR_RESTAURANT, "Receive IWantFood from customer");
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(OmarCustomerRole cust) {  //last - waits for msg, marks table as available
		synchronized(tables){
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(AlertTag.OMAR_RESTAURANT, cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
		}
	}
	
	public void msgLeavingWaitList(OmarCustomerRole cust) {
		waitingCustomers.remove(cust);
		print(AlertTag.OMAR_RESTAURANT, "Customer " + cust.getName() + " left from waiting too long");
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(tables){
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						if(!waiters.isEmpty()){
							int j = 0;
							synchronized(waiters){
							for(int i = 0; i < waiters.size() - 1; i++){
								//System.out.println("Waiters size: " + waiters.size());
								if(waiters.get(i+1).myCustomerListSize() < waiters.get(i).myCustomerListSize()){
									j = i+1;
								}
							}
							callWaiter(waitingCustomers.remove(0), table, waiters.get(j)); //the action
							}
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	// Actions
	public void wantBreak(OmarWaiterRole w){
		if(waiters.size() <= 1){
			w.breakRejected();
		} else{
			w.breakApproved();
		}
	}
	
	public void callWaiter(OmarCustomerRole customer, Table table, OmarWaiterRole w){
			print(AlertTag.OMAR_RESTAURANT, this.name + ": Called Waiter.");
			if(waiters.size() != 0){
				int custNum = waiters.get(0).getNumCustomers();
				OmarWaiterRole selectedWaiter = null;
				synchronized(waiters){
				for (OmarWaiterRole wa: waiters){
					if(wa.getNumCustomers() <= custNum && wa.onBreak == false){
						custNum = wa.getNumCustomers();
						selectedWaiter = wa;
					}
				}
				}
				selectedWaiter.msgSeatCustomer(customer, table);
				table.setOccupant(customer);
				waitingCustomers.remove(customer);
			}
	}

	//utilities
	private void DoCallWaiter(OmarWaiterRole w){
		//TODO remove this method??
		System.out.println("Called waiter" + w.getName());
	}
	
	public ArrayList<Table> getTableList(){
		return (ArrayList<Table>) tables;
	}

	@Override
	public Place place() {
		return restaurant;
	}

	@Override	//TODO INTEGRATION REQUIRED
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		active = false;
		stateChanged();
	}
}
