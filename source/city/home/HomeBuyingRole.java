package city.home;

import city.PersonAgent;
import city.Place;
import agent.Role;

public abstract class HomeBuyingRole extends Role
{
	// ---------------------------------- DATA ------------------------------------
	protected LandlordRole _landlord;
	
	
	
	// ------------------------ CONSTRUCTOR & PROPERTIES --------------------------
	public HomeBuyingRole(PersonAgent person, Place place) { super(person, place); }
	public void setLandlord(LandlordRole landlord) { _landlord = landlord; }
}
