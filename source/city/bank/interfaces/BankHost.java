package city.bank.interfaces;

import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;

public interface BankHost {
	public void msgWaiting(BankCustomer c);
	public void msgLeavingBank(BankTeller teller);
	public boolean isWaitingCustomersEmpty();
}
