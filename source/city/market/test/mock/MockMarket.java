package city.market.test.mock;

import city.market.MarketCashierRole;
import city.market.MarketEmployeeRole;
import city.transportation.TruckAgent;
import agent.Mock;

public class MockMarket extends Mock{
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	public TruckAgent truck;
	public int businessAccountNumber = -1;
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
