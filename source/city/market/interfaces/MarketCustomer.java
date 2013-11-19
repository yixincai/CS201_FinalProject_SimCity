package city.market.interfaces;

import java.util.List;
import java.util.Map;

import city.market.Item;

public interface MarketCustomer {
	public void msgHereIsBill (double payment, Map<String, Double> price_list, List<Item> orderFulfillment);
	
	public void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change);
	
	public void msgHereIsGoodAndDebt(List<Item> orderFulfillment, double debt);
}
