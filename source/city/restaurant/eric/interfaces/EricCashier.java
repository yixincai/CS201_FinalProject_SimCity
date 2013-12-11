package city.restaurant.eric.interfaces;

import java.util.Map;

import city.market.Market;
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
	public void msgIReceivedTheseFoods(Market market, Map<String, Integer> foodsReceived);
}
