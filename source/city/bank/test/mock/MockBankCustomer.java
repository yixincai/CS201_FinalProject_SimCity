package city.bank.test.mock;

import agent.Mock;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankCustomer;

public class MockBankCustomer extends Mock implements BankCustomer{

	public MockBankCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWeAreClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCalledToDesk(BankTellerRole teller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsInfoPickARequest(double funds, double amountOwed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionComplete(double amountReceived, double funds,
			double amountOwed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionDenied() {
		// TODO Auto-generated method stub
		
	}
}
