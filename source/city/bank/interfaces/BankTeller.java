package city.bank.interfaces;

import city.bank.BankCustomerRole;

public interface BankTeller {
	public void msgIAmHere(BankCustomerRole c);
	public void msgHereIsMyRequest(BankCustomerRole c, String request, int amount);
	public void msgLeavingBank(BankCustomerRole c);
	public boolean isOccupied();
	public void setOccupied(boolean b);
	public int getTellerNum();
}
