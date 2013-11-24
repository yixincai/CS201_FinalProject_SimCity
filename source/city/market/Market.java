package city.market;

import java.util.concurrent.Semaphore;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.PersonAgent;
import city.Place;
import city.market.gui.MarketAnimationPanel;
import city.restaurant.yixin.gui.YixinAnimationPanel;
import city.transportation.TruckAgent;

public class Market extends Place{
	
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	private MarketAnimationPanel animationPanel;
	public TruckAgent truck;
	private int businessAccountNumber = -1;
	private Semaphore _cashierSemaphore = new Semaphore(1, true);
	private Semaphore _employeeSemaphore = new Semaphore(1, true);
	
	public Market(String s, WorldViewBuilding _worldViewBuilding, BuildingInteriorAnimationPanel map){
		super("Market", _worldViewBuilding);
		this.animationPanel = (MarketAnimationPanel)map.getBuildingAnimation();
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	//constructor for Yixin unit testing
	public Market(){
		super("Market", null);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	//constructor for Ryan unit testing
	public Market(String name){
		super(name, null);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	
	public MarketCashierRole tryAcquireCashier(){
		if (_cashierSemaphore.tryAcquire()){
			return MarketCashier;
		}
		return null;
	}

	public MarketEmployeeRole tryAcquireEmployee(){
		if (_employeeSemaphore.tryAcquire()){
			return MarketEmployee;
		}
		return null;
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
	
	public MarketAnimationPanel getAnimationPanel() {
		return animationPanel;
	}
}
