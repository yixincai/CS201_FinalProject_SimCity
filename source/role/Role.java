package role;

import agent.PersonAgent;

public abstract class Role
{
	// ----------------------------- DATA ------------------------------------
	private PersonAgent _person;
	public boolean active = false;
	
	// ----------------------------- ACCESSORS ------------------------------------
	public void setPersonAgent(PersonAgent person) { _person = person; }
	
	
	
	// ----------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		if(active) _person.stateChanged();
	}
	public abstract boolean pickAndExecuteAnAction();
}
