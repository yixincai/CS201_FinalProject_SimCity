package city.restaurant.yixin.interfaces;

import java.util.*;

import city.market.Item;
import city.market.Market;

public interface YixinCook {
	public void msgHereIsTheOrder(YixinWaiter w, String choice, int table);
	
	public void msgOrderFulfillment(Market m, List<Item> order);
	
	public void msgOrderFinished();
}
