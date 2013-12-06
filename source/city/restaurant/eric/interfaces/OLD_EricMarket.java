package city.restaurant.eric.interfaces;

import java.util.Map;

//TODO REMOVE AND APPROPRIATELY REFACTOR OTHER CODE. THIS IS HERE ONLY FOR REFERENCE.

public interface OLD_EricMarket
{
	// Properties
	public String getName();
	// Messages
	public void msgPlaceOrder(EricCook sender, Map<String,Integer> foods, EricCashier cashier);
	public void msgPayment(EricCashier sender, double amountToPay);
}
