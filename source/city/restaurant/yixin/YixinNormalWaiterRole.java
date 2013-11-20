package city.restaurant.yixin;

public class YixinNormalWaiterRole extends YixinWaiterRole{

	public YixinNormalWaiterRole(String name) {
		super(name);
	}

	@Override
	protected void processOrder(MyCustomer customer) {
		print("Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToCook();
		cook.msgHereIsTheOrder(this, customer.choice, customer.tableNumber);
	}
}

