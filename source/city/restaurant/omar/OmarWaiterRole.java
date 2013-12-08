package city.restaurant.omar;

import gui.trace.AlertTag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import agent.Role;
import city.PersonAgent;
import city.Place;
import city.restaurant.omar.gui.OmarWaiterGui;

public class OmarWaiterRole extends Role {

	//Data
	OmarCookRole cook;
	OmarHostRole host;
	OmarCashierRole cashier;
	OmarRestaurant restaurant;
	
	String name;
	public boolean wantBreak; //these booleans are needed for going on break
	public boolean onBreak;
	Timer breakTimer;
	private static int breakTime = 15000; //used for break
	public List<MyCustomer> myCustomers
	= Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	OmarWaiterGui waiterGui = null;
	
	Semaphore waiterSem = new Semaphore(0, true);
	
	public enum MycustomerState { waiting, seated, readyToOrder,
		ordering, ordered, needToReorder, waitingForFood, orderReady, 
			eating, paying, awaitingCheck,hasCorrectBill, paid, leaving};
	
	class MyCustomer{
		  public OmarCustomerRole customer;
		  public Table table;
		  public String choice;
		  double check;
		
		  MycustomerState myCustomerState;
		  
		  public MyCustomer(OmarCustomerRole customer, Table table){
			  this.customer = customer;
			  this.table = table;
		  }
	}
	
	public OmarWaiterRole(PersonAgent p, OmarRestaurant r, OmarCookRole cook, OmarHostRole host, String name){
		super(p);
		this.restaurant = r;
		this.cook = cook;
		this.host = host;
		this.name = name;
		wantBreak = false;
		onBreak = false;
	}
	
