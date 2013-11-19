package city.market.interfaces;

import city.market.MarketCashierRole.CustomerOrder;
import city.market.MarketCashierRole.RestaurantOrder;

public interface MarketEmployee {
	public void msgPickOrder(CustomerOrder mc);

	public void msgPickOrder(RestaurantOrder rc);
}
