package city.restaurant.tanner.interfaces;

import city.market.interfaces.MarketCashier;


public interface TannerRestaurantCashier
{
	public void msgComputeBill(int choice, TannerRestaurantCustomer customer, TannerRestaurantWaiter w);
	
	public void msgHereIsMyPayment(double billAmount, double myMoney, TannerRestaurantCustomer c);
	
	public void msgIDontHaveEnoughMoney(double billAmount, double myMoney, TannerRestaurantCustomer c);
	
	public void msgHereIsAMarketBill(double amount, MarketCashier m);
	
	public void msgHereIsYourChange(double change, MarketCashier m);
}
