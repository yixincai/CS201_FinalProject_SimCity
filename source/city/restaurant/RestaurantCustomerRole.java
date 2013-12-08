package city.restaurant;

import city.interfaces.Person;
import agent.Role;

public abstract class RestaurantCustomerRole extends Role {

	public RestaurantCustomerRole(Person person) {
		super(person);
	}

	public abstract void cmdGotHungry();

}
