package city;

import java.util.List;

import city.transportation.CommuterRole;
import agent.Agent;
import agent.Role;
import role.*;

public class PersonAgent extends Agent
{
	// --------------------------------------- DATA -------------------------------------------
	// Personal data:
	private String _name;
	
	// Role data:
	private List<Role> _temporaryRoles; // these are temporary roles that you do when you're at a place e.g. RestaurantXCustomerRole, MarketCustomerRole
	private Role _currentRole;
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole = new CommuterRole();
	private Role _occupation;
	//HomeRole _homeRole;
	
	// State data:
	private double _money;
	enum WealthState { RICH, NORMAL, DEADBEAT, BROKE, POOR } // the word "deadbeat" courtesy of Wilczynski lol
	enum NourishmentState { HUNGRY, FULL }
	enum LocationState { NONE, TRAVELING, HOME, WORK, BANK, MARKET, RESTAURANT }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		NourishmentState nourishment;
		
		/** Get the current wealth state, based on money and occupation status. */
		WealthState wealth()
		{
			if(_money <= 10)
			{
				return (_occupation != null) ? WealthState.BROKE : WealthState.POOR;
			}
			else if(_money <= 60)
			{
				return WealthState.DEADBEAT;
			}
			else if(_money <= 250)
			{
				return WealthState.NORMAL;
			}
			else
			{
				return WealthState.RICH;
			}
		}

		LocationState location()
		{
			//if(_commuterRole.currentPlace instanceof Restaurant) return LocationState.RESTAURANT;
			//else if(_commuterRole.currentPlace instanceof Bank) return LocationState.BANK;
			//etc.
			//else if(_commuterRole.currentPlace == null) {
			//	return _commuterRole.active ? LocationState.TRAVELING : LocationState.NONE;
			//}
			
			return LocationState.NONE; //TEMP
		}
	}
	State state = new State();
	
	
	
	// -------------------------------- CONSTRUCTORS & PROPERTIES ------------------------------------
	public PersonAgent(String name) { _name = name; }
	public String name() { return _name; }
	
	

	// --------------------------------------- SCHEDULER -------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
		// here, check for and do emergencies/important actions
		
		if(_currentRole != null)
		{
			if(_currentRole.active)
			{
				if(_currentRole.pickAndExecuteAnAction())
				{
					return true;
				}
			}
			else
			{
				if(_currentRole == _commuterRole)
				{
					// We must have just reached the destination since the role was just set to inactive and the previous value of _currentRole was _transportationRole.
					_currentRole = _nextRole;
				}
				_currentRole = null;
			}
		}
		else // i.e. currentRole == null
		{
			// Choose the next role to do.  Set _nextRole to the next role you will do, set _currentRole to the  
			
			// If we chose a role, return true.
			// note: it's possible that the program will not get here, because of doing an action in the code above.
			if(_currentRole != null) return true;
		}
		// if((e.g. just entered the weekend) and homeRole instanceof ApartmentRenterRole) _homeRole.pickAndExecuteAnAction(); // to pay rent
		//check for and do non-important actions, like check your phone
		if(_name.equals("Wilczynski"))
		{
			actTellLongStory();
			return true;
		}
		return false;
	}
	
	/**
	 * Chooses the next place you are going to go to
	 */
	private void chooseNextRole()
	{
		// For example:
		if(state.nourishment == NourishmentState.HUNGRY)
		{
			actGoToRestaurant();
		}
	}
	
	
	
// ---------------------------------------- ACTIONS ----------------------------------------
	private void actGoToRestaurant()
	{
		// Restaurant r = chooseRestaurant();
		// gui.getToRestaurant(r);
		// Role r = r.createNewCustRole();
		// r.active = true;
		// roles.add(r);
	}
	
	private void actTellLongStory()
	{
		print("When I was a young programmer, my boss ......");
	}
}
