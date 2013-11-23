package city.transportation.interfaces;

import java.util.List;

import city.market.Item;
import city.restaurant.Restaurant;

public interface Truck {
	public void msgDeliverToCook(List<Item> items, Restaurant restaurant);
	
	public void msgAtDestination();
	
	public void msgGoodsUnloaded(int order_id);

	public void msgAtMarket();
}
