package city.restaurant.tanner;

import java.util.Map;

import city.PersonAgent;
import city.Place;
import city.market.Market;
import city.market.interfaces.MarketCashier;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCashierRole extends RestaurantCashierRole implements TannerRestaurantCashier
{
	
//---------------------------------------------Data-----------------------------------------------------------
	
//------------------------------------------Constructor-------------------------------------------------------
	
	public TannerRestaurantCashierRole(PersonAgent person, TannerRestaurant rest) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	
	
//------------------------------------------Accessors-----------------------------------------------------------
	
	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}


//------------------------------------------Messages-----------------------------------------------------------
	
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
	public void msgComputeBill(int choice, TannerRestaurantCustomer customer,
			TannerRestaurantWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyPayment(float bill, float cash,
			TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIDontHaveEnoughMoney(float bill, float cash,
			TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAMarketBill(float amount, MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourChange(float change, MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	
//-----------------------------------------Scheduler-----------------------------------------------------------
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
//-----------------------------------------Commands------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}
}
