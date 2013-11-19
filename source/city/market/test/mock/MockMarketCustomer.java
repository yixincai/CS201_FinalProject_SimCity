package city.market.test.mock;

import java.util.List;
import java.util.Map;

import city.market.Item;
import city.market.interfaces.MarketCustomer;
import agent.Mock;

public class MockMarketCustomer extends Mock implements MarketCustomer{
	public MockMarketCustomer(String name){
		super(name);
	}
	
	public void msgHereIsBill (double payment, Map<String, Double> price_list, List<Item> orderFulfillment){}
	
	public void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change){}
	
	public void msgHereIsGoodAndDebt(List<Item> orderFulfillment, double debt){}
}
