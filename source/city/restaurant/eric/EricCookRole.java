package city.restaurant.eric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import agent.Agent;
import city.restaurant.eric.interfaces.*;

public class EricCookRole extends Agent implements Cook
{
	// --------------------------------------- DATA ------------------------------------------------
	
	// Personal data:
	private String _name;
	
	// Correspondence:
	// use Order.waiter for waiter correspondence.
	private Cashier _cashier;
	
	// Agent data:
	private class Order
	{
		public Waiter waiter;
		public Food food;
		public int table;
		public OrderState state;
	}
	private enum OrderState { PENDING, COOKING, DONE }
	private List<Order> _orders = Collections.synchronizedList(new ArrayList<Order>());
	
	private Timer cookingTimer = new Timer();
	
	private class Food
	{
		public String name;
		public int cookingTime; // in seconds
		public int inventory; // the current amount of the food
		public int stockLevel; // the total amount for this food; the cook will order enough food to raise the inventory to this level
		public int lowLevel; // the level at which the cook will start reordering the food
		public int amountComing; // the amount that the market has confirmed to be coming
		public int amountNeededToFulfillOrder; // the amount that is needed to fill the inventory back up to the stockLevel.  Only matters if one market cannot fulfill the whole order.
		
		public Food(String name, int cookingTime, int stockLevel, int inventory, int lowLevel)
		{
			this.name = name;
			this.cookingTime = cookingTime;
			this.stockLevel = stockLevel;
			this.inventory = inventory;
			this.lowLevel = lowLevel;
			amountComing = 0;
			amountNeededToFulfillOrder = 0;
		}
		
		// Note that this value of amountNeededToFulfillOrder should be accurate even if more than one market is being ordered from, because amountComing is assigned using +=.
		public void updateAmountNeededToFulfillOrder()
		{
			amountNeededToFulfillOrder = stockLevel - (inventory + amountComing);
		}
	}
	private List<Food> _foods = Collections.synchronizedList(new ArrayList<Food>());
	
	private class MyMarket
	{
		Market agent;
		public MyMarket(Market m) { agent = m; }
	}
	private List<MyMarket> _markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	private int _lastMarketUsed = -1; // starts at -1 so that when we increment it before first using, it will be zero
	private boolean _awaitingMarketResponse = false; // indicates whether an order to a market is pending the market's response (to say what's coming and what's not coming in the order)
	
	// ------------------------------------------ CONSTRUCTOR ----------------------------------------
	public EricCookRole(String name)
	{
		_name = name;
		
		//         new Food(name, cookingTime, stockLevel, inventory, lowLevel)
		_foods.add(new Food("Steak",   15,     8,          1,         5       ));
		_foods.add(new Food("Chicken",  7,     8,          3,         7       ));
		_foods.add(new Food("Salad",    2,     8,          2,         6       ));
		_foods.add(new Food("Pizza",   15,     8,          5,         6       ));
	}
	
	// ----------------------------------------- PROPERTIES ------------------------------------------------
	public String getName() { return _name; }
	public String toString() { return "cook " + getName(); }
	public void addMarket(Market m) { _markets.add(new MyMarket(m)); }
	public void setCashier(Cashier c) { _cashier = c; }
	
	
	
	// ------------------------------------------- MESSAGES ----------------------------------------------
	
	public void msgHereIsOrder(Waiter sender, String choice, int table)
	{
		Order o = new Order();
		o.waiter = sender;
		for(Food f : _foods)
		{
			if(choice.equals(f.name))
			{
				o.food = f;
			}
		}
		o.table = table;
		o.state = OrderState.PENDING;
		_orders.add(o);
		stateChanged();
	}
	
	private void msgFoodDone(Order o) // from timer (not in Cook interface)
	{
		o.state = OrderState.DONE;
		stateChanged();
	}
	
