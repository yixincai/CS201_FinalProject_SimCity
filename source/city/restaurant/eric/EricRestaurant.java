package city.restaurant.eric;

import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;

public class EricRestaurant extends Restaurant {

	public EricRestaurant(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}

	@Override
	public Role getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person, boolean isSharedDataWaiter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateCashierGui() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateCookGui() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateHostGui() {
		// TODO Auto-generated method stub

	}

}
