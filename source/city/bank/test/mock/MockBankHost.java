package city.bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankHost;
import city.bank.interfaces.BankTeller;
import agent.Mock;

public class MockBankHost extends Mock implements BankHost{

	public EventLog log;
	List<BankCustomer> waitingCustomers;
	
	public MockBankHost(String name) {
		super(name);
		log = new EventLog();
		waitingCustomers = new ArrayList<BankCustomer>();
	}

	@Override
	public void msgWaiting(BankCustomer c) {
		log.add(new LoggedEvent("msgWaiting recieved"));
	}

	@Override
	public void msgLeavingBank(BankTeller teller) {
		log.add(new LoggedEvent("msgLeavingBank recieved"));
	}

	@Override
	public boolean isWaitingCustomersEmpty() {
		return waitingCustomers.isEmpty();
	}
	
	public void addWaitingCustomer(BankCustomer c) {
		
	}

}
