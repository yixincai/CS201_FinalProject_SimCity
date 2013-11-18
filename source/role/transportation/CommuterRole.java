package role.transportation;

import city.Place;
import role.Role;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public Place destination;

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
