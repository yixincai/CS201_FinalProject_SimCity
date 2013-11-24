package city.market;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.PersonAgent;
import city.Place;
import city.market.gui.MarketAnimationPanel;
import city.transportation.TruckAgent;

public class Market extends Place{
	
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	private MarketAnimationPanel animationPanel;
	public TruckAgent truck;
	private int businessAccountNumber = -1;

	public Market(String s, WorldViewBuilding _worldViewBuilding, BuildingInteriorAnimationPanel map){
		super("Market", _worldViewBuilding);
		this.animationPanel = (MarketAnimationPanel)map.getBuildingAnimation();
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
		animationPanel.hideRestaurantOrder(0);
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
