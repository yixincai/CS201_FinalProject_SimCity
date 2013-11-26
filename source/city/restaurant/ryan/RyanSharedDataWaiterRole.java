package city.restaurant.ryan;

import city.PersonAgent;

public class RyanSharedDataWaiterRole extends RyanWaiterRole {

	public RyanSharedDataWaiterRole(PersonAgent p, RyanRestaurant r, String name) {
		// TODO Auto-generated constructor stub
		super(p, r, name);
	}

	@Override
	public void GoToChef(MyCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		role_state = RoleState.WantToLeave;
		stateChanged();	
	}

}
