package city.restaurant.tanner.interfaces;

import city.restaurant.tanner.gui.TannerRestaurantHostRoleGui;


public interface TannerRestaurantHost 
{
	public void msgHowLongIsTheWait(TannerRestaurantCustomer c);
	
	public void msgIWantFood(TannerRestaurantCustomer c);
	
	public void msgTheWaitIsTooLong(TannerRestaurantCustomer c);

	public void msgTableIsFree(int tableNumber, TannerRestaurantWaiter waiter);
	
	public void addWaiter(TannerRestaurantWaiter w);

	public void setGui(TannerRestaurantHostRoleGui tannerHostGui);
	
}
