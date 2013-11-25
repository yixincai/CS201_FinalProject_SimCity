package city.bank.interfaces;

import city.bank.BankCustomerRole;

public interface BankTeller {
	public void msgIAmHere(BankCustomer c);
	public void msgHereIsMyRequest(BankCustomer c, String request, double amount);
	public void msgLeavingBank(BankCustomer c);
	public boolean isOccupied();
	public void setOccupied(boolean b);
	public int getTellerNum();
}
