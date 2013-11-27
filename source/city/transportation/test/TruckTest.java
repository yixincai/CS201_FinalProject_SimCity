package city.transportation.test;

import junit.framework.TestCase;
import gui.WorldViewBuilding;
import city.market.Market;
import city.restaurant.Restaurant;
import city.restaurant.ryan.RyanRestaurant;
import city.restaurant.yixin.YixinRestaurant;
import city.transportation.TruckAgent;
import city.transportation.TruckAgent.truckState;

public class TruckTest extends TestCase{
	
	TruckAgent truck;
	
	WorldViewBuilding WV;
	Market market;
	
	WorldViewBuilding WV1;
	RyanRestaurant restaurant;
	YixinRestaurant restaurant1;
	
	public void setUp() throws Exception{
		super.setUp();
		
		WV = new WorldViewBuilding(1, 1, 10, 10);
		market = new Market("Market", WV);
		truck = new TruckAgent(market);
		market.setTruck(truck);
		
		restaurant = new RyanRestaurant();
		restaurant1 = new YixinRestaurant();

		
	}
	
	public void testZeroNormalCommuterScenario(){ //Basic Setting up
		System.out.println("Test");
		assertEquals("The trucks are the same", market.truck, truck);
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
		
		assertFalse("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertFalse("Out should be false", truck.pickAndExecuteAnAction());
		
		truck.msgDeliverToCook(null, restaurant);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.docking);
	}
	
	public void testOneNormalCommuterScenario(){ //1 Package delivered
		System.out.println("Test");
		assertEquals("The trucks are the same", market.truck, truck);
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
		
		assertFalse("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertFalse("Out should be false", truck.pickAndExecuteAnAction());
		assertEquals("Package list should be zero", truck.getPackageListSize(), 0);
		
		truck.msgDeliverToCook(null, restaurant);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 1);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.docking);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.atRestaurant);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
	}
	
	public void testTwoNormalCommuterScenario(){
		System.out.println("Test");
		assertEquals("The trucks are the same", market.truck, truck);
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
		
		assertFalse("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertFalse("Out should be false", truck.pickAndExecuteAnAction());
		assertEquals("Package list should be zero", truck.getPackageListSize(), 0);
		
		truck.msgDeliverToCook(null, restaurant);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 1);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.docking);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.atRestaurant);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
	}
	
	public void testThreeNormalCommuterScenario(){ //Two packages, one message sent in middle of delivery
		System.out.println("Test");
		assertEquals("The trucks are the same", market.truck, truck);
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
		
		assertFalse("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertFalse("Out should be false", truck.pickAndExecuteAnAction());
		assertEquals("Package list should be zero", truck.getPackageListSize(), 0);
		
		truck.msgDeliverToCook(null, restaurant);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 1);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.docking);
		
		truck.msgDeliverToCook(null, restaurant1);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 2);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.atRestaurant);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 1);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.docking);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.atRestaurant);
		assertEquals("Package list should be zero", truck.getPackageListSize(), 0);
		
		truck.msgAtDestination();
		assertTrue("The scheduler should return true", truck.pickAndExecuteAnAction());
		assertEquals("The truck state is at parking lot", truck.trState, truckState.parkingLot);
	}
}
