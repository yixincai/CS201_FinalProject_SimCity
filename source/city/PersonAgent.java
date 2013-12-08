package city;

import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.lang.reflect.Type;
import java.util.*;

import city.interfaces.Person;
import city.home.*;
import city.bank.*;
import city.bank.gui.*;
import city.market.*;
import city.market.gui.*;
import city.restaurant.*;
import city.restaurant.eric.*;
import city.restaurant.omar.*;
import city.restaurant.ryan.*;
import city.restaurant.yixin.*;
import city.transportation.CommuterRole;
import agent.*;

public class PersonAgent extends Agent implements Person
{
	// Constants:
	public static final int RICH_LEVEL = 250;
	public static final int POOR_LEVEL = 10;
	
	// --------------------------------------- DATA -------------------------------------------
	// Personal data:
	private String _name;
	
	// Role data:
	private List<Role> _roles; // these are roles that you have had do when you're at a place e.g. EricCustomerRole, MarketCustomerRole
	private Role _currentRole; // this should never be null
	private boolean _sentCmdFinishAndLeave = false;
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole = null;
	private Role _occupation;
	private boolean _weekday_notWeekend;
	private HomeOccupantRole _homeOccupantRole;
	private HomeBuyingRole _homeBuyingRole; // Will handle buying an apartment or house (now, just pays rent on apartment)
	
	// Commands for scenarios
	private List<String> _actionsToDo = new ArrayList<String>();
	
	// State data:
	public double _money;
	enum WealthState { RICH, NORMAL, BROKE, POOR } // the word "deadbeat" courtesy of Wilczynski lol
	boolean _deadbeat = false; // if true, means this person doesn't pay loans
	enum NourishmentState { HUNGRY, FULL }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		NourishmentState nourishment = NourishmentState.FULL; //TODO implement value for hunger
		double nourishmentLevel;
		NourishmentState nourishment() { return nourishment; }
		
		/** Get the current wealth state, based on money and occupation status. */
		WealthState wealth()
		{
			if(_money < POOR_LEVEL)
			{
				return (_occupation != null) ? WealthState.BROKE : WealthState.POOR;
			}
			else if(_money < RICH_LEVEL)
			{
				return WealthState.NORMAL;
			}
			else
			{
				return WealthState.RICH;
			}
		}
		
		double time()
		{
			return Time.currentTime();
		}
		
