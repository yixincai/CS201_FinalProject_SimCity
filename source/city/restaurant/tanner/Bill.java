package city.restaurant.tanner;

import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;


public class Bill
{
	//Data Members
	public double amount;
	public TannerRestaurantWaiter waiter;
	public TannerRestaurantCustomer customer;
	public enum BillState {computed, pending, paid, settled};
	public BillState state;
	public double changeDue;
	public double amountShort;
	public boolean paidInFull;
	public TannerRestaurant restaurant;
	
	
	public Bill(int choice, TannerRestaurantCustomer c, TannerRestaurantWaiter w, TannerRestaurant r)
	{
		this.restaurant = r;
		if(choice == -1)
		{
			amount = 10.0;
		}
		else
		{
			amount = restaurant.menu.get(choice).cost;
		}
		waiter = w;
		customer = c;
		state = BillState.computed;
		paidInFull = false;
	}
}
