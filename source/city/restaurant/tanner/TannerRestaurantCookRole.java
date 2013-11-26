package city.restaurant.tanner;

import java.awt.Point;
import java.util.List;

import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.market.interfaces.MarketCashier;
import city.restaurant.RestaurantCookRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCookRole extends RestaurantCookRole implements TannerRestaurantCook
{

//--------------------------------------------Data---------------------------------------------------------------------
	
//----------------------------------------Constructors-----------------------------------------------------------------	
	public TannerRestaurantCookRole(PersonAgent person, TannerRestaurant rest, TannerRestaurantCashierRole cashier) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	
//-----------------------------------------Accessors------------------------------------------------------------------
	
	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

//------------------------------------------Messages------------------------------------------------------------------
	
	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgThisIsWhatIHave(int[] ItemsGiven, MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAllOutOfGoods(MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheBill(float amount, MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	
//------------------------------------------Scheduler-----------------------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

//------------------------------------------Actions------------------------------------------------------------------
	
	
//------------------------------------------Commands-----------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsANewOrder(int choice, int tableNumber,
			TannerRestaurantWaiter w) {
		// TODO Auto-generated method stub
		
	}
}
