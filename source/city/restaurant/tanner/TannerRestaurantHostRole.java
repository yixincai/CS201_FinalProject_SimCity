package city.restaurant.tanner;

import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import agent.Role;

public class TannerRestaurantHostRole extends Role implements TannerRestaurantHost 
{

	public TannerRestaurantHostRole(PersonAgent person, TannerRestaurant rest, String name) {
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

	@Override
	public void msgHowLongIsTheWait(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantFood(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTheWaitIsTooLong(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(int tableNumber, TannerRestaurantWaiter waiter) {
		// TODO Auto-generated method stub
		
	}

}
