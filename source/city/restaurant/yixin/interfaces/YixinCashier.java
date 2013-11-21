package city.restaurant.yixin.interfaces;
import java.util.*;

import city.market.Item;
import city.market.Market;

public interface YixinCashier {

	public void msgComputeBill(YixinWaiter w, YixinCustomer c, String choice);
	
	public void msgHereIsThePayment(YixinCustomer c, double check, double cash);
	
	public void msgHereIsTheBill(Market m, double bill);
	
	public void msgHereIsTheInvoice(Market m, List<Item> invoice);
}
