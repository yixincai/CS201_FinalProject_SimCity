package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
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
	int customerNumber;
	
//---------------------------------------------Constructor-----------------------------------------------------	
	public TannerRestaurantCustomerRole(PersonAgent person, TannerRestaurant rest, String name, int count) 
	{
		super(person);
		customerNumber = count;
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
	
	public void setGui(TannerRestaurantCustomerRoleGui g)
	{
		myGui = g;
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
	public void msgYouOweUs(double debt) 
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
	public boolean pickAndExecuteAnAction() 
	{
		try {
			if(myEvent == Event.gotHungry && myState == State.none)
			{
				GoToRestaurant();
				return true;
			}
			else if(myEvent == Event.decideToStayOrGo && myState == State.WaitingAtRestaurant)
			{
				StayOrGo();
				return true;
			}
			else if(myEvent == Event.followHost && myState == State.WaitingAtRestaurant)
			{
				SitDown();
				return true;
			}
			else if(myEvent == Event.seated && myState == State.BeingSeated)
			{
				LookAtMenu();
				return true;
			}
			else if(myEvent == Event.madeDecision && myState == State.WaitingToOrder)
			{
				CallWaiterToOrder();
				return true;
			}
			else if(myEvent == Event.orderFood && myState == State.Ordering)
			{
				OrderFood();
				return true;
			}
			
			else if(myEvent == Event.reOrder && myState == State.Ordering)
			{
				Reorder();
				return true;
			}
			
			else if(myEvent == Event.eatFood && myState == State.WaitingForFood)
			{
				EatFood();
				return true;
			}
			else if(myEvent == Event.doneEating && myState == State.Eating)
			{
				GetCheck();
				return true;
			}
			
			else if(myEvent == Event.recievedCheck && myState == State.WaitingForCheck)
			{
				GoPay();
				return true;
			}
			
			else if(myEvent == Event.payed && myState == State.Paying)
			{
				Leave();
				return true;
			}
			
			else if(myEvent == Event.left && myState == State.Leaving)
			{
				myState = State.none;
				myEvent = Event.none;
				return false;
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		
		return false;
	}

//----------------------------------------------Actions--------------------------------------------------------
	
	private void GoToRestaurant()
	{
		log.add(new utilities.LoggedEvent("Go To Restaurant"));
		myState = State.WaitingAtRestaurant;
		host.msgHowLongIsTheWait(this);
	}
	
	private void StayOrGo()
	{
		log.add(new utilities.LoggedEvent("Determining whether or not to stay"));
		myEvent = Event.waitingInLine;
		if(name_.toLowerCase().contains("hurry".toLowerCase()) && waitTime >= 2)
		{
			host.msgTheWaitIsTooLong(this);
			Leave();
		}
		else
		{
			myGui.DoGoToWaitArea();
			try {
				doingAction.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			host.msgIWantFood(this);
		}		
	}
	
	private void SitDown()
	{
		print("Sit Down");
		myState = State.BeingSeated;
		myGui.DoGoToSeat(table.tableNumber);
	}
	
	private void LookAtMenu()
	{
		print("Look at menu");
		myState = State.WaitingToOrder;
		myGui.DoLookAtMenu();
	}
	
	private void CallWaiterToOrder()
	{
		print("Call waiter to order");
		myState = State.Ordering;
		waiter.msgImReadyToOrder(this);	
	}
	
	private void OrderFood()
	{
		if(name_.toLowerCase().contains("cheap".toLowerCase()))
		{
			myMoney = 7.00f;
			float cheapestPrice = 1000;
			int index = 0;
			for(int i = 0; i < menu.size(); i++)
			{
				if(restaurant.menu.get(i).cost < cheapestPrice)
				{
					print("Order cheapest food");
					cheapestPrice = restaurant.menu.get(i).cost;
					index = i;
				}
			}
			
			if(myMoney < cheapestPrice)
			{
				print("Cant afford anything");
				waiter.msgICantAffordAnything(this);
				Leave();
			}
			else
			{
				print("order cheapest food");
				choice = menu.get(index);
				waiter.msgHereIsMyOrder(this, choice);
				myState = State.WaitingForFood;
			}
		}
		
		else
		{
			print("Order normally");
			Random orderGenerator = new Random();
			int choiceIndex = orderGenerator.nextInt(menu.size());
			choice = menu.get(choiceIndex);
			waiter.msgHereIsMyOrder(this, choice);
			myState = State.WaitingForFood;
		}
	}
	
	private void Reorder()
	{
		if(name_.toLowerCase().contains("cheap".toLowerCase()))
		{
			float cheapestPrice = 1000;
			int index = 0;
			for(int i = 0; i < menu.size(); i++)
			{
				if(restaurant.menu.get(i).cost < cheapestPrice)
				{
					cheapestPrice = restaurant.menu.get(i).cost;
					index = i;
				}
			}
			
			if(myMoney < cheapestPrice)
			{
				print("cant afford anyhting");
				waiter.msgICantAffordAnything(this);
				Leave();
			}
			else
			{
				print("Ordering food");
				choice = menu.get(index);
				waiter.msgHereIsMyReOrder(this, choice);
				myState = State.WaitingForFood;
			}
		}
		else
		{
			print("Ordering food");
			Random orderGenerator = new Random();
			int choiceIndex = orderGenerator.nextInt(menu.size());
			choice = menu.get(choiceIndex);
			waiter.msgHereIsMyReOrder(this, choice);
			myState = State.WaitingForFood;
		}
	}
	
	private void EatFood()
	{
		print("Eat food");
		myState = State.Eating;
		myGui.DoEatFood();
	}
	
	private void GetCheck()
	{
		print("Asking waiter for check");
		myState = State.WaitingForCheck;
		waiter.msgIWouldLikeTheCheck(this, choice);
	}
	
	private void GoPay()
	{
		myGui.DoGoToFront();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myState = State.Paying;
		if(name_.toLowerCase().contains("thief".toLowerCase()))
		{
			print("I dont have enough money");
			cashier.msgIDontHaveEnoughMoney(billAmount, myMoney, this);
		}
		else
		{
			print("Paying");
			cashier.msgHereIsMyPayment(billAmount, myMoney, this);
		}
	}
	
	private void Leave()
	{
		print("Leaving");
		myState = State.Leaving;
		try {
			waiter.msgGoodBye(this);
		} catch (NullPointerException e) {
			System.out.println("If leaving from the front, the customer won't have a waiter");
		}
		myGui.DoExitRestaurant();	
	}
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