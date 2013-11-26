package city.restaurant.ryan;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.Random;

import city.PersonAgent;
import city.Place;
import city.restaurant.ryan.gui.RyanWaiterGui;
import agent.Agent;
import agent.Role;

public abstract class RyanWaiterRole extends Role{
	String waiterName;
	public RyanWaiterGui gui;
	List<MyCustomer> Customers = new ArrayList<MyCustomer>();
	List<Menu> wMenu = new ArrayList<Menu>();
	Map<Integer, Dimension> posSeats = new HashMap<Integer, Dimension>();
	final int numSeats = 5;
	private RyanHostRole host;
	protected RyanCookRole cook;
	private RyanCashierRole cashier;
	boolean chosen;
	boolean onBreak = false;
	Timer timer = new Timer();
	//Menu wMenu = new Menu();
	Semaphore isMoving = new Semaphore(0, true);
	
	enum waiterState{Working, WantBreak, Waiting, CanBreak, OnBreak, BreakDone};
	waiterState wState = waiterState.Working;
	
	enum waiterPlace{inRestaurant, atChef, atCashier}
	waiterPlace wPlace = waiterPlace.inRestaurant;
	
	enum customerState{Waiting, Seated, ReadyToOrder, Asked, Ordered, Reorder, Out, WaitingForFood, FoodIsDone, Eating, DoneEating, AwaitingCheck, GetCheck, GotCheck, Done, Gone};
	
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	
	class MyCustomer{
		RyanCustomerRole customer;
		int tableNumber;
		String choice;
		double payment;
		int seatNumber;
		customerState state = customerState.Waiting;
		
		public MyCustomer(RyanCustomerRole tempCustomer, int tempNumber, int seatNumber){
			customer = tempCustomer;
			tableNumber = tempNumber;
			this.seatNumber = seatNumber;
		}
	}
	
	public RyanWaiterRole(PersonAgent p, RyanRestaurant restaurant, String name){
		super(p);
		waiterName = name;
		wMenu.add(new Menu("Steak", 15.00));
		wMenu.add(new Menu("Chicken", 10.00));
		wMenu.add(new Menu("Pizza", 8.00));
		wMenu.add(new Menu("Salad", 5.00));
		
		for (int s = 1; s <= numSeats; s++) {
			posSeats.put(s, new Dimension((10+25*(s-1)), 5));
		}
	}
	
	public void setCook(RyanCookRole c){
		cook = c;
	}
	
	public void setCashier(RyanCashierRole cashier2) {
		cashier = cashier2;
		
	}
	
	public void setGui(RyanWaiterGui gui) {
		this.gui = gui;
	}
	
	public void setHost(RyanHostRole host) {
		this.host = host;
		host.addWaiter(this);
	}
	
	//messages******************************************************************************************************************
	//Break
	public void WantToGoOnBreak(){
		print("I, " + waiterName + ", want to go on break");
		wState = waiterState.WantBreak;
		stateChanged();
	}
	
	public void msgGoOnBreak(){
		onBreak = true;
		wState = waiterState.CanBreak;
		if(Customers.isEmpty()){
			stateChanged();
		}
	}
	
	public void msgNoBreak(){
		wState = waiterState.Working;
		timer.schedule(new TimerTask() { //Timer here because there has to be a delay between clicking button and not clicking it
			public void run() {
				gui.OffBreak();
			}
		},
		300);
	}
	
	public void msgJobsDone(){
		print("Done with jobs. Going on break");
		stateChanged();
	}
	
	public void msgBreakIsDone(){
		stateChanged();
	}
	
	//Customer
	public void msgSeatCustomer(RyanCustomerRole customer, int tablenumber, int seatNumber){
		MyCustomer temp = new MyCustomer(customer, tablenumber, seatNumber);
		Customers.add(temp);
		stateChanged();
	}
	
	public void msgReadytoOrder(RyanCustomerRole customer){
		MyCustomer cust = doSearch(customer);
		print("Coming " + cust.customer.getName() + "!");
		cust.state = customerState.ReadyToOrder;
		stateChanged();
	}
	
	public void msgMyOrder(String choice, RyanCustomerRole customer){
		MyCustomer cust = doSearch(customer);
		cust.choice = choice;
		cust.state = customerState.Ordered;
		print("So customer " + cust.customer.getName() + " wants " + cust.choice);
		stateChanged();
		//ordered
	}
	
