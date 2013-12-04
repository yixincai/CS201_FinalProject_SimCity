package city.restaurant;

import java.util.List;

import city.PersonAgent;
import city.market.Item;
import city.market.Market;
import agent.Role;

public abstract class RestaurantCookRole extends Role{

	public RestaurantCookRole(PersonAgent person) {
		super(person);
	}
	public abstract void msgOrderFulfillment(Market m, List<Item> order);

}
