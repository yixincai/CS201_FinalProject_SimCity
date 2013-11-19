package city.market;

import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketEmployee;

public class Market {
	boolean open;
	public MarketCashier MarketCashier;
	public MarketEmployee MarketEmployee;
	
	public void updateMarketStatus(){
		if (MarketCashier == null || MarketEmployee == null)
			open = false;
		else
			open = true;
	}
}
