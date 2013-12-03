package city.restaurant.eric;

import java.util.Random;

import city.restaurant.eric.gui.CustomerGui;
import agent.Agent;
import city.restaurant.eric.interfaces.*;

public class CustomerAgent extends Agent implements Customer
{
	// ------------------------------------- DATA ------------------------------------------
	
	// Personal data:
	private String _name;
	private double _money;
	private int _hungerLevel = 0; // determines length of meal (in seconds)
	private Menu _menu;
	private String _choice;
	private Check _check;
	
	// Correspondence:
	private CustomerGui _gui;
	private Host _host; // as of v2, this is only used to tell the host I'm Hungry
	private Waiter _waiter;
	private Cashier _cashier;
	
	// Agent data:
	// State:
	public enum CustomerState { IDLE, WAITING_TO_BE_SEATED, GOING_TO_CASHIER_TO_PAY_DEBT, WAITING_FOR_CHANGE_DEBT, GOING_TO_FRONT_DESK, AT_FRONT_DESK, GOING_TO_TABLE, SEATED, READY_TO_ORDER, WAITING_FOR_FOOD, EATING, WAITING_FOR_CHECK, GOING_TO_CASHIER, WAITING_FOR_CHANGE, WALKING_OUT }
	private CustomerState _state = CustomerState.IDLE; //The start state
	// Event:
	public enum CustomerEvent { NONE, GOT_HUNGRY, TOLD_RESTAURANT_IS_FULL, TOLD_TO_PAY_DEBT, TOLD_TO_LEAVE, TOLD_TO_GO_TO_FRONT_DESK, TOLD_TO_FOLLOW_WAITER, GOT_TO_TABLE, OUT_OF_CHOICE, WAITER_ASKED_FOR_ORDER, GOT_FOOD, FINISHED_EATING, GOT_CHECK, GOT_TO_CASHIER, RECEIVED_CHANGE, DONE_LEAVING }
	private CustomerEvent _event = CustomerEvent.NONE;
	
	// ---------------------------------------- CONSTRUCTOR --------------------------------
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public CustomerAgent(String name)
	{
		_name = name;
		
		_money = 20.00;
		if(name.contains("Money"))
		{
			if(name.contains("Money12"))
			{
				_money = 12.00;
			}
			else if(name.contains("Money10"))
			{
				_money = 10.00;
			}
			else if(name.contains("Money5"))
			{
				_money = 5.00;
			}
			else if(name.contains("Money0"))
			{
				_money = 0.00;
			}
			print("HACKER [money = " + _money + "]");
		}
	}

	// ---------------------------------------- PROPERTIES ----------------------------------------

	public String getName() { return _name; }
	public int getHungerLevel() { return _hungerLevel; }
	public void setHungerLevel(int hungerLevel)
	{
		this._hungerLevel = hungerLevel;
	}
	public String toString() { return "customer " + getName(); }
	public void setGui(CustomerGui g) { _gui = g; }
	public CustomerGui gui() { return _gui; }
	public void setHost(Host host) { _host = host; }
	//public String getCustomerName() { return _name; }
	
	
	
	// ------------------------------------- MESSAGES ------------------------------------

	public void msgGotHungry() // from CustomerGui
	{
		print("I'm hungry");
		_hungerLevel = 10;
		_event = CustomerEvent.GOT_HUNGRY;
		stateChanged();
	}
	
	public void msgRestaurantIsFull() // from Host
	{
		_event = CustomerEvent.TOLD_RESTAURANT_IS_FULL;
		stateChanged();
	}
	
	public void msgGoToCashierAndPayDebt(Cashier cashier) // from Host
	{
		_cashier = cashier;
		_event = CustomerEvent.TOLD_TO_PAY_DEBT;
		stateChanged();
	}
	
	public void msgWeWontServeYou() // from Host
	{
		_event = CustomerEvent.TOLD_TO_LEAVE;
		stateChanged();
	}
	
	public void msgComeToFrontDesk() // from Host
	{
		_event = CustomerEvent.TOLD_TO_GO_TO_FRONT_DESK;
		stateChanged();
	}
	
	public void msgReachedFrontDesk()
	{
		_state = CustomerState.AT_FRONT_DESK;
		stateChanged();
	}
	
