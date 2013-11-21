package city.market;

import city.Place;
import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketEmployee;

public class Market extends Place{
	
	boolean open;
	public MarketCashier MarketCashier;
	public MarketEmployee MarketEmployee;
	
	public Market(){
		super();
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
	}
		
	public void updateMarketStatus(){
		if (MarketCashier == null || MarketEmployee == null)
			open = false;
		else
			open = true;
	}
}
