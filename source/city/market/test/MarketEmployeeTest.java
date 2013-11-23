package city.market.test;

import java.util.ArrayList;
import java.util.List;

import city.PersonAgent;
import city.market.*;
import city.market.test.mock.*;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import junit.framework.TestCase;

public class MarketEmployeeTest  extends TestCase {
	Market market;
	PersonAgent p;
	MockMarketCashier cashier;
	MarketEmployeeRole employee;
	
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("Mike");
		market = new Market();
		employee = market.MarketEmployee;
		employee.setPersonAgent(p);
	}
	
	public void testOneNormalCustomerScenario(){


	}
	
	public void testOneNormalRestaurantScenario(){

	}
}
