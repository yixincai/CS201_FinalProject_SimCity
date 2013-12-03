package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;

public interface Cashier
{
	// Properties
	public String getName();
	// Messages
	public void msgGiveMeCheck(Waiter sender, String choice, int table);
	public void msgHereIsMoney(Customer sender, double money, Check c);
	public void msgDoesCustomerOwe(Customer customer); // from Host
	public void msgHereIsOwedMoney(Customer sender, double money);
	public void msgYouOwe(Market sender, double amount);
}
