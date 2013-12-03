package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;

public interface Waiter
{
	// Properties
	public String getName();
	
	// Messages
	public void msgReachedDestination(); //from WaiterGui
	public void msgSeatCustomer(Customer ca, int tableNumber); // from Host
	public void msgReadyToOrder(Customer sender);
	public void msgHeresMyChoice(Customer sender, String choice);
	public void msgOutOfOrder(String choice, int tableNumber); // from Cook
	public void msgOrderReady(String choice, int tableNumber); // from Cook
	public void msgGiveMeCheck(Customer sender);// from Customer
	public void msgHereIsCheck(Check ch); // from Cashier
	public void msgLeaving(Customer sender); // from Customer
	// Going on break messages:
	public void msgWantABreak(); // from RestaurantGui/RestaurantPanel
	public void msgNoBreak(); // from Waiter
	public void msgFinishCustomersGoOnBreak(); //from Waiter
	public void msgBreaksOver(); // from RestaurantGui/RestaurantPanel
}
