package city.restaurant.eric;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import city.Directory;
import city.Place;
import city.interfaces.Person;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCookRole;
import city.restaurant.eric.gui.EricCookGui;
import city.restaurant.eric.interfaces.*;

public class EricCookRole extends RestaurantCookRole implements EricCook
{
	// --------------------------------------- DATA ------------------------------------------------
	
	// Constants:
	/** Number of seconds to wait before checking revolving stand. */
	private static final int CHECK_REVOLVING_STAND_INTERVAL = 5;
	
	// Correspondence:
	// use Order.waiter for waiter correspondence.
	private EricCashier _cashier;
	private EricRestaurant _restaurant;
	private EricCookGui _gui;
	
	// Agent data:
	private class Order
	{
		public EricWaiter waiter;
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
		public int neededAmount() // the amount that is needed to fill the inventory back up to the stockLevel.
		{
			return stockLevel - inventory;
		}
		
		public Food(String name, int cookingTime, int stockLevel, int inventory, int lowLevel)
		{
			this.name = name;
			this.cookingTime = cookingTime;
			this.stockLevel = stockLevel;
			this.inventory = inventory;
			this.lowLevel = lowLevel;
		}
	}
	private List<Food> _foods = Collections.synchronizedList(new ArrayList<Food>());
	private int _lastMarketUsed = -1; // starts at -1 so that when we increment it before first using, it will be zero
	private boolean _awaitingMarketDelivery = false; // indicates whether an order to a market is pending the market's response (to say what's coming and what's not coming in the order)
	private class Invoice
	{
		public Map<String, Integer> foodsReceived = new HashMap<String, Integer>();
		public Market market;
	}
	private List<Invoice> _invoices = Collections.synchronizedList(new ArrayList<Invoice>());
	
	// Utility:
	private Timer _schedulerTimer = new Timer();
	
	
	
	// ------------------------------------------ CONSTRUCTOR & PROPERTIES ----------------------------------------
	public EricCookRole(Person person, EricRestaurant restaurant)
	{
		super(person);
		_restaurant = restaurant;
		
		//         new Food(name, cookingTime, stockLevel, inventory, lowLevel)
		_foods.add(new Food("Steak",   15,     8,          1,         5       ));
		_foods.add(new Food("Chicken",  7,     8,          3,         7       ));
		_foods.add(new Food("Salad",    2,     8,          2,         6       ));
		_foods.add(new Food("Pizza",   15,     8,          5,         6       ));
	}
	public String name() { return _person.name(); }
	public void setCashier(EricCashier c) { _cashier = c; }
	public Place place() { return _restaurant; }
	private EricRevolvingStand revolvingStand() { return _restaurant.revolvingStand(); }
	public void setGui(EricCookGui gui) { _gui = gui; }
	
	
	
	// ------------------------------------------- MESSAGES ----------------------------------------------
	
	public void msgHereIsOrder(EricWaiter sender, String choice, int table)
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
	
