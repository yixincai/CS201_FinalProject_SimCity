package city.market.test;

import java.util.*;

import city.PersonAgent;
import city.market.*;
import city.market.gui.MarketCustomerGui;
import junit.framework.TestCase;

public class MarketCustomerTest  extends TestCase {
	Market market;
	PersonAgent p;
	MarketCustomerRole customer;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		market.MarketCashier.setPersonAgent(p);
		customer = new MarketCustomerRole(p, market);
		MarketCustomerGui gui = new MarketCustomerGui(customer);
		customer.gui = gui;
	}
	
	public void testNormalCustomerScenario(){

		assertEquals("Customer should be in none state. It doesn't.", customer.state, MarketCustomerRole.CustomerState.none);
		assertFalse("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		
		//send first message to customer
		customer.cmdBuyFood(5);
		assertEquals("Customer should be in wantToBuy state. It doesn't.", customer.state, MarketCustomerRole.CustomerState.wantToBuy);
		
		customer.msgAnimationFinished();
		assertTrue("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		assertFalse("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		
		Map<String, Double> price_list = new HashMap<String, Double>();
		List<Item> orderFulfillment = new ArrayList<Item>();
		orderFulfillment.add(new Item("Meal", 5));
		customer.msgHereIsBill(25, price_list, orderFulfillment);
		
		customer.msgAnimationFinished();
		customer.msgAnimationFinished();
		assertTrue("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		assertFalse("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		
		customer.msgHereIsGoodAndChange(orderFulfillment, 0);
		customer.msgAnimationFinished();
		customer.msgAnimationFinished();
		assertTrue("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
		assertFalse("Cashier's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());
	}
	
}
