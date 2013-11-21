package city.market;
import java.util.*;

import city.PersonAgent;
import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketCustomer;
import city.restaurant.Restaurant;
import agent.Role;
import utilities.EventLog;
import utilities.LoggedEvent;

public class MarketCashierRole extends Role implements MarketCashier{

	public EventLog log = new EventLog();
	public Map<String, Good> inventory = new HashMap<String, Good>();
	public List<CustomerOrder> customers = new ArrayList<CustomerOrder>(); 
	public List<RestaurantOrder> restaurantOrders = new ArrayList<RestaurantOrder>();
	Market market;
	double moneyInHand, moneyInBank;
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	enum MoneyState{OrderedFromBank, none}
	MoneyState money_state = MoneyState.none;
	
	public MarketCashierRole(PersonAgent person, Market m){
		super(person);
		this.market = m;
	}
	
	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
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
				return;
			}
		}
		//person.stateChanged();
	}

	public void msgPay(MarketCustomer mc, double payment){
		log.add(new LoggedEvent("Received Payment from customer."));
		for (CustomerOrder customer : customers){
			if( customer.mc == mc){
				customer.payment = payment;
				customer.state = CustomerOrder.customerState.paid;
				return;
			}
		}
	}

	//restaurant messages
	public void msgPlaceOrder(Restaurant r, List<Item> order){
		log.add(new LoggedEvent("Received PlaceOrder from restaurant."));
		restaurantOrders.add(new RestaurantOrder(r,order,RestaurantOrder.State.placedBill));
	}

	public void msgHereIsPayment(Restaurant r, double payment){
		log.add(new LoggedEvent("Received HereIsPayment from restaurant."));
		for (RestaurantOrder order : restaurantOrders){
			if( order.r == r){
				order.payment = payment;
				order.state = RestaurantOrder.State.paid;
				return;
			}
		}
	}
	
	//bank messages
	public void msgSuccessTransaction(){
		money_state = MoneyState.none;
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
			//Bank.bankCashierRole.msg();
			money_state = MoneyState.OrderedFromBank;
			return true;
		}
		if (restaurantOrders.size() == 0 && customers.size() == 0 && role_state == RoleState.WantToLeave){
			//DoLeave();
			role_state = RoleState.none;
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
		//customer.r.RestaurantCashier.msgHereIsBill(customer.bill, price_list);
		customer.state = RestaurantOrder.State.none;
	}
	
	public void makeChange(RestaurantOrder customer){
		moneyInHand += customer.bill;
		//customer.r.RestaurantCashier.msgHereIsChange(customer.payment ¨C customer.bill)
		customer.state = RestaurantOrder.State.none;
	}

	class Good {
		String name;
		double price;
		int amount;
	}

	public static class CustomerOrder {
		CustomerOrder(MarketCustomer mc, List<Item> order, CustomerOrder.customerState state){
			this.mc = mc;
			this.state = state;
			this.order = order;
		}
		MarketCustomer mc;
		List<Item> order, orderFulfillment;
		double bill, payment;
		enum customerState{placedBill, collected, paid, none};
		customerState state;
	}

	public static class RestaurantOrder {
		RestaurantOrder(Restaurant r, List<Item> order, RestaurantOrder.State state){
			this.r = r;
			this.state = state;
			this.order = order;
		}
		Restaurant r;
		List<Item> order, orderFulfillment;
		double bill, payment;
		enum State{placedBill, paid, none}
		State state;
	}

}