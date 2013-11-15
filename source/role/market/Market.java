package role.market;

public class Market {
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	
	public void updateMarketStatus(){
		if (MarketCashier == null || MarketEmployee == null)
			open = false;
		else
			open = true;
	}
}
