package city.home;

import city.PersonAgent;
import agent.Role;

public abstract class HomeBuyingRole extends Role
{
	// ---------------------------------- DATA ------------------------------------
	protected LandlordRole _landlord;
	
	// ------------------------ CONSTRUCTOR & PROPERTIES --------------------------
	public HomeBuyingRole(PersonAgent person) { super(person); }
	public void setLandlord(LandlordRole landlord) { _landlord = landlord; }
}
