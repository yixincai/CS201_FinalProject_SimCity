package city.restaurant.eric.interfaces;

import java.util.Map;

public interface Market
{
	// Properties
	public String getName();
	// Messages
	public void msgPlaceOrder(Cook sender, Map<String,Integer> foods, Cashier cashier);
	public void msgPayment(Cashier sender, double amountToPay);
}
