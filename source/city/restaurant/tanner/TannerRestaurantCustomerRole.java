package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.tanner.gui.TannerRestaurantCustomerRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCustomerRole extends RestaurantCustomerRole implements TannerRestaurantCustomer
{
	
//-----------------------------------------------Data----------------------------------------------------------

	TannerRestaurant restaurant;
	TannerRestaurantHost host;
	TannerRestaurantWaiter waiter;
	TannerRestaurantCashier cashier;
	Table table;
	TannerRestaurantCustomerRoleGui myGui;
	public double myMoney;
	public double myDebt;
	double billAmount;
	int waitTime;
	int choice;
	ArrayList<Integer> menu;
	Semaphore doingAction;
	enum Event
	{
		none, gotHungry, decideToStayOrGo, waitingInLine, followHost, seated, madeDecision, orderFood, reOrder, eatFood, doneEating, recievedCheck, payed, left 
	}
	
	enum State
	{
		none, WaitingAtRestaurant, BeingSeated, Seated, WaitingToOrder, Ordering, WaitingForFood, Eating, WaitingForCheck, Paying, Leaving
	}
	utilities.EventLog log;
	State myState;
	Event myEvent;
	String name_;
	
//---------------------------------------------Constructor-----------------------------------------------------	
	public TannerRestaurantCustomerRole(PersonAgent person, TannerRestaurant rest, String name) 
	{
		super(person);
		log = new utilities.EventLog();
		name_ = name;
		myState = State.none;
		myEvent = Event.none;
		myMoney = 50;
		myDebt = 0;
		billAmount = 0;
		doingAction = new Semaphore(0, true);
		restaurant = rest;
	}
	
//---------------------------------------------Accessors--------------------------------------------------------

	@Override
	public Place place() 
	{
		return this.restaurant;
	}
	
//---------------------------------------------Messages----------------------------------------------------------
	@Override
	public void msgImHungry() 
	{
		myEvent = Event.gotHungry;
		stateChanged();
	}

	@Override
	public void msgHereIsTheWaitingList(int numberInLine)
	{
		myEvent = Event.decideToStayOrGo;
		waitTime = numberInLine;
		stateChanged();		
	}

	@Override
	public void msgFollowMeToTable(int tableNumber, ArrayList<Integer> menu, TannerRestaurantWaiter w) 
	{
		myEvent = Event.followHost;
		this.menu = menu;
		table = restaurant.tableMap.get(tableNumber);
		waiter = w;
		stateChanged();		
	}
	
	@Override
	public void msgAnimationFinishedSeat()
	{
		myEvent = Event.seated;
		stateChanged();			
	}
	
	@Override
	public void msgFiguredOutMyOrder() 
	{
		myEvent = Event.madeDecision;
		stateChanged();		
	}

	@Override
	public void msgWhatWouldYouLike() 
	{
		myEvent = Event.orderFood;
		stateChanged();
	}

	@Override
	public void msgYourChoiceIsOutOfStock(ArrayList<Integer> newMenu)
	{
		myEvent = Event.reOrder;
		this.menu = newMenu;
		stateChanged();	
	}

	@Override
	public void msgHereIsYourFood(int choice) 
	{
		myEvent = Event.eatFood;
		stateChanged();		
	}

	@Override
	public void msgHereIsYourCheck(double bill) 
	{
		myEvent = Event.recievedCheck;
		billAmount = bill;
		stateChanged();
	}

	@Override
	public void msgHereIsYourChange(double changeAmount) 
	{
		myEvent = Event.payed;
		myMoney = changeAmount;
		stateChanged();		
	}

	@Override
	public void msgYouOweUs(float debt) 
	{
		myEvent = Event.payed;
		myMoney = 0;
		myDebt = debt;
		stateChanged();		
	}
	
	public void msgAnimationFinishEating()
	{
		myEvent = Event.doneEating;
		stateChanged();
	}
	
	public void msgAnimationFinishLeaving()
	{
		myEvent = Event.left;
		try {
			table.occupant = null;
		} catch (NullPointerException e) {
		System.out.println("May not yet have bee at a table");
		}
		stateChanged();
	}
	
	public void msgReadyToPay()
	{
		doingAction.release();
	}
	
	public void msgAtWaitArea()
	{
		doingAction.release();
	}
	
//---------------------------------------------Scheduler--------------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

//----------------------------------------------Actions--------------------------------------------------------
	
//----------------------------------------------Commands-------------------------------------------------------

	@Override
	public void cmdGotHungry() 
	{
		this.msgImHungry();
	}
	
	@Override
	public void cmdFinishAndLeave()
	{
		// TODO Auto-generated method stub

	}




}