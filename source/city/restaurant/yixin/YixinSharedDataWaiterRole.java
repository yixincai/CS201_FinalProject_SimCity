package city.restaurant.yixin;

import city.PersonAgent;
import city.Place;

public class YixinSharedDataWaiterRole extends YixinWaiterRole{
	public YixinSharedDataWaiterRole(PersonAgent person, YixinRestaurant r, String name) {
		super(person, r, name);
	}

	@Override
	protected void processOrder(YixinWaiterRole.MyCustomer customer) {
		print("Process order");
		customer.state = MyCustomer.CustomerState.none;
		DoGoToRevolvingStand();
		print("Making a new order");
        Order data = new Order(this, customer.choice, customer.tableNumber, Order.OrderState.NotCooked);
        print("Trying to put order on revolving stand");
        restaurant.revolving_stand.insert(data);
	}

	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}
	public Place place() {
		// TODO Auto-generated method stub
		return restaurant;
	}
}

