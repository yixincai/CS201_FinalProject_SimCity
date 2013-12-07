package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;

public interface EricCashier
{
	// Properties
	public String name();
	// Messages
	public void msgGiveMeCheck(EricWaiter sender, String choice, int table);
	public void msgHereIsMoney(EricCustomer sender, double money, Check c);
	public void msgDoesCustomerOwe(EricCustomer customer); // from Host
	public void msgHereIsOwedMoney(EricCustomer sender, double money);
	public void msgYouOwe(OLD_EricMarket sender, double amount);
}
