package city.home;

import city.PersonAgent;
import city.Place;
import agent.Role;

public class HomeRole extends Role
{
	//private List<Meal> _meals;



	protected enum Command { NONE, WATCH_TV, COOK_AND_EAT_FOOD, GO_TO_BED, WAKE_UP, LEAVE }
	protected Command _command;
	
	public enum State { IDLE, COOKING, SLEEPING }
	public State state; //TODO fully implement these states
	
	private int _mealCount; //TODO

	private Home _home;
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	public HomeRole(PersonAgent person) { super(person); }
	public boolean haveHome() { return _home != null; } // return false if you don't have a home; if you don't have a home, the first thing your scheduler should do is try to buy a home.
	public boolean sleeping() { return state == State.SLEEPING; }
	public boolean cooking() { return state == State.COOKING; }
	public boolean haveFood() { return _mealCount > 0; } //TODO implement _mealCount;
	@Override
	public Place place() { return _home.place(); }
	
	
	
	// --------------------------------- COMMANDS -----------------------------------
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
	public void cmdWakeUp()
	{
		_command = Command.WAKE_UP;
		stateChanged();
	}
	public void cmdFinishAndLeave() {
		_command = Command.LEAVE;
	}
	
	
	
	public boolean pickAndExecuteAnAction() {
		//TODO if cmdGoToBed, set wakeTime to 7
		return false;
	}
	
}
