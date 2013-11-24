package agent;

import utilities.StringUtil;
import city.PersonAgent;
import city.Place;

public abstract class Role
{
	// ----------------------------- DATA ------------------------------------
	protected PersonAgent _person; // Not really sure if you even need this
	public boolean active = false;
	
	// ----------------------------- CONSTRUCTOR & ACCESSORS ------------------------------------
	public Role(PersonAgent person) { _person = person; }
	public void setPersonAgent(PersonAgent person) { _person = person; }
	public abstract Place place();
	
	// ----------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		_person.stateChanged();
		//if(active) _person.stateChanged();
	}
	
	// --------- ABSTRACT ---------
	public abstract boolean pickAndExecuteAnAction();
	public abstract void cmdFinishAndLeave(); // from PersonAgent
	
	// --------- UTILITIES ---------
    /**
     * Print message with this agent's name and exception stack trace
     */
    protected void print(String msg, Throwable e)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(_person.getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) sb.append(StringUtil.stackTraceString(e));
        System.out.print(sb.toString());
    }
    protected void print(String msg)
    {
    	print(msg, null);
    }
}
