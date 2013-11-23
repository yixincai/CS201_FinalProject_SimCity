package city.market.test;

import java.util.ArrayList;
import java.util.List;

import city.PersonAgent;
import city.market.*;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketEmployeeTest  extends TestCase {
	Market market;
	PersonAgent p;
	MockMarketCashier cashier;
	MarketEmployeeRole employee;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		employee = market.MarketEmployee;
		employee.setPersonAgent(p);
	}
	
	public void testOneNormalCustomerScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",employee.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. "
				+ "Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer 1 should have an empty event log before the Cashier's scheduler is called. "
				+ "Instead, the Market's event log reads: " + customer.log.toString(), 0, customer.log.size());
		
		//send first message to cashier
		List<Item> order = new ArrayList<Item>();
		order.add(new Item("Meal", 1));
		cashier.msgPlaceOrder(customer, order);//send the message from a waiter
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Cashier should have 0 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("Bill should have the same market. It doesn't.",cashier.customers.get(0).mc, customer);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state,
				MarketCashierRole.CustomerOrder.customerState.placedBill);


	}
	
	public void testOneNormalRestaurantScenario(){

	}
}
