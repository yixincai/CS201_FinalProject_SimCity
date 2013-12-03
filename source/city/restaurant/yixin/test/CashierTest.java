package city.restaurant.yixin.test;

import java.util.*;

import city.Directory;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankTellerRole;
import city.bank.gui.BankAnimationPanel;
import city.market.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.*;
import city.restaurant.yixin.test.mock.*;
import city.transportation.TruckAgent;
import junit.framework.*;

public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	YixinCashierRole cashier;
	MockCook cook;
	Map<String, Double> price_list;
	Market market;
	PersonAgent p;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		Restaurant restaurant = new YixinRestaurant();
		cashier = (YixinCashierRole)restaurant.cashier;
		market = new Market();
		p =new PersonAgent("Dummy");
		cashier.setPerson(p);
		price_list = new HashMap<String, Double>();
		price_list.put("Steak", 10.0);
		price_list.put("Chicken", 7.0);
		price_list.put("Salad", 3.0);
		price_list.put("Pizza", 4.0);
		Bank b = new Bank();
		Directory.addPlace(b);
	}
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	
	public void testOneNormalMarketWithInvoiceScenario()
	{			
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.marketBills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("CashierAgent should have 130 dollars initially. Instead, the Cashier's balance is: "
				+ cashier.money, 130.0, cashier.money);	
		
		//send first message to cashier
		cashier.msgHereIsTheBill(market, 100, price_list);//send the message from a waiter
		assertEquals("Cashier should have 1 market bill in it. It doesn't.",cashier.marketBills.size(), 1);	
		assertEquals("Cashier should have 0 customer bill in it. It doesn't.",cashier.bills.size(), 0);
		assertEquals("Bill should have the same market. It doesn't.",cashier.marketBills.get(0).market, market);
		assertEquals("Bill should have the same amount. It doesn't.",cashier.marketBills.get(0).balance, 100.0);
		assertEquals("CashierAgent should have one line after the Cashier's ComputeBill is called. "
				+ "Instead, the Cashier's event log reads: " + cashier.log.toString(), 1, cashier.log.size());
		assertTrue("Cashier should have logged an event for receiving \"HereIsTheBill\" with the correct change, "
				+ "but his last event logged reads instead: " + cashier.log.getLastLoggedEvent().toString(), 
				cashier.log.containsString("Received HereIsTheBill from market. Bill = "+ 100.0));

		//run scheduler
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 market bill after the sceduler has been run. It doesn't.",cashier.marketBills.size(), 1);
		assertEquals("CashierAgent should have one event in log after scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 1, cashier.log.size());
		
		List<Item> invoice = new ArrayList<Item>();
		cashier.msgHereIsTheInvoice(market, invoice);
		
		assertTrue("Cashier's scheduler should have returned false (no actions to do), but didn't.", cashier.pickAndExecuteAnAction());

		assertEquals("CashierAgent should have 30 remaining dollars. Instead, the Cashier's balance is: "
				+ cashier.money, 30.0, cashier.money);	
		
		//check postconditions for step 1 and preconditions for step 2
		assertFalse("Cashier's scheduler should have returned false (no actions to do), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("CashierAgent should have one event in log after scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 1, cashier.log.size());
		
	}//end one normal market scenario	

	public void testRestaurantBankInteractionScenario()
	{			
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);
		assertEquals("Cashier should have 0 marketBills in it. It doesn't.",cashier.marketBills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsTheBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("CashierAgent should have 130 dollars initially. Instead, the Cashier's balance is: "
				+ cashier.money, 130.0, cashier.money);	
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//send first message to cashier
		cashier.money = 300;//send the message from a waiter
		Directory.banks().get(0)._tellers.get(0).makeDatabase();
		Directory.banks().get(0)._tellers.get(0).setPerson(new PersonAgent("Dummy"));
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		cashier.msgTransactionComplete(150, 150.0, 0.0, 5134);
		assertFalse("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
	}//end one normal market scenario	
}
