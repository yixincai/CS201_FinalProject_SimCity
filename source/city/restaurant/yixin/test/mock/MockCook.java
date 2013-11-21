package city.restaurant.yixin.test.mock;

import java.util.Map;

import restaurant.interfaces.*;

public class MockCook extends Mock implements YixinCook{
	public EventLog log = new EventLog();

	public MockCook(String name) {
		super(name);
	}
	
	public void msgHereIsTheOrder(YixinWaiter w, String choice, int table){};
	
	public void msgOrderFulfillment(Market m, Map<String, Integer> order){};
}
