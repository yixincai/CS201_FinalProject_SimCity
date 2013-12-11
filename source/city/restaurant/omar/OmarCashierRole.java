package city.restaurant.omar;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.omar.gui.OmarCashierGui;
import city.restaurant.yixin.YixinRestaurant;

public class OmarCashierRole extends RestaurantCashierRole {

	/**
	 * Restaurant Cashier Agent
	 */
	//Data
	public double cashierFunds;
	private OmarRestaurant restaurant;
	private OmarCashierGui gui;
	public class MyCustomer { //similar to mycustomer in waiter
		OmarWaiterRole waiter;
		OmarCustomerRole customer;
		String choice;
		public CustomerState state;
		double money;

		MyCustomer(OmarCustomerRole c, OmarWaiterRole w, String choice){
			waiter = w;

			this.customer = c;
			this.choice = choice;
			money = 0;
		}
	}

	public enum CustomerState {paying, paid, awaitingChange, canAfford, cantAfford};
	enum Command{None, Leave};
	Command command = Command.None;

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


	enum MarketOrderState{none, billReceived, payBill};
	private class Order {
		public Market market;
		public Map<String, Double> price_list;
		public double cost;
		List<Item> orderItems;
		MarketOrderState orderState = MarketOrderState.none;

		public Order(Market market, Map<String, Double> price_list, double bill){
			this.market = market;
			this.price_list = price_list;
			this.cost = bill;
		}

		public void setOrderItems(List<Item> orderItems){
			this.orderItems = orderItems;
		}
	}

	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Hashtable<String, Double> foodPrices;
	public Menu menu;
	private String name;

	public OmarCashierRole(PersonAgent p, OmarRestaurant r) {
		super(p);
		this.restaurant = r;
		gui = new OmarCashierGui(this);
		restaurant.animationPanel().addGui(gui);
		command = Command.None;
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
		synchronized(orders){
			for(Order o: orders){
				if(o.orderState == MarketOrderState.payBill){
					processOrder(o);
					return true;
				}
			}
		}
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

		if(command == Command.Leave && orders.isEmpty() && myCustomers.isEmpty() && restaurant.getNumberOfCustomers() == 0){
			leave();
			return true;
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
		}//
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
		print(AlertTag.OMAR_RESTAURANT, "Processed Order.  Gave market $" + (int)o.cost);
		o.market.MarketCashier.msgHereIsPayment(restaurant, o.cost);
		cashierFunds-=(int)o.cost;
		orders.remove(o);
		stateChanged();
	}

	void leave(){
		command = Command.None;
		active = false;
	}

	//Messages
	public void msgCustomerDoneAndNeedsToPay(OmarCustomerRole c, OmarWaiterRole w, String choice){
		MyCustomer m = new MyCustomer(c, w, choice);
		myCustomers.add(m);
		m.state = CustomerState.paying;
		stateChanged();
	}

	public void msgTakeMoney(OmarCustomerRole c, int customerMoney){
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

	public void msgICantAffordMyMeal(OmarCustomerRole c){
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

	//utilities

	public void msgHereIsTheBill(Market m, double bill,
			Map<String, Double> price_list) {
		orders.add(new Order(m, price_list, bill));
		print(AlertTag.OMAR_RESTAURANT, "Market Order Added");
		stateChanged();
	}

	public void msgPayInvoice(Market m, List<Item> currentOrder){
		for(Order o: orders){
			if(o.market == m){
				o.setOrderItems(currentOrder);
				o.orderState = MarketOrderState.payBill;
				stateChanged();
				return;
			}
		}
	}

	@Override 
	public void msgHereIsTheChange(Market m, double change) {
		cashierFunds+=change;
	}

	@Override
	public void msgTransactionComplete(double amount, Double balance,
			Double debt, int newAccountNumber) {
		//Never called
	}

	@Override
	public Place place() {
		return restaurant;
	}

	@Override
	public void cmdFinishAndLeave() {
		command = Command.Leave;
		stateChanged();
	}	
}
