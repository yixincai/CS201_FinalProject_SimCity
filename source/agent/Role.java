package agent;

import utilities.EventLog;
import utilities.StringUtil;
import city.PersonAgent;
import city.Place;

public abstract class Role
{
	// ----------------------------- DATA ------------------------------------
	protected PersonAgent _person;
	public boolean active = false;
	public EventLog log = new EventLog();
	
	// ----------------------------- CONSTRUCTOR & PROPERTIES ------------------------------------
	public Role(PersonAgent person) { _person = person; }
	public void setPersonAgent(PersonAgent person) { _person = person; }
	public abstract Place place();
	public String toString() { return StringUtil.shortName(getClass()); }
	
	// ----------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		if(_person != null) _person.stateChanged(); // this checking for _person != null is necessary in cases like when the cook sends a message to a market whose cashier role isn't yet filled 
		// note: it should be okay to perform extra scheduler calls in the person (i.e. if active == false) (since they all take place in one thread anyway)
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
