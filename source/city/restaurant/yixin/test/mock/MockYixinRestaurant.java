package city.restaurant.yixin.test.mock;

import java.util.ArrayList;
import java.util.List;

import city.restaurant.yixin.*;
import city.restaurant.yixin.interfaces.*;
import agent.Mock;

public class MockYixinRestaurant extends Mock{

	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	public int businessAccountNumber = -1;

	public YixinHost Host;
	public YixinCook Cook;
	public YixinCashier Cashier;
	public List<YixinWaiter> Waiters = new ArrayList<YixinWaiter>();

	public MockYixinRestaurant(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
