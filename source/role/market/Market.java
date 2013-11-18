package role.market;

import role.market.interfaces.MarketCashier;
import role.market.interfaces.MarketEmployee;

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
