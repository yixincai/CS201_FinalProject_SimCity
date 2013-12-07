package city.home;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.Time;
import city.home.gui.HomeOccupantGui;
import city.interfaces.Person;
import agent.Role;

public abstract class HomeOccupantRole extends Role
{
	//private List<Meal> _meals;



	public enum Command { NONE, WATCH_TV, COOK_AND_EAT_FOOD, GO_TO_BED, WAKE_UP, LEAVE }
	private Command _command = Command.NONE; // Maybe change this to a list of Command so we can keep track of multiple sequential commands
	
	public enum Event { NONE, ALARM_CLOCK_RANG, GOT_HOME, }
	private Event _event = Event.GOT_HOME;
	
	public enum State { AWAY, IDLE, COOKING, SLEEPING, LEAVING }
	private State _state = State.AWAY;
	
	private int _mealCount = 2; // Starting people out with 2 meals

	protected Home _home = null;
	
	protected HomeOccupantGui _gui;
	
	private Semaphore _reachedDestination = new Semaphore(0, true);
	
	private Timer _alarmClock = new Timer();
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	// ------------- SETUP ------------
	public HomeOccupantRole(Person person, Home home)
	{
		super(person);
		setHome(home);
	}
	public boolean haveHome() { return _home != null; }
	public Command command() { return _command; }
	public Event event() { return _event; }
	public State state() { return _state; }
	public boolean sleeping() { return _state == State.SLEEPING; }
	public boolean cooking() { return _state == State.COOKING; }
	public boolean haveFood() { return _mealCount > 0; }
	public void addMeals(int addMealCount) { _mealCount += addMealCount; }
	public HomeOccupantGui gui() { return _gui; }
	public void setGui(HomeOccupantGui gui) { _gui = gui; }
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
	public void msgGotHome()
	{
		_event = Event.GOT_HOME;
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
				actCookAndEat();
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
				return false;
			}
		}
		else if(_state == State.COOKING)
		{
			// note: the whole cook and eat scenario takes place in one action, in HomeOccupantGui, so we shouldn't ever get here
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
		// don't change _command (so PersonAgent can set commands before we get home)
		// Note: _event had the value of GOT_HOME when this method got called; no longer need it to be that
		_event = Event.NONE;
		_state = State.IDLE;
		_gui.doGotHome();
	}
	private void actCookAndEat()
	{
		print("Starting to cook.");
		_command = Command.NONE;
		_event = Event.NONE;
		_state = State.COOKING;
		
		_mealCount--;
		_gui.doCookAndEatFood(); // calls a bunch of sequential actions for the animation
		waitForGuiToReachDestination();
		
		print("Finished cooking and eating");
		_person.cmdNoLongerHungry();
		_state = State.IDLE;
		_gui.doGoIdle();
	}
	private void actWatchTv()
	{
		print("Watching TV");
		_command = Command.NONE;
		// do we need to reset _event?
		_state = State.IDLE;
		
		_gui.doWatchTv();
	}
	private void actGoToBed()
	{
		print("Going to bed.");
		_command = Command.NONE;
		_event = Event.NONE; // this is important just to make sure that the scheduler won't wake up
		_state = State.SLEEPING;
		_gui.doGoToBed();
		waitForGuiToReachDestination();
		
		_alarmClock.schedule(
				new TimerTask() {
					public void run() { msgAlarmClockRang(); }
				},
				Time.getRealTime(8) // number of real milliseconds in 8 simulation hours
				);
	}
	private void actWakeUp()
	{
		print("Waking up.");
		// do we need to reset _command?
		_event = Event.NONE;
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
		_event = Event.GOT_HOME; // for next time
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
