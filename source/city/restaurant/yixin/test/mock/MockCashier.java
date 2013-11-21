package city.restaurant.yixin.test.mock;

import restaurant.interfaces.*;

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
}
