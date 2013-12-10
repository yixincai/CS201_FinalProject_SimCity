package city.restaurant.yixin;

import gui.trace.AlertTag;
import city.PersonAgent;
import city.Place;

public class YixinNormalWaiterRole extends YixinWaiterRole{

	public YixinNormalWaiterRole(PersonAgent person, YixinRestaurant restaurant, String name) {
		super(person, restaurant, name);
	}

	@Override
	protected void processOrder(MyCustomer customer) {
		print(AlertTag.YIXIN_RESTAURANT, "Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToCook();
		cook.msgHereIsTheOrder(this, customer.choice, customer.tableNumber);
	}

	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}
}

