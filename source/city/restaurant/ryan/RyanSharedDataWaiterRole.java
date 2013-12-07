package city.restaurant.ryan;

import city.PersonAgent;
import city.restaurant.ryan.RyanWaiterRole.customerState;
import city.restaurant.ryan.RyanWaiterRole.waiterPlace;

public class RyanSharedDataWaiterRole extends RyanWaiterRole {

	public RyanSharedDataWaiterRole(PersonAgent p, RyanRestaurant r, String name) {
		// TODO Auto-generated constructor stub
		super(p, r, name);
	}

	@Override
	public void GoToChef(MyCustomer customer) {
		try{
			gui.DoGoToRevolvingStand();
			isMoving.acquire();
			wPlace = waiterPlace.inRestaurant;
			customer.state = customerState.WaitingForFood;
			print("Putting on Revolving that Customer " + customer.customer.getName() + " wants " + customer.choice);
			_restaurant.revolvingStand.insert(new RyanOrder(this, (RyanCustomerRole) customer.customer, customer.choice));
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print("Unexpected exception caught in Agent thread:", a);
    	}
		
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		role_state = RoleState.WantToLeave;
		stateChanged();	
	}

}
