package city.transportation;

import agent.Role;
import city.PersonAgent;
import city.Place;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	
	public CommuterRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}

	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	private Place _destination;
	public Place destination() { return _destination; }
	public void setDestination(Place place) { _destination = place; }
	
	private Place _currentPlace;
	public Place currentPlace() { return _currentPlace; }

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}

}
