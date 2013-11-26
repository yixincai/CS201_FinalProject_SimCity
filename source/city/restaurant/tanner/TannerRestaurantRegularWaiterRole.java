package city.restaurant.tanner;

import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.MyCustomer.customerState;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import agent.Role;

public class TannerRestaurantRegularWaiterRole extends TannerRestaurantBaseWaiterRole
{

	public TannerRestaurantRegularWaiterRole(PersonAgent person, TannerRestaurant rest, String name)
	{
		super(person, rest, name);
	}

	@Override
	protected void SubmitOrder(MyCustomer c) 
	{
		print("Submit order to cook");
		myGUI.DoGoToCook(cook.getPosition());
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.currentState = customerState.orderIn;
	}

}
