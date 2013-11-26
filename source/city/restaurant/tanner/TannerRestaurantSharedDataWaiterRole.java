package city.restaurant.tanner;

import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import agent.Role;

public class TannerRestaurantSharedDataWaiterRole extends TannerRestaurantRegularWaiterRole 
{

	public TannerRestaurantSharedDataWaiterRole(PersonAgent person,
			TannerRestaurant rest, TannerRestaurantCook cook,
			TannerRestaurantCashier cashier, String name) {
		super(person, rest, cook, cashier, name);
	}
	
	
	

/*
//-------------------------------------------------Data--------------------------------------------------------------------------------------------------------------------

	
//----------------------------------------------Constructors---------------------------------------------------------------------------------------------------------------
	public TannerRestaurantSharedDataWaiterRole(PersonAgent person, TannerRestaurant rest, TannerRestaurantCook cook, TannerRestaurantCashier cashier, String name) {
		super(person);
		// TODO Auto-generated constructor stub
	}

//-----------------------------------------------Accessors-----------------------------------------------------------------------------------------------------------------
	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}

//-----------------------------------------------Messages-----------------------------------------------------------------------------------------------------------------

	@Override
	public void msgHereIsANewCustomer(TannerRestaurantCustomer c,
			int tableNumber, TannerRestaurantHost h, TannerRestaurantCook co) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(TannerRestaurantCustomer c, int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgICantAffordAnything(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgThatChoiceIsOutOfStock(int choice, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyReOrder(TannerRestaurantCustomer c, int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(int choice, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLikeTheCheck(TannerRestaurantCustomer c, int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheChek(float amount, TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoodBye(TannerRestaurantCustomer c) {
		// TODO Auto-generated method stub
		
	}
	
	
//-----------------------------------------------Scheduler-------------------------------------------------------------------------------------------------------------------
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
//------------------------------------------------Actions--------------------------------------------------------------------------------------------------------------------	
	
	
//-----------------------------------------------Commands--------------------------------------------------------------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}
*/
}
