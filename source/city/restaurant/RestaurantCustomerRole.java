package city.restaurant;

import city.PersonAgent;
import agent.Role;

public abstract class RestaurantCustomerRole extends Role {

	public RestaurantCustomerRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}

	public abstract void cmdGotHungry();

}
