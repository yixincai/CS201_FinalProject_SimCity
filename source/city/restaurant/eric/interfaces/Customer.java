package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;
import city.restaurant.eric.Menu;
import city.restaurant.eric.gui.CustomerGui;

public interface Customer
{
	// Properties
	public String getName();
	public CustomerGui gui();
	// Messages
	public void msgGotHungry(); // from CustomerGui
	public void msgRestaurantIsFull(); // from Host
	public void msgGoToCashierAndPayDebt(Cashier cashier); // from Host
	public void msgWeWontServeYou(); // from Host
	public void msgComeToFrontDesk(); // from Host
	public void msgReachedFrontDesk(); // from CustomerGui
	public void msgFollowMeToTable(Waiter sender, Menu menu); // from Waiter
	public void msgReachedTable(); // from CustomerGui
	public void msgWhatDoYouWant(); // from Waiter
	public void msgOutOfChoice(Menu m); // from Waiter
	public void msgHeresYourFood(String choice); // from Waiter
	public void msgFinishedEating(); // from CustomerGui
	public void msgHeresYourCheck(Check check, Cashier cashier); // from Waiter
	public void msgReachedCashier(); // from CustomerGui
	public void msgHereIsChange(double change); // from Cashier
	public void msgFinishedLeavingRestaurant(); // from CustomerGui
}
