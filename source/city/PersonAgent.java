package city;

import java.util.List;

import city.bank.BankCustomerRole;
import city.transportation.CommuterRole;
import agent.Agent;
import agent.Role;

public class PersonAgent extends Agent
{
	// --------------------------------------- DATA -------------------------------------------
	// Personal data:
	private String _name;
	
	// Role data:
	private List<Role> _roles; // these are roles that you do when you're at a place e.g. RestaurantXCustomerRole, MarketCustomerRole, BankTellerRole
	private Role _currentRole; // this should never be null
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole = new CommuterRole(this, null); //TODO null should be a Place
	private Role _occupation;
	private double _occupationStartTime;
	private double _occupationEndTime;
	private boolean _weekday_notWeekend;
	//HomeRole _homeRole;
	
	// State data:
	private double _money;
	enum WealthState { RICH, NORMAL, BROKE, POOR } // the word "deadbeat" courtesy of Wilczynski lol
	boolean _deadbeat = false; // if true, means this person doesn't pay loans
	enum NourishmentState { HUNGRY, FULL }
	enum LocationState { NONE, TRAVELING, HOME, WORK, BANK, MARKET, RESTAURANT }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		NourishmentState nourishment;
		
		/** Get the current wealth state, based on money and occupation status. */
		WealthState wealth()
		{
			if(_money < Constants.POOR_LEVEL)
			{
				return (_occupation != null) ? WealthState.BROKE : WealthState.POOR;
			}
			else if(_money < Constants.RICH_LEVEL)
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
		
		double time()
		{
			return Time.getTime();
		}
		
		Time.Day today()
		{
			return Time.today();
		}
	}
	State _state = new State();
	
	
	
	// -------------------------------- CONSTRUCTORS & PROPERTIES ------------------------------------
	public PersonAgent(String name) { _name = name; }
	// public PersonAgent(String name, Role r) { _name = name; ... }
	public String name() { return _name; }
	public void changeMoney(double delta) { _money += delta; }
	public void setOccupationStartTime(double occupationStartTime) { _occupationStartTime = occupationStartTime; }
	public void setOccupationEndTime(double occupationEndTime) { _occupationEndTime = occupationEndTime; }
	/** Shortcut for setting the occupation start and end times
	 * @param shift Morning, Afternoon, or Evening; if weekday_notWeekend is false, only matters whether shift is Evening or not
	 * @param weekday_notWeekend frue if weekday shift, false if weekend shift
	 */
	public void setShift(String shift, boolean weekday_notWeekend) {
		if(weekday_notWeekend) {
			switch(shift) {
			case "Morning":
				_occupationStartTime = 8;
				_occupationEndTime = 12;
				break;
			case "Afternoon":
				_occupationStartTime = 12;
				_occupationEndTime = 16;
				break;
			case "Evening":
				_occupationStartTime = 16;
				_occupationEndTime = 20;
				break;
			}
		}
		else {
			if(shift.equals("Evening")) {
				_occupationStartTime = 18;
				_occupationEndTime = 23;
			}
			else {
				_occupationStartTime = 8;
				_occupationEndTime = 18;
			}
		}
		_weekday_notWeekend = weekday_notWeekend;
	}
	
	

	// --------------------------------------- SCHEDULER -------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
		// here, check for and do emergencies/important actions
		
		if(_currentRole.active)
		{
			// Finish role because you have to get to work:
			if(workingToday())
			{
				if(_currentRole != _occupation && _occupation != null && timeToBeAtWork())
				{
					_currentRole.cmdFinishAndLeave();
				}
			}
			
			if(_currentRole.pickAndExecuteAnAction()) { return true; }
		}
		else // i.e. _currentRole.active == false
		{
			if(_currentRole == _commuterRole)
			{
				// We must have just reached the destination since the role was just set to inactive and the previous value of _currentRole was _transportationRole.
				_currentRole = _nextRole;
				_currentRole.active = true;
				return true;
			}
			else
			{
				// note: the program will only get to here if we just finished one role, which is not transportation role
				// Choose the next role to do.  Set _nextRole to the next role you will do, set _currentRole to _commuterRole
				
				if(workingToday())
				{
					if(_occupation != null && timeToBeAtWork()) //TODO add JoblessRole
					{
						_nextRole = _occupation;
						_commuterRole.setDestination(_occupation.place());
						_currentRole = _commuterRole;
						_currentRole.active = true;
						return true;
					}
				}
				
				/*// The model for conditions:
				if(condition)
				{
					restaurant.eric.CustomerRole c = new restaurant.eric.CustomerRole();
					c.cmdGotHungry();
					_nextRole = c;
					_commuterRole.setDestination(restaurantEric);
					_currentRole = _commuterRole;
					_currentRole.active = true;
					return true;
				}*/
				/*
				// Decide whether or not to go to the bank
				for(Role r : _roles)
				{
					if(r instanceof BankCustomerRole)
					{
						BankCustomerRole bcr = (BankCustomerRole)r;
						
						if(true) //if I want to go to the bank
						{
							//bcr.cmd....();
							_nextRole = bcr;
							_commuterRole.setDestination(bcr.place());
							_currentRole = _commuterRole;
							_currentRole.active = true;
							return true;
						}
					}
				}*/
				
				//_nextRole = _HomeRole;
			}
		}
		/*
		}
		else // i.e. currentRole == null
		{
			// If we chose a role, return true.
			// note: it's possible that the program will not get here, because of doing an action in the code above.
			if(_currentRole != null) return true;
		}
		// if((e.g. just entered the weekend) and homeRole instanceof ApartmentRenterRole) { _homeRole.cmdPayRent(); _homeRole.pickAndExecuteAnAction(); } // to pay rent
		*/
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
		if(_state.nourishment == NourishmentState.HUNGRY)
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
		print("When I was a young programmer, my boss was skeptical of my design.  I proved him wrong.");
	}
	
	
	
	// ------------------------------------------ UTILITIES -------------------------------------
	private boolean workingToday()
	{
		return ((_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && !_weekday_notWeekend) ||
				(!(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && _weekday_notWeekend);
	}
	private boolean timeToBeAtWork()
	{
		return _state.time() > _occupationStartTime - .5 && // .5 is half an hour
				_state.time() < _occupationEndTime;
	}
}