	public boolean pickAndExecuteAnAction() {
		
		if(wantBreak){
			host.wantBreak(this);
			return true;
		}
		
		if(myCustomers.isEmpty() && onBreak){
			goOnBreak();
			return true;
		}
		
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.waiting){
				seatCustomer(m);
				return true;
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.readyToOrder){
				takeCustomerOrder(m);
				return true;
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.ordered){
				giveOrderToCook(m, cook);
				return true;
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.needToReorder){
				rePickFood(m);
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.orderReady){
				giveCustomerFood(m);
				return true;
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.paying){
				getCheck(m);
				return true;
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m:myCustomers){
			if(m.myCustomerState == MycustomerState.hasCorrectBill){
				giveCorrectBillToCustomer(m);
			}
		}
	}
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.myCustomerState == MycustomerState.leaving){
				customerLeaving(m);
				return true;
			}
		}
	}
		waiterGui.DoGoIdle();
		return false;
	}
	//
	//Actions
	public void breakApproved(){
		onBreak = true;
		wantBreak = false;
		stateChanged();
	}
	
	public void goOnBreak(){
		waiterGui.DoGoOnBreak();
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		breakTimer = new Timer(breakTime, new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				onBreak = false;
				breakTimer.stop();}});
		breakTimer.start();
	}
	
	public void breakRejected(){
		System.out.println(this.name + ": Can't go on break.  Only one waiter");
		waiterGui.setWaiterBreakBoxEnabled();
		wantBreak = false;
	}
	
	public void seatCustomer(MyCustomer m){
		waiterGui.setCurrentStatus("Seating");
		waiterGui.DoGetCustomer(m.customer, m.table);
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m.customer.msgFollowToTable(new Menu(), this, m.table.tableNumber);
	    m.myCustomerState = MycustomerState.seated;
		DoSeatCustomer(m.customer, m.table);
	}
	
	private void DoSeatCustomer(OmarCustomerRole customer, Table table) {
		print(AlertTag.OMAR_RESTAURANT, "Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table); 
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void takeCustomerOrder(MyCustomer m){
		waiterGui.DoTakeCustomerOrder(m.customer, m.table);
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m.customer.msgWhatWouldYouLike();
	}
	
	public void rePickFood(MyCustomer m){
		waiterGui.DoTakeCustomerOrder(m.customer, m.table);
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m.customer.menu.menuItems.remove(m.customer.choice);
		System.out.println(this.name + ": What do you want instead");
		m.myCustomerState = MycustomerState.ordering;
		m.customer.msgNeedReorder();
	}
	
	protected void giveOrderToCook(MyCustomer m, OmarCookRole c){
		waiterGui.setCurrentStatus("Giving Order");
		System.out.println("Gave " + m.customer.getName() + "'s order " + m.choice + " to cook " + c.name);
		waiterGui.DoGiveOrderToCook();
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
		while(cook.cookSem.availablePermits() > 0){}
		m.myCustomerState = MycustomerState.waitingForFood;
		c.msgHereIsAnOrder(new FoodTicket(this, m.customer));
		
	}
	public void giveCustomerFood(MyCustomer m){
		waiterGui.DoGetFoodFromCook();
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.cookGui.setPickupStatus("");
		waiterGui.setCurrentStatus(m.choice);
		waiterGui.DoGiveCustomerFood(m.customer, m.table);
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Gave " + m.customer.getName() + " his order: " + m.choice);
			 
		m.customer.msgHereIsYourFood(m.choice);
		m.myCustomerState = MycustomerState.eating;
		stateChanged();
	}
	
	public void getCheck(MyCustomer m){
		waiterGui.DoGetCheck();
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cashier.msgCustomerDoneAndNeedsToPay(m.customer,this,  m.choice);
		m.myCustomerState = MycustomerState.awaitingCheck;
		stateChanged();
	}
	
	public void giveCorrectBillToCustomer(MyCustomer m){
		waiterGui.DoGiveCorrectBillToCustomer(m.customer, m.table);
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m.customer.msgHereIsCheck(cashier, m.check);
		m.myCustomerState = MycustomerState.paid;
	}
	
	public void customerLeaving(MyCustomer m){
		waiterGui.setCurrentStatus("Idle");
		waiterGui.DoGoIdle();

		host.msgLeavingTable(m.customer);
		myCustomers.remove(m);
	}

	//Messages
	public void msgNeedRechoose(OmarCustomerRole customer){
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.customer == customer){
				m.myCustomerState = MycustomerState.needToReorder;
				System.out.println("Out of the Selection");
				stateChanged();
				return;
			}
		}
	}
	}
	
	public void msgSeatCustomer(OmarCustomerRole c, Table table){
		MyCustomer m = new MyCustomer(c, table);
		m.myCustomerState = MycustomerState.waiting;
		myCustomers.add(m);
		
		System.out.println("Seating customer " + c.getName() + " at table " + table.tableNumber);
		  stateChanged();
	}
	
	public void msgReadyToOrder(OmarCustomerRole c){
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.customer == c){
				m.myCustomerState = MycustomerState.readyToOrder;
				stateChanged();
				return;
			}
		}
	}
	}

	public void msgHereIsMyChoice(OmarCustomerRole c, String choice){
	synchronized(myCustomers){
		for(MyCustomer myC : myCustomers){
			if(myC.customer == c){
				myC.choice = choice;
				myC.myCustomerState = MycustomerState.ordered;
				stateChanged();
				return;
			}
		}
	}
	}
	
	public void msgOrderIsReady(int tableNum){
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.table.tableNumber == tableNum){
				m.myCustomerState = MycustomerState.orderReady;
				stateChanged();
				return;
			}
		}
	}
	}
	
	public void msgDoneEatingAndReadyToPay(OmarCustomerRole c){
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.customer == c){
				m.myCustomerState = MycustomerState.paying;
				stateChanged();
				return;
			}
		}
	}
	}
	
	public void msgHereIsCheck(OmarCustomerRole customer, double checkAmount) {
	synchronized(myCustomers){
		for(MyCustomer m: myCustomers){
			if(m.customer == customer){
				m.myCustomerState = MycustomerState.hasCorrectBill;
				m.customer.check = checkAmount;
				stateChanged();
				return;
			}
		}
	}	
	}
	
	public void msgCustomerPaidWithLabor(OmarCustomerRole customer) {
	synchronized(myCustomers){
		for(MyCustomer m:myCustomers){
			if(m.customer == customer){
				m.myCustomerState = MycustomerState.leaving;
				stateChanged();
				return;
			}
		}	
	}
	}
	
	public void msgDoneEatingAndLeaving(OmarCustomerRole c){
	synchronized(myCustomers){
		for(MyCustomer myC : myCustomers){
			if(myC.customer == c){
				myC.myCustomerState = MycustomerState.leaving;
				stateChanged();
				return;
			}
		}
	}
	}
	
	//Utilities
	public int getNumCustomers(){
		return myCustomers.size();
	}
	
	public void setGui(OmarWaiterGui g) {
		waiterGui = g;
	}

	public OmarWaiterGui getGui() {
		return waiterGui;
	}
	
	public int myCustomerListSize(){
		return myCustomers.size();
	}
	
	public void msgArrived(){
		waiterSem.release();
	}
	
	public void msgSetWantBreak(boolean wantBreak){
		this.wantBreak = wantBreak;
	}
	
	public void setCashier(OmarCashierRole cashier){
		this.cashier = cashier;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean doesWantBreak(){
		return wantBreak;
	}
	
	public void setWantBreak(boolean wantBreak){
		this.wantBreak = wantBreak;
		stateChanged();
	}
	
	public void setHomePosition(int x, int y){
		waiterGui.setHomePosition(x, y);
	}

	@Override
	public Place place() {
		return restaurant;
	}

	@Override //TODO INTEGRATION COMPLETE
	public void cmdFinishAndLeave() {
		active = false;
		stateChanged();
	}
}
