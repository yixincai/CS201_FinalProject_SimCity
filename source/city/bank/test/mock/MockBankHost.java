package city.bank.test.mock;

import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankHost;
import agent.Mock;

public class MockBankHost extends Mock implements BankHost{

	public MockBankHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWaiting(BankCustomerRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingBank(BankTellerRole teller) {
		// TODO Auto-generated method stub
		
	}

}
