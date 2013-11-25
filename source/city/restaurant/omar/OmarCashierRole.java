package city.restaurant.omar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import city.restaurant.RestaurantCashierRole;

public class OmarCashierRole extends RestaurantCashierRole {

	/**
	 * Restaurant Cashier Agent
	 */
		//Data
		public double cashierFunds;
		public class MyCustomer { //similar to mycustomer in waiter
			Waiter waiter;
			Customer customer;
			String choice;
			public CustomerState state;
			double money;
			
			MyCustomer(Customer c, Waiter w, String choice){
				waiter = w;
				
				this.customer = c;
				this.choice = choice;
				money = 0;
			}
		}
		
		public enum CustomerState {paying, paid, awaitingChange, canAfford, cantAfford};

		class Food {
			String foodType;
			double cookTime;
			int price;
			int inventoryAmount;
			
			Food(String type, double cookTime, int inventoryAmount){
				this.foodType = type;
				this.cookTime = cookTime;
				this.inventoryAmount = inventoryAmount;
			}
		}
		
		private class Order {
			public Market market;
			public double cost;
			
			public Order(Market market2, double cost){
				this.market = market2;
				this.cost = cost;
			}
		}
		
		public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		public Hashtable<String, Double> foodPrices;
		public Menu menu;
		private String name;
//
		public CashierAgent() {
			super();
			cashierFunds = 10000;
			menu = new Menu();
			foodPrices = new Hashtable<String, Double>();
		
			name = "Cashier David";
			
			foodPrices.put("Pizza", 12.0);
			foodPrices.put("Hot Dog", 15.0);
			foodPrices.put("Burger", 20.0);
			foodPrices.put("Filet Mignon", 35.0);
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		public boolean pickAndExecuteAnAction() {
		synchronized(myCustomers){
			for(MyCustomer m: myCustomers){
				if(m.state == CustomerState.paying){
					calcCheck(m);
					return true;
				}
			}
		}
		synchronized(myCustomers){
			for(MyCustomer m: myCustomers){
				if(m.state == CustomerState.canAfford){
					calcChange(m);
					return true;
				}
			}
		}
		synchronized(myCustomers){
			for(MyCustomer m: myCustomers){
				if(m.state == CustomerState.cantAfford){
					customerDies(m);
					return true;
				}
			}
		}
		synchronized(orders){
			for(Order o: orders){
				processOrder(o);
				return true;
			}
		}
			return false;
		}

		//Actions
		void calcCheck(MyCustomer m){
			String tempChoice = m.choice;
			double checkAmount = 0.0;
			if(tempChoice.equals("Pizza")){
				checkAmount = 12.99;
			} else if(tempChoice.equals("Hot Dog")){
				checkAmount = 14.99;
			} else if(tempChoice.equals("Burger")){
				checkAmount = 19.99;
			} else {
				checkAmount = 34.99;
			}
			m.waiter.msgHereIsCheck(m.customer, checkAmount);
			m.state = CustomerState.awaitingChange;
			stateChanged();
		}
		
		void calcChange(MyCustomer m){
			String tempChoice = m.choice;
			double change = m.money - foodPrices.get(tempChoice);;
			m.customer.msgHereIsYourChange(change);
			myCustomers.remove(m);
		}
		
		void customerDies(MyCustomer m){
			m.customer.msgGoDie();
			m.waiter.msgCustomerPaidWithLabor(m.customer);
			myCustomers.remove(m);
		}
		
		void processOrder(Order o){
			Do("Processed Order.  Gave market $" + (int)o.cost);
			o.market.msgTakeMoney(this, (int)o.cost);
			cashierFunds-=(int)o.cost;
			orders.remove(o);
			stateChanged();
		}
		
		//Messages
		public void msgCustomerDoneAndNeedsToPay(Customer c, Waiter w, String choice){
			MyCustomer m = new MyCustomer(c, w, choice);
			myCustomers.add(m);
			m.state = CustomerState.paying;
			stateChanged();
		}
		
		public void msgTakeMoney(Customer c, int customerMoney){
			synchronized(myCustomers){
			int i;
			for(i = 0; i < myCustomers.size(); i++){
				if(myCustomers.get(i).customer == c){
					break;
				}
			}
			
			myCustomers.get(i).state = CustomerState.canAfford;
			myCustomers.get(i).money = customerMoney;
			stateChanged();
			}
		}
		
		public void msgICantAffordMyMeal(Customer c){
			synchronized(myCustomers){
			int i;
			for(i = 0; i < myCustomers.size(); i++){
				if(myCustomers.get(i).customer == c){
					break;
				}
			}
			myCustomers.get(i).state = CustomerState.cantAfford;
			stateChanged();
			}
		}
		
		public void msgPayTheMarket(Market market, double currentOrderCost){
			orders.add(new Order(market, currentOrderCost));
			Do("Market Order Added");
			stateChanged();
		}
		

		//utilities
		public String toString(){
			return name;
		}	
}
