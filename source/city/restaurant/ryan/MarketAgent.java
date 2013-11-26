package city.restaurant.ryan;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.lang.Math;

import restaurant.CookAgent.DishState;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.CookGui;
import restaurant.gui.HostGui;
import restaurant.interfaces.*;
import agent.Agent;

public class MarketAgent extends Agent implements Market{
	String name;
	Timer timer = new Timer();
	RyanCookRole cook;
	RyanCashierRole cashier;
	double cash = 0;
	double invoicebalance = 0;
	
	Map<String, Integer> Inventory = new HashMap<String, Integer>();
	Map<String, Double> Prices = new HashMap<String, Double>();
	List<Order> Orders = new ArrayList<Order>();
	List<Payment> Payments = new ArrayList<Payment>();
	enum OrderState{Waiting, Checking, Filling, Full, Partial, None, Fulfilled, Done};
	
	
	//Constructor
	public MarketAgent(String name){
		super();
		this.name = name;
		Inventory.put("Steak", randomNumber());
		Inventory.put("Chicken", randomNumber());
		Inventory.put("Pizza", 15);
		Inventory.put("Salad", 30);
		Prices.put("Steak", 5.00);
		Prices.put("Chicken", 3.00);
		Prices.put("Pizza", 2.00);
		Prices.put("Salad", 1.00);
		boolean empty = false;
	}
	
	public void setCook(RyanCookRole cook){
		this.cook = cook;
	}
	
	public void setCashier(RyanCashierRole cashier){
		this.cashier = cashier;
	}
	
	//Messages*************************************************************************************************************************************************
	public void msgPlaceOrder(String order, int amount){
		Do("Received order for " + order + " for amount " + amount);
		Orders.add(new Order(order, amount));
		stateChanged();
	}
	
	public void msgFillOrder(){
		stateChanged();
	}
	
	public void msgHeresPayment(String choice, double payment){
		Do("Received payment for " + choice + " for amount " + payment);
		Payments.add(new Payment(choice, payment));
		stateChanged();
	}
	
	
	
	//Scheduler******************************************************************************************************************************************************************************
	protected boolean pickAndExecuteAnAction() {
		synchronized(Orders){
			for(int i = 0; i < Orders.size(); i++){
				Order temp = Orders.get(i);
				if(temp.state == OrderState.Waiting){
					checkStock(temp);
					return true;
				}
				if(temp.state == OrderState.Filling){
					fulfillOrder(temp);
					return true;
				}
			}
		}
		synchronized(Payments){
			if(!Payments.isEmpty()){
				balanceBooks(Payments.get(0));
			}
		}
		return false;
	
	}
	
	//Actions******************************************************************************************************************************************************************************
	public void checkStock(Order order){
		order.state = OrderState.Checking;
		order.stockTime();
	}
	
	public void fulfillOrder(Order order){
		order.state = OrderState.Done;
		double cost;
		if(Inventory.containsKey(order.choice)){
			if(Inventory.get(order.choice) == 0){
				Do("Out of stock for " + order.choice + " " + order.given + " " + order.amount);
				order.given = 0;
				cook.msgGivenStock(order.choice, 0, order.amount);
			}
			else if(Inventory.get(order.choice) <= order.amount){
				order.given = Inventory.get(order.choice);
				Do("Given " + order.given + " of " + order.choice + ": Now out of stock");
				Inventory.put(order.choice, 0);
				cook.msgGivenStock(order.choice, order.given, order.amount);
				cost = order.given*Prices.get(order.choice);
				Do("Here is a bill for " + order.choice + " of amount " + cost + " to " + cashier.getName());
				invoicebalance += cost;
				cashier.msgHeresBill(this, order.choice, cost);
			}
			else if(Inventory.get(order.choice) >= order.amount){
				order.given = order.amount;
				Do("Given " + order.given + " of " + order.choice);
				int left = Inventory.get(order.choice)-order.amount;
				Inventory.put(order.choice, left);
				cook.msgGivenStock(order.choice, order.given, order.amount);
				cost = order.given*Prices.get(order.choice);
				Do("Here is a bill for " + order.choice + " of amount " + cost + " to " + cashier.getName());
				invoicebalance += cost;
				cashier.msgHeresBill(this, order.choice, cost);
			}
		}
	}
	
	public void balanceBooks(Payment payment){
		invoicebalance -= payment.amount;
		cash += payment.amount;
		Do("Restaurant Ryan has paid " + payment.amount + " for " + payment.choice);
		Do("Amount Restaurant Ryan still owes to " + getName() + " for other orders is " + invoicebalance);
		Payments.remove(payment);
	}
	
	//Utilities************************************************************************************************************************************************* 
	public int randomNumber(){
		Random generator = new Random();
		int inventory = generator.nextInt(20) + 40;
		return inventory;
	}
	
	public String getName(){
		return name;
	}
	
	class Order{
		String choice;
		int amount;
		int given;
		OrderState state = OrderState.Waiting;
		
		Order(String order, int amount){
			this.choice = order;
			this.amount = amount;
			given = 0;
		}
		
		public void stockTime(){
			Do("Timer on: Stock " + choice);
			timer.schedule(new TimerTask() {
				public void run() {
					Do("Timer off: Stock");
					state = OrderState.Filling;
					stateChanged();
					msgFillOrder();
				}
			},
			2000);
		}
	}
	
	class Payment{
		String choice;
		double amount;
		
		Payment(String choice, double amount){
			this.choice = choice;
			this.amount = amount;
		}
	}
}
