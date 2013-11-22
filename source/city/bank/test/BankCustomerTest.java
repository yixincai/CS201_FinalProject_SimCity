package city.bank.test;

import junit.framework.TestCase;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankCustomerRole;
import city.bank.test.mock.MockBankHost;
import city.bank.test.mock.MockBankTeller;

public class BankCustomerTest extends TestCase { //
		Bank bank;
		BankCustomerRole customer;
		MockBankTeller teller;
		MockBankHost host;
		
		public void setUp() throws Exception{
			bank = new Bank();
			super.setUp();
			PersonAgent p = new PersonAgent("Mike");
			customer = new BankCustomerRole(p, 100, bank);
			teller = new MockBankTeller("BankTeller");
			host = new MockBankHost("BankHost");
	/*		bank.addTeller(teller);
			b.host = host; */
		}
		
		public void testOneNormalCustomerScenario(){
		}
}
