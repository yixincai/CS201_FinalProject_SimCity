package city.restaurant.tanner;

import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import agent.Role;

public class TannerRestaurantSharedDataWaiterRole extends Role {

	public TannerRestaurantSharedDataWaiterRole(PersonAgent person, TannerRestaurant rest, TannerRestaurantCook cook, TannerRestaurantCashier cashier, String name) {
		super(person);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}

}
