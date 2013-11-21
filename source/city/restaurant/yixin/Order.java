package city.restaurant.yixin;

public class Order {
	public String choice;
	public int tableNumber;
	public YixinWaiterRole w;
	public enum OrderState
	{None, NotCooked, Cooking, Cooked, Delivered};
	public OrderState state = OrderState.None;

	Order(YixinWaiterRole w, String choice, int tableNumber, OrderState state) {
		this.choice = choice;
		this.tableNumber = tableNumber;
		this.w = w;
		this.state = state;
	}
}