	public void msgOutofOrder(String choice, RyanCustomerRole customer){
		print("We are out of " + choice);
		MyCustomer cust = doSearch(customer);
		cust.state = customerState.Out;
		for(int i = 0; i < wMenu.size(); i++){
			Menu temp = wMenu.get(i);
			if(temp.getOption().equals(choice)){
				wMenu.remove(temp);
			}
		}
		stateChanged();
	}
	
	public void msgOrderDone(String choice, RyanCustomerRole customer){
		MyCustomer cust = doSearch(customer);
		cust.state = customerState.FoodIsDone;
		print("Order " + cust.choice + " for Customer " + cust.customer.getName() + " is on counter");
		stateChanged();
	}
	
	public void msgDoneEating(RyanCustomerRole customer){
		MyCustomer cust = doSearch(customer);
		cust.state = customerState.DoneEating;
		stateChanged();
	}
	
	public void msgHeresReceipt(RyanCustomerRole customer, double payment){
		MyCustomer cust = doSearch(customer);
		cust.payment = payment;
		cust.state = customerState.GetCheck;
		stateChanged();
	}
	
	public void msgLeaving(RyanCustomerRole customer){
		MyCustomer cust = doSearch(customer);
		cust.state = customerState.Done;
		stateChanged();
	}
	
