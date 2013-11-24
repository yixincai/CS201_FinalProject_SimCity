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
		
		//Make the customer go to the Bank (messageing the host)
		host.msgWaiting(customer);
		assertTrue("Should have received \"msgWaiting recieved\" but but got " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
		host.addWaitingCustomer(customer);
		assertFalse(host.isWaitingCustomersEmpty());
		
		
		
		
		
				
		
	}

}
