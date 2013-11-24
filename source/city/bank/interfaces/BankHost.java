package city.bank.interfaces;

import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;

public interface BankHost {
	public void msgWaiting(BankCustomerRole c);
	public void msgLeavingBank(BankTellerRole teller);
	public boolean isWaitingCustomersEmpty();
}
