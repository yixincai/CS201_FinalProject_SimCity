package city.restaurant;

import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.Place;

public abstract class Restaurant extends Place {
	
	// ------------------------------------ TYPE ------------------------------------------
	
	public Restaurant(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
		// TODO Auto-generated constructor stub
	}
	
	public enum Cuisine { BREAKFAST, NORMAL }
	public enum Upscaleness { UPSCALE, NORMAL, CHEAP }
	
	protected Cuisine _cuisine;
	protected Upscaleness _upscaleness;

	public Cuisine cuisine() { return _cuisine; }
	public Upscaleness upscaleness() { return _upscaleness; }
	
	public RestaurantCashierRole Cashier;
	public RestaurantCookRole Cook;
	// --------------------------------- CORRESPONDENCE ----------------------------------
	
	//Do we need this?
	//private Host _host;
	//protected void setHost(Host host) { _host = host; }
	//public host() { return _host; }
	
	
	
	// ------------------------------------ FACTORIES ---------------------------------------------
	public abstract RestaurantCustomerRole generateCustomerRole(PersonAgent person); // Make a new CustomerRole, which is initialized with a pointer to the HostRole.
	public abstract Role generateWaiterRole();
	public abstract Role getHostRole();
	public RestaurantCashierRole getCashier(){
		return Cashier;
	}

}
