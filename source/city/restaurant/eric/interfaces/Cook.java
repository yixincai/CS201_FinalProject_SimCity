package city.restaurant.eric.interfaces;

import java.util.Map;

public interface Cook
{
	// Properties
	public String getName();
	// Messages
	public void msgHereIsOrder(Waiter sender, String choice, int table);
	public void msgOrderComing(Market sender, Map<String, Integer> foodsComing); // from Market
	public void msgDelivery(Market sender, Map<String, Integer> foodsComing); // from Market
}
