package city.market.test;

import java.util.ArrayList;
import java.util.List;

import city.PersonAgent;
import city.market.*;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketCashierTest  extends TestCase {
	Market market;
	MarketCashierRole cashier;
	MockMarketCustomer customer;
	MockMarketEmployee employee;
	
	public void setUp() throws Exception{
		super.setUp();
		market = new Market();
		cashier = market.MarketCashier;
		customer = new MockMarketCustomer("Customer1");
	}
	
	public void testOneNormalCustomerScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer 1 should have an empty event log before the Cashier's scheduler is called. Instead, the Market's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		//send first message to cashier
		List<Item> order = new ArrayList<Item>();
		order.add(new Item("Car", 1));
		order.add(new Item("Meal", 1));
		cashier.msgPlaceOrder(customer, order);//send the message from a waiter
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Cashier should have 0 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("Bill should have the same market. It doesn't.",cashier.customers.get(0).mc, customer);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, MarketCashierRole.CustomerOrder.customerState.placedBill);

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 market bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
		/*assertEquals("CashierAgent should have one event in log after scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 1, cashier.log.size());
		assertEquals("MockMarket should have an event in log after the Cashier's scheduler is called. Instead, the MockMarket1's event log reads: "
						+ market1.log.toString(), 1, market1.log.size());
		assertTrue("MockMarket should have logged an event for receiving \"HereIsTheBill\" with the correct change, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received HereIsThePayment from cashier. Check = "+ 100.0));
		assertEquals("CashierAgent should have 30 remaining dollars. Instead, the Cashier's balance is: "
				+ cashier.money, 30.0, cashier.money);	
		
		//check postconditions for step 1 and preconditions for step 2
		assertFalse("Cashier's scheduler should have returned false (no actions to do), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("CashierAgent should have one event in log after scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 1, cashier.log.size());*/
	}
	
	public void testOneNormalRestaurantScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer 1 should have an empty event log before the Cashier's scheduler is called. Instead, the Market's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		//send first message to cashier
		Restaurant r = new YixinRestaurant();
		List<Item> order = new ArrayList<Item>();
		order.add(new Item("Steak", 1));
		order.add(new Item("Chicken", 1));
		cashier.msgPlaceOrder(r, order);//send the message from a waiter
		assertEquals("Cashier should have 0 customer bill in it. It doesn't.",cashier.customers.size(), 0);	
		assertEquals("Cashier should have 1 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 1);
		//assertEquals("Bill should have the same market. It doesn't.",cashier.customers.get(0).mc, customer);
		//assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, MarketCashierRole.CustomerOrder.customerState.placedBill);

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertEquals("Cashier should have 1 market bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
	}
}
