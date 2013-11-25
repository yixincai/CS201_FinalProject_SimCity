package city.market;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.Directory;
import city.PersonAgent;
import city.Place;
import city.bank.BankTellerRole;
import city.market.gui.MarketCashierGui;
import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketCustomer;
import city.restaurant.Restaurant;
import agent.Role;
import utilities.EventLog;
import utilities.LoggedEvent;

public class MarketCashierRole extends Role implements MarketCashier{

	public MarketCashierGui gui;
		
	public EventLog log = new EventLog();
	public Map<String, Good> inventory = new HashMap<String, Good>();
	public List<CustomerOrder> customers = new ArrayList<CustomerOrder>(); 
	public List<RestaurantOrder> restaurantOrders = new ArrayList<RestaurantOrder>();
	
	Market market;
	double moneyInHand = 1000, moneyInBank;
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	public enum MoneyState{OrderedFromBank, none}
	MoneyState money_state = MoneyState.none;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	public MarketCashierRole(PersonAgent person, Market m){
		super(person);
		this.market = m;
		inventory.put("Steak", new Good("Steak", 10, 1000));
		inventory.put("Chicken", new Good("Chicken", 7, 1000));		
		inventory.put("Salad", new Good("Salad", 3, 1000));
		inventory.put("Pizza", new Good("Pizza", 4, 1000));
		inventory.put("Car", new Good("Car", 200, 100));
		inventory.put("Meal", new Good("Meal", 5, 1000));
		moneyInHand = 50;
	}
	
	public void setGui(MarketCashierGui g) {
		gui = g;
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return market;
	}
	
