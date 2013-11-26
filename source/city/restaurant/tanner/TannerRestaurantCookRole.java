package city.restaurant.tanner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.market.interfaces.MarketCashier;
import city.restaurant.RestaurantCookRole;
import city.restaurant.tanner.MarketOrder.OrderState;
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

	}

	@Override
	public void msgThisIsWhatIHave(int[] ItemsGiven, MarketCashier m)
	{
		synchronized(marketOrders)
		{
			for(int i = 0; i < marketOrders.size(); i++)
			{
				if(marketOrders.get(i).market == m)
				{
					marketOrders.get(i).whatIGot = ItemsGiven;
					marketOrders.get(i).orderState = OrderState.orderRecieved;
					for(int j = 0; j < marketOrders.get(i).whatIGot.length; j++)
					{
						restaurant.menu.get(j).stock += marketOrders.get(i).whatIGot[j];
					}
					stateChanged();
				}
			}
		}		
	}

	@Override
	public void msgAllOutOfGoods(MarketCashier m) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheBill(float amount, MarketCashier m) 
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
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

//------------------------------------------Actions------------------------------------------------------------------
	
	
//------------------------------------------Commands-----------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}


}

//------------------------------------------Utilities-----------------------------------------------------------------

class MarketOrder
{
	Market market;
	int[] whatIOrdered;
	int[] whatIGot;
	float cost;
	public enum OrderState {orderSubmitted, orderRecieved, readyToPay}
	OrderState orderState;
	
	public MarketOrder(int[] order, Market m)
	{
		whatIOrdered = order;
		market = m;
		orderState = OrderState.orderSubmitted;
	}
}