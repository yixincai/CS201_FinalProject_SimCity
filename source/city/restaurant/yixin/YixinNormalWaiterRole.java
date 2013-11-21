package city.restaurant.yixin;

import city.PersonAgent;

public class YixinNormalWaiterRole extends YixinWaiterRole{

	public YixinNormalWaiterRole(PersonAgent person, YixinRestaurant restaurant, String name) {
		super(person, restaurant, name);
	}

	@Override
	protected void processOrder(MyCustomer customer) {
		print("Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToCook();
		cook.msgHereIsTheOrder(this, customer.choice, customer.tableNumber);
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
}

