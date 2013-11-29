package agent;

import utilities.EventLog;
import utilities.LoggedEvent;

/**
 * This is the base class for all mocks.
 */
public abstract class Mock
{
	private String _name;
	public EventLog log = new EventLog();

	public Mock(String name) {
		this._name = name;
	}

	public String getName() {
		return _name;
	}

	public String toString() {
		return "[" + this.getClass().getName() + "] " + _name;
	}
    
    /**
     * Add the message to the log
     */
    protected void logThis(String msg)
    {
    	log.add(new LoggedEvent(msg));
    }
}
