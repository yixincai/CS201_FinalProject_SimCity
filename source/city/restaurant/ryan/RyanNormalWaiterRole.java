package city.restaurant.ryan;

import gui.trace.AlertTag;
import city.PersonAgent;

public class RyanNormalWaiterRole extends RyanWaiterRole {

	public RyanNormalWaiterRole(PersonAgent p, RyanRestaurant restaurant,String name) {
		super(p, restaurant, name);
	}

	@Override
	public void GoToChef(MyCustomer customer) {
		try{
			gui.DoGoToChef();
			isMoving.acquire();
			wPlace = waiterPlace.inRestaurant;
			customer.state = customerState.WaitingForFood;
			print(AlertTag.RYAN_RESTAURANT, "Chef, Customer " + customer.customer.getName() + " wants " + customer.choice);
			cook.msgTryToCookOrder(this, (RyanCustomerRole) customer.customer, customer.choice);
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print(AlertTag.RYAN_RESTAURANT, "Unexpected exception caught in Agent thread:", a);
    	}
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}

}
