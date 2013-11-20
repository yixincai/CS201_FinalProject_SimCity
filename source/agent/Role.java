package agent;

import city.PersonAgent;
import city.Place;

public abstract class Role
{
	// ----------------------------- DATA ------------------------------------
	protected PersonAgent _person; // Not really sure if you even need this
	public boolean active = false;
	private Place _place;
	
	// ----------------------------- CONSTRUCTOR & ACCESSORS ------------------------------------
	public Role(PersonAgent person) { _person = person; }
	public void setPersonAgent(PersonAgent person) { _person = person; }
	public Place place() { return _place; }
	
	
	
	// ----------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		if(active) _person.stateChanged();
	}
	public abstract void cmdFinishAndLeave(); // from PersonAgent
	public abstract boolean pickAndExecuteAnAction();
}
