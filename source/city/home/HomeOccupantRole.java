package city.home;

import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.home.gui.HomeOccupantGui;
import agent.Role;

public abstract class HomeOccupantRole extends Role
{
	//private List<Meal> _meals;



	private enum Command { NONE, WATCH_TV, COOK_AND_EAT_FOOD, GO_TO_BED, WAKE_UP, LEAVE }
	private Command _command = Command.NONE;
	
	private enum Event { NONE, ALARM_CLOCK_RANG, GOT_HOME, }
	private Event _event = Event.NONE;
	
	public enum State { AWAY, IDLE, COOKING, SLEEPING, LEAVING }
	private State _state = State.AWAY;
	
	private int _mealCount = 2; // Starting people out with 2 meals

	protected Home _home = null;
	
	protected HomeOccupantGui _gui;
	
	private Semaphore _reachedDestination = new Semaphore(0, true);
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	// ------------- SETUP ------------
	public HomeOccupantRole(PersonAgent person, Home home)
	{
		super(person);
		setHome(home);
	}
	public boolean haveHome() { return _home != null; }
	public State state() { return _state; }
	public boolean sleeping() { return _state == State.SLEEPING; }
	public boolean cooking() { return _state == State.COOKING; }
	public boolean haveFood() { return _mealCount > 0; } //TODO implement _mealCount;
	public HomeOccupantGui gui() { return _gui; }
	public Place place()
	{
		if(_home != null)
		{
			return _home.place();
		}
		else
		{
			return null;
		}
	}
	public void setHome(Home home) { _home = home; }
	
	
	
	// --------------------------------- COMMANDS & MESSAGES -----------------------------------
	// note: commands are only from PersonAgent
	public void cmdGotHome()
	{
		_event = Event.GOT_HOME;
		stateChanged();
	}
	public void cmdWatchTv()
	{
		_command = Command.WATCH_TV;
		stateChanged();
	}
	public void cmdCookAndEatFood()
	{
		_command = Command.COOK_AND_EAT_FOOD;
		stateChanged();
	}
	public void cmdGoToBed()
	{
		_command = Command.GO_TO_BED;
		stateChanged();
	}
	public void cmdFinishAndLeave()
	{
		_command = Command.LEAVE;
		stateChanged();
	}
	public void msgReachedDestination() // from HomeOccupantGui
	{
		_reachedDestination.release();
		// no stateChanged() because this message just releases the semaphore
	}
	public void msgAlarmClockRang() // from the timer that's set in actGoToBed()
	{
		_event = Event.ALARM_CLOCK_RANG;
		stateChanged();
	}
	
	
	
	// ---------------------------------- SCHEDULER ------------------------------------
	public boolean pickAndExecuteAnAction()
	{
		if(_state == State.AWAY)
		{
			if(_event == Event.GOT_HOME)
			{
				actGotHome();
				return true;
			}
		}
		else if(_state == State.IDLE)
		{
			if(_command == Command.COOK_AND_EAT_FOOD)
			{
				actStartCooking();
				return true;
			}
			else if(_command == Command.GO_TO_BED)
			{
				actGoToBed();
				return true;
			}
			else if(_command == Command.LEAVE)
			{
				actLeave();
				return true;
			}
			else if(_command == Command.WATCH_TV)
			{
				actWatchTv();
				return true;
			}
		}
		else if(_state == State.COOKING)
		{
			// note: don't check for commands except possibly LEAVE
			//TODO finish cooking scenario
		}
		else if(_state == State.SLEEPING)
		{
			if(_event == Event.ALARM_CLOCK_RANG)
			{
				actWakeUp();
				return true;
			}
			else if(_command == Command.LEAVE)
			{
				actWakeUp();
				actLeave();
				return true;
			}
		}
		return false;
	}
	
	
	
	// ---------------------------------- ACTIONS ------------------------------------
	private void actGotHome()
	{
		// note: the whole getting-home process does not change the command, so you will be able to set a command then get home.
		print("Just got home.");
		_state = State.IDLE;
		_gui.doGotHome();
	}
	private void actStartCooking()
	{
		print("Starting to cook.");
		_state = State.COOKING;
		
		_gui.doGoToKitchen();
		waitForGuiToReachDestination();
		//_gui.doCookAndEatFood(); //TODO figure out this implementation
	}
	private void actWatchTv()
	{
		print("Watching TV");
		_state = State.IDLE;
		
		_gui.doWatchTv();
	}
	private void actGoToBed()
	{
		print("Going to bed.");
		_state = State.SLEEPING;
		_gui.doGoToBed();
		waitForGuiToReachDestination();
		//TODO set a timer to call msgAlarmClockRang
	}
	private void actWakeUp()
	{
		print("Waking up.");
		_state = State.IDLE;
		_gui.doWakeUp();
	}
	private void actLeave()
	{
		print("Leaving my home");
		_command = Command.NONE;
		_event = Event.NONE;
		_state = State.LEAVING;
		_gui.doLeaveHome();
		waitForGuiToReachDestination();
		print("Finished leaving home");
		_state = State.AWAY;
		active = false;
	}
	
	
	
	// ------------------------------------ UTILITIES -----------------------------------
	private void waitForGuiToReachDestination()
	{
		// for DEBUG: print("Waiting for gui to reach destination.");
		try { _reachedDestination.acquire(); }
		catch (InterruptedException e) { e.printStackTrace(); }
	}
}
