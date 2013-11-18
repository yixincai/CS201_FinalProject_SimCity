package role.market.test.mock;

import java.util.List;
import java.util.Map;
import role.market.interfaces.*;
import role.market.Item;
import test.mock.Mock;

public class MockMarketCustomer extends Mock implements MarketCustomer{
	MockMarketCustomer(String name){
		super(name);
	}
	
	public void msgHereIsBill (double payment, Map<String, Double> price_list, List<Item> orderFulfillment){}
	
	public void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change){}
}
