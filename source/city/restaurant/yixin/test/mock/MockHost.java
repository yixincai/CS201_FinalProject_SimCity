package city.restaurant.yixin.test.mock;

import restaurant.interfaces.*;

public class MockHost extends Mock implements YixinHost{
	public EventLog log = new EventLog();

	public MockHost(String name) {
		super(name);
	}
	
	public void msgIWantFood(YixinCustomer cust, int count){}

	public void msgIAmLeaving(YixinCustomer cust){}
	
	public void msgIWantToStay(YixinCustomer cust){}

	public void msgTableIsFree(YixinCustomer cust, int tablenumber){}
	
	public void msgWantToBreak(YixinWaiter w){}
	
	public void msgWantToComeBack(YixinWaiter w){}
}
