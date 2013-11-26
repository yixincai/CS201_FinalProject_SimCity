package city.restaurant.tanner.interfaces;

import city.market.interfaces.MarketCashier;


public interface TannerRestaurantCashier
{
	public void msgComputeBill(int choice, TannerRestaurantCustomer customer, TannerRestaurantWaiter w);
	
	public void msgHereIsMyPayment(float bill, float cash, TannerRestaurantCustomer c);
	
	public void msgIDontHaveEnoughMoney(float bill, float cash, TannerRestaurantCustomer c);
	
	public void msgHereIsAMarketBill(float amount, MarketCashier m);
	
	public void msgHereIsYourChange(float change, MarketCashier m);
}
