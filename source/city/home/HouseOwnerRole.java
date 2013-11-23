package city.home;

import city.PersonAgent;

public class HouseOwnerRole extends HomeBuyingRole
{
	// ---------------------------------- DATA ------------------------------------
	private House _house;
	
	// ------------------------ CONSTRUCTOR & PROPERTIES --------------------------
	public HouseOwnerRole(PersonAgent person) { super(person); }

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
