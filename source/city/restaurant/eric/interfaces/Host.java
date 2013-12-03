package city.restaurant.eric.interfaces;

public interface Host
{
	// Properties
	public String getName();
	// Messages
	public void msgImOnDuty(Waiter sender);
	public void msgIWantFood(Customer sender);
	public void msgCustomerOwes(Customer customer, double owedAmount); // from Cashier
	public void msgLeaving(Customer sender);
	public void msgTableFree(Waiter sender, int tableNumber);
	public void msgIWantABreak(Waiter sender);
	public void msgGoingBackToWork(Waiter sender);
}
