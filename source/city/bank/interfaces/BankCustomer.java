package city.bank.interfaces;

import city.bank.BankTellerRole;

public interface BankCustomer {
	public void msgWeAreClosed();
	public void msgCalledToDesk(BankTeller t);
	public void msgHereIsInfoPickARequest(double funds, double amountOwed, int newAccntNum);
	public void msgTransactionComplete(double amountReceived, double funds, double amountOwed);
	public void msgTransactionDenied();
}
