package city.restaurant.tanner;

import city.PersonAgent;
import city.Place;
import city.restaurant.RestaurantCustomerRole;

public class TannerRestaurantCustomerRole extends RestaurantCustomerRole {

	public TannerRestaurantCustomerRole(PersonAgent person, TannerRestaurant rest, String name) {
		super(person);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cmdGotHungry() {
		// TODO Auto-generated method stub

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
