package city.restaurant.eric;

import gui.trace.AlertTag;
import city.interfaces.Person;

public class EricNormalWaiterRole extends EricWaiterRole {

	public EricNormalWaiterRole(Person person, EricRestaurant restaurant) {
		super(person, restaurant);
	}

	protected void actGiveOrderToCook(MyCustomer c)
	{
		print(AlertTag.ERIC_RESTAURANT,"Going to cook to give Customer " + c.agent.name() + "'s order");
		_gui.doTakeOrderToCook(c.order.choice);
		waitForGuiToReachDestination();
		
		print(AlertTag.ERIC_RESTAURANT,"Giving order of " + c.order.choice + " to cook.");
		
		_cook.msgHereIsOrder(this, c.order.choice, c.tableNumber);
		c.order.state = OrderState.COOKING;
		
		_gui.doGoIdle();
	}

}
