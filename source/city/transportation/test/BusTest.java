package city.transportation.test;

import junit.framework.TestCase;
import gui.WorldViewBuilding;
import city.Directory;
import city.PersonAgent;
import city.market.Market;
import city.transportation.BusAgent;
import city.transportation.BusAgent.BusState;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;
import city.transportation.CommuterRole.TravelState;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.CommuterGui;
import city.transportation.interfaces.Commuter;
import city.transportation.mock.MockBus;
import city.transportation.mock.MockCommuter;

public class BusTest extends TestCase {
	
	BusAgent bus;
	BusAgentGui gui;
	BusState bState;
	
	MockCommuter mockCommuter;
	MockCommuter mockCommuter1;
	MockCommuter mockCommuter2;
	
	BusStopObject busStop;
	BusStopObject busStop1;

	WorldViewBuilding WV;
	WorldViewBuilding WV1;
	
	public void setUp() throws Exception{
		super.setUp();
		
		WV = new WorldViewBuilding(1, 1, 10, 10);
		WV1 = new WorldViewBuilding(100, 100, 10, 10);
		
		busStop = new BusStopObject("bus stop", WV);
		busStop1 = new BusStopObject("bus stop 1", WV1);
		
		Directory.addPlace(busStop);
		Directory.addPlace(busStop1);
		
		bus = new BusAgent("Real Bus");
		gui = new BusAgentGui(bus, busStop);
		bus.setBusAgentGui(gui);
		
		mockCommuter = new MockCommuter("mock commuter");
		mockCommuter1 = new MockCommuter("mock commuter1");
		mockCommuter2 = new MockCommuter("mock commuter2");
		
	}
	public void testZeroBusCommuterScenario(){ //Setting it up
		//Nothing should happen
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
	
	public void testOneNormalBusScenario(){//Going to bus stop destinations
		//Nothing should happen
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		bus.msgAtDestination(busStop);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);

		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		bus.msgAtDestination(busStop1);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);

		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
	public void testTwoNormalBusScenario(){ //People getting on board (1 person)
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop.addCommuterRole(mockCommuter);
		bus.msgAtDestination(busStop);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 1);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);

		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertFalse("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		bus.msgGettingOnBoard(mockCommuter, busStop1, 0);
		
		assertEquals("busstop list should be size 1", busStop.getList().size(), 0);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		bus.msgAtDestination(busStop1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		
		bus.msgGotOff(mockCommuter);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
	public void testThreeNormalBusScenario(){ //Multiple people boarding
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop.addCommuterRole(mockCommuter);
		busStop.addCommuterRole(mockCommuter1);
		bus.msgAtDestination(busStop);
		
		assertEquals("busstop list should be size 1", busStop.getList().size(), 2);
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);

		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertFalse("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		bus.msgGettingOnBoard(mockCommuter, busStop1, 0);
		bus.msgGettingOnBoard(mockCommuter1, busStop, 0);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 0);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		bus.msgAtDestination(busStop1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		bus.msgGotOff(mockCommuter);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		bus.msgAtDestination(busStop);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		
		bus.msgGotOff(mockCommuter1);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
	public void testFourNormalBusScenario(){//Multiple boarding customers at different stops
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop.addCommuterRole(mockCommuter);
		busStop.addCommuterRole(mockCommuter1);
		bus.msgAtDestination(busStop);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 2);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);

		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertFalse("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		bus.msgGettingOnBoard(mockCommuter, busStop1, 0);
		bus.msgGettingOnBoard(mockCommuter1, busStop, 0);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 0);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop1.addCommuterRole(mockCommuter2);
		
		assertEquals("BusStop1 has one commuter", busStop1.getList().size(), 1);
		
		bus.msgAtDestination(busStop1);
		
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		bus.msgGotOff(mockCommuter);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("busstop list should be size 1", busStop1.getList(), bus.currentBusStopList);
		
		assertEquals("Bus stop is 1", bus.getCurrentBusStop(), busStop1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 1);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		
		bus.msgGettingOnBoard(mockCommuter2, busStop, 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		
		bus.msgAtDestination(busStop);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		bus.msgGotOff(mockCommuter1);
		bus.msgGotOff(mockCommuter2);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 0);
	}
	public void testFiveNormalBusScenario(){ //Check if capacity works
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.setCapacity(2);
		bus.releaseSem();
		
		assertEquals("Capacity is at 2", bus.getCapacity(), 2);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop.addCommuterRole(mockCommuter);
		busStop.addCommuterRole(mockCommuter1);
		bus.msgAtDestination(busStop);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 2);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.atDestination);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertFalse("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		bus.msgGettingOnBoard(mockCommuter, busStop, 0);
		bus.msgGettingOnBoard(mockCommuter1, busStop, 0);
		assertEquals("busstop list should be size 1", busStop.getList().size(), 0);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop1.addCommuterRole(mockCommuter2);
		
		assertEquals("BusStop1 has one commuter", busStop1.getList().size(), 1);
		
		bus.msgAtDestination(busStop1);
		
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 1);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		assertEquals("busstop list should be size 1", busStop1.getList(), bus.currentBusStopList);
		
		assertEquals("Bus stop is 1", bus.getCurrentBusStop(), busStop1);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 2);
		
		bus.msgAtDestination(busStop);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.droppingoff);
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 2);
		
		bus.msgGotOff(mockCommuter1);
		bus.msgGotOff(mockCommuter2);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.pickingup);
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		assertEquals("busstop list should be size 1", busStop1.getList().size(), 1);
	}
	
	
}
