package city.restaurant.tanner;

import java.util.ArrayList;

import city.PersonAgent;
import city.Place;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCustomerRole extends RestaurantCustomerRole implements TannerRestaurantCustomer
{

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

	@Override
	public void msgImHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheWaitingList(int numberInLine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(int tableNumber, ArrayList<Integer> menu,
			TannerRestaurantWaiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYourChoiceIsOutOfStock(ArrayList<Integer> menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourCheck(float bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourChange(float changeAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouOweUs(float debt) {
		// TODO Auto-generated method stub
		
	}

}
