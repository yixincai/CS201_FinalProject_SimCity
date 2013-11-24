package city.market;

import gui.WorldViewBuilding;
import city.PersonAgent;
import city.Place;
import city.transportation.TruckAgent;

public class Market extends Place{
	
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	public TruckAgent truck;
	private int businessAccountNumber = -1;

	public Market(String s, WorldViewBuilding _worldViewBuilding){
		super("Market", _worldViewBuilding);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	
	public Market(){
		super("Market", null);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	
	public Market(String name){
		super(name, null);
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

	public void updateAccountNumber(int newAccountNumber) {
		this.businessAccountNumber = newAccountNumber;
	}
	
	public int getAccountNumber(){
		return this.businessAccountNumber;
	}
}
