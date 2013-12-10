package city.restaurant.tanner;

import gui.trace.AlertTag;
import city.PersonAgent;
import city.Place;
import city.restaurant.tanner.MyCustomer.customerState;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import agent.Role;

public class TannerRestaurantSharedDataWaiterRole extends TannerRestaurantBaseWaiterRole 
{

	public TannerRestaurantSharedDataWaiterRole(PersonAgent person, TannerRestaurant rest, String name) 
	{
		super(person, rest, name);
	}

	@Override
	protected void SubmitOrder(MyCustomer c) 
	{
		print(AlertTag.TANNER_RESTAURANT, "Process order");
		c.currentState = customerState.orderIn; 
		myGUI.DoGoToRevolvingStand();
		try {
			doingAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Order o = new Order(this, c.order, c.tableNumber);
        print(AlertTag.TANNER_RESTAURANT, "Trying to put order on revolving stand");
        restaurant.revolvingStand.insert(o);		
	}

}
