package city.home.gui;

import java.awt.Graphics2D;

import gui.Gui;

/**
 * This class should implement all the home "animation stubs".
 * It is abstract just so that HouseOccupantGui and ApartmentOccupantGui can extend and define the x- and y-positions of things (through methods like bedX() and bedY())
 */
public abstract class HomeOccupantGui implements Gui {

	// For these functions, return the coordinates of each respective place, depending on whether it's a house or apartment, and if apartment, which one within the building.  (get the coordinates from HomeAnimationPanel or ApartmentAnimationPanel)
	protected abstract int bedX();
	protected abstract int bedY();
	protected abstract int kitchenX();
	protected abstract int kitchenY();
	protected abstract int idleX();
	protected abstract int idleY();

	public void doGoToKitchen() {
		// TODO Auto-generated method stub
		
	}

	public void doGoToBed() {
		// TODO Auto-generated method stub
		
	}

	public void doGoIdle() {
		// TODO Auto-generated method stub
		
	}

	public void doLeaveHome() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

}
