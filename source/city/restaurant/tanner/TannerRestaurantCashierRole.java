package city.restaurant.tanner;

import java.util.Map;

import city.PersonAgent;
import city.Place;
import city.market.Market;
import city.restaurant.RestaurantCashierRole;

public class TannerRestaurantCashierRole extends RestaurantCashierRole {

	public TannerRestaurantCashierRole(PersonAgent person, TannerRestaurant rest) {
		super(person);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsTheBill(Market m, double bill,
			Map<String, Double> price_list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsTheChange(Market m, double change) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgTransactionComplete(double amount, Double balance,
			Double debt, int newAccountNumber) {
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
