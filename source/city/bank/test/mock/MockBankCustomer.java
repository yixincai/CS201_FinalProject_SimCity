package city.bank.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import agent.Mock;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankTeller;

public class MockBankCustomer extends Mock implements BankCustomer{

	public EventLog log;
	
	public MockBankCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgWeAreClosed() {
		log.add(new LoggedEvent("msgWeAreClosed recieved"));
	}

	@Override
	public void msgCalledToDesk(BankTellerRole teller) {
		log.add(new LoggedEvent("msgCalledToDesk recieved"));
	}

	@Override
	public void msgHereIsInfoPickARequest(double funds, double amountOwed, int newAccntNum) {
		log.add(new LoggedEvent("msgHereIsInfoPickARequest recieved"));
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

	@Override
	public void msgCalledToDesk(BankTeller t) {
		// TODO Auto-generated method stub
		
	}

}