	public void msgFollowMeToTable(Waiter sender, Menu menu) // from Waiter
	{
		_waiter = sender; // since this is the first time we get correspondence from Waiter
		_menu = menu;
		print("Received msgFollowMeToTable");
		_event = CustomerEvent.TOLD_TO_FOLLOW_WAITER;
		stateChanged();
	}
	
	public void msgReachedTable() // from CustomerGui // TODO change this to be the generic ReachedDestination
	{
		_event = CustomerEvent.GOT_TO_TABLE;
		stateChanged();
	}
	
	public void msgWhatDoYouWant() // from Waiter
	{
		print("I will decide what to pick");
		_event = CustomerEvent.WAITER_ASKED_FOR_ORDER;
		stateChanged();
	}
	
	public void msgOutOfChoice(Menu m)
	{
		_menu = m;
		_event = CustomerEvent.OUT_OF_CHOICE;
		stateChanged();
	}

	public void msgHeresYourFood(String choice)
	{
		if(_choice.equals(choice))
		{
			print("I just got my " + _choice);
			_event = CustomerEvent.GOT_FOOD;
		}
		else
		{
			print("I didn't order " + choice + "!");
			// possibly enter another state; this should technically never happen.
		}
		stateChanged();
	}

	public void msgFinishedEating() // from CustomerGui
	{
		print("Done eating");
		_event = CustomerEvent.FINISHED_EATING;
		stateChanged();
	}
	
	public void msgHeresYourCheck(Check check, Cashier cashier)
	{
		print("Got check.");
		_check = check;
		_cashier = cashier;
		_event = CustomerEvent.GOT_CHECK;
		stateChanged();
	}

	public void msgReachedCashier() // from CustomerGui // TODO change this to be the generic ReachedDestination
	{
		print("Reached cashier.");
		_event = CustomerEvent.GOT_TO_CASHIER;
		stateChanged();
	}
	
	public void msgHereIsChange(double change)
	{
		_money += change;
		print("Received change. I now have $" + _money);
		_event = CustomerEvent.RECEIVED_CHANGE;
		stateChanged();
	}
	
	public void msgFinishedLeavingRestaurant() // from CustomerGui // TODO change this to be the generic ReachedDestination
	{
		_event = CustomerEvent.DONE_LEAVING;
		stateChanged();
	}
	
	
	
	// ------------------------------------------- SCHEDULER -------------------------------------
	
	protected boolean pickAndExecuteAnAction()
	{
		// note: CustomerAgent is a finite state machine

		if (_state == CustomerState.IDLE && _event == CustomerEvent.GOT_HUNGRY ) {
			actGoToRestaurant();
			return true;
		}
		if (_state == CustomerState.WAITING_TO_BE_SEATED && _event == CustomerEvent.TOLD_RESTAURANT_IS_FULL ) {
			actDecideToLeaveOrNot();
			return true;
		}
		if (_state == CustomerState.WAITING_TO_BE_SEATED && _event == CustomerEvent.TOLD_TO_PAY_DEBT) {
			actGoToCashierToPayDebt();
			return true;
		}
		if (_state == CustomerState.GOING_TO_CASHIER_TO_PAY_DEBT && _event == CustomerEvent.GOT_TO_CASHIER) {
			actPayDebt();
			return true;
		}
		if (_state == CustomerState.WAITING_FOR_CHANGE_DEBT && _event == CustomerEvent.RECEIVED_CHANGE) {
			actGoToRestaurant();
			return true;
		}
		if (_state == CustomerState.WAITING_TO_BE_SEATED && _event == CustomerEvent.TOLD_TO_LEAVE) {
			actAngrilyLeave();
			return true;
		}
		if (_state == CustomerState.WAITING_TO_BE_SEATED && _event == CustomerEvent.TOLD_TO_GO_TO_FRONT_DESK ) {
			actGoToFrontDesk();
			return true;
		}
		if ((_state == CustomerState.AT_FRONT_DESK || _state == CustomerState.GOING_TO_FRONT_DESK) && _event == CustomerEvent.TOLD_TO_FOLLOW_WAITER ) {
			actGoToTable();
			return true;
		}
		if (_state == CustomerState.GOING_TO_TABLE && _event == CustomerEvent.GOT_TO_TABLE) {
			actSitDown();
			return true;
		}
		if (_state == CustomerState.SEATED && _menu != null) {
			actChooseOrder();
			return true;
		}
		if (_state == CustomerState.READY_TO_ORDER && _event == CustomerEvent.WAITER_ASKED_FOR_ORDER) {
			actGiveOrder();
			return true;
		}
		if (_state == CustomerState.WAITING_FOR_FOOD && _event == CustomerEvent.OUT_OF_CHOICE) {
			actChooseAgain();
			return true;
		}
		if (_state == CustomerState.WAITING_FOR_FOOD && _event == CustomerEvent.GOT_FOOD) {
			actEatFood();
			return true;
		}
		if (_state == CustomerState.EATING && _event == CustomerEvent.FINISHED_EATING) {
			actRequestCheck();
			return true;
		}
		if (_state == CustomerState.WAITING_FOR_CHECK && _event == CustomerEvent.GOT_CHECK) {
			actGoToCashier();
			return true;
		}
		if (_state == CustomerState.GOING_TO_CASHIER && _event == CustomerEvent.GOT_TO_CASHIER) {
			actPay();
			return true;
		}
		if (_state == CustomerState.WAITING_FOR_CHANGE && _event == CustomerEvent.RECEIVED_CHANGE) {
			actLeaveRestaurant();
			return true;
		}
		if (_state == CustomerState.WALKING_OUT && _event == CustomerEvent.DONE_LEAVING) {
			actLeftRestaurantReset();
			return true;
		}
		return false;
	}

