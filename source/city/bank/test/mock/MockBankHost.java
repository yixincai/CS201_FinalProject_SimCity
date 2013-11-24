package city.bank.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankHost;
import agent.Mock;

public class MockBankHost extends Mock implements BankHost{

	public EventLog log;
	
	public MockBankHost(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgWaiting(BankCustomerRole c) {
		log.add(new LoggedEvent("msgWaiting recieved"));
	}

	@Override
	public void msgLeavingBank(BankTellerRole teller) {
		log.add(new LoggedEvent("msgLeavingBank recieved"));
	}

	@Override
	public boolean isWaitingCustomersEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
