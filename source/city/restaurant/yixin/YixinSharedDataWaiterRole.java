package city.restaurant.yixin;

public class YixinSharedDataWaiterRole extends YixinWaiterRole{
	public YixinSharedDataWaiterRole(String name) {
		super(name);
	}

	@Override
	protected void processOrder(YixinWaiterRole.MyCustomer customer) {
		print("Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToRevolvingStand();
		print("Making a new order");
        Order data = new Order(this, customer.choice, customer.tableNumber, Order.OrderState.NotCooked);
        print("Trying to put order on revolving stand");
        r.revolving_stand.insert(data);
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
}