	// ----------------------------------------------- ACTIONS ---------------------------------------------------

	private void actGoToRestaurant()
	{
		print("Going to restaurant");
		_state = CustomerState.WAITING_TO_BE_SEATED;
		_gui.doGoToWaiting();
		_host.msgIWantFood(this);
		_event = CustomerEvent.NONE;
	}
	
	private void actDecideToLeaveOrNot()
	{
		Random rand = new Random();
		if(rand.nextInt(2) == 0)
		{
			// Stay
			print("Restaurant is full but I'm going to wait.");
		}
		else
		{
			// Leave
			print("Restaurant is full. I'm leaving.");
			_host.msgLeaving(this);
			_gui.doLeaveRestaurant();
			_state = CustomerState.WALKING_OUT;
		}
		_event = CustomerEvent.NONE;
	}
	
	private void actGoToCashierToPayDebt()
	{
		print("Going to cashier to pay debt");
		_gui.doGoToCashier();
		_state = CustomerState.GOING_TO_CASHIER_TO_PAY_DEBT;
		_event = CustomerEvent.NONE;
	}

	private void actPayDebt()
	{
		print("Paying debt");
		_cashier.msgHereIsOwedMoney(this, _money);
		_money = 0;
		_state = CustomerState.WAITING_FOR_CHANGE_DEBT;
		_event = CustomerEvent.NONE;
	}

	private void actAngrilyLeave()
	{
		print("Angrily leaving restaurant");
		_gui.doLeaveRestaurant();
		_state = CustomerState.WALKING_OUT;
		_event = CustomerEvent.NONE;
	}
	
	private void actGoToFrontDesk()
	{
		print("Going to front desk to meet waiter");
		_gui.doGoToFrontDesk();
		_state = CustomerState.GOING_TO_FRONT_DESK;
	}

	private void actGoToTable()
	{
		print("Being seated. Going to table");
		_state = CustomerState.GOING_TO_TABLE;
		_gui.doGoToTable(-1); // -1 is the seat number, which is not implemented yet
		_event = CustomerEvent.NONE;
	}
	
	private void actSitDown()
	{
		print("Sitting down");
		
		_state = CustomerState.SEATED;
		_event = CustomerEvent.NONE;
	}
	
