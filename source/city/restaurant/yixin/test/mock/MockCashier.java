package city.restaurant.yixin.test.mock;

import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.market.Item;
import city.market.Market;
import city.restaurant.yixin.interfaces.YixinCashier;
import city.restaurant.yixin.interfaces.YixinCustomer;
import city.restaurant.yixin.interfaces.YixinWaiter;
import agent.Mock;

public class MockCashier extends Mock implements YixinCashier{
	public EventLog log = new EventLog();

	public MockCashier(String name) {
		super(name);
	}
	
	public void msgComputeBill(YixinWaiter w, YixinCustomer c, String choice){
		log.add(new LoggedEvent("Received ComputeBill from waiter. Choice = "+ choice));
	}
	
	public void msgHereIsThePayment(YixinCustomer c, double check, double cash){
		log.add(new LoggedEvent("Received HereIsTheCheck from customer. Check = "+ check + " Payment = "+ cash));
	}
	
	public void msgHereIsTheBill(Market m, double bill){
		log.add(new LoggedEvent("Received HereIsTheBill from market. Bill = "+ bill));
	}

	@Override
	public void msgHereIsTheInvoice(Market m, List<Item> invoice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Invoice received"));
	}
}
