package city.restaurant.omar;

import city.PersonAgent;
import city.Place;

public class OmarSharedDataWaiterRole extends OmarWaiterRole {
	public OmarSharedDataWaiterRole(PersonAgent person, OmarRestaurant r, OmarCookRole cook, OmarHostRole host, String name) {
		super(person, r, cook, host, name);
	}

	@Override
	protected void giveOrderToCook(OmarWaiterRole.MyCustomer m, OmarCookRole cook) {
		print("Process order");
		m.myCustomerState = MycustomerState.waitingForFood;
		waiterGui.DoGoToRevolvingStand();
		try {
			waiterSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//print("Making a new order");
        print("Trying to put order on revolving stand"); //FoodTicket put on revolving stand
        restaurant.revolving_stand.insert(new FoodTicket(this, m.customer));
	}

	@Override
	public void cmdFinishAndLeave() {//TODO INTEGRATE
		//role_state = RoleState.WantToLeave;
		//active = false;
		stateChanged();		
	}
	public Place place() {
		// TODO Auto-generated method stub
		return restaurant;
	}
}
