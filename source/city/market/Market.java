package city.market;

import city.PersonAgent;
import city.Place;
import city.market.interfaces.MarketCashier;
import city.market.interfaces.MarketEmployee;
import city.transportation.TruckAgent;

public class Market extends Place{
	
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	public TruckAgent truck;
	
	public Market(){
		super();
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	
	public void msgPickUpItems(){
		//gui.removeFirstRestaurantOrder();
	}
		
	public void updateMarketStatus(){
		if (MarketCashier == null || MarketEmployee == null)
			open = false;
		else
			open = true;
	}
	
	public MarketCustomerRole generateCustomerRole(PersonAgent p){
		return (new MarketCustomerRole(p,this));
	}
	
	public MarketCashierRole getCashier(){
		return MarketCashier;
	}
}
