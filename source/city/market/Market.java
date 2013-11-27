package city.market;

import java.util.concurrent.Semaphore;

import gui.*;
import city.*;
import city.interfaces.PlaceWithAnimation;
import city.market.gui.*;
import city.restaurant.yixin.gui.*;
import city.transportation.TruckAgent;

public class Market extends Place implements PlaceWithAnimation {
	
	boolean open;
	public MarketCashierRole MarketCashier;
	public MarketEmployeeRole MarketEmployee;
	private MarketAnimationPanel animationPanel;
	public TruckAgent truck;
	private int businessAccountNumber = -1;
	private Semaphore _cashierSemaphore = new Semaphore(1, true);
	private Semaphore _employeeSemaphore = new Semaphore(1, true);
	
	public Market(String s, WorldViewBuilding _worldViewBuilding, BuildingInteriorAnimationPanel map, WorldView worldView){
		super("Market", _worldViewBuilding);
		this.animationPanel = (MarketAnimationPanel)map.getBuildingAnimation();
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this, worldView);
		truck.startThread();
	}
	//constructor for Yixin unit testing
	public Market(){
		super("Market", null);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	//constructor for Ryan unit testing
	public Market(String name, WorldViewBuilding _worldViewBuilding){
		super(name, _worldViewBuilding);
		MarketCashier = new MarketCashierRole(null,this);
		MarketEmployee = new MarketEmployeeRole(null,this);
		truck = new TruckAgent(this);
	}
	
	public MarketCashierRole tryAcquireCashier(PersonAgent person){
		if (_cashierSemaphore.tryAcquire()){
			MarketCashier.setPersonAgent(person);
			return MarketCashier;
		}
		return null;
	}

	public MarketEmployeeRole tryAcquireEmployee(PersonAgent person){
		if (_employeeSemaphore.tryAcquire()){
			MarketEmployee.setPersonAgent(person);
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
		MarketCustomerRole customer = new MarketCustomerRole(p,this);
		MarketCustomerGui gui = new MarketCustomerGui(customer);
		customer.setGui(gui);
		animationPanel().addGui(gui);
		return customer;
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
	
	public MarketAnimationPanel animationPanel() {
		return animationPanel;
	}
	
	public void setTruck(TruckAgent truck){
		this.truck = truck;
	}
}
