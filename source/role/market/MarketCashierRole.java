package role.market;
import Container.*;
import Utli.Item;
import java.util.*;

public class MarketCashierRole {

	Map<String, Good> inventory;
	List<CustomerOrder> customers; 
	List<RestaurantOrder> restaurantOrders;
	Market m;
	
	class Good {
		String name;
		double price;
		int amount;
	}

	public static class CustomerOrder {
		CustomerOrder(MarketCustomerRole mc, List<Item> order, CustomerOrder.customerState state){
			this.mc = mc;
			this.state = state;
			this.order = order;
		}
		MarketCustomerRole mc;
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

	public void msgPlaceOrder(MarketCustomerRole mc, List<Item> order){
		customers.add(new CustomerOrder(mc,order, CustomerOrder.customerState.placedBill));
	}

	public void msgHereAreGoods(CustomerOrder mc){
		for (CustomerOrder customer : customers){
			if( customer == mc){
				customer.state = CustomerOrder.customerState.collected;
				return;
			}
		}
		//person.stateChanged();
	}

	public void msgPay(MarketCustomerRole mc, double payment){
		for (CustomerOrder customer : customers){
			if( customer.mc == mc){
				customer.payment = payment;
				customer.state = CustomerOrder.customerState.paid;
				return;
			}
		}
	}

	public void msgPlaceOrder(Restaurant r, List<Item> order){
		restaurantOrders.add(new RestaurantOrder(r,order,RestaurantOrder.State.placedBill));
	}

	public void msgHereIsPayment(Restaurant r, double payment){
		for (RestaurantOrder order : restaurantOrders){
			if( order.r == r){
				order.state = RestaurantOrder.State.paid;
				return;
			}
		}
	}

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
				customers.remove(customer);
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
		return false;
	}

	public void pickOrder(CustomerOrder customer){
		for (Item item : customer.order){
			int amount = Math.min(inventory.get(item.name).amount, item.amount);
			inventory.get(item.name).amount -= amount;
			customer.orderFulfillment.add(new Item(item.name, amount));
			customer.bill += amount* inventory.get(item.name).price;
		}
		m.MarketEmployee.msgPickOrder(customer);
		customer.state = CustomerOrder.customerState.none;
	}

	public void giveBill(CustomerOrder customer){
		customer.mc.msgHereIsBill(customer.bill);
		customer.state = CustomerOrder.customerState.none;
	}
	
	public void giveChange(CustomerOrder customer){
		customer.mc.msgHereIsGoodAndChange(customer.orderFulfillment, customer.payment - customer.bill);
	}
	
	public void deliverOrder(RestaurantOrder customer){
		for (Item item : customer.order){
			int amount = Math.min(inventory.get(item.name).amount, item.amount);
			inventory.get(item.name).amount -= amount;
			customer.orderFulfillment.add(new Item(item.name, amount));
			customer.bill += amount* inventory.get(item.name).price;
		}
		m.MarketEmployee.msgPickOrder(customer);
		//customer.r.RestaurantCashier.msgHereIsBill(customer.bill, Map<String, Double());
		customer.state = RestaurantOrder.State.none;
	}
	
	public void makeChange(RestaurantOrder customer){
		//customer.r.RestaurantCashier.msgHereIsChange(customer.payment ¨C customer.bill)
		customer.state = RestaurantOrder.State.none;
	}

}
