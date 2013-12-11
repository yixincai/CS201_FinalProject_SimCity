//package city.restaurant.eric;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.ConcurrentModificationException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import agent.Agent;
//import city.restaurant.eric.interfaces.*;
//
////TODO THIS CLASS IS ONLY HERE NOW FOR REFERENCE; REFACTOR CODE TO USE THE CITY MARKET
//public class OLD_EricMarketAgent extends Agent implements OLD_EricMarket
//{
//	// ----------------------------------------- DATA ----------------------------------------------
//	
//	// Personal data:
//	private String _name;
//	
//	// Agent data:
//	private class Order
//	{
//		public EricCook cook;
//		public EricCashier cashier;
//		public Map<String,Integer> foods = new HashMap<String,Integer>();
//		public OrderState state;
//		public double owedAmount = 0;
//	}
//	private enum OrderState { NEEDS_PROCESSING, PROCESSING, READY_TO_DELIVER, OWED_MONEY }
//	private List<Order> _orders = Collections.synchronizedList(new ArrayList<Order>());
//	private class FoodData
//	{
//		public int inventory;
//		public double price;
//		public FoodData(int inventory, double price) {
//			this.inventory = inventory;
//			this.price = price;
//		}
//	}
//	private Map<String, FoodData> _foods = new HashMap<String, FoodData>();
//	private Timer _orderTimer = new Timer();
//	private int _orderTime; // in seconds
//	
//	// ----------------------------------------- CONSTRUCTOR ----------------------------------------------
//	/**
//	 * @param whichMarket hack for choosing which of the 3 hard-coded markets to choose from (can be 0, 1, or 2)
//	 */
//	public OLD_EricMarketAgent(String name, int whichMarket)
//	{
//		_name = name;
//		
//		switch(whichMarket)
//		{
//			case 0:
//				//         name                   (inventory, price)
//				_foods.put("Steak",   new FoodData(4,         20));
//				_foods.put("Chicken", new FoodData(4,         9));
//				_foods.put("Salad",   new FoodData(4,         2));
//				_foods.put("Pizza",   new FoodData(4,         4));
//				_orderTime = 5;
//				break;
//			case 1:
//				_foods.put("Steak",   new FoodData(4,         20));
//				_foods.put("Chicken", new FoodData(3,         9));
//				_foods.put("Salad",   new FoodData(3,         2));
//				_foods.put("Pizza",   new FoodData(3,         4));
//				_orderTime = 3;
//				break;
//			case 2:
//				_foods.put("Steak",   new FoodData(3,         20));
//				_foods.put("Chicken", new FoodData(3,         9));
//				_foods.put("Salad",   new FoodData(3,         2));
//				_foods.put("Pizza",   new FoodData(3,         4));
//				_orderTime = 2;
//				break;
//			default:
//				print("Error: incorrect instantiation of market: whichMarket:int is out of range");
//		}
//	}
//
//	// ----------------------------------------- PROPERTIES ----------------------------------------------
//	// No setCook cause cook will be set in the order-food message
//	public String getName() { return _name; }
//	
//	
//	
//	// ----------------------------------------- MESSAGES ----------------------------------------------
//	
//	public void msgPlaceOrder(EricCook sender, Map<String,Integer> foods, EricCashier cashier)
//	{
//		print("Order placed by " + sender.name() + ":");
//		
//		Order o = new Order();
//		// Copy the foods over so data isn't shared
//		for(Map.Entry<String,Integer> e : foods.entrySet())
//		{
//			print(" > " + e.getValue() + "  " + e.getKey());
//			o.foods.put(new String(e.getKey()), new Integer(e.getValue()));
//		}
//		o.cook = sender;
//		o.cashier = cashier;
//		o.state = OrderState.NEEDS_PROCESSING;
//		_orders.add(o);
//		stateChanged();
//	}
//	
//	private void msgOrderDone(Order o) // from timer, so it's not in the Market interface.
//	{
//		print("Order for " + o.cook.name() + " is ready.");
//		o.state = OrderState.READY_TO_DELIVER;
//		stateChanged();
//	}
//	
//	public void msgPayment(EricCashier sender, double amount)
//	{
//		Order current = null;
//		for(Order o : _orders)
//		{
//			if(o.cashier == sender)
//			{
//				current = o;
//				break;
//			}
//		}
//		current.owedAmount -= amount;
//		// This is placed outside the above for-loop in order to avoid ConcurrentModificationExceptions.
//		if(current.owedAmount < .01)
//		{
//			print("Received payment of $" + amount + " from " + sender.name() + ". Order paid in full.");
//			_orders.remove(current);
//		}
//		else
//		{
//			print("Received payment of $" + amount + " from " + sender.name() + ". Still owes " + current.owedAmount + ".");
//		}
//		stateChanged();
//	}
//	
//	
//	
//	// ----------------------------------------- SCHEDULER ----------------------------------------------
//	@Override
//	protected boolean pickAndExecuteAnAction()
//	{
//		try {
//			for(Order o : _orders) {
//				if(o.state == OrderState.NEEDS_PROCESSING) {
//					actProcessOrder(o);
//					return true;
//				}
//			}
//			for(Order o : _orders) {
//				if(o.state == OrderState.READY_TO_DELIVER) {
//					actDeliverOrder(o);
//					return true;
//				}
//			}
//		}
//		catch(ConcurrentModificationException e) { return true; }
//		return false;
//	}
//	
//	
//
//	// ----------------------------------------- ACTIONS ----------------------------------------------
//	
//	private void actProcessOrder(final Order o)
//	{
//		print("Processing order from " + o.cook.name() + ".");
//		
//		// Fill up foodsComing
//		Map<String,Integer> foodsComing = new HashMap<String,Integer>();
//		for(Map.Entry<String,Integer> e : o.foods.entrySet())
//		{
//			// Set amountComing to the desired value in the order
//			int amountComing = e.getValue();
//			
//			// If the inventory doesn't have enough, set amountComing to the rest of the inventory
//			if(_foods.get(e.getKey()).inventory < amountComing) amountComing = _foods.get(e.getKey()).inventory;
//			
//			// Decrease the inventory for this item
//			_foods.get(e.getKey()).inventory -= amountComing;
//			
//			// Set the order's foods to the correct amountComing so that the correct amount is sent
//			e.setValue(amountComing);
//			
//			// Fill up foodsComing with the current value.
//			foodsComing.put(new String(e.getKey()), amountComing);
//		}
//		
//		o.state = OrderState.PROCESSING; // putting this before sending the message to prevent concurrency errors
//		
//		// Notify the cook which foods are coming
//		o.cook.msgOrderComing(this, foodsComing); // note: We don't need to copy because the reference to foodsComing will die at the end of this function
//
//		_orderTimer.schedule(
//				new TimerTask() {
//					public void run() { msgOrderDone(o); }
//				},
//				_orderTime * 1000 // orderTime (seconds) * 1000 (ms/second)
//				);
//	}
//	
//	private void actDeliverOrder(Order o)
//	{
//		print("Delivering order to " + o.cook.name() + ".");
//		// note: We don't need to copy because this MarketAgent's reference to o.foods will die on _orders.remove(o)
//		o.cook.msgDelivery(this, o.foods);
//		
//		// Set owedAmount and send bill to Cashier
//		for(Map.Entry<String,Integer> e : o.foods.entrySet())
//		{
//			o.owedAmount += _foods.get(e.getKey()).price * e.getValue();
//		}
//		
//		o.state = OrderState.OWED_MONEY;
//		
//		print(o.cashier.name() + " owes " + o.owedAmount + ".");
//		o.cashier.msgYouOwe(this, o.owedAmount);
//	}
//}
