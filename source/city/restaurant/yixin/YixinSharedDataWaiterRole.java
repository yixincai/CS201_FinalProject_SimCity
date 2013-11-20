package city.restaurant.yixin;

import restaurant.CookAgent.Order;

public class YixinSharedDataWaiterRole extends YixinWaiterRole{
	public YixinSharedDataWaiterRole(String name) {
		super(name);
	}

	@Override
	protected void processOrder(YixinWaiterRole.MyCustomer customer) {
		Do("Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToRevolvingStand();
		Do("Making a new order");
        Order data = new Order(this, customer.choice, customer.tableNumber, Order.OrderState.NotCooked);
        Do("Trying to put order on revolving stand");
        r.revolving_stand.insert(data);
	}
}

