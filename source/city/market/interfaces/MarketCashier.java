package city.market.interfaces;

import java.util.List;

import city.market.Item;
import city.market.MarketCashierRole.CustomerOrder;
import city.restaurant.Restaurant;

public interface MarketCashier {

	public void msgPlaceOrder(MarketCustomer mc, List<Item> order);

	public void msgHereAreGoods(CustomerOrder mc);

	public void msgPay(MarketCustomer mc, double payment);

	public void msgPlaceOrder(Restaurant r, List<Item> order);

	public void msgHereIsPayment(Restaurant r, double payment);
}
