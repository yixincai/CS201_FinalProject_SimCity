package city.home.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.concurrent.Semaphore;

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
	protected Image _picture = null;
	
	protected Semaphore _finishedAction = new Semaphore(0, true);
	protected boolean _doingAction = false;
	
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
	protected abstract int chairX();
	protected abstract int chairY();
	
	protected Image getImage(){
		return _role.getImage();
	}
	
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
	public void doCookAndEatFood() {
		_doingAction = true;
		_destinationX = kitchenX() + 40;
		_destinationY = kitchenY() + 30;
		waitForActionToFinish();
		_doingAction = true;
		_destinationX = kitchenX();
		waitForActionToFinish();
		_doingAction = true;
		_destinationX = kitchenX();
		waitForActionToFinish();
		_doingAction = true;
		_destinationX = chairX();
		_destinationY = chairY();
		waitForActionToFinish();
		// TODO add a kitchen table, possibly add a string for which food he has
		_role.msgReachedDestination();
	}
	public void doWatchTv() {
		doGoIdle();
	}
	public void doGoToBed() {
		_destinationX = bedX();
		_destinationY = bedY();
		_goingSomewhere = true;
	}
	public void doWakeUp() {
		doGoIdle();
	}
	public void doLeaveHome() {
		_destinationX = frontDoorX();
		_destinationY = frontDoorY();
		_goingSomewhere = true;
	}
	
	
	
	// ------------------------------------ ANIMATION ----------------------------------------
	@Override
	public void updatePosition() {
		//System.out.println("HomeOccupantGui.updatePosition() \n"
		//		+ "   _positionX: " + _positionX + "   _destinationX: " + _destinationX + "\n"
		//		+ "   _positionY: " + _positionY + "   _destinationY: " + _destinationY);
		
		// Update position
		if (_positionX < _destinationX) _positionX++;
		else if (_positionX > _destinationX) _positionX--;

		if (_positionY < _destinationY) _positionY++;
		else if (_positionY > _destinationY) _positionY--;
		
		// Check if reached destination
		if(_positionX == _destinationX && _positionY == _destinationY)
		{
			if(_goingSomewhere)
			{
				_goingSomewhere = false;
				_role.msgReachedDestination();
			}
			if(_doingAction)
			{
				_doingAction = false;
				_finishedAction.release();
			}
			if(_destinationX == frontDoorX() && _destinationY == frontDoorY())
			{
				setPresent(false);
			}
		}
	}
	@Override
	public void draw(Graphics2D g) {
		if(_isPresent){
			_picture = _role.getImage();
			g.drawImage(_picture, _positionX, _positionY, 20, 27, null);
			
		}
	}
	
	
	
	// ----------------------------------- UTILITIES ---------------------------------------------
	protected void waitForActionToFinish()
	{
		try { _finishedAction.acquire(); }
		catch (InterruptedException e) { e.printStackTrace(); }
	}
}
