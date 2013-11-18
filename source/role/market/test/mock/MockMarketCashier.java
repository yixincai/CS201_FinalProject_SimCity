package role.market.test.mock;
import java.util.List;

import test.mock.*;
import role.market.Item;
import role.market.Restaurant;
import role.market.MarketCashierRole.CustomerOrder;
import role.market.interfaces.*;

public class MockMarketCashier extends Mock implements MarketCashier{

	MockMarketCashier(String name){
		super(name);
	}
	
	public void msgPlaceOrder(MarketCustomer mc, List<Item> order){}

	public void msgHereAreGoods(CustomerOrder mc){}

	public void msgPay(MarketCustomer mc, double payment){}

	public void msgPlaceOrder(Restaurant r, List<Item> order){}

	public void msgHereIsPayment(Restaurant r, double payment){}
}
