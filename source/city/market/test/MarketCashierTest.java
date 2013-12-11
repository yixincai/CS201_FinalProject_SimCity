package city.market.test;

import java.util.ArrayList;
import java.util.List;

import city.Directory;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankTellerRole;
import city.bank.gui.BankAnimationPanel;
import city.market.*;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketCashierTest  extends TestCase {
	Market market;
	PersonAgent p;
	MarketCashierRole cashier;
	MockMarketCustomer customer;
	MockMarketEmployee employee;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		market.MarketEmployee.setPerson(p);
		cashier = market.MarketCashier;
		cashier.setPerson(p);
		customer = new MockMarketCustomer("Customer1");
		Bank b = new Bank();
		Directory.addBank(b);
		Directory.banks().get(0)._tellers.get(0).makeDatabase();
		Directory.banks().get(0)._tellers.get(0).setPerson(new PersonAgent("Dummy"));
	}
	
	public void testOneNormalCustomerWithoutBankScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
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

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state,
				MarketCashierRole.CustomerOrder.customerState.none);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgHereAreGoods(cashier.customers.get(0));
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, 
				MarketCashierRole.CustomerOrder.customerState.collected);

		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgPay(customer, 250);
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, 
				MarketCashierRole.CustomerOrder.customerState.paid);

		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 0);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
	}
	
	public void testOneNormalCustomerWithBankScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. "
				+ "Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer 1 should have an empty event log before the Cashier's scheduler is called. "
				+ "Instead, the Market's event log reads: " + customer.log.toString(), 0, customer.log.size());
		
		//send first message to cashier
		List<Item> order = new ArrayList<Item>();
		order.add(new Item("Car", 1));
		cashier.msgPlaceOrder(customer, order);//send the message from a waiter
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Cashier should have 0 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("Bill should have the same market. It doesn't.",cashier.customers.get(0).mc, customer);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state,
				MarketCashierRole.CustomerOrder.customerState.placedBill);

		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state,
				MarketCashierRole.CustomerOrder.customerState.none);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgHereAreGoods(cashier.customers.get(0));
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, 
				MarketCashierRole.CustomerOrder.customerState.collected);

		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgPay(customer, 250);
		assertEquals("Cashier should have 1 customer bill in it. It doesn't.",cashier.customers.size(), 1);	
		assertEquals("Bill should have the correct state. It doesn't.",cashier.customers.get(0).state, 
				MarketCashierRole.CustomerOrder.customerState.paid);

		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 customer bill after the sceduler has been run. It doesn't.",cashier.customers.size(), 0);
		
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
	}
	
	public void testOneNormalRestaurantScenario(){

		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.restaurantOrders.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. "
				+ "Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer 1 should have an empty event log before the Cashier's scheduler is called. "
				+ "Instead, the Market's event log reads: " + customer.log.toString(), 0, customer.log.size());
		
		//send first message to cashier
		Restaurant r = new YixinRestaurant();
		PersonAgent p1 = new PersonAgent("Dummy");
		r.getCashier().setPerson(p1);
		List<Item> order = new ArrayList<Item>();
		order.add(new Item("Steak", 1));
		order.add(new Item("Chicken", 1));
		cashier.msgPlaceOrder(r, order);//send the message from a waiter
		assertEquals("Cashier should have 0 customer bill in it. It doesn't.",cashier.customers.size(), 0);	
		assertEquals("Cashier should have 1 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 1);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.restaurantOrders.get(0).state, 
				MarketCashierRole.RestaurantOrder.State.placedBill);
		
		//run scheduler
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 market bill after the sceduler has been run. It doesn't.", cashier.restaurantOrders.size(), 1);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.restaurantOrders.get(0).state, 
				MarketCashierRole.RestaurantOrder.State.none);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		cashier.msgHereIsPayment(r, 200);
		assertEquals("Cashier should have 1 restaurant bill in it. It doesn't.",cashier.restaurantOrders.size(), 1);
		assertEquals("Bill should have the correct state. It doesn't.",cashier.restaurantOrders.get(0).state, 
				MarketCashierRole.RestaurantOrder.State.paid);
		
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 market bill after the sceduler has been run. It doesn't.", cashier.restaurantOrders.size(), 0);
		
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
	}
}
