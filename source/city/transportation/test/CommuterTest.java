package city.transportation.test;

import city.PersonAgent;
import city.transportation.CommuterRole;
import city.transportation.CommuterRole.TravelState;
import city.transportation.mock.MockBus;
import junit.framework.TestCase;

public class CommuterTest extends TestCase{
	
	PersonAgent person;
	CommuterRole commuter;
	MockBus mockBus;
	TravelState tState;
	
	public void setUp() throws Exception{
		super.setUp();
		person = new PersonAgent("Person 1");
		person.changeMoney(100);
		commuter = new CommuterRole(person, null);
		
		mockBus = new MockBus("MockBus");
		mockBus.setFare(1);
	}
	
	public void testZeroNormalCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		
		commuter.msgGoToDestination(null);
		
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		
	}
	
	public void testOneNormalBusCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		
		commuter.msgGoToDestination(null);
		
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
	}
}
