package city.market.test;

import city.PersonAgent;
import city.market.Market;
import city.market.MarketCashierRole;
import city.market.test.mock.MockMarketCustomer;
import city.market.test.mock.MockMarketEmployee;
import junit.framework.TestCase;

public class MarketCashierTest  extends TestCase {
	Market m;
	MarketCashierRole cashier;
	MockMarketCustomer customer;
	MockMarketEmployee employee;
	
	public void setUp() throws Exception{
		m = new Market();
		super.setUp();
		PersonAgent p = new PersonAgent("Mike");
		cashier = new MarketCashierRole(p,m);
		customer = new MockMarketCustomer("Customer1");
		employee = new MockMarketEmployee("Employee1");
		m.MarketCashier = cashier;
		m.MarketEmployee = employee;
	}
	
	public void testOneNormalCustomerScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		/*assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockMarket 1 should have an empty event log before the Cashier's scheduler is called. Instead, the Market's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());
		assertEquals("CashierAgent should have 130 dollars initially. Instead, the Cashier's balance is: "
				+ cashier.money, 130.0, cashier.money);	
		
		//send first message to cashier
		cashier.msgHereIsTheBill(market1, 100);//send the message from a waiter
		assertEquals("Cashier should have 1 market bill in it. It doesn't.",cashier.marketBills.size(), 1);	
		assertEquals("Cashier should have 0 customer bill in it. It doesn't.",cashier.bills.size(), 0);
		assertEquals("Bill should have the same market. It doesn't.",cashier.marketBills.get(0).market, market1);
		assertEquals("Bill should have the same amount. It doesn't.",cashier.marketBills.get(0).balance, 100.0);
		assertEquals("CashierAgent should have one line after the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 1, cashier.log.size());
		assertTrue("Cashier should have logged an event for receiving \"HereIsTheBill\" with the correct change, but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsTheBill from market. Bill = "+ 100.0));
		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket1's event log reads: "
						+ market1.log.toString(), 0, market1.log.size());

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 market bill after the sceduler has been run. It doesn't.",cashier.marketBills.size(), 0);
		assertEquals("CashierAgent should have one event in log after scheduler is called. Instead, the Cashier's event log reads: "
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

}