	public void msgOrderComing(Market sender, Map<String,Integer> foodsComing) // from Market
	{
		// Set mySender to the correct MyMarket
		//MyMarket mySender = null;
		//for(MyMarket m : _markets) { if(m.agent == sender) mySender = m; }
		
		for(Food f : _foods)
		{
			print("Will receive " + foodsComing.get(f.name) + " " + f.name + " from " + sender.getName());
			f.amountComing += foodsComing.get(f.name);
			
			f.updateAmountNeededToFulfillOrder();
		}
		
		_awaitingMarketResponse = false;
		
		printInventory();
		stateChanged();
	}
	
	/**
	 * @param foods Map of food names to the number of each food.
	 */
	public void msgDelivery(Market sender, Map<String,Integer> foods) // from Market
	{
		print("Received a delivery from " + sender.getName());
		for(Food f : _foods)
		{
			print("Received " + foods.get(f.name) + "  " + f.name);
			f.amountComing -= foods.get(f.name);
			f.inventory += foods.get(f.name);
		}
		
		printInventory();
		stateChanged();
	}
	
	
	
	// ----------------------------------------- SCHEDULER -------------------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction()
	{
		synchronized(_orders) {
			for(Order o : _orders) {
				if(o.state == OrderState.PENDING) {
					actCookIfAvailable(o);
					return true;
				}
			}
		}
		synchronized(_orders) {
			for(Order o : _orders) {
				if(o.state == OrderState.DONE) {
					actGiveToWaiter(o);
					return true;
				}
			}
		}
		if(!_awaitingMarketResponse) {
			synchronized(_foods) {
				for(Food f : _foods) {
					if((f.amountNeededToFulfillOrder > 0) || (f.inventory + f.amountComing <= f.lowLevel)) {
						actRestock();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	// ------------------------------------------ ACTIONS --------------------------------------------------
	
	void actCookIfAvailable(final Order o)
	{
		if(o.food.inventory == 0)
		{
			o.waiter.msgOutOfOrder(o.food.name, o.table);
			_orders.remove(o);
		}
		else
		{
			o.food.inventory--;
			
			print("Order for " + o.waiter.getName() + " is cooking.");
			
			cookingTimer.schedule(
					new TimerTask() {
						public void run() { msgFoodDone(o); }
					},
					o.food.cookingTime * 1000 // cookingTime (seconds) * 1000 (ms/second)
					);
			
			o.state = OrderState.COOKING;
		}
	}
	
	void actGiveToWaiter(Order o)
	{
		print("Order for " + o.waiter.getName() + " is ready.");
		
		o.waiter.msgOrderReady(o.food.name, o.table);
		_orders.remove(o);
	}
	
	void actRestock()
	{
		print("Ordering needed food.");
		printInventory();
		
		// Fill up neededFoods:
		Map<String,Integer> neededFoods = new HashMap<String,Integer>();
		for(Food f : _foods) {
			int neededAmount = f.stockLevel - (f.inventory + f.amountComing);
			print("Ordering " + neededAmount + " " + f.name);
			neededFoods.put(f.name, neededAmount);
		}
		
		// Update _lastMarketUsed:
		_lastMarketUsed++;
		if(_lastMarketUsed == _markets.size()) _lastMarketUsed = 0;
		// note: from now until the end of the action, lastMarketUsed is the current market to be ordered from
		
		// Send the message:
		// Note: the following boolean prevents the cook from sending more than one order at a time without first hearing back which items inside each order will be filled.
		// Note: the following line is placed before the call to msgPlaceOrder to prevent the Market's thread from sending msgOrderComing before this thread sets _orderPending to true.
		_awaitingMarketResponse = true;
		// note: we don't need to make a copy of neededFoods because this thread forgets the reference to it right after sending msgPlaceOrder.
		_markets.get(_lastMarketUsed).agent.msgPlaceOrder(this, neededFoods, _cashier);
	}
	
	
	
	// ------------------------------------------------------ HELPERS --------------------------------------------------------
	
	void printInventory()
	{
		print("Inventory:");
		for(Food f : _foods)
		{
			print("    " + f.name + ":");
			print("      inventory:     " + f.inventory + "/" + f.stockLevel);
			print("      low level:     " + f.lowLevel);
			print("      amount coming: " + f.amountComing);
		}
	}
}
