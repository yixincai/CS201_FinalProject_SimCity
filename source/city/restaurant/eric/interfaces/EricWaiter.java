package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;

public interface EricWaiter
{
	// Properties
	public String getName();
	
	// Messages
	public void msgReachedDestination(); //from WaiterGui
	public void msgSeatCustomer(EricCustomer ca, int tableNumber); // from Host
	public void msgReadyToOrder(EricCustomer sender);
	public void msgHeresMyChoice(EricCustomer sender, String choice);
	public void msgOutOfOrder(String choice, int tableNumber); // from Cook
	public void msgOrderReady(String choice, int tableNumber); // from Cook
	public void msgGiveMeCheck(EricCustomer sender);// from Customer
	public void msgHereIsCheck(Check ch); // from Cashier
	public void msgLeaving(EricCustomer sender); // from Customer
	// Going on break messages:
	public void msgWantABreak(); // from RestaurantGui/RestaurantPanel
	public void msgNoBreak(); // from Waiter
	public void msgFinishCustomersGoOnBreak(); //from Waiter
	public void msgBreaksOver(); // from RestaurantGui/RestaurantPanel
}
