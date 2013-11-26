package city.home.gui;

import java.awt.Color;
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
	protected boolean _isPresent = false;
	
	private int _positionX = 0;
	private int _positionY = 0;
	
	private int _destinationX = 0;
	private int _destinationY = 0;
	
	
	
	// -------------------------- CONSTRUCTOR & PROPERTIES -------------------------------
	public HomeOccupantGui(HomeOccupantRole role) { _role = role; }
	
	// For these functions, return the coordinates of each respective place, depending on whether it's a house or apartment, and if apartment, which one within the building.  (get the coordinates from HomeAnimationPanel or ApartmentAnimationPanel)
	protected abstract int bedX();
	protected abstract int bedY();
	protected abstract int kitchenX();
	protected abstract int kitchenY();
	protected abstract int idleX();
	protected abstract int idleY();
	protected abstract int frontDoorX();
	protected abstract int frontDoorY();

	@Override
	public boolean isPresent() {
		return _isPresent;
	}
	
	public void setPresent(boolean present){
		this._isPresent = present;
	}
	
	
	
	// ----------------------------------- COMMANDS ------------------------------------------
	// (from HomeOccupantRole)
	public void doGoIdle() {
		_destinationX = idleX();
		_destinationY = idleY();
		_goingSomewhere = false;
	}
	public void doGotHome() {
		_positionX = frontDoorX();
		_positionY = frontDoorY();
		setPresent(true);
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
		// Update position
		if (_positionX < _destinationX) _positionX++;
		else if (_positionX > _destinationX) _positionX--;

		if (_positionY < _destinationY) _positionY++;
		else if (_positionY > _destinationY) _positionY--;
		
		// Check if reached destination
		if(_positionX == _destinationX && _positionY == _destinationY && _goingSomewhere){
			_goingSomewhere = false;
			setPresent(false);
			_role.msgReachedDestination();
		}
		// TODO if(reached destination && destination is front door) { setPresent(false); } (also see restaurant.gui.WaiterGui)
	}
	@Override
	public void draw(Graphics2D g) {
		if(_isPresent){
			g.setColor(Color.GREEN);
			g.fillRect(_positionX, _positionY, 20, 20);
		}
	}
}
