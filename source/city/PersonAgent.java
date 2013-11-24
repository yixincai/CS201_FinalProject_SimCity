package city;

import java.util.List;
import java.util.Random;

import city.bank.BankCustomerRole;
import city.home.HomeBuyingRole;
import city.bank.BankHostRole;
import city.bank.BankTellerRole;
import city.bank.Bank;
import city.home.HomeRole;
import city.market.Market;
import city.market.MarketCashierRole;
import city.market.MarketCustomerRole;
import city.market.MarketEmployeeRole;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.yixin.YixinCashierRole;
import city.restaurant.yixin.YixinCookRole;
import city.restaurant.yixin.YixinCustomerRole;
import city.restaurant.yixin.YixinHostRole;
import city.restaurant.yixin.YixinNormalWaiterRole;
import city.restaurant.yixin.YixinRestaurant;
import city.restaurant.yixin.YixinWaiterRole;
import city.restaurant.yixin.gui.YixinCashierGui;
import city.restaurant.yixin.gui.YixinWaiterGui;
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
	private boolean _sentCmdFinishAndLeave = false;
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole = null;
	private String _occupationType; // this should be "RestaurantCashier", "Cook", etc. (not specific to any restaurant type or instantiation)
	private Role _occupation;
	private double _occupationStartTime;
	private double _occupationEndTime;
	private boolean _weekday_notWeekend;
	private HomeRole _homeRole;
	private HomeBuyingRole _homeBuyingRole; // Will handle buying an apartment or house
	private int bankAccountNumber = -1;
	
	// State data:
	public double _money;
	enum WealthState { RICH, NORMAL, BROKE, POOR } // the word "deadbeat" courtesy of Wilczynski lol
	boolean _deadbeat = false; // if true, means this person doesn't pay loans
	enum NourishmentState { HUNGRY, FULL }
	enum LocationState { NONE, TRAVELING, HOME, WORK, BANK, MARKET, RESTAURANT }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		NourishmentState nourishment; //TODO implement value for hunger
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
	
	
	
	// -------------------------------- CONSTRUCTORS & PROPERTIES ------------------------------------
	public PersonAgent(String name) { _name = name; }
	public PersonAgent(String name, double money, String initOccupation) {_name = name; _money = money; setOccupation(initOccupation);}
	public String getName() { return _name; }
	public double money() { return _money; }
	public int getAccountNumber() { return bankAccountNumber; }
	public void setAccountNumber(int newAccntNum) { this.bankAccountNumber = newAccntNum;} 
	public void changeMoney(double delta) { _money += delta; }
	public void setCommuterRole(CommuterRole commuterRole) { _commuterRole = commuterRole; _currentRole = _commuterRole; _commuterRole.active = true; }
	public void acquireHome(String homeType)
	{
		
	}
	public void setOccupation(String occupationType) 
	{
		_occupationType = occupationType;
		switch(occupationType)
		{
			case "Waiter":
				_occupation = Directory.restaurants().get(0).generateWaiterRole();
				new YixinNormalWaiterRole(this, (YixinRestaurant)Directory.restaurants().get(0) ,this._name);
				YixinWaiterGui yixinWaiterGui = new YixinWaiterGui((YixinWaiterRole)_occupation, ((YixinRestaurant)_occupation.place()).Waiters.size());
				((YixinNormalWaiterRole)_occupation).setGui(yixinWaiterGui);
				((YixinRestaurant)_occupation.place()).getAnimationPanel().addGui(yixinWaiterGui);
				break;
			case "Restaurant Cashier":
				_occupation = new YixinCashierRole(this, (YixinRestaurant)Directory.restaurants().get(0) );
				YixinCashierGui yixinCashierGui = new YixinCashierGui((YixinCashierRole)_occupation);
				((YixinCashierRole)_occupation).setGui(yixinCashierGui);
				((YixinRestaurant)_occupation.place()).getAnimationPanel().addGui(yixinCashierGui);
				break;
			case "Cook":
				_occupation = new YixinCookRole(this, (YixinRestaurant)Directory.restaurants().get(0) );
				break;
			case "Restaurant Host":
				_occupation = new YixinHostRole(this, (YixinRestaurant)Directory.restaurants().get(0) , this._name);
				break;
			case "Bank Teller":
				_occupation = new BankTellerRole(this, (Bank)Directory.banks().get(0), 0); // fix
				break;
			case "Bank Host":
				//_occupation = new BankHostRole(this, (Bank)Directory.banks().get(0), ((Bank)Directory.banks().get(0)).getTellers());
				break;
			case "Market Cashier":
			//	_occupation = new MarketCashierRole(this, market);
				break;
			case "Market Employee":
			//	_occupation = new MarketEmployeeRole(this, market);
				break;
		}
		/*
		switch(occupationType)
		{
			case "Waiter":
				_occupation = new YixinNormalWaiterRole(this, (YixinRestaurant)Directory.restaurants().get(0) ,this._name);
				YixinWaiterGui yixinWaiterGui = new YixinWaiterGui((YixinWaiterRole)_occupation, ((YixinRestaurant)_occupation.place()).Waiters.size());
				((YixinNormalWaiterRole)_occupation).setGui(yixinWaiterGui);
				((YixinRestaurant)_occupation.place()).getAnimationPanel().addGui(yixinWaiterGui);
				break;
			case "Restaurant Cashier":
				_occupation = new YixinCashierRole(this, (YixinRestaurant)Directory.restaurants().get(0) );
				YixinCashierGui yixinCashierGui = new YixinCashierGui((YixinCashierRole)_occupation);
				((YixinCashierRole)_occupation).setGui(yixinCashierGui);
				((YixinRestaurant)_occupation.place()).getAnimationPanel().addGui(yixinCashierGui);
				break;
			case "Cook":
				_occupation = new YixinCookRole(this, (YixinRestaurant)Directory.restaurants().get(0) );
				break;
			case "Restaurant Host":
				_occupation = new YixinHostRole(this, (YixinRestaurant)Directory.restaurants().get(0) , this._name);
				break;
			case "Bank Teller":
				_occupation = new BankTellerRole(this, (Bank)Directory.banks().get(0));
				break;
			case "Bank Host":
				_occupation = new BankHostRole(this, (Bank)Directory.banks().get(0), ((Bank)Directory.banks().get(0)).getTellers());
				break;
			case "Market Cashier":
			//	_occupation = new MarketCashierRole(this, market);
				break;
			case "Market Employee":
			//	_occupation = new MarketEmployeeRole(this, market);
				break;
		}
		*/
	}
	/** Sets the days the person works. @param weekday_notWeekend True if working weekdays, false if working weekends. */
	public void setWorkDays(boolean weekday_notWeekend) {
		_weekday_notWeekend = weekday_notWeekend;
	}
	public HomeRole homeRole() { return _homeRole; }
	
	
	
	// --------------------------------------- SCHEDULER -------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
		// here, check for and do emergencies/important actions
		
		if(_currentRole.active)
		{
			//System.out.println("Current Role Active");
			// Finish role because you have to get to work:
			if(workingToday() && !_sentCmdFinishAndLeave)
			{
				if(_occupation != null)
				{
					if(_currentRole != _occupation)
					{
						if(timeToBeAtWork()) finishAndLeaveCurrentRole();
						return true;
					}
					else // i.e. if you're currently at your job and your shift just finished
					{
						if(!timeToBeAtWork()) finishAndLeaveCurrentRole();
						return true;
					}
				}
			}
			if(_currentRole == _homeRole && (_state.time() > 20 || _state.time() < 7))
			{
				if(_state.nourishment() == NourishmentState.HUNGRY)
				{
					if(!_homeRole.cooking())
					{
						_homeRole.cmdCookAndEatFood();
						return true;
					}
				}
				else
				{
					if(!_homeRole.sleeping())
					{
						_homeRole.cmdGoToBed();
						return true;
					}
				}
			}
			
			// Call current role's scheduler
			if(_currentRole.pickAndExecuteAnAction()) { 
				System.out.println("Current Role Scheduler called");
				return true; 
			}
		}
		else // i.e. _currentRole.active == false
		{
			System.out.println("Here");
			// note: if we get here, a role just finished leaving.
			_sentCmdFinishAndLeave = false;
			
			if(_currentRole == _commuterRole)
			{
				if(timeToBeAtWork()) setNextRole(_occupation); // do we need this?
				// commuter role must have just reached the destination
				_currentRole = _nextRole;
				_currentRole.active = true;
				return true;
			}
			else
			{
				// note: the program will only get to here if we just finished a role that is not transportation role.
				// Choose the next role to do.  Set _nextRole to the next role you will do, set _currentRole to _commuterRole
				
				if(_occupation != null && workingToday() && timeToBeAtWork()) //TODO add JoblessRole
				{
					setNextRole(_occupation);
					return true;
				}
				else if(_state.time() > 20 || _state.time() < 7) //could replace with variables for sleepTime and wakeTime
				{
					setNextRole(_homeRole);
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
							goToRestaurant();
						}
						else
						{
							if(_homeRole.haveFood())
							{
								_homeRole.cmdCookAndEatFood();
								setNextRole(_homeRole);
								return true;
							}
							else
							{
								goToMarket(3); // 3 meals
							}
						}
					}
				}
				
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
		print("*_____________________________________*");
	}
	
	
	
	// ------------------------------------------ UTILITIES -------------------------------------
	private boolean workingToday()
	{
		return ((_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && !_weekday_notWeekend) ||
				(!(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && _weekday_notWeekend);
	}
	private boolean timeToBeAtWork()
	{
		return _state.time() > Directory.openingTime() - .5 && // .5 is half an hour
				_state.time() < Directory.closingTime();
	}
	private void finishAndLeaveCurrentRole()
	{
		if(_currentRole == _commuterRole) setNextRole(_occupation);
		_sentCmdFinishAndLeave = true;
		_currentRole.cmdFinishAndLeave();
		stateChanged();
	}
	private void setNextRole(Role nextRole)
	{
		System.out.println("Role set");
		_nextRole = nextRole;
		_commuterRole.setDestination(nextRole.place());
		_commuterRole.msgGoToDestination(nextRole.place());
		_currentRole = _commuterRole;
		_currentRole.active = true;
		stateChanged();
	}
	private boolean goToMarket(int meals)
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
			if(r instanceof YixinCustomerRole)
			{
				YixinCustomerRole ycr = (YixinCustomerRole)r;
				ycr.cmdGotHungry();
				setNextRole(ycr);
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
