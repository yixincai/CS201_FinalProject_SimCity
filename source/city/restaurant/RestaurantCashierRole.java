package city.restaurant;

import java.util.Map;

import city.PersonAgent;
import city.market.Market;
import agent.Role;

public abstract class RestaurantCashierRole extends Role{

	public RestaurantCashierRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	public abstract void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list);

	public abstract void msgHereIsTheChange(Market m, double change);
	
	public abstract void msgTransactionComplete(double amount, Double balance, Double debt);
}
