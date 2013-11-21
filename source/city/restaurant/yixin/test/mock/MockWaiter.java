package city.restaurant.yixin.test.mock;

import city.restaurant.yixin.interfaces.YixinCustomer;
import city.restaurant.yixin.interfaces.YixinWaiter;
import agent.Mock;
import utilities.EventLog;
import utilities.LoggedEvent;

public class MockWaiter extends Mock implements YixinWaiter{
	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}
	
	public void msgSitAtTable(YixinCustomer cust, int tablenumber, int count){}

	public void msgNoMoneyAndLeaving(YixinCustomer cust){}

	public void msgReadyToOrder(YixinCustomer cust){}

	public void msgHereIsTheChoice(YixinCustomer cust, String choice){}

	public void msgOrderIsReady(String choice, int tableNumber){}

	public void msgFoodRunsOut(String choice, int tableNumber){}

	public void msgDoneEating(YixinCustomer cust){}
	
	public void msgHereIsTheCheck(double money, YixinCustomer cust){
		log.add(new LoggedEvent("Received HereIsTheCheck from cashier. Check = "+ money));
	}

	public void msgLeavingRestaurant(YixinCustomer cust){}
	
	public void msgAskForBreak(){}

	public void msgBreakGranted(){}

	public void msgAskToComeBack(){}
	
}