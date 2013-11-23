package city.restaurant.yixin.test;

import city.PersonAgent;
import city.restaurant.yixin.*;
import city.restaurant.yixin.gui.*;
import junit.framework.*;

public class CookTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	YixinCookRole cook;
	YixinRestaurant restaurant;
	YixinWaiterRole waiter;
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		restaurant = new YixinRestaurant();
		PersonAgent p = new PersonAgent("Dummy");
		cook = new YixinCookRole(p, (YixinRestaurant)restaurant);
		YixinCookGui gui = new YixinCookGui(cook);
		cook.setGui(gui);
		waiter = new YixinSharedDataWaiterRole(p, restaurant, "Mike");
	}
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	
	public void testOneNormalMarketWithInvoiceScenario()
	{			
		assertEquals("Revolving stand should have 0 market bill after the sceduler has been run. It doesn't.",
				restaurant.revolving_stand.getSize(), 0);
		restaurant.revolving_stand.insert(new Order(waiter, "Steak", 1, Order.OrderState.NotCooked));
		
		assertEquals("Revolving stand should have 1 market bill after the sceduler has been run. It doesn't.",
				restaurant.revolving_stand.getSize(), 1);
		cook.releaseSemaphore();
		cook.releaseSemaphore();
		cook.releaseSemaphore();
		assertTrue("Cook's scheduler should have returned false (no actions to do), but didn't.", cook.pickAndExecuteAnAction());
		assertEquals("Revolving stand should have 0 market bill after the sceduler has been run. It doesn't.",
				restaurant.revolving_stand.getSize(), 0);

		
	}//end one normal market scenario	
	
}
