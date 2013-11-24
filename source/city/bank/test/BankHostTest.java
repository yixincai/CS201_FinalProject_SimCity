package city.bank.test;

import junit.framework.TestCase;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankCustomerRole;
import city.bank.BankHostRole;
import city.bank.test.mock.MockBankCustomer;
import city.bank.test.mock.MockBankHost;
import city.bank.test.mock.MockBankTeller;

public class BankHostTest extends TestCase{
	Bank bank;
	BankHostRole host;
	MockBankTeller teller;
	MockBankCustomer customer;
	
	public void setUp() throws Exception{
		bank = new Bank();
		super.setUp();
		PersonAgent p = new PersonAgent("Mike");
//		host = new BankHostRole();
		customer = new MockBankCustomer("Omar");
		teller = new MockBankTeller("BankTeller", 0);
/*		bank.addTeller(teller);
		b.host = host; */
	}
	
	public void testOneNormalCustomerScenario(){
	}
}
