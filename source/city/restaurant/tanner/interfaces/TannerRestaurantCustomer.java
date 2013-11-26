package city.restaurant.tanner.interfaces;

import java.util.ArrayList;

public interface TannerRestaurantCustomer 
{
	public void msgImHungry();
	
	public void msgHereIsTheWaitingList(int numberInLine);
	
	public void msgFollowMeToTable(int tableNumber, ArrayList<Integer> menu, TannerRestaurantWaiter w);
	
	public void msgWhatWouldYouLike();
	
	public void msgYourChoiceIsOutOfStock(ArrayList<Integer> menu);
	
	public void msgHereIsYourFood(int choice);
	
	public void msgHereIsYourCheck(double billAmount);
	
	public void msgHereIsYourChange(double changeAmount);
	
	public void msgYouOweUs(float debt);
}
