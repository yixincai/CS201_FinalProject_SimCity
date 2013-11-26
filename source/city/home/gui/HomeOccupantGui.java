package city.home.gui;

import java.awt.Graphics2D;

import city.home.HomeOccupantRole;
import gui.Gui;

/**
 * This class should implement all the home "animation stubs".
 * It is abstract just so that HouseOccupantGui and ApartmentOccupantGui can extend and define the x- and y-positions of things (through methods like bedX() and bedY())
 */
public abstract class HomeOccupantGui implements Gui {
	
	// ---------------------------------- DATA ------------------------------------------
	protected HomeOccupantRole _role;
	protected boolean _goingSomewhere = false;
	
	
	
	// -------------------------- CONSTRUCTOR & PROPERTIES -------------------------------
	public HomeOccupantGui(HomeOccupantRole role) { _role = role; }
	
	// For these functions, return the coordinates of each respective place, depending on whether it's a house or apartment, and if apartment, which one within the building.  (get the coordinates from HomeAnimationPanel or ApartmentAnimationPanel)
	protected abstract int bedX();
	protected abstract int bedY();
	protected abstract int kitchenX();
	protected abstract int kitchenY();
	protected abstract int idleX();
	protected abstract int idleY();
	
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	// ----------------------------------- COMMANDS ------------------------------------------
	// (from HomeOccupantRole)
	public void doGoIdle() {
		//TODO set destination
		_goingSomewhere = false;
	}
	public void doGotHome() {
		doGoIdle();
	}
	public void doGoToKitchen() {
		//TODO set destination
		_goingSomewhere = true;
	}
	public void doWatchTv() {
		doGoIdle();
	}
	public void doGoToBed() {
		//TODO set destination
		_goingSomewhere = true;
	}
	public void doWakeUp() {
		doGoIdle();
	}
	public void doLeaveHome() {
		//TODO set destination (make an allowance for calling setPresent(false) when you get there???)
		_goingSomewhere = true;
	}
	
	
	
	// ------------------------------------ METHODS ----------------------------------------
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		// TODO if(reached destination && _goingSomewhere) { _role.msgReachedDestination(); } (see restaurant.gui.WaiterGui)
		// TODO if(reached destination && destination is front door) { setPresent(false); } (also see restaurant.gui.WaiterGui)
	}
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}


}
