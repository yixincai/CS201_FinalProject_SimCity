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
	
	private enum Event { NONE, ALARM_CLOCK_RANG, }
	private Event _event = Event.NONE;
	
	public enum State { AWAY, IDLE, COOKING, SLEEPING, LEAVING }
	private State _state = State.IDLE; //TODO fully implement these states
	
	private int _mealCount = 2; // Starting people out with 2 meals

	protected Home _home = null;
	
	protected HomeOccupantGui _gui;
	
	private Semaphore _reachedDestination = new Semaphore(0, true);
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	// ------------- SETUP ------------
	public HomeOccupantRole(PersonAgent person, Home home) { super(person); setHome(home); }
	public boolean haveHome() { return _home != null; }
	public State state() { return _state; }
	public boolean sleeping() { return _state == State.SLEEPING; }
	public boolean cooking() { return _state == State.COOKING; }
	public boolean haveFood() { return _mealCount > 0; } //TODO implement _mealCount;
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
	public void msgWakeUp()
	{
		_command = Command.WAKE_UP;
		stateChanged();
	}
	
	
	
	// ---------------------------------- SCHEDULER ------------------------------------
	public boolean pickAndExecuteAnAction()
	{
		//TODO if cmdGoToBed, set wakeTime to 7
		if(_state == State.IDLE)
		{
			if(_command == Command.COOK_AND_EAT_FOOD)
			{
				actStartCooking();
				return true;
			}
		}
		else if(_state == State.COOKING)
		{
			// don't check for commands
		}
		else if(_state == State.SLEEPING)
		{
			if(_event == Event.ALARM_CLOCK_RANG)
			{
				actWakeUp();
				return true;
			}
		}
		return false;
	}
	
	
	
	// ---------------------------------- ACTIONS ------------------------------------
	private void actStartCooking()
	{
		print("Starting to cook.");
		_state = State.COOKING;
		
		_gui.doGoToKitchen();
		waitForGuiToReachDestination();
	}
	private void actGoToBed()
	{
		print("Going to bed.");
		_state = State.SLEEPING;
		_gui.doGoToBed();
	}
	private void actWakeUp()
	{
		print("Waking up.");
		_state = State.IDLE;
		_gui.doGoIdle();
	}
	private void actLeave()
	{
		print("Leaving my home");
		_state = State.LEAVING;
		_gui.doLeaveHome();
	}
	private void actFinishedLeaving()
	{
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
