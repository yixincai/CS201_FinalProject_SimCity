package city.restaurant.eric.gui;

import city.restaurant.eric.EricCustomerRole;
import gui.Gui;

import java.awt.*;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

// For timing eating food:
import java.util.Timer;
//import java.util.TimerTask; // not needed

public class EricCustomerGui implements Gui
{
	// ---------------------------------- DATA ----------------------------------
	
	// RestDims used for positions:
	private final RestDim FRONT_DESK = new RestDim(EricAnimationConstants.FRONTDESK_X, EricAnimationConstants.FRONTDESK_Y);
	private final RestDim OUTSIDE = new RestDim(EricAnimationConstants.OUTSIDE_X, EricAnimationConstants.OUTSIDE_Y);
	private RestDim WAITING_LOCATION;
	private RestDim _table = null;
	private RestDim _cashier = null;
	private RestDim _position;
	private RestDim _destination;

	// Correspondence:
	private EricCustomerRole _role;
	// RestaurantGui _restaurantGui; //TODO implement a system to correctly replace this
	
	// Status data:
	private boolean _isHungry = false;
	private String _food = null;


	private enum Commands { NONE, GO_TO_FRONT_DESK, GO_TO_TABLE, GO_TO_CASHIER, LEAVE_RESTAURANT };
	private Commands _command = Commands.NONE;
	
	// This is to make sure we have the location before we can go to the table or cashier. Kinda a hack.
	private Semaphore _tableLocationKnown = new Semaphore(0, true);
	private Semaphore _cashierLocationKnown = new Semaphore(0, true);
	
	// Timer for timing food-eating
	Timer _timer = new Timer();

    // -------------------------------- CONSTRUCTOR -----------------------------
	public EricCustomerGui(EricCustomerRole role) // , RestaurantGui gui)
	{
		_role = role;
		// _restaurantGui = gui; // this is for re-enabling the hunger checkbox
		
		WAITING_LOCATION = new RestDim(EricAnimationConstants.NEXT_CUSTOMER_X, EricAnimationConstants.NEXT_CUSTOMER_Y);
		EricAnimationConstants.updateNextCustomer();
		
		_position = new RestDim(OUTSIDE);
		_destination = new RestDim(FRONT_DESK);
	}
	
	// ------------------------------------ PROPERTIES/MESSAGES ---------------------------------
    // public void setRestGui(RestaurantGui rg) { _restaurantGui = rg; }
    public boolean atDestination() { return _position.equals(_destination); }
	public void tableIsHere(int x, int y) // from WaiterGui (hack)
	{
		_table = new RestDim(x, y);
		_tableLocationKnown.release();
	}
	public void cashierIsHere(int x, int y) // from WaiterGui (hack)
	{
		_cashier = new RestDim(x, y);
		_cashierLocationKnown.release();
	}
	public void setHungry()
	{
		_isHungry = true;
		_role.cmdGotHungry();
	}
	public boolean isPresent() { return _role.active; }
	public boolean isHungry() { return _isHungry; }
	private void setFood(String food, boolean ordered)
	{
    	if(food != null) {
    		_food = food.substring(0, ((food.length() > 1) ? 2 : 1) ) + (ordered ? "?" : "");
    	}
    	else { _food = food; }
	}
    
    
    
    // ------------------------------------- ACTIONS ----------------------------------

	public void doGoToWaiting() // from CustomerAgent
	{
		_destination.set(WAITING_LOCATION);
	}

	public void doGoToFrontDesk() // from CustomerAgent
	{
		_command = Commands.GO_TO_FRONT_DESK;
		_destination.set(FRONT_DESK);
	}

	public void doGoToTable(int seatNumber) // from CustomerAgent
	{
		// Make sure we have set the table location.
		if(_table == null)
		{
			try { _tableLocationKnown.acquire(); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		// when we implement different seats, insert code right here to pick one.
        
		_command = Commands.GO_TO_TABLE;
        _destination.set(_table);
	}
	
	public void doOrderFood(String food)
	{
		setFood(food, true);
	}
	
	/**
	 * Sets up a timer, then when the timer finishes, sends a message to the agent that eating food is done.
	 * @param hungerLevel The hunger level of the customer agent, which determines how long to eat.
	 */
	public void doEatFood(int hungerLevel, String food) // from CustomerAgent
	{
    	setFood(food, false);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not allow us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		_timer.schedule(
				new TimerTask()
				{
					public void run()
					{
						setFood(null, false);
						_role.msgFinishedEating();
					}
				},
				hungerLevel * 1000 // convert hungerLevel from s to ms
				);
	}
	
	public void doGoToCashier() // from CustomerAgent
	{
		// Make sure we have set the cashier location.
		if(_cashier == null)
		{
			try { _cashierLocationKnown.acquire(); } catch (InterruptedException e) { e.printStackTrace(); }
		}
        
		_command = Commands.GO_TO_CASHIER;
        _destination.set(_cashier);
	}

	public void doLeaveRestaurant() // from CustomerAgent
	{
		_command = Commands.LEAVE_RESTAURANT;
		_destination.set(OUTSIDE);
	}
	
	
	
	// -------------------------------------- METHODS -------------------------------------

	public void updatePosition()
	{
    	// To see if the position changed
    	// RestDim oldPosition = new RestDim(_position);
    	
        if (_position.x < _destination.x) _position.x++;
        else if (_position.x > _destination.x) _position.x--;

        if (_position.y < _destination.y) _position.y++;
        else if (_position.y > _destination.y) _position.y--;
        
		if (atDestination())
		{
			if (_command==Commands.GO_TO_FRONT_DESK)
			{
				_role.msgReachedFrontDesk();
			}
			if (_command==Commands.GO_TO_TABLE)
			{
				_role.msgReachedTable();
			}
			if (_command==Commands.GO_TO_CASHIER)
			{
				_role.msgReachedCashier();
			}
			if (_command==Commands.LEAVE_RESTAURANT)
			{
				_role.msgFinishedLeavingRestaurant();
				_isHungry = false;
				// _restaurantGui.setHungerCBEnabled(_agent);
			}
			_command = Commands.NONE;
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.GREEN);
		g.fillRect(_position.x, _position.y, EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.PERSON_HEIGHT);
        
        if(_food != null)
        {
        	g.setFont(EricAnimationConstants.FONT);
        	g.setColor(Color.BLACK);
        	g.drawString(_food,
        			_position.x + EricAnimationConstants.PERSON_WIDTH,
        			_position.y + EricAnimationConstants.PERSON_HEIGHT
        			);
        }
	}
}
