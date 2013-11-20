package city.restaurant.omar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import city.restaurant.omar.interfaces.Market;
import agent.Agent;   

public class MarketAgent extends Agent implements Market {


	/**
	 * Restaurant Cook Agent
	 */
		//Data
		Hashtable<String, Integer> inventory;
		MyCook myCook;
		String name;
		double moneyFromCashier;
		private static int marketSupply = 2;
		
		class MyCook { //similar to myCustomer
			CookAgent cook;
			CookStatus cookState;
			List<Food> currentOrder;
			int currentOrderCost;
			
			MyCook(CookAgent c){
				this.cook = c;
				currentOrder = Collections.synchronizedList(new ArrayList<Food>());
			}
		}
		
		enum CookStatus {nothing, ordering, paying, paid};

		public MarketAgent(CookAgent c) {
			super();
			myCook = new MyCook(c);
			name = "Market";
			moneyFromCashier = 0;

			inventory = new Hashtable<String, Integer>();
			inventory.put("Pizza", marketSupply);
			inventory.put("Hot Dog", marketSupply);
			inventory.put("Burger", marketSupply);
			inventory.put("Filet Mignon", marketSupply);
		}
		
		// Messages
		public void msgINeedFood(CookAgent cook, List<Food> order){
			myCook.cook = cook;
			myCook.cookState = CookStatus.ordering; 
			myCook.currentOrder = order;
			stateChanged();
		}
		
		public void msgTakeMoney(CashierAgent cashier, double price) { //essentially pays for restock
			myCook.cookState = CookStatus.paid;
			moneyFromCashier+=price;
			stateChanged();
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			if(myCook.cookState == CookStatus.ordering){
				calcPurchase();
				return true;
			}
			
			if(myCook.cookState == CookStatus.paid){
				processOrder();
				return true;
			}
			return false;
		}

		//Actions
		public void calcPurchase(){
			boolean outOfStock = true;
			synchronized(myCook.currentOrder){
			for(Food f: myCook.currentOrder){
				if(f.inventoryAmount < inventory.get(f.type)){
					f.inventoryAmount = inventory.get(f.type);
					myCook.currentOrderCost += f.inventoryAmount;
						if(f.inventoryAmount > 0){
							outOfStock = false;
						}
				}
			}
			}
			if(outOfStock){
				myCook.cook.msgIHaveNoFood(this);
				myCook.cookState = CookStatus.nothing;
			} else{
				myCook.cook.msgHereIsMyPrice(this, myCook.currentOrder, myCook.currentOrderCost);
				myCook.cookState = CookStatus.paying;
			}
		}
		
		public void processOrder(){
			System.out.println("Market order complete");
			myCook.cookState = CookStatus.nothing;
			stateChanged();
		}

		//utilities
		public void reduceInventory(){
			inventory.remove("Pizza");
			inventory.remove("Hot Dog");
			inventory.remove("Burger");
			inventory.remove("Filet Mignon");
			
			inventory.put("Pizza", 0);
			inventory.put("Hot Dog", 0);
			inventory.put("Burger", 0);
			inventory.put("Filet Mignon", 0);
		}	
}