	public void msgAnimationFinished() {
		//from animation
		atDestination.release();
		stateChanged();
	}
	
	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();
	}

	//customer messages
	public void msgPlaceOrder(MarketCustomer mc, List<Item> order){
		log.add(new LoggedEvent("Received PlaceOrder from customer."));
		customers.add(new CustomerOrder(mc,order, CustomerOrder.customerState.placedBill));
	}

	public void msgHereAreGoods(CustomerOrder mc){
		log.add(new LoggedEvent("Received HereAreGoods from employee."));
		for (CustomerOrder customer : customers){
			if(customer == mc){
				customer.state = CustomerOrder.customerState.collected;
				stateChanged();
				return;
			}
		}
	}

	public void msgPay(MarketCustomer mc, double payment){
		log.add(new LoggedEvent("Received Payment from customer."));
		for (CustomerOrder customer : customers){
			if( customer.mc == mc){
				customer.payment = payment;
				customer.state = CustomerOrder.customerState.paid;
				stateChanged();
				return;
			}
		}
	}

	//restaurant messages
	public void msgPlaceOrder(Restaurant r, List<Item> order){
		log.add(new LoggedEvent("Received PlaceOrder from restaurant."));
		restaurantOrders.add(new RestaurantOrder(r,order,RestaurantOrder.State.placedBill));
		stateChanged();
	}

	public void msgHereIsPayment(Restaurant r, double payment){
		log.add(new LoggedEvent("Received HereIsPayment from restaurant."));
		for (RestaurantOrder order : restaurantOrders){
			if( order.r == r){
				order.payment = payment;
				order.state = RestaurantOrder.State.paid;
				stateChanged();
				return;
			}
		}
	}
	
	//bank messages
	
	public void msgTransactionComplete(double amount, Double balance, Double debt, int newAccountNumber){
		market.updateAccountNumber(newAccountNumber);
		money_state = MoneyState.none;
		moneyInHand -= amount;
		moneyInBank = balance;
	}

	//Scheduler
	public boolean pickAndExecuteAnAction(){
		for (CustomerOrder customer : customers){
			if( customer.state == CustomerOrder.customerState.placedBill){
				pickOrder(customer);
				return true;
			}
		}
		for (CustomerOrder customer : customers){
			if( customer.state == CustomerOrder.customerState.collected){
				giveBill(customer);
				return true;
			}
		}
		for (CustomerOrder customer : customers){
			if( customer.state == CustomerOrder.customerState.paid){
				giveChange(customer);
				return true;
			}
		}
		for (RestaurantOrder order : restaurantOrders){
			if( order.state == RestaurantOrder.State.placedBill){
				deliverOrder(order);
				return true;
			}
		}
		for (RestaurantOrder order : restaurantOrders){
			if( order.state == RestaurantOrder.State.paid){
				makeChange(order);
				restaurantOrders.remove(order);
				return true;
			}
		}
		if (moneyInHand > 200 && money_state == MoneyState.none){
			DepositMoney();
			return true;
		}
		if (restaurantOrders.size() == 0 && customers.size() == 0 && role_state == RoleState.WantToLeave){
			LeaveMarket();
			role_state = RoleState.none;
			active = false;
			return true;
		}
		return false;
	}

	public void pickOrder(CustomerOrder customer){
		for (Item item : customer.order){
			int amount = Math.min(inventory.get(item.name).amount, item.amount);
			inventory.get(item.name).amount -= amount;
			customer.orderFulfillment.add(new Item(item.name, amount));
			customer.bill += amount* inventory.get(item.name).price;
		}
		market.MarketEmployee.msgPickOrder(customer);
		customer.state = CustomerOrder.customerState.none;
	}

	public void giveBill(CustomerOrder customer){
		Map<String, Double> price_list = new HashMap<String, Double>();
		for (Item item : customer.order){
			price_list.put(item.name, inventory.get(item.name).price);
		}
		customer.mc.msgHereIsBill(customer.bill, price_list, customer.orderFulfillment);
		customer.state = CustomerOrder.customerState.none;
	}
	
	public void giveChange(CustomerOrder customer){
		if (customer.payment >= customer.bill){
			moneyInHand += customer.bill;
			customer.mc.msgHereIsGoodAndChange(customer.orderFulfillment, customer.payment - customer.bill);
			customers.remove(customer);
		}
		else{
			moneyInHand += customer.payment;
			//pay next time
			customer.mc.msgHereIsGoodAndDebt(customer.orderFulfillment, customer.bill - customer.payment);
			customer.payment = 0;
			customer.bill = customer.bill - customer.payment;
			customer.state = CustomerOrder.customerState.none;
		}
	}
	
	public void deliverOrder(RestaurantOrder customer){
		Map<String, Double> price_list = new HashMap<String, Double>();
		for (Item item : customer.order){
			int amount = Math.min(inventory.get(item.name).amount, item.amount);
			inventory.get(item.name).amount -= amount;
			customer.orderFulfillment.add(new Item(item.name, amount));
			customer.bill += amount* inventory.get(item.name).price;
			price_list.put(item.name, inventory.get(item.name).price);
		}
		market.MarketEmployee.msgPickOrder(customer);
		customer.r.cashier.msgHereIsTheBill(market, customer.bill, price_list);
		customer.state = RestaurantOrder.State.none;
	}
	
	public void makeChange(RestaurantOrder customer){
		moneyInHand += customer.bill;
		customer.r.cashier.msgHereIsTheChange(market, customer.payment - customer.bill);
		customer.state = RestaurantOrder.State.none;
	}
	
	public void DepositMoney(){
		Directory.banks().get(0).tellers.get(0).msgWiredTransaction(market, market.getAccountNumber(), moneyInHand / 2, "Desposit");
		money_state = MoneyState.OrderedFromBank;
	}
	
	public void LeaveMarket(){
		gui.LeaveMarket();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class Good {
		String name;
		double price;
		int amount;
		Good(String name, double price, int amount){
			this.name = name;
			this.price = price;
			this.amount = amount;
		}
	}

	public static class CustomerOrder {
		public CustomerOrder(MarketCustomer mc, List<Item> order, CustomerOrder.customerState state){
			this.mc = mc;
			this.state = state;
			this.order = order;
			this.orderFulfillment = new ArrayList<Item>();
		}
		public MarketCustomer mc;
		public List<Item> order, orderFulfillment;
		public double bill, payment;
		public enum customerState{placedBill, collected, paid, none};
		public customerState state;
	}

	public static class RestaurantOrder {
		public RestaurantOrder(Restaurant r, List<Item> order, RestaurantOrder.State state){
			this.r = r;
			this.state = state;
			this.order = order;
			this.orderFulfillment = new ArrayList<Item>();
		}
		public Restaurant r;
		public List<Item> order, orderFulfillment;
		public double bill, payment;
		public enum State{placedBill, paid, none}
		public State state;
	}

}