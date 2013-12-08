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
			assertTrue(customer._accountNumber == -1);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amount == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._state == State.DoingNothing);
			assertTrue(customer._event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdDeposit((double)500.0);
			assertTrue(customer._amount == (double)500.0);
			assertTrue(customer._request.equalsIgnoreCase("deposit"));
			
			if(customer._state == State.DoingNothing && customer._event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer._state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer._event == Event.CalledToDesk);
			
			if(customer._state == State.Waiting && customer._event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer._state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)0.0, (int)9000);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._accountNumber == (int)9000);
			assertTrue(customer._event == Event.GivenRequestPermission);
			
			if(customer._state == State.AtTeller && customer._event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer._request, customer._amount);
				customer._state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(-customer._amount, customer._accountFunds + customer._amount, customer._amountOwed);
			assertTrue(customer._event == Event.ApprovedTransaction);
			assertTrue(customer._accountFunds == (double)500.0);
			assertTrue(customer._amountOwed == (double)0.0);	
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
			assertTrue(customer._accountNumber == -1);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amount == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._state == State.DoingNothing);
			assertTrue(customer._event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdWithdraw((double)500.0);
			assertTrue(customer._amount == (double)500.0);
			assertTrue(customer._request.equalsIgnoreCase("withdraw"));
			
			if(customer._state == State.DoingNothing && customer._event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer._state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer._event == Event.CalledToDesk);
			
			if(customer._state == State.Waiting && customer._event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer._state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)500.0, (double)0.0, (int)9000);
			assertTrue(customer._accountFunds == (double)500.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._accountNumber == (int)9000);
			assertTrue(customer._event == Event.GivenRequestPermission);
			
			if(customer._state == State.AtTeller && customer._event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer._request, customer._amount);
				customer._state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer._amount, customer._accountFunds - customer._amount, customer._amountOwed);
			assertTrue(customer._event == Event.ApprovedTransaction);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);	
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
			assertTrue(customer._accountNumber == -1);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amount == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._state == State.DoingNothing);
			assertTrue(customer._event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdWithdrawLoan((double)500.0);
			assertTrue(customer._amount == (double)500.0);
			assertTrue(customer._request.equalsIgnoreCase("withdraw loan"));
			
			if(customer._state == State.DoingNothing && customer._event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer._state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer._event == Event.CalledToDesk);
			
			if(customer._state == State.Waiting && customer._event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer._state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)0.0, (int)9000);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._accountNumber == (int)9000);
			assertTrue(customer._event == Event.GivenRequestPermission);
			
			if(customer._state == State.AtTeller && customer._event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer._request, customer._amount);
				customer._state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer._amount, customer._accountFunds, customer._amount);
			assertTrue(customer._event == Event.ApprovedTransaction);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)500.0);	
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
			assertTrue(customer._accountNumber == -1);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amount == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);
			assertTrue(customer._state == State.DoingNothing);
			assertTrue(customer._event == Event.None);
			
			//now add some money for the customer to deposit
			customer.cmdPayLoan((double)500.0);
			assertTrue(customer._amount == (double)500.0);
			assertTrue(customer._request.equalsIgnoreCase("pay loan"));
			
			if(customer._state == State.DoingNothing && customer._event == Event.None) //same thing from scheduler
			{
				host.msgWaiting(customer);
				customer._state = State.Waiting;
			}
			assertTrue("host should have logged \"msgWaiting recieved\" but logged " + host.log.getLastLoggedEvent(), host.log.containsString("msgWaiting recieved"));
			customer.msgCalledToDesk(teller);
			assertTrue(customer._event == Event.CalledToDesk);
			
			if(customer._state == State.Waiting && customer._event == Event.CalledToDesk) //from scheduler
			{
				teller.msgIAmHere(customer);
				customer._state = State.AtTeller;
			}
			assertTrue("teller should have logged \"msgIAmHere recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgIAmHere recieved"));
			customer.msgHereIsInfoPickARequest((double)0.0, (double)500.0, (int)9000);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)500.0);
			assertTrue(customer._accountNumber == (int)9000);
			assertTrue(customer._event == Event.GivenRequestPermission);
			
			if(customer._state == State.AtTeller && customer._event == Event.GivenRequestPermission)
			{
				teller.msgHereIsMyRequest(customer, customer._request, customer._amount);
				customer._state = State.GaveRequest;
			}
			assertTrue("teller should have logged \"msgHereIsMyRequest recieved\" but logged " + teller.log.getLastLoggedEvent(), teller.log.containsString("msgHereIsMyRequest recieved"));
			customer.msgTransactionComplete(customer._amount, customer._accountFunds, customer._amountOwed - customer._amount);
			assertTrue(customer._event == Event.ApprovedTransaction);
			assertTrue(customer._accountFunds == (double)0.0);
			assertTrue(customer._amountOwed == (double)0.0);	
		}
}
