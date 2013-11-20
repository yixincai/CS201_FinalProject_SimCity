package city.restaurant.omar.interfaces;

import city.restaurant.omar.CashierAgent;

public interface Market {
	public abstract void msgTakeMoney(CashierAgent cashier, double price);
}
