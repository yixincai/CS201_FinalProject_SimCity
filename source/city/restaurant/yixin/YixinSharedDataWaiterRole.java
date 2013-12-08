package city.restaurant.yixin;

import gui.trace.AlertTag;
import city.PersonAgent;
import city.Place;

public class YixinSharedDataWaiterRole extends YixinWaiterRole{
	public YixinSharedDataWaiterRole(PersonAgent person, YixinRestaurant r, String name) {
		super(person, r, name);
	}

	@Override
	protected void processOrder(YixinWaiterRole.MyCustomer customer) {
		print(AlertTag.YIXIN_RESTAURANT,"Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToRevolvingStand();
		print(AlertTag.YIXIN_RESTAURANT,"Making a new order");
        Order data = new Order(this, customer.choice, customer.tableNumber, Order.OrderState.NotCooked);
        print(AlertTag.YIXIN_RESTAURANT,"Trying to put order on revolving stand");
        restaurant.revolving_stand.insert(data);
	}

	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}
}

