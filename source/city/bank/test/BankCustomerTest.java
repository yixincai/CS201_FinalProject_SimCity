package city.bank.test;

import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;
import city.PersonAgent;
import city.bank.Bank;
import city.bank.BankCustomerRole;
import city.bank.BankCustomerRole.Event;
import city.bank.BankCustomerRole.State;
import city.bank.gui.BankAnimationPanel;
import city.bank.gui.BankCustomerRoleGui;
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
			customer = new BankCustomerRole(p, bank);
			teller = new MockBankTeller("BankTeller", 0); //the zero is irrelevant in testing as it is more for GUI stuff
			host = new MockBankHost("BankHost");
		}
		
		public void testOneNormalCustomerDepositScenario()
		{
			try {
				this.setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Make sure everyhting was created correctly (Preconditions)
			assertTrue(teller.log.size() == 0);
			assertTrue(host.log.size() == 0);
			assertTrue(customer.accountNumber == -1);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amount == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.state == State.DoingNothing);
			assertTrue(customer.event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdDeposit((double)500.0);
			assertTrue(customer.amount == (double)500.0);
			assertTrue(customer.request.equalsIgnoreCase("deposit"));
			
			if(customer.state == State.DoingNothing && customer.event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer.state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer.event == Event.CalledToDesk);
			
			if(customer.state == State.Waiting && customer.event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer.state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)0.0, (int)9000);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.accountNumber == (int)9000);
			assertTrue(customer.event == Event.GivenRequestPermission);
			
			if(customer.state == State.AtTeller && customer.event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer.request, customer.amount);
				customer.state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(-customer.amount, customer.accountFunds + customer.amount, customer.amountOwed);
			assertTrue(customer.event == Event.ApprovedTransaction);
			assertTrue(customer.accountFunds == (double)500.0);
			assertTrue(customer.amountOwed == (double)0.0);	
		}
		
		public void testOneNormalCustomerWithdraw()
		{
			try {
				this.setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Make sure everyhting was created correctly (Preconditions)
			assertTrue(teller.log.size() == 0);
			assertTrue(host.log.size() == 0);
			assertTrue(customer.accountNumber == -1);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amount == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.state == State.DoingNothing);
			assertTrue(customer.event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdWithdraw((double)500.0);
			assertTrue(customer.amount == (double)500.0);
			assertTrue(customer.request.equalsIgnoreCase("withdraw"));
			
			if(customer.state == State.DoingNothing && customer.event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer.state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer.event == Event.CalledToDesk);
			
			if(customer.state == State.Waiting && customer.event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer.state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)500.0, (double)0.0, (int)9000);
			assertTrue(customer.accountFunds == (double)500.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.accountNumber == (int)9000);
			assertTrue(customer.event == Event.GivenRequestPermission);
			
			if(customer.state == State.AtTeller && customer.event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer.request, customer.amount);
				customer.state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer.amount, customer.accountFunds - customer.amount, customer.amountOwed);
			assertTrue(customer.event == Event.ApprovedTransaction);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);	
		}
		
		public void testOneNormalCustomerGetLoan()
		{
			try {
				this.setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Make sure everyhting was created correctly (Preconditions)
			assertTrue(teller.log.size() == 0);
			assertTrue(host.log.size() == 0);
			assertTrue(customer.accountNumber == -1);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amount == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.state == State.DoingNothing);
			assertTrue(customer.event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdWithdrawLoan((double)500.0);
			assertTrue(customer.amount == (double)500.0);
			assertTrue(customer.request.equalsIgnoreCase("withdraw loan"));
			
			if(customer.state == State.DoingNothing && customer.event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer.state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer.event == Event.CalledToDesk);
			
			if(customer.state == State.Waiting && customer.event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer.state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)0.0, (int)9000);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.accountNumber == (int)9000);
			assertTrue(customer.event == Event.GivenRequestPermission);
			
			if(customer.state == State.AtTeller && customer.event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer.request, customer.amount);
				customer.state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer.amount, customer.accountFunds, customer.amount);
			assertTrue(customer.event == Event.ApprovedTransaction);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)500.0);	
		}
		
		public void testOneNormalCustomerPayLoan()
		{
			try {
				this.setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Make sure everyhting was created correctly (Preconditions)
			assertTrue(teller.log.size() == 0);
			assertTrue(host.log.size() == 0);
			assertTrue(customer.accountNumber == -1);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amount == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);
			assertTrue(customer.state == State.DoingNothing);
			assertTrue(customer.event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdPayLoan((double)500.0);
			assertTrue(customer.amount == (double)500.0);
			assertTrue(customer.request.equalsIgnoreCase("pay loan"));
			
			if(customer.state == State.DoingNothing && customer.event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer.state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer.event == Event.CalledToDesk);
			
			if(customer.state == State.Waiting && customer.event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer.state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)500.0, (int)9000);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)500.0);
			assertTrue(customer.accountNumber == (int)9000);
			assertTrue(customer.event == Event.GivenRequestPermission);
			
			if(customer.state == State.AtTeller && customer.event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer.request, customer.amount);
				customer.state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer.amount, customer.accountFunds, customer.amountOwed - customer.amount);
			assertTrue(customer.event == Event.ApprovedTransaction);
			assertTrue(customer.accountFunds == (double)0.0);
			assertTrue(customer.amountOwed == (double)0.0);	
		}
}
