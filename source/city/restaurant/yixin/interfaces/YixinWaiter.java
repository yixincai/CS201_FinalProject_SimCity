package city.restaurant.yixin.interfaces;

public interface YixinWaiter {

	public abstract void msgSitAtTable(YixinCustomer cust, int tablenumber, int count);

	public abstract void msgNoMoneyAndLeaving(YixinCustomer cust);

	public abstract void msgReadyToOrder(YixinCustomer cust);

	public abstract void msgHereIsTheChoice(YixinCustomer cust, String choice);

	public abstract void msgOrderIsReady(String choice, int tableNumber);

	public abstract void msgFoodRunsOut(String choice, int tableNumber);

	public abstract void msgDoneEating(YixinCustomer cust);
	
	public abstract void msgHereIsTheCheck(double money, YixinCustomer cust);

	public abstract void msgLeavingRestaurant(YixinCustomer cust);
	
	public void msgAskForBreak();

	public void msgBreakGranted();

	public void msgAskToComeBack();

}
