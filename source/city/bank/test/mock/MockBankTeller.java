package city.bank.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import agent.Mock;
import city.bank.BankCustomerRole;
import city.bank.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller{
	
	EventLog log;

	public MockBankTeller(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgIAmHere(BankCustomerRole c) {
		log.add(new LoggedEvent("msgIAmHere recieved"));
	}

	@Override
	public void msgHereIsMyRequest(BankCustomerRole c, String request,
			int amount) {
		log.add(new LoggedEvent("msgHereIsMyRequest recieved"));
	}

	@Override
	public void msgLeavingBank(BankCustomerRole c) {
		log.add(new LoggedEvent("msgLeavingBank recieved"));
	}
}
