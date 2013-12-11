package city.restaurant.eric.interfaces;

import java.util.Map;

public interface EricCook
{
	// Properties
	public String name();
	// Messages
	public void msgHereIsOrder(EricWaiter sender, String choice, int table);
}
