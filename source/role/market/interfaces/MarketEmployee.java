package role.market.interfaces;

import role.market.MarketCashierRole.CustomerOrder;
import role.market.MarketCashierRole.RestaurantOrder;

public interface MarketEmployee {
	public void msgPickOrder(CustomerOrder mc);

	public void msgPickOrder(RestaurantOrder rc);
}
