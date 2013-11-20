package city.home;

import city.PersonAgent;
import agent.Role;

public abstract class HomeRole extends Role
{
	//private List<Food> _groceries;



	protected enum Command { NONE, WATCH_TV, COOK_FOOD, GO_TO_BED }
	protected Command _command;
	
	
	
	// --------------------------- CONSTRUCTOR & PROPERTIES --------------------------
	public HomeRole(PersonAgent person) { super(person); }
	
	
	
	// --------------------------------- COMMANDS -----------------------------------
	// note: commands are only from PersonAgent
	public void cmdWatchTv()
	{
		_command = Command.WATCH_TV;
		stateChanged();
	}
	// note: commands are only from PersonAgent
	public void cmdGoToBed()
	{
		_command = Command.GO_TO_BED;
		stateChanged();
	}
	
	
	
	public abstract boolean pickAndExecuteAnAction();
}
