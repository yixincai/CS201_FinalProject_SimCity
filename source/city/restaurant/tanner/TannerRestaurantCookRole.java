package city.restaurant.tanner;

import gui.trace.AlertTag;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Directory;
import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.market.interfaces.MarketCashier;
import city.restaurant.RestaurantCookRole;
import city.restaurant.tanner.MarketOrder.OrderState;
import city.restaurant.tanner.Order.State;
import city.restaurant.tanner.gui.TannerRestaurantCookRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCookRole extends RestaurantCookRole implements TannerRestaurantCook
{

//--------------------------------------------Data---------------------------------------------------------------------
	List<Order> orders;
	List<MarketOrder> marketOrders;
	String name_;
	Point position;
	boolean underStocked;
	Timer cookTimer = new Timer();
	Semaphore doingAction;
	TannerRestaurantCookRoleGui myGui;
	TannerRestaurantCashier cashier;
	TannerRestaurant restaurant;
	Market markets;
	boolean canOrder = true;
	
	
//----------------------------------------Constructors-----------------------------------------------------------------	
	public TannerRestaurantCookRole(PersonAgent person, TannerRestaurant rest, TannerRestaurantCashierRole cashier, String name)
	{
		super(person);
		name_ = name;
		orders = Collections.synchronizedList(new ArrayList<Order>());
		marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
		doingAction = new Semaphore(0, true);	
		this.cashier = cashier;
		restaurant = rest;
		markets = Directory.markets().get(0);
		
	}
	
//-----------------------------------------Accessors------------------------------------------------------------------
	
	@Override
	public Place place() 
	{
		return this.restaurant;
	}
	
	@Override
	public Point getPosition() 
	{
		return null;
	}
	
	public void setGui(TannerRestaurantCookRoleGui g)
	{
		this.myGui = g;
	}

//------------------------------------------Messages------------------------------------------------------------------
	
	
	@Override
	public void msgHereIsANewOrder(int choice, int tableNumber, TannerRestaurantWaiter w) 
	{
		if(restaurant.menu.get(choice).stock > 0)
		{
			orders.add(new Order(w, choice, tableNumber));
			restaurant.menu.get(choice).stock--;
			stateChanged();
		}
		else
		{
			w.msgThatChoiceIsOutOfStock(choice, tableNumber);
		}	
	}
	
	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) 
	{
		synchronized(marketOrders)
		{
			for(int i = 0; i < marketOrders.size(); i++)
			{
				if(marketOrders.get(i).market == m)
				{
					marketOrders.get(i).whatIGot = order ;
					marketOrders.get(i).orderState = OrderState.orderRecieved;
					for(int j = 0; j < marketOrders.get(i).whatIGot.size(); j++)
					{
						for(int k = 1; k <= restaurant.numDishes; k++)
						{
							if(restaurant.menu.get(k).dishName == marketOrders.get(i).whatIGot.get(j).name)
							{
								restaurant.menu.get(k).stock += marketOrders.get(i).whatIGot.get(j).amount;
							}
						}
					}
					
				}
			}
		}
		canOrder = true;
		stateChanged();
	}

	@Override
	public void msgThisIsWhatIHave(int[] ItemsGiven, MarketCashier m)
	{
		//this message has been replaced by msgorderFulfillment
	}

	@Override
	public void msgAllOutOfGoods(MarketCashier m) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheBill(double amount, MarketCashier m) 
	{
		synchronized(marketOrders)
		{
			for(int i = 0; i < marketOrders.size(); i++)
			{
				if(marketOrders.get(i).market == m)
				{
					marketOrders.get(i).orderState = OrderState.readyToPay;
					marketOrders.get(i).cost = amount;
					stateChanged();
				}
			}
		}		
	}

	
//------------------------------------------Scheduler-----------------------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() 
	{
		try {
			if(CheckStock())
			{
				PlaceNewMarketOrder();
				return true;
			}
			
			
			
			else if(orders.size() > 0)
			{
				synchronized(orders)
				{
					for(int i = 0; i < orders.size(); i++)
					{
						if(orders.get(i).orderState == State.orderReady)
						{
							PlateOrder(orders.get(i));
							return true;
						}
					}
				}
				
				synchronized(orders)
				{
					for(int i = 0; i < orders.size(); i++)
					{
						if(orders.get(i).orderState == State.orderPending)
						{
							CookOrder(orders.get(i));
							return true;
						}
					}
				}
			}
			
			Order order = restaurant.revolvingStand.remove();
			if (order!=null){
				myGui.DoGoToRevolvingStand();
				orders.add(order);
				return true;
			}
			else
			{
				
			}
			
		} catch (ConcurrentModificationException e) {
			return false;
		}
		
		return false;
	}

//------------------------------------------Actions------------------------------------------------------------------
	
	private boolean CheckStock()
	{
		boolean needMoreFood = false;
		for(int i = 0; i < restaurant.menu.size(); i++)
		{
			if(restaurant.menu.get(i+1).stock <= restaurant.menu.get(i+1).buffer)
			{
				needMoreFood = true;
			}
		}
		return needMoreFood;
	}
	
	private void AskForMarketOrderPrice(MarketOrder mo)
	{
	}
	
	private void PayMarket(MarketOrder mo)
	{
	}
	
	private void PlaceNewMarketOrder()
	{
		print(AlertTag.TANNER_RESTAURANT, "Ordering from market");
		List<Item> groceryList = new ArrayList<Item>();
		for(int i = 1; i <= restaurant.numDishes; i++)
		{
			if(restaurant.menu.get(i).stock < restaurant.menu.get(i).buffer)
			{
				groceryList.add(new Item(restaurant.menu.get(i).dishName, restaurant.menu.get(i).max-restaurant.menu.get(i).stock));
			}
		}
		markets.MarketCashier.msgPlaceOrder(restaurant, groceryList);
		canOrder = false;
	}
	
	private void CookOrder(final Order o)
	{
		print(AlertTag.TANNER_RESTAURANT, "Cook food");
		myGui.DoGoToIngredients();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGui.DoGoToGrills();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.orderState = State.orderCooking;
		cookTimer.schedule(new TimerTask() {
			public void run()
			{
				o.orderState = State.orderReady;
				stateChanged();
			}
		}, (long) (restaurant.menu.get(o.choice).cookTime) * 1000);
		
	}
	
	private void PlateOrder(Order o)
	{
		print(AlertTag.TANNER_RESTAURANT, "Plate Food");
		myGui.DoGoToGrills();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myGui.DoGoToHeatLamp();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.waiter.msgOrderIsReady(o.choice, o.tableNumber);
		orders.remove(o);
	}
	
//------------------------------------------Commands-----------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearInventory() 
	{
		for(int i = 0; i < restaurant.numDishes; i++)
		{
			restaurant.menu.get(i).stock = 0;
		}
	}


}

//------------------------------------------Utilities-----------------------------------------------------------------

class MarketOrder
{
	Market market;
	List<Item> whatIOrdered;
	List<Item> whatIGot;
	double cost;
	public enum OrderState {orderSubmitted, orderRecieved, readyToPay}
	OrderState orderState;
	
	public MarketOrder(List<Item> order, Market m)
	{
		whatIOrdered = order;
		market = m;
		orderState = OrderState.orderSubmitted;
	}
}