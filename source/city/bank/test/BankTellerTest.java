package city.bank.test;

import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankHostRole;
import city.bank.BankTellerRole;
import city.bank.test.mock.MockBankCustomer;
import city.bank.test.mock.MockBankHost;
import city.bank.test.mock.MockBankTeller;
import junit.framework.TestCase;

public class BankTellerTest extends TestCase
{
	Bank bank;
	MockBankHost host;
	BankTellerRole teller;
	MockBankCustomer customer;
	
	public void setUp() throws Exception{
		bank = new Bank();
		super.setUp();
		PersonAgent p = new PersonAgent("Mike");
		host = new MockBankHost("Host");
		customer = new MockBankCustomer("Omar");
		teller = new BankTellerRole(p, bank, 0);
		teller.makeDatabase();
	}
	
	public void testOneNormalCustomerScenario()
	{
		try {
			this.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Check to make sure everything was created correctly (Preconditions)
		assertTrue(host.log.size() == 0);
		assertTrue(customer.log.size() == 0);
		assertTrue(teller.myCustomers.size() == 0);
		assertTrue(teller.myBusinessCustomers.size() == 0);
		assertTrue(!teller.isOccupied());
		assertTrue(host.isWaitingCustomersEmpty());
		
		//Make the customer go to the Bank (messaging the host)
		host.msgWaiting(customer);
		assertTrue("Should have received \"msgWaiting recieved\" but but got " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
		host.addWaitingCustomer(customer);
		assertFalse(host.isWaitingCustomersEmpty());
		
		//Now make the host send the customer to the bankTeller.
		customer.msgCalledToDesk(teller);
		assertTrue("customer log should read \"msgCalledToDesk recieved.\" but reads " + customer.log.getLastLoggedEvent(), customer.log.containsString("msgCalledToDesk recieved"));
		
		//Customer now begins interaction with teller
		teller.msgIAmHere(customer);
		assertTrue(teller.myCustomers.size() == 1);
		assertTrue(teller.pickAndExecuteAnAction());
		customer.msgHereIsInfoPickARequest(1000, 0, 10531); //these number are random for the sake of testing
		assertTrue("The customer should have logged \"msgHereIsInfoPickARequest\" but actually logges " + customer.log.getLastLoggedEvent(), customer.log.containsString("msgHereIsInfoPickARequest recieved"));
		teller.msgHereIsMyRequest(customer, "deposit", 200);
		assertTrue(teller.pickAndExecuteAnAction());
		
		
		
	
		
		
		
				
		
	}

}
