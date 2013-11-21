package city.restaurant.yixin.interfaces;

public interface YixinHost {

	public void msgIWantFood(YixinCustomer cust, int count);

	public void msgIAmLeaving(YixinCustomer cust);
	
	public void msgIWantToStay(YixinCustomer cust);

	public void msgTableIsFree(YixinCustomer cust, int tablenumber);
	
	public void msgWantToBreak(YixinWaiter w);
	
	public void msgWantToComeBack(YixinWaiter w);

}
