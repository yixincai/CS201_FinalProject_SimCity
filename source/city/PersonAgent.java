package city;

import java.util.*;

// TODO the gui packages are basically only here for the setOccupation() function. We will move the gui instantiation elsewhere, probably to the roles' respective constructors.
import city.home.*;
import city.bank.*;
import city.bank.gui.*;
import city.market.*;
import city.market.gui.*;
import city.restaurant.*;
import city.restaurant.yixin.*;
import city.transportation.CommuterRole;
import agent.*;

public class PersonAgent extends Agent
{
	// --------------------------------------- DATA -------------------------------------------
	// Personal data:
	private String _name;
	
	// Role data:
	private List<Role> _roles; // these are roles that you do when you're at a place e.g. RestaurantXCustomerRole, MarketCustomerRole, BankTellerRole
	private Role _currentRole; // this should never be null
	private boolean _sentCmdFinishAndLeave = false;
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole = null;
	private Role _occupation;
	private boolean _weekday_notWeekend;
	private HomeOccupantRole _homeOccupantRole;
	private HomeBuyingRole _homeBuyingRole; // Will handle buying an apartment or house (now, just pays rent on apartment)
	
	// State data:
	public double _money;
	enum WealthState { RICH, NORMAL, BROKE, POOR } // the word "deadbeat" courtesy of Wilczynski lol
	boolean _deadbeat = false; // if true, means this person doesn't pay loans
	enum NourishmentState { HUNGRY, FULL }
	enum LocationState { NONE, TRAVELING, HOME, WORK, BANK, MARKET, RESTAURANT }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		NourishmentState nourishment = NourishmentState.FULL; //TODO implement value for hunger
		double nourishmentLevel;
		NourishmentState nourishment() { return nourishment; }
		
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
	
	
	
	// ------------------------------------------- CONSTRUCTORS & PROPERTIES --------------------------------------------
	// ------------------ CONSTRUCTORS & SETUP ---------------------
	public PersonAgent(String name) { _name = name; }
	/**
	 * Constructor
	 * @param name Name
	 * @param money Initial amount of money
	 * @param occupationType I.e. Restaurant Cashier or Restaurant Host or Bank Teller etc.
	 * @param housingType House or Apartment
	 */
	public PersonAgent(String name, double money, String occupationType, String housingType) 
	{
		_name = name; 
		_money = money; 
		acquireOccupation(occupationType);
		if(_occupation != null) print("Acquired occupation " + _occupation.toString() + ".");
		else print("Acquired null occupation.");
		acquireHome(housingType);
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
		}
		else
		{
			throw new IllegalArgumentException("Invalid value of homeType: " + homeType);
		}
		
		print("Failed to acquire a(n) " + homeType + ".");
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
				_occupation = restaurants.get(0).generateWaiterRole(this);
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
				((BankCustomerRole)_occupation).cmdRequest("Deposit",100);
				return;
			case "Yixin Waiter":
				_occupation = restaurants.get(0).generateWaiterRole(this);
				return;
			case "Omar Waiter":
				_occupation = restaurants.get(1).generateWaiterRole(this);
				return;
			case "Ryan Waiter":
				_occupation = restaurants.get(2).generateWaiterRole(this);
				return;
			case "None":
				_occupation = null;
				return;
		}
		// note: control reaches here because no jobs were found
		newOccupation = restaurants.get((new Random()).nextInt(restaurants.size())).generateWaiterRole(this);
		_occupation = newOccupation;
	}
	// ---------------------- OTHER PROPERTIES -------------------------
	public String getName() { return _name; }
	public double money() { return _money; }
	/** Sets the days the person works. @param weekday_notWeekend True if working weekdays, false if working weekends. */
	public void setWorkDays(boolean weekday_notWeekend) {
		_weekday_notWeekend = weekday_notWeekend;
	}
	public HomeOccupantRole homeOccupantRole() { return _homeOccupantRole; }
	public CommuterRole commuterRole() { return _commuterRole; }
	
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
				if(_currentRole == _homeOccupantRole) //TODO I'm a little skeptical of this if-statement.  Maybe a better place would be to call _homeOccupantRole.cmdGotHome() before calling setNextRole(_homeOccupantRole)
				{
					// i.e. if we just got home
					_homeOccupantRole.cmdGotHome();
				}
				return true;
			}
			else
			{
				// note: the program will only get to here if we just finished a role that is not transportation role.
				// Choose the next role to do.  Set _nextRole to the next role you will do, set _currentRole to _commuterRole
				
				if(_occupation != null && workingToday() && timeToBeAtWork())
				{
					setNextRole(_occupation);
					return true;
				}
				else if(_occupation == null) //DEBUG
				{
					_homeOccupantRole.cmdGoToBed();
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
						goToRestaurant();
					}
					else
					{
						Random rand = new Random();
						if(rand.nextInt(4) == 0)
						{
							if(goToRestaurant())
							{
								return true;
							}
						}
						else
						{
							if(_homeOccupantRole.haveFood())
							{
								_homeOccupantRole.cmdCookAndEatFood();
								setNextRole(_homeOccupantRole);
								return true;
							}
							else
							{
								buyMealsFromMarket(3); // 3 meals
								return true;
							}
						}
					}
				}
			}
		}
		
		//check for and do non-important actions, like check your phone
		if(_name.equals("Wilczynski"))
		{
			actTellLongStory();
			return true;
		}
		else if(_name.equals("iWhale"))
		{
			actIWhale();
			return true;
		}
		return false;
	}
	
	
	
	// ---------------------------------------- ACTIONS ----------------------------------------
	
	// (Peanut gallery)
	private void actTellLongStory()
	{
		print("When I was a young programmer, my boss was skeptical of my design.  I proved him wrong. \n"
				+ "Some of my students placed me in a giant hamster ball and dropped me in the middle of the Pacific Ocean, and I had to find my way back.");
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
		return true;
		/*// Commenting this out because we're currently not taking account of weekends
		return ((_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && !_weekday_notWeekend) ||
				(!(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && _weekday_notWeekend);
				*/
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
	private boolean buyMealsFromMarket(int meals)
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
	private boolean goToRestaurant()
	{
		// Search for a YixinCustomerRole in _roles, use that;
		// if no YixinCustomerRole in _roles, choose a Restaurant from the Directory, and get a new YixinCustomerRole from it
		for(Role r : _roles)
		{
			if(r instanceof RestaurantCustomerRole)
			{
				RestaurantCustomerRole restaurantCustomerRole = (RestaurantCustomerRole)r;
				restaurantCustomerRole.cmdGotHungry();
				setNextRole(restaurantCustomerRole);
				return true;
			}
		}
		// note: we only get here if no YixinCustomerRole was found in _roles
		List<Restaurant> restaurants = Directory.restaurants();
		for(Restaurant r : restaurants)
		{
			RestaurantCustomerRole ycr = r.generateCustomerRole(this);
			ycr.cmdGotHungry();
			setNextRole(ycr);
			_roles.add(ycr);
			return true;
		}
		return false;
	}
}
