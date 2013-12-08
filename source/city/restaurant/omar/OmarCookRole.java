package city.restaurant.omar;

import gui.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.*;
import city.market.*;
import city.restaurant.RestaurantCookRole;
import city.restaurant.omar.Order.OrderStatus;
import city.restaurant.omar.gui.OmarCookGui;

public class OmarCookRole extends RestaurantCookRole {

	/**
	 * Restaurant Cook Agent
	 */
	//Data
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());

	Hashtable<String, Item> cookInventory;
	Timer cookTimer;
	Timer revolving_stand_timer = new Timer();
	String name;
	OmarCashierRole cashier;

	OmarCookGui cookGui = null;
	OmarRestaurant restaurant;

	public Semaphore cookSem = new Semaphore(0, true);

	enum Command {None, Leave};
	Command command;
	enum MarketStatus {available, ordering, waiting, paying, paid, gone};
	enum CheckState{notChecked,Checked};
	CheckState check_state = CheckState.notChecked;	

	public class MyMarket{
		public Market market;
		public MarketStatus marketState;
		public List<Item> currentOrder;
		public double currentOrderCost;

		public MyMarket(Market market){
			this.market = market;
			marketState = MarketStatus.available;
			currentOrder = Collections.synchronizedList(new ArrayList<Item>());
			currentOrderCost = 0;
		}
	}

	public OmarCookRole(OmarCashierRole cashier, PersonAgent p, OmarRestaurant r) { //starts out of food
		super(p);
		addMarkets();
		this.restaurant = r;
		name = "Chef Matt";
		this.cashier = cashier;
		//System.out.println("Chef Matt: About to Restock");

		cookInventory = new Hashtable<String, Item>();
		cookInventory.put("Pizza", new Item("Pizza", 0));
		cookInventory.put("Hot Dog", new Item("Hot Dog", 0));
		cookInventory.put("Burger", new Item("Burger", 0));
		cookInventory.put("Filet Mignon", new Item("Filet Mignon", 0));
		command = Command.None;
	}

	// Messages
	public void notifyCook(){
		stateChanged();
		check_state = CheckState.notChecked;
	}
	
	public void msgHereIsAnOrder(FoodTicket ticket){
		orders.add(new Order(ticket.getW(), ticket.getC().tableNum, this, ticket.getC()));
		stateChanged();
	}

	public void msgIHaveNoFood(Market market){
		synchronized(markets){
			System.out.println("Market is out of Food! Ordering from next Market, Paid previous market $2");
			for(MyMarket m: markets){
				if(m.market == market){
					m.marketState = MarketStatus.gone;
					markets.remove(m);
					System.out.println("Ordering from second Market");
					markets.get(0).marketState = MarketStatus.ordering;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgHereIsMyPrice(Market market, List<Item> currentOrder, int currentOrderCost){
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.market == market){
					m.currentOrderCost = currentOrderCost;
					m.currentOrder = currentOrder;
					m.marketState = MarketStatus.paying;
					stateChanged();
					return;
				}
			}
		}
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.gone){
					//markets.remove(m);
					return true;
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.ordering){
					purchaseFoodFromMarket(m);
					return true;
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.paying){
					restockFood(m);
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.paid){
					tellCashierToPayMarket(m);
				}
			}
		}	
		if(orders.isEmpty() && command == Command.Leave){
			leaveRestaurant();
		}

		if(!orders.isEmpty()){
			synchronized(orders){
				for(Order o: orders){
					if(o.status == OrderStatus.pending){
						cookOrder(o);
						return true;
					}
				}
			}
			synchronized(orders){
				for(Order o: orders){
					if(o.status == OrderStatus.cooked){
						System.out.println("Order " + o.toString() + " is ready.");
						tellWaiter(o);
						return true;
					}
				}
			}
			return true;
		}

		FoodTicket ticket = restaurant.revolving_stand.remove();
		if (ticket!=null){
			cookGui.DoGoToRevolvingStand();
			try {
				cookSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Order order = new Order(ticket.getW(), ticket.getC().tableNum, this, ticket.getC());
			orders.add(order);
			cookOrder(order);
			return true;
		}
		if (check_state == CheckState.notChecked){
			revolving_stand_timer.schedule(new TimerTask() {
				public void run() {
					print(AlertTag.OMAR_RESTAURANT, "Notify the cook to check revolving stand");
					notifyCook();
				}
			}, 10000);
			check_state = CheckState.Checked;
		}
		return false;
	}

	//Actions
	public void addMarkets(){
		for(int i = 0; i < Directory.markets().size(); i++){
			markets.add(new MyMarket(Directory.markets().get(i)));
				System.out.println("Added Market");
		}
	}

	public void purchaseFoodFromMarket(MyMarket m){
		List<Item> newOrder = new ArrayList<Item>();
		newOrder.add(new Item("Pizza", 5));
		newOrder.add(new Item("Hot Dog", 5));
		newOrder.add(new Item("Burger", 5));
		newOrder.add(new Item("Filet Mignon", 5));

		m.market.MarketCashier.msgPlaceOrder(this.restaurant, newOrder);
		m.marketState = MarketStatus.waiting;
		stateChanged();
	}

	public void restockFood(MyMarket m){
		synchronized(m.currentOrder){
			for(Item f: m.currentOrder){
				cookInventory.remove(f.name);
				cookInventory.put(f.name, f);
			}
		}
		System.out.println("Restocking from market");
		m.marketState = MarketStatus.paid;
		stateChanged();
	}

	public void cookOrder(Order o){
		String choice = o.toString();
		int currentInventory = cookInventory.get(choice).amount;
		if(currentInventory == 0){
			o.getWaiter().msgNeedRechoose(o.getCustomer());
			orders.remove(o);
			markets.get(0).marketState = MarketStatus.ordering; 
			stateChanged();
		} else{
			cookGui.DoGoToFridge();
			try {
				cookSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cookGui.DoGoToGrill(o.getTableNumber());
			try {
				cookSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cookGui.DoGoBackToRest();
			o.status = OrderStatus.cooking;
			System.out.println("Cooking Order " + o.toString());
			o.isCooking();
		}
	}

	public void tellWaiter(Order o){
		cookGui.setCurrentStatus(o.toString());
		cookGui.DoGoToGrill(o.getTableNumber());
		try {
			cookSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoMoveFoodToPlatingArea();
		System.out.println(this.name + ": Told Waiter " + o.getWaiter().getName()+ " that " + 
				o.toString() + " is ready");
		o.status = OrderStatus.pickup;
		o.getWaiter().msgOrderIsReady(o.getTableNumber());
		orders.remove(o);
		cookGui.DoGoBackToRest();
	}

	public void tellCashierToPayMarket(MyMarket m){ //initialize cashier
		cashier.msgPayInvoice(m.market, m.currentOrder);
		m.marketState = MarketStatus.available;
		System.out.println("Told Cashier To Pay Market");
		stateChanged();
	}

	//utilities

	public void setGui(OmarCookGui g){
		this.cookGui = g;
	}

	public void msgArrived(){
		cookSem.release();
	}
	//----------------------------------------------INTEGRATION-----------------------------
	@Override	//INTEGRATED
	public void msgOrderFulfillment(Market market, List<Item> order) {
		synchronized(markets){
			print(AlertTag.OMAR_RESTAURANT, market.name() + " is giving me food");
			for(MyMarket m:markets) {
				if(m.market == market){
					m.currentOrder = order;
					for(int i = 0; i < order.size(); i++){
						cookInventory.remove(order.get(i).name);
						cookInventory.put(order.get(i).name, order.get(i));
					}
					m.marketState = MarketStatus.paid;
					stateChanged();
					return;
				}
			}
		}
	}

	@Override
	public Place place() {
		return restaurant;
	}

	private void leaveRestaurant(){
		active = false;
		stateChanged();
	}

	@Override
	public void cmdFinishAndLeave() {
		command = Command.Leave;
		stateChanged();
	}
}