	//scheduler******************************************************************************************************************
	public boolean pickAndExecuteAnAction(){
		synchronized(Customers){
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.Waiting){
					DoSeatCustomer(temp);
					return true;
				}
			}
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.ReadyToOrder){
					TakeOrder(temp);
					return true;
				}
			}
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.Ordered){
					GoToChef(temp);
					return true;
				}
			}
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.Out){
					Reorder(temp);
					return true;
				}
			}
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.FoodIsDone){
					BringFoodToCustomer(temp);
					return true;
				}
			}
			for(MyCustomer temp: Customers){
				if(temp.state == customerState.DoneEating){
					AskReceipt(temp);
					return true;
				}
			}
			for(MyCustomer temp: Customers){
				if(temp.state == customerState.GetCheck){
					GetReceipt(temp);
					return true;
				}
			}
			for(MyCustomer temp:Customers){
				if(temp.state == customerState.Done){
					SayGoodbye(temp);
					return true;
				}
			}
			
			if(wState == waiterState.WantBreak){
				AskForBreak();
				return true;
			}
			if(Customers.isEmpty() && wState == waiterState.CanBreak){
				goOnBreak();
				return true;
			}
		}
		gui.setPosition(0);
		return false;
	}
	
	//actions*************************************************************************************************************************************
	public void DoSeatCustomer(MyCustomer customer){
		Dimension seatPos = posSeats.get(customer.seatNumber);
		if(gui.getXPos() == seatPos.width+20 && gui.getYPos() == seatPos.height+20){
			try{
				host.msgSeatUnoccupied(customer.seatNumber);
				customer.state = customerState.Seated;
				print("Seating " + customer.customer.getName() + " at " + customer.tableNumber);
				gui.DoGoToTable(customer.tableNumber);
				customer.customer.msgFollowMe(customer.tableNumber, this, wMenu);
				print("I'm " + waiterName);
				isMoving.acquire();
				if(waiterName.equals("onbreak")){
					WantToGoOnBreak();
				}
    		}catch(InterruptedException a){
        		
        	} catch(Exception a){
        		print("Unexpected exception caught in Agent thread:", a);
        	}
		}
		else{
			gui.DoGoToPosition(seatPos);
		}
		
	}
	
	public void TakeOrder(MyCustomer customer){
		try{
			gui.DoGoToTable(customer.tableNumber);
			customer.state = customerState.Asked;
			isMoving.acquire();
			print("What is your order?");
			customer.customer.msgWhatsYourOrder();
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print("Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public abstract void GoToChef(MyCustomer customer);
//	{
//		try{
//			gui.DoGoToChef();
//			isMoving.acquire();
//			wPlace = waiterPlace.inRestaurant;
//			customer.state = customerState.WaitingForFood;
//			print("Chef, Customer " + customer.customer.getName() + " wants " + customer.choice);
//			cook.msgTryToCookOrder(this, (RyanCustomerRole) customer.customer, customer.choice);
//		}catch(InterruptedException a){
//    		
//    	} catch(Exception a){
//    		print("Unexpected exception caught in Agent thread:", a);
//    	}
//	}
	
	public void Reorder(MyCustomer customer){
		try{
			gui.DoGoToTable(customer.tableNumber);
			isMoving.acquire();
			customer.state = customerState.Asked;
			print("Please reorder, customer " + customer.customer.getName());
			customer.customer.msgPleaseReorder(wMenu);
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print("Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public void BringFoodToCustomer(MyCustomer customer){
		if(wPlace == waiterPlace.atChef){
			try{
				wPlace = waiterPlace.inRestaurant;
				cook.msgGrabbedDish(this, (RyanCustomerRole) customer.customer);
				gui.DoGoToTable(customer.tableNumber);
				gui.thisFood(customer.choice);
				gui.hasFood();
				isMoving.acquire();
				gui.noFood();
				print("Bringing " + customer.choice + " to " + customer.customer.getName());
				customer.state = customerState.Eating;
				customer.customer.msgHeresYourFood();
				print("Here's your food, customer " + customer.customer.getName());
			}catch(InterruptedException a){
	    		
	    	} catch(Exception a){
	    		print("Unexpected exception caught in Agent thread:", a);
	    	}
		}
		else{
			try{
				gui.DoGoToChef();
				isMoving.acquire();
			}catch(InterruptedException a){
	    		
	    	} catch(Exception a){
	    		print("Unexpected exception caught in Agent thread:", a);
	    	}
		}
		
	}
	
	public void AskReceipt(MyCustomer customer){
		print("To Cashier: Get receipt for " + customer.customer.getName());
		customer.state = customerState.AwaitingCheck;
		cashier.msgMakeReceipt(this, customer.customer, customer.choice);
	}
	
	public void GetReceipt(MyCustomer customer){
		if(wPlace == waiterPlace.atCashier){
			try{
				wPlace = waiterPlace.inRestaurant;
				print("Bringing check amount " + customer.payment + " to " + customer.customer.getName());
				gui.DoGoToTable(customer.tableNumber);
				gui.hasCheck();
				isMoving.acquire();
				gui.noCheck();
				customer.state = customerState.GotCheck;
				print("Here's your check, customer " + customer.customer.getName());
				customer.customer.msgHeresYourCheck(customer.payment);
			}catch(InterruptedException a){
	    		
	    	} catch(Exception a){
	    		print("Unexpected exception caught in Agent thread:", a);
	    	}
		}
		else{
			try{
				gui.DoGoToCashier();
				isMoving.acquire();
			}catch(InterruptedException a){
	    		
	    	} catch(Exception a){
	    		print("Unexpected exception caught in Agent thread:", a);
	    	}
		}
	}
	
	public void SayGoodbye(MyCustomer customer){
		print("Goodbye " + customer.customer.getName());
		customer.state = customerState.Gone;
		Customers.remove(customer);
		host.msgFreeTable((RyanCustomerRole) customer.customer);
		if(Customers.isEmpty() && wState == waiterState.CanBreak){
			print("Done with jobs. Going on break");
		}
	}
	
	//Break
	public void AskForBreak(){
		print(host.getName() + ", Can I go on break?");
		wState = waiterState.Waiting;
		host.msgAskForBreak(this);
	}
	
	public void goOnBreak(){
		Random generator = new Random();
		wState = waiterState.OnBreak;
		print("I'm on break");
		timer.schedule(new TimerTask() {
			public void run() {
				OffBreak();
			}
		},
		generator.nextInt(5000)+10000);
	}
	
	public void OffBreak(){
		print("My break is done");
		onBreak = false;
		wState = waiterState.Working;
		host.msgOffBreak(this);
		gui.OffBreak();
	}
	
	//additional************************************************************************************************************************************************
	public MyCustomer doSearch(RyanCustomerRole customer){
		for(MyCustomer temp:Customers){
			if(temp.customer == customer){
				return temp; 
			}
		}
		return null;
	}
	
	public void msgAtTable() {//from animation
		print("At Table");
		isMoving.release();// = true;
		stateChanged();
	}
	
	public void msgAtChef(){
		print("At Chefs");
		wPlace = waiterPlace.atChef;
		isMoving.release();// = true;
		stateChanged();
	}
	
	public void msgAtCashier(){
		print("At Cashier");
		wPlace = waiterPlace.atCashier;
		isMoving.release(); // = true;
		stateChanged();
	}

	public String getName() {
		return waiterName;
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
