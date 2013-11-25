package city.home;

import city.PersonAgent;
import city.Place;

public class HouseOwnerRole extends HomeBuyingRole
{
	// ************************************************************************
	// ****************** NOTE: for v1, not using this class at all **********
	// ********************************************************************
	
	// ---------------------------------- DATA ------------------------------------
	private House _house;
	
	// ------------------------ CONSTRUCTOR & PROPERTIES --------------------------
	public HouseOwnerRole(PersonAgent person) { super(person); }
	public Place place() { return _house; }
	
	
	
	// -------------------------------- SCHEDULER ---------------------------------

	public boolean pickAndExecuteAnAction() {
		if(_house == null)
		{
			// buy a house
		}
		else
		{
			active = false;
			return true;
		}
		return false;
	}

	public void cmdFinishAndLeave() { } // do nothing
}
