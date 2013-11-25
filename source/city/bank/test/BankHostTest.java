package city.bank.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankCustomerRole;
import city.bank.BankHostRole;
import city.bank.interfaces.BankTeller;
import city.bank.test.mock.MockBankCustomer;
import city.bank.test.mock.MockBankHost;
import city.bank.test.mock.MockBankTeller;

public class BankHostTest extends TestCase{
	Bank bank;
	BankHostRole host;
	MockBankTeller teller;
	MockBankCustomer c1;
	MockBankCustomer c2;
	
	public void setUp() throws Exception{
		bank = new Bank();
		super.setUp();
		PersonAgent p = new PersonAgent("Mike");
		teller = new MockBankTeller("BankTeller", 0);
		List<BankTeller> tellers = new ArrayList<BankTeller>();
		tellers.add(teller);
		host = new BankHostRole(p, bank, tellers);
		c1 = new MockBankCustomer("Omar");
		c2 = new MockBankCustomer("Bill");
		
	}
	
	public void testOneNormalCustomerScenario(){
		try {
			this.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Check to make sure everything was created correctly (Preconditions)
		assertTrue(!teller.isOccupied());
		assertTrue(teller.log.size() == 0);
		assertTrue(c1.log.size() == 0);
		assertTrue(c2.log.size() == 0);
		assertTrue(host.waitingCustomers.isEmpty());
		
		//now the customer comes to the line
		host.msgWaiting(c1);
		assertTrue(host.waitingCustomers.size() == 1);
		assertTrue(host.pickAndExecuteAnAction());
		assertTrue("the customer (c1) should have logged \"msgCalledToDesk recieved\" but logged" + c1.log.getLastLoggedEvent(), c1.log.containsString("msgCalledToDesk recieved"));
		assertTrue(teller.isOccupied());
		
		//now we tell the host that the customer is leaving
		host.msgLeavingBank(teller);
		assertTrue(!teller.isOccupied());
	}
	
	public void testMultipleCustomerScenario()
	{
		try {
			this.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Check to make sure everything was created correctly (Preconditions)
		assertTrue(!teller.isOccupied());
		assertTrue(teller.log.size() == 0);
		assertTrue(c1.log.size() == 0);
		assertTrue(c2.log.size() == 0);
		assertTrue(host.waitingCustomers.isEmpty());
		
		//first we will send c1 in
		host.msgWaiting(c1);
		assertTrue(host.waitingCustomers.size() == 1);
		assertTrue(host.pickAndExecuteAnAction());
		assertTrue("the customer (c1) should have logged \"msgCalledToDesk recieved\" but logged" + c1.log.getLastLoggedEvent(), c1.log.containsString("msgCalledToDesk recieved"));
		assertTrue(teller.isOccupied());
		
		//next we will send in c2, but pickAndExecute will return false
		host.msgWaiting(c2);
		assertTrue(host.waitingCustomers.size() == 1);
		assertFalse(host.pickAndExecuteAnAction());
		
		//now we will have c1 leave
		host.msgLeavingBank(teller);
		assertTrue(!teller.isOccupied());
		assertTrue(host.pickAndExecuteAnAction());
		assertTrue("the customer (c2) should have logged \"msgCalledToDesk recieved\" but logged" + c1.log.getLastLoggedEvent(), c1.log.containsString("msgCalledToDesk recieved"));
		assertTrue(teller.isOccupied());
		
		//finally we will have c2 leave
		host.msgLeavingBank(teller);
		assertTrue(!teller.isOccupied());
		assertTrue(host.waitingCustomers.isEmpty());
		assertFalse(host.pickAndExecuteAnAction());
	}
}
