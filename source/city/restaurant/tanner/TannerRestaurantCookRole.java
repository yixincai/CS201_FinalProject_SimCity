package city.restaurant.tanner;

import java.util.List;

import city.Place;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCookRole;

public class TannerRestaurantCookRole extends RestaurantCookRole {

	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) {
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
