package city.restaurant.omar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import city.restaurant.RestaurantCookRole;

public class OmarCookRole extends RestaurantCookRole {

	/**
	 * Restaurant Cook Agent
	 */
		//Data
		List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
		
		Hashtable<String, Food> cookInventory;
		Timer cookTimer;
		
		String name;
		CashierAgent cashier;
		
		CookGui cookGui = null;
		
		public Semaphore cookSem = new Semaphore(0, true);
		
		enum MarketStatus {available, ordering, paying, paid, gone};
		
		public class MyMarket{
			public MarketAgent market;
			public MarketStatus marketState;
			public List<Food> currentOrder;
			public double currentOrderCost;
			
			public MyMarket(MarketAgent market){
				this.market = market;
				marketState = MarketStatus.available;
				currentOrder = Collections.synchronizedList(new ArrayList<Food>());
				currentOrderCost = 0;
			}
		}

		public CookAgent(CashierAgent cashier) { //starts out of food
			super();
			name = "Chef Matt";
			this.cashier = cashier;
			System.out.println("Chef Matt: About to Restock");
			
			cookInventory = new Hashtable<String, Food>();
			cookInventory.put("Pizza", new Food("Pizza", 1200.0, 0));
			cookInventory.put("Hot Dog", new Food("Hot Dog", 1500.0, 0));
			cookInventory.put("Burger", new Food("Burger", 2000.0, 0));
			cookInventory.put("Filet Mignon", new Food("Filet Mignon", 3500.0, 0));
		}
		
		// Messages
		public void msgHereIsAnOrder(WaiterAgent w, CustomerAgent c){
			orders.add(new Order(w, c.tableNum, this, c));
			stateChanged();
		}
		
		public void msgIHaveNoFood(MarketAgent market){
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
		
		public void msgHereIsMyPrice(MarketAgent market, List<Food> currentOrder, int currentOrderCost){
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
		
		public void msgTakeMyFood(MarketAgent market, List<Food> currentOrder){
		synchronized(markets){
			print(market.toString() + " is giving me food");
			for(MyMarket m:markets) {
				if(m.market == market){
					m.currentOrder = currentOrder;
					m.marketState = MarketStatus.paid;
					stateChanged();
					return;
				}
			}
		}
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.gone){
					markets.remove(m);
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
						Do("Order " + o.toString() + " is ready.");
						tellWaiter(o);
						return true;
					}
				}
				}
				return true;
			}
			return false;
		}

		//Actions
		public void addMarket(MarketAgent m){
			markets.add(new MyMarket(m));
		}
		
		public void purchaseFoodFromMarket(MyMarket m){
			List<Food> newOrder = new ArrayList<Food>();
			newOrder.add(cookInventory.get("Pizza"));
			newOrder.add(cookInventory.get("Hot Dog"));
			newOrder.add(cookInventory.get("Burger"));
			newOrder.add(cookInventory.get("Filet Mignon"));
			
			m.market.msgINeedFood(this, newOrder);
		}
		
		public void restockFood(MyMarket m){
		synchronized(m.currentOrder){
			for(Food f: m.currentOrder){
				cookInventory.remove(f.type);
				cookInventory.put(f.type, f);
			}
		}
			Do("Restocking from market");
			m.marketState = MarketStatus.paid;
			stateChanged();
		}
		
		public void cookOrder(Order o){
			String choice = o.toString();
			int currentInventory = cookInventory.get(choice).inventoryAmount;
			if(currentInventory == 0){
				o.getWaiter().msgNeedRechoose(o.getCustomer());
				orders.remove(o);
				markets.get(0).marketState = MarketStatus.ordering; 
				stateChanged();
			} else{
				cookInventory.get(choice).decrementInventory();
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
				Do("Cooking Order " + o.toString());
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
			Do("Told Waiter " + o.getWaiter().toString()+ " that " + 
					o.toString() + " is ready");
			o.status = OrderStatus.pickup;
			o.getWaiter().msgOrderIsReady(o.getTableNumber());
			orders.remove(o);
			cookGui.DoGoBackToRest();
		}
		
		public void tellCashierToPayMarket(MyMarket m){ //initialize cashier
			cashier.msgPayTheMarket(m.market, m.currentOrderCost);
			m.marketState = MarketStatus.available;
			Do("Told Cashier To Pay Market");
			stateChanged();
		}

		//utilities
		public String toString(){
			return name;
		}
		
		public void setGui(CookGui g){
			this.cookGui = g;
		}
		
		public void msgArrived(){
			cookSem.release();
		}
}
