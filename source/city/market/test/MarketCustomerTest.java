package city.market.test;

import java.util.ArrayList;
import java.util.List;

import city.PersonAgent;
import city.market.*;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketCustomerTest  extends TestCase {
	Market market;
	PersonAgent p;
	MarketCustomerRole customer;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		customer = new MarketCustomerRole(p, market);
	}
	
	public void testNormalCustomerScenario(){

		assertEquals("Customer should be in none state. It doesn't.", customer.state, MarketCustomerRole.CustomerState.none);
		assertFalse("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		
		//send first message to customer
		customer.cmdBuyFood(5);
		assertEquals("Customer should be in wantToBuy state. It doesn't.", customer.state, MarketCustomerRole.CustomerState.wantToBuy);

		assertTrue("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		
	}
	
}
