package city.market.interfaces;

import java.util.List;

import city.market.Item;
import city.market.Restaurant;
import city.market.MarketCashierRole.CustomerOrder;

public interface MarketCashier {

	public void msgPlaceOrder(MarketCustomer mc, List<Item> order);

	public void msgHereAreGoods(CustomerOrder mc);

	public void msgPay(MarketCustomer mc, double payment);

	public void msgPlaceOrder(Restaurant r, List<Item> order);

	public void msgHereIsPayment(Restaurant r, double payment);
}
