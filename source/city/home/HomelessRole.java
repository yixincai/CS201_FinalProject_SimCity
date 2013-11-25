package city.home;

import city.PersonAgent;

public class HomelessRole extends HomeOccupantRole {

	public HomelessRole(PersonAgent person) {
		super(person, null); // passing null for home because HomelessRoles have no home.
	}

}
