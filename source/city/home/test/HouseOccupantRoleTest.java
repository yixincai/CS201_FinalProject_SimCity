package city.home.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import city.home.HomeOccupantRole;
import city.home.House;
import city.home.HouseOccupantRole;
import city.home.test.mock.PersonMock;

public class HouseOccupantRoleTest extends TestCase {
	
	private HouseOccupantRole uut;
	private House house;
	private PersonMock personMock;

	@Before
	public void setUp() throws Exception {
		personMock = new PersonMock();
		house = new House("TheHouse");
		uut = new HouseOccupantRole(personMock, house);
		
		assertEquals("uut's log is not empty",
				0,
				uut.log.size());
		assertEquals("uut has incorrect command",
				HomeOccupantRole.Command.NONE,
				uut.command());
		assertEquals("uut has incorrect event",
				HomeOccupantRole.Event.GOT_HOME,
				uut.event());
		assertEquals("uut has incorrect state",
				HomeOccupantRole.State.AWAY,
				uut.state());
	}

	@Test
	public void testGotHome() {
		
		// Because command should stay the same when you call actGotHome
		HomeOccupantRole.Command c = uut.command();
		
		assertTrue("uut's scheduler should have called an action",
				uut.pickAndExecuteAnAction());
		
		assertTrue("uut should have called actGotHome",
				uut.log.containsString("Just got home."));

		assertEquals("uut has incorrect command",
				c,
				uut.command());
		assertEquals("uut has incorrect event",
				HomeOccupantRole.Event.NONE,
				uut.event());
		assertEquals("uut has incorrect state",
				HomeOccupantRole.State.IDLE,
				uut.state());
	}
	
	@Test
	public void testCookAndEatFood() {
		
		uut.cmdCookAndEatFood();
		assertEquals("uut has incorrect command",
				HomeOccupantRole.Command.COOK_AND_EAT_FOOD,
				uut.command());
		
		// Because command should stay the same when you call actGotHome
		HomeOccupantRole.Command c = uut.command();
		
		assertTrue("uut's scheduler should have called an action",
				uut.pickAndExecuteAnAction());
		
		assertTrue("uut should have called actGotHome",
				uut.log.containsString("Just got home."));

		assertEquals("uut has incorrect command",
				c,
				uut.command());
		assertEquals("uut has incorrect event",
				HomeOccupantRole.Event.NONE,
				uut.event());
		assertEquals("uut has incorrect state",
				HomeOccupantRole.State.IDLE,
				uut.state());
		assertEquals("uut has incorrect command",
				HomeOccupantRole.Command.COOK_AND_EAT_FOOD,
				uut.command());
		
		
		
		// Have to call this so that the scheduler thread doesn't freeze on waitForGuiToReachDestination()
		uut.msgReachedDestination();
		
		//Rest of the test is commented out because the gui needs a semaphore release action called for its 
		//try.acquire to continue. As it is set up right now, that action can only be released by the animation
		//of the gui. Trying to call the gui update function continually to achieve this animation would be 
		//unreasonable as it will be a lot of calls (the destination is a couple of hundred pixels away)
		
//		assertTrue("uut's scheduler should have called an action",
//				uut.pickAndExecuteAnAction());
		
		

//		assertTrue("uut should have called actCookAndEat",
//				uut.log.containsString("Starting to cook."));
//		assertTrue("uut should have finished cooking",
//				uut.log.containsString("Finished cooking and eating"));
//
//		assertEquals("uut has incorrect command",
//				HomeOccupantRole.Command.NONE,
//				uut.command());
//		assertEquals("uut has incorrect event",
//				HomeOccupantRole.Event.NONE,
//				uut.event());
//		assertEquals("uut has incorrect state",
//				HomeOccupantRole.State.IDLE,
//				uut.state());
	}
	
	@Test
	public void testGoToBed() {
		
		uut.cmdGoToBed();
		assertEquals("uut has incorrect command",
				HomeOccupantRole.Command.GO_TO_BED,
				uut.command());
		
		// Because command should stay the same when you call actGotHome
		HomeOccupantRole.Command c = uut.command();
		
		assertTrue("uut's scheduler should have called an action",
				uut.pickAndExecuteAnAction());
		
		assertTrue("uut should have called actGotHome",
				uut.log.containsString("Just got home."));

		assertEquals("uut has incorrect command",
				c,
				uut.command());
		assertEquals("uut has incorrect event",
				HomeOccupantRole.Event.NONE,
				uut.event());
		assertEquals("uut has incorrect state",
				HomeOccupantRole.State.IDLE,
				uut.state());
		
		
		
		// Have to call this so that the scheduler thread doesn't freeze on waitForGuiToReachDestination()
		uut.msgReachedDestination();
		
		assertTrue("uut's scheduler should have called an action",
				uut.pickAndExecuteAnAction());

		assertTrue("uut should have called actGoToBed",
				uut.log.containsString("Going to bed."));

		assertEquals("uut has incorrect command",
				HomeOccupantRole.Command.NONE,
				uut.command());
		assertEquals("uut has incorrect event",
				HomeOccupantRole.Event.NONE,
				uut.event());
		assertEquals("uut has incorrect state",
				HomeOccupantRole.State.SLEEPING,
				uut.state());
	}

}
