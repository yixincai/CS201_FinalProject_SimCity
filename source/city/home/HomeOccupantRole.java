package city.home;

import city.PersonAgent;
import city.Place;
import city.home.gui.HomeOccupantGui;
import agent.Role;

public abstract class HomeOccupantRole extends Role
{
	//private List<Meal> _meals;



	protected enum Event { NONE, WATCH_TV, COOK_AND_EAT_FOOD, GO_TO_BED, WAKE_UP, LEAVE }
	protected Event _event;
	
	public enum State { IDLE, COOKING, SLEEPING }
	private State _state; //TODO fully implement these states
	
	private int _mealCount = 2; // Starting people out with 2 meals

	private Home _home = null;
	
	private HomeOccupantGui _gui;
	
	
	
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
		_event = Event.WATCH_TV;
		stateChanged();
	}
	public void cmdCookAndEatFood()
	{
		_event = Event.COOK_AND_EAT_FOOD;
		stateChanged();
	}
	public void cmdGoToBed()
	{
		_event = Event.GO_TO_BED;
		stateChanged();
	}
	public void cmdFinishAndLeave() {
		_event = Event.LEAVE;
	}
	public void msgWakeUp()
	{
		_event = Event.WAKE_UP;
		stateChanged();
	}
	
	
	
	// ---------------------------------- SCHEDULER ------------------------------------
	public boolean pickAndExecuteAnAction()
	{
		//TODO if cmdGoToBed, set wakeTime to 7
		if(_state == State.IDLE)
		{
			if(_event == Event.COOK_AND_EAT_FOOD)
			{
				actStartCooking();
				return true;
			}
		}
		else if(_state == State.COOKING)
		{
			
		}
		else if(_state == State.SLEEPING)
		{
			
		}
		return false;
	}
	
	
	
	// ---------------------------------- ACTIONS ------------------------------------
	private void actStartCooking()
	{
		print("Starting to cook.");
		_state = State.COOKING;
		
		//_gui.doGoToKitchen();
		
	}
}
