package city.restaurant.eric.interfaces;

import java.util.Map;

public interface EricCook
{
	// Properties
	public String name();
	// Messages
	public void msgHereIsOrder(EricWaiter sender, String choice, int table);
	public void msgOrderComing(OLD_EricMarket sender, Map<String, Integer> foodsComing); // from Market
	public void msgDelivery(OLD_EricMarket sender, Map<String, Integer> foodsComing); // from Market
}
