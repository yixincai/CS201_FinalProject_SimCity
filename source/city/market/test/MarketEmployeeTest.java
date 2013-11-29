package city.market.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.PersonAgent;
import city.market.*;
import city.market.MarketCashierRole.CustomerOrder;
import city.market.MarketCashierRole.RestaurantOrder;
import city.market.gui.MarketEmployeeGui;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketEmployeeTest  extends TestCase {
	Market market;
	PersonAgent p;
	MockMarketCashier cashier;
	MockMarketCustomer customer;
	MarketEmployeeRole employee;
	MarketEmployeeGui gui;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		employee = market.MarketEmployee;
		employee.setPerson(p);
		customer = new MockMarketCustomer("Dummy");
		gui = new MarketEmployeeGui(employee);
		employee.gui = gui;
	}
	
	public void testOneNormalCustomerScenario(){

		assertEquals("Cashier should have 0 restaurant orders in it. It doesn't.", employee.deliverOrders.size(), 0);
		assertEquals("Cashier should have 0 customer orders in it. It doesn't.", employee.pickUpOrders.size(), 0);
		
		//send first message to cashier
		List<Item> orders = new ArrayList<Item>();
		CustomerOrder order = new CustomerOrder(customer, orders, MarketCashierRole.CustomerOrder.customerState.none);
		employee.msgPickOrder(order);//send the message from a waiter
		assertEquals("Cashier should have 1 customer order in it. It doesn't.", employee.pickUpOrders.size(), 1);
		assertEquals("Order should have the same list. It doesn't.", employee.pickUpOrders.get(0).order, orders);
		
		employee.msgAnimationFinished();
		assertTrue("Cashier's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer order in it. It doesn't.", employee.pickUpOrders.size(), 0);
		
		employee.msgAnimationFinished();
		assertFalse("Cashier's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
	}
	
	public void testOneNormalRestaurantScenario(){
		assertEquals("Cashier should have 0 restaurant orders in it. It doesn't.", employee.deliverOrders.size(), 0);
		assertEquals("Cashier should have 0 customer orders in it. It doesn't.", employee.pickUpOrders.size(), 0);
		
		//send first message to cashier
		List<Item> orders = new ArrayList<Item>();
		Restaurant r = new YixinRestaurant();
		RestaurantOrder order = new RestaurantOrder(r, orders, MarketCashierRole.RestaurantOrder.State.none);
		employee.msgPickOrder(order);//send the message from a waiter
		assertEquals("Cashier should have 1 restaurant order in it. It doesn't.", employee.deliverOrders.size(), 1);
		assertEquals("Order should have the same list. It doesn't.", employee.deliverOrders.get(0).order, orders);

		employee.msgAnimationFinished();
		assertTrue("Cashier's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer order in it. It doesn't.", employee.deliverOrders.size(), 0);
		
		employee.msgAnimationFinished();
		assertFalse("Cashier's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
	}
}
