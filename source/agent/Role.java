package agent;

import utilities.EventLog;
import utilities.StringUtil;
import city.Place;
import city.interfaces.Person;

public abstract class Role
{
	// --------------------------------------- DATA ------------------------------------
	protected Person _person;
	public boolean active = false;
	public EventLog log = new EventLog(); // unit testing
	
	// ----------------------------- CONSTRUCTOR & PROPERTIES ------------------------------------
	public Role(Person person) { _person = person; }
	public void setPerson(Person person) { _person = person; }
	public abstract Place place();
	public final String typeToString() { return StringUtil.shortName(getClass()); }
	public final String toString() { return _person.name() + " as " + typeToString();}
	
	// ------------------------------------- METHODS ------------------------------------
	protected void stateChanged()
	{
		if(_person != null) _person.stateChanged(); // this checking for _person != null is necessary in cases like when the cook sends a message to a market whose cashier role isn't yet filled 
		// note: it should be okay to perform extra scheduler calls in the person (i.e. no need to check if active == true) since they all take place in one thread anyway
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
		log.add(msg);
		StringBuffer sb = new StringBuffer();
		sb.append(_person.name());
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
	protected void logThis(String msg)
	{
		log.add(msg);
	}
}
