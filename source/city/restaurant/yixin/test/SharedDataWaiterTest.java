package city.restaurant.yixin.test;

import city.PersonAgent;
import city.restaurant.yixin.YixinCustomerRole;
import city.restaurant.yixin.YixinRestaurant;
import city.restaurant.yixin.YixinSharedDataWaiterRole;
import city.restaurant.yixin.YixinWaiterRole;
import city.restaurant.yixin.YixinWaiterRole.MyCustomer;
import city.restaurant.yixin.gui.YixinWaiterGui;
import junit.framework.*;

public class SharedDataWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	YixinSharedDataWaiterRole waiter;
	YixinRestaurant restaurant;
	YixinCustomerRole customer;
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		restaurant = new YixinRestaurant();
		PersonAgent p = new PersonAgent("Dummy");
		waiter = new YixinSharedDataWaiterRole(p, (YixinRestaurant)restaurant, "");
		YixinWaiterGui gui = new YixinWaiterGui(waiter, 0);
		waiter.setGui(gui);
		customer = new YixinCustomerRole(p, null, "Dummy2", 0);
	}

	public void testOneNormalMarketWithInvoiceScenario()
	{			
		//check preconditions
		waiter.customers.add(new MyCustomer(customer, 1, YixinWaiterRole.MyCustomer.CustomerState.orderGiven, 1));
		waiter.releaseSemaphore();
		assertTrue("Waiter's scheduler should have returned false (no actions to do), but didn't.", waiter.pickAndExecuteAnAction());
		assertEquals("Revolving stand should have 1 market bill after the sceduler has been run. It doesn't.", restaurant.revolving_stand.getSize(), 1);

	}//end one normal market scenario
}
