package agent;

import city.PersonAgent;

public abstract class Role
{
	// ----------------------------- DATA ------------------------------------
	protected PersonAgent _person; // Not really sure if you even need this
	public boolean active = false;
	
	// ----------------------------- CONSTRUCTOR & ACCESSORS ------------------------------------
	public Role(PersonAgent person) { _person = person; }
	public void setPersonAgent(PersonAgent person) { _person = person; }
	
	
	
	// ----------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		if(active) _person.stateChanged();
	}
	protected abstract void finishCommandAndLeave();
	public abstract boolean pickAndExecuteAnAction();
}
