package city.restaurant.eric.interfaces;

public interface EricHost
{
	// Properties
	public String name();
	// Messages
	public void msgImOnDuty(EricWaiter sender);
	public void msgIWantFood(EricCustomer sender);
	public void msgCustomerOwes(EricCustomer customer, double owedAmount); // from Cashier
	public void msgLeaving(EricCustomer sender);
	public void msgTableFree(EricWaiter sender, int tableNumber);
	public void msgIWantABreak(EricWaiter sender);
	public void msgGoingBackToWork(EricWaiter sender);
}
