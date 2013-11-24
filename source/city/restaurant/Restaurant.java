package city.restaurant;

import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.Place;

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
	
	
	
	// --------------------------------- PROPERTIES -----------------------------
	public abstract Role getHostRole();
	public RestaurantCashierRole getCashier(){
		return cashier;
	}
	
	
	
	// ------------------------------------ FACTORIES ---------------------------------------------
	public abstract RestaurantCustomerRole generateCustomerRole(PersonAgent person); // Make a new CustomerRole, which is initialized with a pointer to the HostRole.
	public abstract Role generateWaiterRole();

}
