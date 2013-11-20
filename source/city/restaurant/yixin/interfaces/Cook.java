package city.restaurant.yixin.interfaces;

import java.util.*;
import restaurant.Item;

public interface Cook {
	public void msgHereIsTheOrder(Waiter w, String choice, int table);
	
	public void msgOrderFulfillment(Market m, List<Item> order);
}
