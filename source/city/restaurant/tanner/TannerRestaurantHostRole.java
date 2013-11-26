package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.gui.TannerRestaurantHostRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import agent.Role;

public class TannerRestaurantHostRole extends Role implements TannerRestaurantHost 
{
	
//-------------------------------------------Data---------------------------------------------------------------------------
	
	public List<TannerRestaurantCustomer> potentialCustomers;
	public List<TannerRestaurantCustomer> waitingCustomers;
	ArrayList<Integer> tables;
	ArrayList<MyWaiter> myWaiters;
	String name_;
	TannerRestaurantCook cook_;
	TannerRestaurantHostRoleGui myGui;
	utilities.EventLog log;
	TannerRestaurant restaurant;
	
//-----------------------------------------Constructor----------------------------------------------------------------------
	
	public TannerRestaurantHostRole(PersonAgent person, TannerRestaurant rest, String name)
	{
		super(person);
		log = new utilities.EventLog();
		name_ = name;
		myWaiters = new ArrayList<MyWaiter>();
		tables = new ArrayList<Integer>();
		waitingCustomers = new ArrayList<TannerRestaurantCustomer>();
		potentialCustomers = new ArrayList<TannerRestaurantCustomer>();
		restaurant = rest;
	}

//-----------------------------------------Accessors-------------------------------------------------------------------------	

	@Override
	public Place place() 
	{
		return this.restaurant;
	}
	
	public void addWaiter(TannerRestaurantWaiter w)
	{
		myWaiters.add(new MyWaiter(w));
	}
	
	public void setGui(TannerRestaurantHostRoleGui g)
	{
		this.myGui = g;
	}
	
	
//-----------------------------------------Messages-----------------------------------------------------------------------------	

	@Override
	public void msgHowLongIsTheWait(TannerRestaurantCustomer c) 
	{
		potentialCustomers.add(c);
		stateChanged();		
	}

	@Override
	public void msgIWantFood(TannerRestaurantCustomer c)
	{
		potentialCustomers.remove(c);
		waitingCustomers.add(c);
		stateChanged();		
	}

	@Override
	public void msgTheWaitIsTooLong(TannerRestaurantCustomer c) 
	{
		potentialCustomers.remove(c);		
	}

	@Override
	public void msgTableIsFree(int tableNumber, TannerRestaurantWaiter waiter)
	{
		restaurant.tableMap.get(tableNumber).occupant = null;
		for(int i = 0; i < myWaiters.size(); i++)
		{
			if(myWaiters.get(i).waiter == waiter)
			{
				myWaiters.get(i).numCustomers--;
				break;
			}
		}		
	}
	
	
//-----------------------------------------Scheduler--------------------------------------------------------------------------
	
	@Override
	public boolean pickAndExecuteAnAction() 
	{
		try {
			if(potentialCustomers.size() > 0 && myWaiters.size() > 0)
			{
				AnswerRequestForWaitTime(potentialCustomers.get(0));
				return false;
			}
			
			else if(waitingCustomers.size() > 0 && myWaiters.size() > 0)
			{
				int tableIndex = 1000; //larger than number of tables
				int waiterIndex = 0;
				
				//find the first open table
				for(int i = 0; i < restaurant.tableMap.size(); i++)
				{
					if(restaurant.tableMap.get(i).occupant == null)
					{
						tableIndex = i;
						break;
					}
				}
				
				//find waiter with least number of customers
				int minCustomers = 1000; //no waiter will have this many customers 
				for(int i = 0; i < myWaiters.size(); i++)
				{
					if(myWaiters.get(i).numCustomers < minCustomers)
					{
						minCustomers = myWaiters.get(i).numCustomers;
						waiterIndex = i;
					}
				}
				
				if(tableIndex < restaurant.tableMap.size())
				{
					SeatCustomer(waitingCustomers.get(0), tableIndex, myWaiters.get(waiterIndex));
					return false;	
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}	
		
		return false;
	}
	
	
//-----------------------------------------Actions----------------------------------------------------------------------------
	
	private void AnswerRequestForWaitTime(TannerRestaurantCustomer c)
	{
		print("Tell potential customer legth of line");
		c.msgHereIsTheWaitingList(waitingCustomers.size());
	}
	
	private void SeatCustomer(TannerRestaurantCustomer c, int tableNumber, MyWaiter w)
	{
		System.out.println(waitingCustomers.size());
		print("Seat new customer");
		restaurant.tableMap.get(tableNumber).occupant = c;
		w.numCustomers++;
		w.waiter.msgHereIsANewCustomer(c, tableNumber, this, cook_);
		waitingCustomers.remove(c);
	}
	
//-----------------------------------------Commands---------------------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() 
	{
		// TODO Auto-generated method stub

	}
}

//-----------------------------------------Utilities--------------------------------------------------------------------------

class MyWaiter
{
	TannerRestaurantWaiter waiter;
	int numCustomers;
	boolean onBreak;
	
	public MyWaiter(TannerRestaurantWaiter w)
	{
		onBreak = false;
		waiter = w;
		numCustomers = 0;
	}
}