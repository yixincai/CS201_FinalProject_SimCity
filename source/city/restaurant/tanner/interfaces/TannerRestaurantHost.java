package city.restaurant.tanner.interfaces;


public interface TannerRestaurantHost 
{
	public void msgHowLongIsTheWait(TannerRestaurantCustomer c);
	
	public void msgIWantFood(TannerRestaurantCustomer c);
	
	public void msgTheWaitIsTooLong(TannerRestaurantCustomer c);

	public void msgTableIsFree(int tableNumber, TannerRestaurantWaiter waiter);
}
