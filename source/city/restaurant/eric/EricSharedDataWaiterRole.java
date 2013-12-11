package city.restaurant.eric;

import gui.trace.AlertTag;
import city.interfaces.Person;

public class EricSharedDataWaiterRole extends EricWaiterRole {

	public EricSharedDataWaiterRole(Person person, EricRestaurant restaurant) {
		super(person, restaurant);
	}

	protected void actGiveOrderToCook(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to revolving stand to place Customer " + c.agent.name() + "'s order");
		_gui.doTakeOrderToRevolvingStand(c.order.choice);
		waitForGuiToReachDestination();
		
		print(AlertTag.ERIC_RESTAURANT,"Placing order of " + c.order.choice + " on revolving stand.");
		_restaurant.revolvingStand().addOrder(this, c.order.choice, c.tableNumber);
		
		c.order.state = OrderState.COOKING;
		
		_gui.doGoIdle();
	}
	
}
