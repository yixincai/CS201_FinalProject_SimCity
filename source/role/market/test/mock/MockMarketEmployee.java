package role.market.test.mock;

import role.market.MarketCashierRole.CustomerOrder;
import role.market.MarketCashierRole.RestaurantOrder;
import test.mock.Mock;
import role.market.interfaces.*;

public class MockMarketEmployee extends Mock implements MarketEmployee{
	public MockMarketEmployee(String name){
		super(name);
	}
	
	public void msgPickOrder(CustomerOrder mc){}

	public void msgPickOrder(RestaurantOrder rc){}
}
