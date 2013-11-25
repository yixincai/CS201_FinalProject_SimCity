package city.bank.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import agent.Mock;
import city.bank.BankCustomerRole;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller{
	
	public EventLog log;
	boolean _occupied;
	int tellerNum;

	public MockBankTeller(String name, int tellerNum) {
		super(name);
		log = new EventLog();
		_occupied = false;
		this.tellerNum = tellerNum;
	}

	@Override
	public void msgIAmHere(BankCustomer c) {
		log.add(new LoggedEvent("msgIAmHere recieved"));
	}

	@Override
	public void msgHereIsMyRequest(BankCustomer c, String request,
			double amount) {
		log.add(new LoggedEvent("msgHereIsMyRequest recieved"));
	}

	@Override
	public void msgLeavingBank(BankCustomer c) {
		log.add(new LoggedEvent("msgLeavingBank recieved"));
	}

	@Override
	public boolean isOccupied() {
		return _occupied;
	}

	@Override
	public void setOccupied(boolean b) {
		_occupied = b;
	}

	@Override
	public int getTellerNum() {
		return tellerNum;
	}
	
}
