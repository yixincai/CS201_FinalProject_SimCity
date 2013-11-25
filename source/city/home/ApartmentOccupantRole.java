package city.home;

import city.PersonAgent;

public class ApartmentOccupantRole extends HomeOccupantRole {
	
	// note: This class exists basically only so that we can use instanceof stuff in PersonAgent.

	public ApartmentOccupantRole(PersonAgent person, Home home) { super(person, home); }

}