	/*
	public void msgOrderComing(OLD_EricMarket sender, Map<String,Integer> foodsComing) // from Market
	{
		// Set mySender to the correct MyMarket
		//MyMarket mySender = null;
		//for(MyMarket m : _markets) { if(m.agent == sender) mySender = m; }
		
		for(Food f : _foods)
		{
			print(AlertTag.ERIC_RESTAURANT,"Will receive " + foodsComing.get(f.name) + " " + f.name + " from " + sender.getName());
			f.amountComing += foodsComing.get(f.name);
			
			f.updateAmountNeededToFulfillOrder();
		}
		
		_awaitingMarketDelivery = false;
		
		printInventory();
		stateChanged();
	}
	*/
	
	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) {
		print(AlertTag.ERIC_RESTAURANT,"Received a delivery from " + m.name());
		Invoice newInvoice = new Invoice();
		newInvoice.market = m;
		for(Item i : order)
		{
			print(AlertTag.ERIC_RESTAURANT,"Received " + i.amount + "  " + i.name);
			for(Food f : _foods)
			{
				if(f.name.equals(i.name))
				{
					f.inventory += i.amount;
					break;
				}
			}
			newInvoice.foodsReceived.put(i.name, i.amount);
		}
		_invoices.add(newInvoice);
		
		printInventory();
		stateChanged();
	}
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
	}
	
	
	
	// ----------------------------------------- SCHEDULER -------------------------------------------------
	public boolean pickAndExecuteAnAction()
	{
		if(actCheckRevolvingStand()) return true;
		
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
		
		Invoice invoice = null;
		synchronized(_invoices) {
			if(!_invoices.isEmpty()) {
				invoice = _invoices.get(0);
			}
		}
		if(invoice != null) {
			actSendInvoice(invoice);
			return true;
		}
		
		if(!_awaitingMarketDelivery) {
			synchronized(_foods) {
				for(Food f : _foods) {
					if((f.neededAmount() > 0) || (f.inventory <= f.lowLevel)) {
						actRestock();
						return true;
					}
				}
			}
		}
		
		// Set timer to call stateChanged and check revolving stand again, every CHECK_REVOLVING_STAND_INTERVAL number of seconds.
		_schedulerTimer.schedule(new TimerTask() { public void run() { stateChanged(); } }, CHECK_REVOLVING_STAND_INTERVAL * 1000);
		
		return false;
	}
	
	
	
	// ------------------------------------------ ACTIONS --------------------------------------------------
	
	private boolean actCheckRevolvingStand()
	{
		//_gui.doGoToRevolvingStand();
		//waitForGuiToReachDestination();
		
		EricRevolvingStand.Order o = revolvingStand().remove();
		if(o != null) {
			this.msgHereIsOrder(o.waiter, o.choice, o.table);
			return true;
		}
		return false;
	}
	
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
			
			print(AlertTag.ERIC_RESTAURANT,"Order for " + o.waiter.name() + " is cooking.");
			
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
		print(AlertTag.ERIC_RESTAURANT,"Order for " + o.waiter.name() + " is ready.");
		
		o.waiter.msgOrderReady(o.food.name, o.table);
		_orders.remove(o);
	}
	
	void actRestock()
	{
		print(AlertTag.ERIC_RESTAURANT,"Ordering needed food.");
		printInventory();
		
		// Fill up neededFoods:
		List<city.market.Item> neededFoods = new ArrayList<city.market.Item>();
		for(Food f : _foods) {
			print(AlertTag.ERIC_RESTAURANT,"Ordering " + f.neededAmount() + " " + f.name);
			neededFoods.add(new city.market.Item(f.name, f.neededAmount()));
		}
		
		List<Market> markets = Directory.markets();
		
		// Update _lastMarketUsed:
		_lastMarketUsed++;
		if(_lastMarketUsed == markets.size()) _lastMarketUsed = 0;
		// note: from now until the end of the action, lastMarketUsed is the current market to be ordered from
		
		// Send the message:
		// Note: the following boolean prevents the cook from sending more than one order at a time without first hearing back which items inside each order will be filled.
		// Note: the following line is placed before the call to msgPlaceOrder to prevent the Market's thread from sending msgOrderComing before this thread sets _orderPending to true.
		_awaitingMarketDelivery = true;
		// note: we don't need to make a copy of neededFoods because this thread forgets the reference to it right after sending msgPlaceOrder.
		markets.get(_lastMarketUsed).getCashier().msgPlaceOrder(_restaurant, neededFoods);
	}
	
	private void actSendInvoice(Invoice invoice)
	{
		print(AlertTag.ERIC_RESTAURANT, "Sending invoice to the cashier.");
		
		_cashier.msgIReceivedTheseFoods(invoice.market, invoice.foodsReceived);
		_invoices.remove(invoice);
	}
	
	
	
	// ------------------------------------------------------ HELPERS --------------------------------------------------------
	
	void printInventory()
	{
		print(AlertTag.ERIC_RESTAURANT,"Inventory:");
		for(Food f : _foods)
		{
			print(AlertTag.ERIC_RESTAURANT,"    " + f.name + ":");
			print(AlertTag.ERIC_RESTAURANT,"      inventory:     " + f.inventory + "/" + f.stockLevel);
			print(AlertTag.ERIC_RESTAURANT,"      low level:     " + f.lowLevel);
		}
	}
	
	public void clearInventory(){
		for(Food f : _foods)
		{
			f.inventory = 0;
		}
	}
}
