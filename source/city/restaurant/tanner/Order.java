package city.restaurant.tanner;

import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class Order 
{
	TannerRestaurantWaiter waiter;
	int choice;
	int tableNumber;
	public enum State{orderPending, orderCooking, orderReady};
	public State orderState;
	
	public Order(TannerRestaurantWaiter w, int c, int tn)
	{
		waiter = w;
		choice = c;
		tableNumber = tn;
		orderState = State.orderPending;
	}

}
