package city.home;

import city.PersonAgent;
import agent.Role;

public abstract class HomeRole extends Role
{
	//private List<Food> _groceries;



	protected enum Command { NONE, WATCH_TV, COOK_FOOD, GO_TO_BED, WAKE_UP }
	protected Command _command;
	
	public enum State { IDLE, COOKING, SLEEPING }
	public State state; //TODO fully implement these states
	
	private int _mealCount; //TODO
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	public HomeRole(PersonAgent person) { super(person); }
	public boolean sleeping() { return state == State.SLEEPING; }
	public boolean cooking() { return state == State.COOKING; }
	public boolean haveFood() { return _mealCount > 0; } //TODO implement _mealCount;
	
	
	
	// --------------------------------- COMMANDS -----------------------------------
	// note: commands are only from PersonAgent
	public void cmdWatchTv()
	{
		_command = Command.WATCH_TV;
		stateChanged();
	}
	public void cmdCookFood()
	{
		_command = Command.COOK_FOOD;
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
	
	
	
	public abstract boolean pickAndExecuteAnAction();
	//TODO if cmdGoToBed, set wakeTime to 7
}
