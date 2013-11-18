package role.market.interfaces;

import java.util.List;

import role.market.Item;
import role.market.Restaurant;
import role.market.MarketCashierRole.CustomerOrder;

public interface MarketCashier {

	public void msgPlaceOrder(MarketCustomer mc, List<Item> order);

	public void msgHereAreGoods(CustomerOrder mc);

	public void msgPay(MarketCustomer mc, double payment);

	public void msgPlaceOrder(Restaurant r, List<Item> order);

	public void msgHereIsPayment(Restaurant r, double payment);
}
