package agent;

import utilities.EventLog;
import utilities.LoggedEvent;

/**
 * This is the base class for all mocks.
 */
public abstract class Mock
{
	private String name;
	public EventLog log = new EventLog();

	public Mock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}
    
    /**
     * Add the message to the log
     */
    protected void logThis(String msg)
    {
    	log.add(new LoggedEvent(msg));
    }
}
