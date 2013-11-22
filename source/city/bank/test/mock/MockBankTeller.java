package city.bank.test.mock;

import agent.Mock;
import city.bank.BankCustomerRole;
import city.bank.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller{

	public MockBankTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgIAmHere(BankCustomerRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyRequest(BankCustomerRole c, String request,
			int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingBank(BankCustomerRole c) {
		// TODO Auto-generated method stub
		
	}
}
