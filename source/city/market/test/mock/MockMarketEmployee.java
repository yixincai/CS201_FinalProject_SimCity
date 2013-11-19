package city.market.test.mock;

import city.market.MarketCashierRole.CustomerOrder;
import city.market.MarketCashierRole.RestaurantOrder;
import city.market.interfaces.MarketEmployee;
import agent.Mock;

public class MockMarketEmployee extends Mock implements MarketEmployee{
	public MockMarketEmployee(String name){
		super(name);
	}
	
	public void msgPickOrder(CustomerOrder mc){}

	public void msgPickOrder(RestaurantOrder rc){}
}
