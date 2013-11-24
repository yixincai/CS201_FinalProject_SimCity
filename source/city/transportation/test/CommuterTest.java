package city.transportation.test;

import city.PersonAgent;
import city.market.Market;
import city.restaurant.Restaurant;
import city.restaurant.yixin.YixinRestaurant;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;
import city.transportation.CommuterRole.TravelState;
import city.transportation.mock.MockBus;
import junit.framework.TestCase;

public class CommuterTest extends TestCase{
	
	PersonAgent person;
	CommuterRole commuter;
	
	MockBus mockBus;
	TravelState tState;
	
	Market market;
	Market market1;
	
	BusStopObject busStop;
	BusStopObject busStop1;
	CarObject car;
	
	public void setUp() throws Exception{
		super.setUp();
		
		person = new PersonAgent("Person 1"); 
		person.changeMoney(100);
		commuter = new CommuterRole(person, null);
		
		mockBus = new MockBus("MockBus");
		
		market = new Market("Market");
		market1 = new Market("Market 1");
		
		busStop = new BusStopObject("bus stop", null);
		busStop1 = new BusStopObject("bus stop 1", null);
		car = new CarObject();
	}
	
	//Remember to use event logs
	
	public void testZeroNormalCommuterScenario(){
		//Nothing should happen
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		assertFalse("Scheduler returns false", commuter.pickAndExecuteAnAction());
		
		//Send Message
		commuter.msgGoToDestination(market);
		
		//Check if it works
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		
	}
	
	public void testOneNormalBusCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		
		//Set Up Message to go to Bus
		commuter.msgGoToDestination(market);
		commuter.chooseTransportation(1);
		
		//Check if it received correctly (Choosing to go to Bus Stop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choseBus);
		
		//No message needed
		//Check if it received correctly (Going to Bus Stop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choseBus);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.goingToBusStop);
		//Nothing happens so should be false
		assertFalse("Scheduler returns false", commuter.pickAndExecuteAnAction());
		
		//Message from GUI that person is at busstop
		commuter.msgAtBusStop(busStop); //Sent by gui
		commuter.setBusStop(busStop); //IMPORTANT - Recheck getting the busstop methods
		
		//Check if it received correctly (At busstop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.atBusStop);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); //CODE COMMENTED OUT NEED TO REIMPLEMENT GET BUSSTOP METHOD
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.waitingAtBusStop);
		//Nothing happens so should be false
		assertFalse("Scheduler returns false", commuter.pickAndExecuteAnAction());
		
		//Message from bus that it's at bus stop
		commuter.msgGetOnBus(5, mockBus);
		
		//Check if it received correctly (Bus is here)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.busIsHere);
		assertEquals("Fare should be 5", commuter._fare, 5);
		assertEquals("Cash should be 100", commuter._person._money, 100.0);
		assertEquals("Bus should be Mock bus", commuter._bus, mockBus);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); 
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.ridingBus);
		assertEquals("Cash should be 100", commuter._person._money, 95.0);
		
		//Message
		commuter.msgGetOffBus(busStop1);
		
		//Check if it received correctly (Bus is at destination)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.busIsAtDestination);
		assertEquals("Current place is busStop1", commuter._currentPlace, busStop1);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); //Commented out code relating to gui
		assertEquals("Bus should be null", commuter._bus, null);
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.walking);
		
		//message
		commuter.msgAtDestination(market1); //Check later on with real places instead of null
		
		//Check if it received correctly (Bus is at destination)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.atDestination);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); //Commented out code relating to gui
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.done);
	}
	
	public void testTwoNormalWalkingCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		
		//Set Up Message to go to Bus
		commuter.msgGoToDestination(market);
		commuter.chooseTransportation(0);
		
		//Check if it received correctly (Choosing to go to Bus Stop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choseWalking);
		
		//message
		commuter.msgAtDestination(market1); //Check later on with real places instead of null
		
		//Check if it received correctly (Bus is at destination)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.atDestination);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); //Commented out code relating to gui
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.done);
	}
	
	public void testThreeNormalCarCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		assertEquals("Car should be null", commuter._car, null);
		
		//Set Up Message to go to Bus
		commuter.msgGoToDestination(market);
		commuter.setCar(car);
		commuter.chooseTransportation(2);
		
		//Check if it received correctly (Choosing to go to Bus Stop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		assertEquals("Car should be null", commuter._car, car);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choseCar);
		
		//message
		commuter.msgAtDestination(market1); //Check later on with real places instead of null
		
		//Check if it received correctly (Bus is at destination)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.atDestination);
		
		//Check if Scheduler executed correctly
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); //Commented out code relating to gui
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.done);
		
	}
}
