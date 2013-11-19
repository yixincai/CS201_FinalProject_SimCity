package city.transportation;

import agent.Role;
import city.Place;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public Place destination;
	public Place currentPlace;

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
