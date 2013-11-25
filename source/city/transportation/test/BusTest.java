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
		
	}
	public void testZeroBusCommuterScenario(){
		//Nothing should happen
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
	
	public void testOneNormalBusScenario(){
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
	public void testTwoNormalBusScenario(){
		assertEquals("Bus state is not moving", bus.bState, BusState.notmoving);
		bus.releaseSem();
		
		assertEquals("Expected number is 0", bus.getExpectedPeople(), 0);
		assertEquals("Number of people is 0", bus.getNumPeople(), 0);
		assertTrue("Scheduler returns true", bus.pickAndExecuteAnAction());
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
		
		busStop.addCommuterRole(mockCommuter);
		bus.msgAtDestination(busStop);
		
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
		
		bus.msgGettingOnBoard(mockCommuter, null, 0);
		
		assertEquals("Bus state is not moving", bus.bState, BusState.moving);
	}
}
