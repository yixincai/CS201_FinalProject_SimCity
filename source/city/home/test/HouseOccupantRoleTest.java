package city.home.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import city.home.HomeOccupantRole;
import city.home.House;
import city.home.HouseOccupantRole;
import city.home.test.mock.PersonMock;

public class HouseOccupantRoleTest {
	
	private HouseOccupantRole uut;
	private House house;
	private PersonMock personMock;

	@Before
	public void setUp() throws Exception {
		personMock = new PersonMock();
		house = new House("TheHouse", null, null);
		uut = house.tryGenerateHomeOccupantRole(personMock);
	}

	@Test
	public void testGotHome() {
		
	}

}
