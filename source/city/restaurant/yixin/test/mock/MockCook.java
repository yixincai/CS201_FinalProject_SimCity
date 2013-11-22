package city.restaurant.yixin.test.mock;

import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.market.Item;
import city.market.Market;
import city.restaurant.yixin.interfaces.YixinCook;
import city.restaurant.yixin.interfaces.YixinWaiter;
import agent.Mock;

public class MockCook extends Mock implements YixinCook{
	public EventLog log = new EventLog();

	public MockCook(String name) {
		super(name);
	}
	
	public void msgHereIsTheOrder(YixinWaiter w, String choice, int table){
		log.add(new LoggedEvent("Received HereIsTheOrder from waiter. Choice = "+ choice + " Table number = " + table));
	}

	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) {
		log.add(new LoggedEvent("Received OrderFulfillment from market"));		
	}

	@Override
	public void msgOrderFinished() {
		log.add(new LoggedEvent("Received OrderFinished from cashier"));				
	}
}