		Time.Day today()
		{
			return Time.today();
		}
	}
	State _state = new State();
	
	
	
	// ------------------------------------------- CONSTRUCTORS & SETUP --------------------------------------------
	public PersonAgent(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType, List<String> actionsToDo)
	{
		this(name, money, occupationType, weekday_notWeekend, housingType);
		_actionsToDo.addAll(actionsToDo);
	}
	// This constructor is for unit testing
	public PersonAgent(String name) { _name = name; }
	/**
	 * Constructor
	 * @param name Name
	 * @param money Initial amount of money
	 * @param occupationType I.e. Restaurant Cashier or Restaurant Host or Bank Teller etc.
	 * @param housingType House or Apartment
	 */
	public PersonAgent(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType) 
	{
		_name = name; 
		_money = money;
		setWorkDays(weekday_notWeekend);
		acquireOccupation(occupationType);
		if(_occupation != null) { AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(), "Acquired occupation " + _occupation.typeToString() + "."); }
		else { AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(),"Acquired null occupation."); }
		acquireHome(housingType);
		
		// For testing purposes for V1, choose a random action to do at home.
		switch((int)(Math.random()*3))
		{
		case 0:
			_homeOccupantRole.cmdWatchTv();
			break;
		case 1:
			_homeOccupantRole.cmdCookAndEatFood();
			break;
		case 2:
			_homeOccupantRole.cmdGoToBed();
			break;
		}
		
		generateAndSetCommuterRole();
		setNextRole(_homeOccupantRole);
	}
	/** Sets _commuterRole to a new CommuterRole */
	public void generateAndSetCommuterRole()
	{
		_commuterRole = new CommuterRole(this, null); // may replace null with _homeOccupantRole.place() to set the person's starting position
	}
	/** Acquires an available house or apartment and sets the _homeOccupantRole and _homeBuyingRole appropriately.
	 * @param homeType Either "house" or "apartment" */
	public void acquireHome(String homeType)
	{
		if(homeType.equalsIgnoreCase("apartment"))
		{
			List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
			for(ApartmentBuilding b : apartmentBuildings)
			{
				List<Apartment> apartments = b.apartments();
				for(Apartment a : apartments)
				{
					HomeOccupantRole newHomeOccupantRole = a.tryGenerateHomeOccupantRole(this);
					if(newHomeOccupantRole != null)
					{
						_homeOccupantRole = newHomeOccupantRole;
						_homeBuyingRole = a.generateHomeBuyingRole(this);
						return;
					}
				}
			}
		}
		else if(homeType.equalsIgnoreCase("house"))
		{
			List<House> houses = Directory.houses();
			for(House h : houses)
			{
				HomeOccupantRole newHomeOccupantRole = h.tryGenerateHomeOccupantRole(this);
				if(newHomeOccupantRole != null)
				{
					_homeOccupantRole = newHomeOccupantRole;
					_homeBuyingRole = null; // will eventually change this to HomeOwnerRole
					return;
				}
			}
			
			List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
			for(ApartmentBuilding b : apartmentBuildings)
			{
				List<Apartment> apartments = b.apartments();
				for(Apartment a : apartments)
				{
					HomeOccupantRole newHomeOccupantRole = a.tryGenerateHomeOccupantRole(this);
					if(newHomeOccupantRole != null)
					{
						_homeOccupantRole = newHomeOccupantRole;
						_homeBuyingRole = a.generateHomeBuyingRole(this);
						return;
					}
				}
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid value of homeType: " + homeType);
		}
		
		AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(),"Failed to acquire a(n) " + homeType + ".");
		_homeOccupantRole = new HomelessRole(this);
		_homeBuyingRole = null;
	}
	/** Sets the value of _occupation to a role that is requested by occupationType if possible; else it sets _occupation to a new waiter role from a randomly chosen restaurant. */
	public void acquireOccupation(String occupationType) 
	{
		Role newOccupation = null;
		List<Restaurant> restaurants = Directory.restaurants();
		List<Bank> banks = Directory.banks();
		List<Market> markets = Directory.markets();
		switch(occupationType)
		{
			// note: if control reaches a break statement, the new occupation will be a waiter.
			case "Waiter":
				_occupation = restaurants.get(0).generateWaiterRole(this,false);
				return; // waiter is generated right after this switch statement
			case "Restaurant Cashier":
				newOccupation = null;
				for(Restaurant r : restaurants)
				{
					newOccupation = r.tryAcquireCashier(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						return;
					}
				}
				break;
			case "Cook":
				newOccupation = null;
				for(Restaurant r : restaurants)
				{
					newOccupation = r.tryAcquireCook(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						return;
					}
				}
				break;
			case "Restaurant Host":
				newOccupation = null;
				for(Restaurant r : restaurants)
				{
					newOccupation = r.tryAcquireHost(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						return;
					}
				}
				break;
			case "Bank Teller":
				newOccupation = null;
				for(Bank b : banks)
				{
					newOccupation = b.tryAcquireTeller(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						BankTellerRoleGui bankTellerRoleGui = new BankTellerRoleGui((BankTellerRole)_occupation);
						((BankTellerRole)_occupation).setGui(bankTellerRoleGui);
						((Bank)_occupation.place()).animationPanel().addGui(bankTellerRoleGui);
						return;
					}
				}
				break;
			case "Bank Host":
				newOccupation = null;
				for(Bank b : banks)
				{
					newOccupation = b.tryAcquireHost(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						BankHostRoleGui bankHostRoleGui = new BankHostRoleGui((BankHostRole)_occupation);
						((BankHostRole)_occupation).setGui(bankHostRoleGui);
						((Bank)_occupation.place()).animationPanel().addGui(bankHostRoleGui);
						return;
					}
				}
				break;
			case "Market Cashier":
				newOccupation = null;
				for(Market m : markets)
				{
					newOccupation = m.tryAcquireCashier(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						MarketCashierGui marketCashierGui = new MarketCashierGui((MarketCashierRole)_occupation);
						((MarketCashierRole)_occupation).setGui(marketCashierGui);
						((Market)_occupation.place()).animationPanel().addGui(marketCashierGui);
						return;
					}
				}
				break;
			case "Market Employee":
				newOccupation = null;
				for(Market m : markets)
				{
					newOccupation = m.tryAcquireEmployee(this);
					if(newOccupation != null)
					{
						_occupation = newOccupation;
						MarketEmployeeGui marketEmployeeGui = new MarketEmployeeGui((MarketEmployeeRole)_occupation);
						((MarketEmployeeRole)_occupation).setGui(marketEmployeeGui);
						((Market)_occupation.place()).animationPanel().addGui(marketEmployeeGui);
						return;
					}
				}
				break;
			// BEGIN HACKS
			case "Market Customer":
				_occupation = markets.get(0).generateCustomerRole(this);
				return;
			case "Yixin Customer":
				_occupation = restaurants.get(0).generateCustomerRole(this);
				return;
			case "Omar Customer":
				_occupation = restaurants.get(1).generateCustomerRole(this);
				return;
			case "Ryan Customer":
				_occupation = restaurants.get(2).generateCustomerRole(this);
				return;
			case "Bank Customer":
				_occupation = banks.get(0).generateCustomerRole(this);
				((BankCustomerRole)_occupation).cmdRequest("Robber", 10000);//"Deposit",100);
				return;
			case "Yixin Waiter":
				_occupation = restaurants.get(0).generateWaiterRole(this, true);
				return;
			case "Omar Waiter":
				_occupation = restaurants.get(1).generateWaiterRole(this, true);
				return;
			case "Ryan Waiter":
				_occupation = restaurants.get(2).generateWaiterRole(this, false);
				return;
			case "None":
				_occupation = null;
				// this causes _occupation to be set to _homeOccupantRole
				return;
		}
		// note: control reaches here because no jobs were found
		newOccupation = restaurants.get((new Random()).nextInt(restaurants.size())).generateWaiterRole(this, false);
		_occupation = newOccupation;
	}
	
	
	
	// ------------------------------------------- PROPERTIES --------------------------------------------------
	public String name() { return _name; }
	public double money() { return _money; }
	/** Sets the days the person works. @param weekday_notWeekend True if working weekdays, false if working weekends. */
	public void setWorkDays(boolean weekday_notWeekend) {
		_weekday_notWeekend = weekday_notWeekend;
	}
	public HomeOccupantRole homeOccupantRole() { return _homeOccupantRole; }
	public CommuterRole commuterRole() { return _commuterRole; }
	// Actions to do:
	/** Adds an action to do to the back of the list of actions to do */
	public void addActionToDo(String actionToDo) { _actionsToDo.add(actionToDo); }
	/** Adds a list of actions to do to the back of the list of actions to do */
	public void addActionsToDo(List<String> actionsToDo) { _actionsToDo.addAll(actionsToDo); }
	/** Inserts an action to do at the beginning of the list */
	public void insertFirstActionToDo(String actionToDo) { _actionsToDo.add(0, actionToDo); }
	public boolean removeActionToDo(String actionToDo) { return _actionsToDo.remove(actionToDo); }
	/** Removes and returns the first action in the list */
	private String popFirstActionToDo() { return _actionsToDo.remove(0); }
	/** Returns the current _actionsToDo list and resets it to a new, empty list. */
	public List<String> clearActionsToDo() {
		List<String> oldActionsToDo = _actionsToDo;
		_actionsToDo = new ArrayList<String>();
		return oldActionsToDo;
	}
	
	
	
	// ------------------------------------------------ COMMANDS -----------------------------------------------------------
	public void cmdChangeMoney(double delta) { _money += delta; }
	public void cmdNoLongerHungry()
	{
		//TODO change hunger state, set timer to change the state to hungry again
	}
	
	
	
	// =========================================================================================================================
	// -------------------------------------------------- SCHEDULER ------------------------------------------------------------
	// =========================================================================================================================
	@Override
	protected boolean pickAndExecuteAnAction() {
		// here, check for and do emergencies/important actions
		
		if(_currentRole.active)
		{
			// Finish current role because you have to get to work:
			if(workingToday() && !_sentCmdFinishAndLeave)
			{
				if(_occupation != null)
				{
					if(_currentRole == _occupation)
					{
						// note: you're currently at you job.
						// If your shift just finished, leave.
						if(!timeToBeAtWork())
						{
							finishAndLeaveCurrentRole();
							return true;
						}
					}
					else
					{
						// note: you're not currently at your job.
						// If you need to go to work, finish your current role.
						if(timeToBeAtWork())
						{
							finishAndLeaveCurrentRole();
							return true;
						}
					}
				}
			}
			/*
			if(_currentRole == _homeOccupantRole && (_state.time() > 20 || _state.time() < 7))
			{
				if(_state.nourishment() == NourishmentState.HUNGRY)
				{
					if(!_homeOccupantRole.cooking())
					{
						_homeOccupantRole.cmdCookAndEatFood();
						return true;
					}
				}
				else
				{
					if(!_homeOccupantRole.sleeping())
					{
						_homeOccupantRole.cmdGoToBed();
						return true;
					}
				}
			}
			*/
			
			// ================================================== Call current role's scheduler =============================================
			// print("About to call _currentRole (" + _currentRole.toString() + ") scheduler.");
			if(_currentRole.pickAndExecuteAnAction())
			{ 
				return true;
			}
		}
		else // i.e. _currentRole.active == false
		{
			// note: if we get here, a role just finished leaving.
			_sentCmdFinishAndLeave = false;
			
			if(_currentRole == _commuterRole)
			{
				// commuter role must have just reached the destination; we need to shift the current role from the commuter role to whatever next role is.
				_currentRole = _nextRole;
				_currentRole.active = true;
				return true;
			}
			
			
			
			// We will only get here if we just finished a role which is not _commuterRole.
			// Choose a new role and call setNextRole on it
			
			
			
			// First, check if there are actions to do in the list.
			if(!_actionsToDo.isEmpty())
			{
				String nextAction = popFirstActionToDo();
				if(nextAction.contains("Restaurant"))
				{
					if(nextAction.contains("Eric")) {
						if(actGoToRestaurantOfType("Eric")) return true;
					}
					if(nextAction.contains("Omar")) {
						if(actGoToRestaurantOfType("Omar")) return true;
					}
					if(nextAction.contains("Ryan")) {
						if(actGoToRestaurantOfType("Ryan")) return true;
					}
				//	if(nextAction.contains("Tanner")) {
				//		if(goToRestaurantOfType("Tanner")) return true;
				//	}
					if(nextAction.contains("Yixin")) {
						if(actGoToRestaurantOfType("Yixin")) return true;
					}
				}
				else if(nextAction.contains("Bank"))
				{
					if(nextAction.contains("Withdraw")) {
						//TODO
					}
				}
				else if(nextAction.contains("Market"))
				{
					// Buy 3 meals from the market
					if(actBuyMealsFromMarket(3)) return true;
				}
				else if(nextAction.contains("Home"))
				{
					if(nextAction.contains("Sleep")) {
						_homeOccupantRole.cmdGoToBed();
					}
					else if(nextAction.contains("Eat") || nextAction.contains("Cook")) {
						_homeOccupantRole.cmdCookAndEatFood();
					}
					else if(nextAction.contains("TV")) {
						_homeOccupantRole.cmdWatchTv();
					}
					setNextRole(_homeOccupantRole);
					return true;
				}
			}
			
			
			
			// Now we begin the free-running, autonomous behavior for the person.
			if(_occupation != null && workingToday() && timeToBeAtWork())
			{
				setNextRole(_occupation);
				return true;
			}
			else if(_occupation == null)
			{
				setNextRole(_homeOccupantRole);
				return true;
			}
			else if(_state.time() > Directory.closingTime() || _state.time() < Directory.openingTime()) //could replace with variables for sleepTime and wakeTime
			{
				_homeOccupantRole.cmdGoToBed();
				setNextRole(_homeOccupantRole);
				return true;
			}
			else if(_state.nourishment() == NourishmentState.HUNGRY)
			{
				if(_state.wealth() == WealthState.RICH)
				{
					actGoToAnyRestaurant();
				}
				else
				{
					Random rand = new Random();
					if(rand.nextInt(4) == 0)
					{
						if(actGoToAnyRestaurant())
						{
							return true;
						}
					}
					if(actEatAtHome()) return true;
				}
			}
		}
		
		//check for and do non-important actions, like check your phone
		
		// (Peanut gallery)
		if(_name.contains("Wilczynski")) { actTellLongStory(); }
		else if(_name.contains("iWhale")) { actIWhale(); }
		return false;
	}
	
	
	
	// ---------------------------------------- ACTIONS ----------------------------------------
	// note: These really just set the next role.
	
	// ----------------- HOME -----------------
	/** Eats at home if you have food, otherwise goes to market first then tries to eat at home. */
	private boolean actEatAtHome()
	{
		if(_homeOccupantRole.haveFood())
		{
			_homeOccupantRole.cmdCookAndEatFood();
			setNextRole(_homeOccupantRole);
			return true;
		}
		else
		{
			if(actBuyMealsFromMarket(3)) // hard-coded 3 meals for now
			{
				insertFirstActionToDo("HomeEat");
				return true;
			}
		}
		return false;
	}
	
	
	
	// ----------------- Market -----------------
	private boolean actBuyMealsFromMarket(int meals)
	{
		// Search for a MarketCustomerRole in _roles, use that;
		// if no MarketCustomerRole in _roles, choose a Market from the Directory, and get a new MarketCustomerRole from it
		for(Role r : _roles)
		{
			if(r instanceof MarketCustomerRole)
			{
				MarketCustomerRole mcr = (MarketCustomerRole)r;
				mcr.cmdBuyFood(meals);
				setNextRole(mcr);
				return true;
			}
		}
		// note: we only get here if no MarketCustomerRole was found in _roles
		List<Market> markets = Directory.markets();
		for(Market m : markets)
		{
			MarketCustomerRole mcr = m.generateCustomerRole(this);
			mcr.cmdBuyFood(meals);
			setNextRole(mcr);
			_roles.add(mcr);
			return true;
		}
		return false;
	}
	
	
	
	// ----------------- Restaurant -----------------
	/** If a RestaurantCustomerRole exists in _roles, set that to the next role.  Else, get a new customer role from a randomly chosen restaurant */
	private boolean actGoToAnyRestaurant()
	{
		RestaurantCustomerRole rcr = (RestaurantCustomerRole)getRoleOfType(RestaurantCustomerRole.class);
		if(rcr != null)
		{
			rcr.cmdGotHungry();
			setNextRole(rcr);
			return true;
		}
		
		// note: we only get here if no RestaurantCustomerRole was found in _roles
		List<Restaurant> restaurants = Directory.restaurants();
		if(restaurants.size() != 0)
		{
			rcr = restaurants.get(new Random().nextInt(restaurants.size())).generateCustomerRole(this);
			rcr.cmdGotHungry();
			setNextRole(rcr);
			_roles.add(rcr);
			return true;
		}
		
		return false;
	}
	/**
	 * Searches for a RestaurantCustomerRole of the correct type in _roles and then in Directory.restaurants() and calls setNextRole on it
	 * @param type "Eric", "Omar", "Ryan", or "Yixin".  Case-sensitive and must match exactly.
	 * @return true if a restaurant of the passed-in type was chosen and setNextRole was called
	 */
	private boolean actGoToRestaurantOfType(String type)
	{
		RestaurantCustomerRole rcr = null;
		switch(type)
		{
		case "Eric":
			rcr = (RestaurantCustomerRole)getRoleOfType(EricCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(EricRestaurant.class);
				if(rcr != null) _roles.add(rcr);
			}
			break;
		case "Omar":
			rcr = (RestaurantCustomerRole)getRoleOfType(OmarCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(OmarRestaurant.class);
				if(rcr != null) _roles.add(rcr);
			}
			break;
		case "Ryan":
			rcr = (RestaurantCustomerRole)getRoleOfType(RyanCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(RyanRestaurant.class);
				if(rcr != null) _roles.add(rcr);
			}
			break;
		//case "Tanner":
		//	rcr = (RestaurantCustomerRole)getRoleOfType(TannerCustomerRole.class);
		//	if(rcr == null)
		//	{
		//		rcr = getNewCustomerRoleFromRestaurantOfType(TannerRestaurant.class);
		//		if(rcr != null) _roles.add(rcr);
		//	}
		//	break;
		case "Yixin":
			rcr = (RestaurantCustomerRole)getRoleOfType(YixinCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(YixinRestaurant.class);
				if(rcr != null) _roles.add(rcr);
			}
			break;
		}
		if(rcr != null)
		{
			rcr.cmdGotHungry();
			setNextRole(rcr);
			return true;
		}
		
		return false;
	}
	
	
	
	// --------------- (Peanut gallery) ---------------------
	private void actTellLongStory()
	{
		AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(),"When I was a young programmer, my boss was skeptical of my design.  I proved him wrong.");
	}
	private void actIWhale()
	{
		print("\n");
		print("\n");
		print("\n");
		print("\n");
		print("\n");
		print("\n");
		print("*_____________________________________*");
	}
	
	
	
	// ------------------------------------------ UTILITIES -------------------------------------
	private boolean workingToday()
	{
		//return true;
		// Commenting this out because we're currently not taking account of weekends
		return ((_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && !_weekday_notWeekend) ||
				(!(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && _weekday_notWeekend);
				
	}
	private boolean timeToBeAtWork()
	{
		return _state.time() > Directory.openingTime() - .5 && // .5 is half an hour
				_state.time() < Directory.closingTime() + .5;
	}
	private void finishAndLeaveCurrentRole()
	{
		//if(_currentRole == _commuterRole) setNextRole(_occupation); // why is that here?
		_sentCmdFinishAndLeave = true;
		_currentRole.cmdFinishAndLeave();
		stateChanged();
	}
	private void setNextRole(Role nextRole)
	{
		_nextRole = nextRole;
		_commuterRole.setDestination(nextRole.place());
		_currentRole = _commuterRole;
		_currentRole.active = true;
		stateChanged();
	}
	private Role getRoleOfType(Type type)
	{
		for(Role r : _roles)
		{
			if(r.getClass().equals(type)) return r;
		}
		return null;
	}
	/** This method returns a new customer role from the restaurant type specified.
	 * It does not take into account whether or not the restaurant's customer role already exists in _roles.
	 */
	private RestaurantCustomerRole getNewCustomerRoleFromRestaurantOfType(Type type)
	{
		List<Restaurant> restaurants = Directory.restaurants();
		for(Restaurant r : restaurants)
		{
			if(r.getClass().equals(type)) return r.generateCustomerRole(this);
		}
		return null;
	}
}