	/**
	 * Set _choice to a randomly chosen choice that is affordable
	 */
	private void actChooseOrder()
	{
		print("Choosing order");

		// Choose a random choice from the list of food choices and make sure customer can pay for it.
		Random rand = new Random();
		String choice = null; // using a local variable for choice to ensure that another thread won't screw up the below algorithm by overwriting.
		while(true)
		{
			if(_menu.entrees().size() > 0)
			{
				// Choose the random number to order
				int choiceNum = rand.nextInt(_menu.entrees().size());

				// Check if I have enough money
				if(_menu.entrees().get(choiceNum).price() <= _money)
				{
					// If I can afford it, set my choice to this first randomly chosen item and break out of the choosing loop
					choice = _menu.entrees().get(choiceNum).choice();
					break;
				}
				else
				{
					// Set whether or not this customer will be a flake and choose the choice that he can't afford
					boolean flake = rand.nextInt(4) == 0;
					if(_name.contains("Flake"))
					{
						print("HACKER [Flake]");
						flake = true;
					}
					if(flake)
					{
						choice = _menu.entrees().get(choiceNum).choice();
						print("Don't have enough money for " + choice + " but I'm ordering it anyway, hehe");
						break;
					}
					// If I can't afford it, remove that entree from the menu, and on the next cycle of this do-loop, if there's still more options in the menu, choose again.
					_menu.removeEntree(_menu.entrees().get(choiceNum).choice());
					continue;
				}
			}
			else // if there's no more choices in the menu, either because the waiter gave an empty menu or because I removed all the entrees because they were all too expensive
			{
				break;
			}
		}
		
		// Check if there's a hack in the customer's name and override the randomly chosen choice (only chooses a choice that's available i.e. on the menu)
		for(Menu.Item i : _menu.entrees())
		{
			if(_name.contains(i.choice()))
			{
				print("HACKER [" + i.choice() + "]");
				choice = i.choice();
				break;
			}
		}
		
		if(choice == null)
		{
			// note: Since the menu passed to the customer only includes items that are available (at least after the waiter goes to and returns from the cook to check availability), this handles the customer not having enough money as well as there being no choices in stock.
			print("No more affordable choices.");
			actLeaveRestaurant();
			return;
		}
		
		print("Chose to order " + choice);
		
		_choice = choice;
		_waiter.msgReadyToOrder(this);
		_state = CustomerState.READY_TO_ORDER;
		_event = CustomerEvent.NONE;
	}
	
	private void actGiveOrder()
	{
		print("Giving order of " + _choice + " to " + _waiter.getName());
		_gui.doOrderFood(_choice);
		_waiter.msgHeresMyChoice(this, _choice);
		_state = CustomerState.WAITING_FOR_FOOD;
		_event = CustomerEvent.NONE;
	}

	private void actChooseAgain()
	{
		print("Choosing again.");
		
		_state = CustomerState.SEATED;
		_event = CustomerEvent.NONE;
	}

	private void actEatFood()
	{
		print("Eating food");
		_gui.doEatFood(_hungerLevel, _choice);
		_state = CustomerState.EATING;
		_event = CustomerEvent.NONE;
	}
	
	private void actRequestCheck()
	{
		print("Asking for check");
		_waiter.msgGiveMeCheck(this);
		_state = CustomerState.WAITING_FOR_CHECK;
		_event = CustomerEvent.NONE;
	}
	
	private void actGoToCashier()
	{
		print("Going to cashier.");
		_state = CustomerState.GOING_TO_CASHIER;
		_gui.doGoToCashier();
		_event = CustomerEvent.NONE;
	}
	
	private void actPay()
	{
		print("Paying.");
		_cashier.msgHereIsMoney(this, _money, _check); // awkwardly giving cashier whole wallet
		_money = 0;
		_state = CustomerState.WAITING_FOR_CHANGE;
		_event = CustomerEvent.NONE;
	}

	private void actLeaveRestaurant()
	{
		print("Leaving restaurant");
		_waiter.msgLeaving(this);
		_gui.doLeaveRestaurant();
		_state = CustomerState.WALKING_OUT;
		_event = CustomerEvent.NONE;
	}
	
	private void actLeftRestaurantReset()
	{
		print("Left restaurant.");
		_state = CustomerState.IDLE;
		_event = CustomerEvent.NONE;
		
		// Check for hack to not reset money
		if(!_name.contains("NoResetMoney"))
		{
			_money = 20.00;
		}
		else
		{
			print("HACKER [not resetting money. _money = " + _money + "]");
		}
	}
}

