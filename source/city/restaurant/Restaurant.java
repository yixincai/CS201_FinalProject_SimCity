package city.restaurant;

import java.util.concurrent.Semaphore;

import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.Place;

public abstract class Restaurant extends Place {

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
	protected RestaurantCashierRole _cashier;
	protected RestaurantCookRole _cook;
	
	// Semaphores for the host, cashier, and cook
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	private Semaphore _cookSemaphore = new Semaphore(1, true);
	private Semaphore _cashierSemaphore = new Semaphore(1, true);
	
	// --------------------------------- PROPERTIES -----------------------------
	public abstract void clearInventory();
	public abstract Role getHost();
	public RestaurantCashierRole getCashier(){ return _cashier; }
	public RestaurantCookRole getCook() { return _cook; }
	public abstract boolean existActiveWaiter();
	//for person agent and market
	public boolean isOpen(){
		if (getCashier().active && getHost().active && getCook().active && existActiveWaiter())
			return true;
		else
			return false;
	}
	
	// ------------------------------------ FACTORIES & ROLE ACQUIRES ---------------------------------------------
	public abstract RestaurantCustomerRole generateCustomerRole(PersonAgent person); // make a new CustomerRole, which is initialized with a pointer to the HostRole and other appropriate initializations such as gui.
	public abstract Role generateWaiterRole(PersonAgent person, boolean isSharedDataWaiter);
	
	// These are a little different from regular factories because they don't return a value; they are more like utilities which are called by the role acquire methods.
	/** Generate an appropriate CashierGui and set the Cashier's gui to it. */
	public abstract void generateCashierGui();
	/** Generate an appropriate CookGui and set the Cook's gui to it. */
	public abstract void generateCookGui();
	/** Generate an appropriate HostGui and set the Host's gui to it. */
	public abstract void generateHostGui();
	
	public RestaurantCookRole tryAcquireCookRole(PersonAgent person) {
		if(_cookSemaphore.tryAcquire()) {
			_cook.setPerson(person);
			generateCookGui();
			return _cook;
		}
		else return null;
	}
	
	public RestaurantCashierRole tryAcquireCashierRole(PersonAgent person) {
		if(_cashierSemaphore.tryAcquire()) {
			_cashier.setPerson(person);
			generateCashierGui();
			return _cashier;
		}
		else return null;
	}
	
	public Role tryAcquireHostRole(PersonAgent person) {
		if(_hostSemaphore.tryAcquire()) {
			getHost().setPerson(person);
			generateHostGui();
			return getHost();
		}
		else return null;
	}

}
