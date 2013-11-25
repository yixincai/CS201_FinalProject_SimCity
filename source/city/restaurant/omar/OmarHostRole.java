package city.restaurant.omar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class OmarHostRole extends Role {

	static final int NTABLES = 3;//a global for the number of tables.

	public Collection<Table> tables; //note that tables is typed with Collection semantics.
	public List<WaiterAgent> waiters = Collections.synchronizedList(new ArrayList<WaiterAgent>());
	public List<CustomerAgent> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerAgent>());

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		synchronized(tables){
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, ix*100, 200));//how you add to a collections
		}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	// Messages
	public void msgIWantFood(CustomerAgent cust) { // 1 receives messages from hungry customers, puts them on list
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(CustomerAgent cust) {  //last - waits for msg, marks table as available
		synchronized(tables){
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
		}
	}
	
	public void msgLeavingWaitList(CustomerAgent cust) {
		waitingCustomers.remove(cust);
		System.out.println("Customer " + cust.toString() + " left from waiting too long");
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
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
	public void wantBreak(WaiterAgent w){
		if(waiters.size() <= 1){
			w.breakRejected();
		} else{
			w.breakApproved();
		}
	}
	
	public void callWaiter(CustomerAgent customer, Table table, WaiterAgent w){
			Do("Called Waiter.");
			if(waiters.size() != 0){
				int custNum = waiters.get(0).getNumCustomers();
				WaiterAgent selectedWaiter = null;
				synchronized(waiters){
				for (WaiterAgent wa: waiters){
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
	private void DoCallWaiter(WaiterAgent w){
		System.out.println("Called waiter" + w.toString());
	}
	
	public String toString(){
		return name;
	}
	
	public ArrayList<Table> getTableList(){
		return (ArrayList<Table>) tables;
	}
}
