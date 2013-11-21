package city.market.test.mock;
import java.util.List;

import city.market.Item;
import city.market.MarketCashierRole.CustomerOrder;
import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketCustomer;
import city.restaurant.Restaurant;
import agent.Mock;

public class MockMarketCashier extends Mock implements MarketCashier{

	public MockMarketCashier(String name){
		super(name);
	}
	
	public void msgPlaceOrder(MarketCustomer mc, List<Item> order){}

	public void msgHereAreGoods(CustomerOrder mc){}

	public void msgPay(MarketCustomer mc, double payment){}

	public void msgPlaceOrder(Restaurant r, List<Item> order){}

	public void msgHereIsPayment(Restaurant r, double payment){}
}
