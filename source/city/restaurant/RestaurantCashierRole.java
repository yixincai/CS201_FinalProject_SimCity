package city.restaurant;

import city.PersonAgent;
import city.market.Market;
import agent.Role;

public abstract class RestaurantCashierRole extends Role{

	public RestaurantCashierRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	public abstract void msgHereIsTheBill(Market m, double bill);

}
