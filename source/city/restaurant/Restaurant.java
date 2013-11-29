package city.restaurant;

import java.util.concurrent.Semaphore;

import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.Place;
import city.restaurant.yixin.YixinCashierRole;
import city.restaurant.yixin.YixinRestaurant;
import city.restaurant.yixin.gui.YixinCashierGui;

public abstract class Restaurant extends Place {
	
	// ------------------------------------ TYPE ------------------------------------------
	

	public Restaurant(String name) {
		super(name, null);
	}

	public Restaurant(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}
	
	// Type:
	public enum Cuisine { BREAKFAST, NORMAL }
	public enum Upscaleness { UPSCALE, NORMAL, CHEAP }
	protected Cuisine _cuisine;
	protected Upscaleness _upscaleness;
	public Cuisine cuisine() { return _cuisine; }
	public Upscaleness upscaleness() { return _upscaleness; }
	
	// Correspondence for Markets:
	public RestaurantCashierRole cashier;
	public RestaurantCookRole cook;
	
	// Semaphores for the host, cashier, and cook
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	private Semaphore _cookSemaphore = new Semaphore(1, true);
	private Semaphore _cashierSemaphore = new Semaphore(1, true);
	
	// --------------------------------- PROPERTIES -----------------------------
	public abstract Role getHostRole();
	public RestaurantCashierRole getCashier(){
		return cashier;
	}
	
	// ------------------------------------ FACTORIES & ROLE ACQUIRES ---------------------------------------------
	public abstract RestaurantCustomerRole generateCustomerRole(PersonAgent person); // Make a new CustomerRole, which is initialized with a pointer to the HostRole.
	public abstract Role generateWaiterRole(PersonAgent person);
	public abstract void generateCashierGui();
	public abstract void generateCookGui();
	public abstract void generateHostGui();
	
	public RestaurantCookRole tryAcquireCook(PersonAgent person) {
		if(_cookSemaphore.tryAcquire()) {
			cook.setPerson(person);
			generateCookGui();
			return cook;
		}
		else return null;
	}
	
	public RestaurantCashierRole tryAcquireCashier(PersonAgent person) {
		if(_cashierSemaphore.tryAcquire()) {
			cashier.setPerson(person);
			generateCashierGui();
			return cashier;
		}
		else return null;
	}
	
	public Role tryAcquireHost(PersonAgent person) {
		if(_hostSemaphore.tryAcquire()) {
			getHostRole().setPerson(person);
			generateHostGui();
			return getHostRole();
		}
		else return null;
	}

}
