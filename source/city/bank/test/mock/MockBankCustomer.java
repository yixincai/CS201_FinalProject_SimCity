package city.bank.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import agent.Mock;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankTeller;

public class MockBankCustomer extends Mock implements BankCustomer
{

	public EventLog log;
	public double amountOwed;
	public double balance;
	public int accNumber;
	
	public MockBankCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgWeAreClosed() {
		log.add(new LoggedEvent("msgWeAreClosed recieved"));
	}

	@Override
	public void msgCalledToDesk(BankTeller teller) {
		log.add(new LoggedEvent("msgCalledToDesk recieved"));
	}

	@Override
	public void msgHereIsInfoPickARequest(double funds, double amountOwed, int newAccntNum) {
		log.add(new LoggedEvent("msgHereIsInfoPickARequest recieved"));
		balance = funds;
		this.amountOwed = amountOwed;
		accNumber = newAccntNum;
	}

	@Override
	public void msgTransactionComplete(double amountReceived, double funds,
			double amountOwed) {
		log.add(new LoggedEvent("msgTransactionComplete recieved"));
	}

	@Override
	public void msgTransactionDenied() {
		log.add(new LoggedEvent("msgTransactionDenied recieved"));
	}


}
