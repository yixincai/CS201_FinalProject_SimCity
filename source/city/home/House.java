package city.home;

import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.PersonAgent;
import city.Place;
import city.home.gui.HouseAnimationPanel;
import city.home.gui.HouseOccupantGui;
import city.interfaces.PlaceWithAnimation;

public class House extends Place implements Home, PlaceWithAnimation {
	
	// ------------------------------------- DATA -----------------------------------
	HouseAnimationPanel _animationPanel;
	private Semaphore _occupiedSemaphore = new Semaphore(1, true);
	
	
	
	// ------------------------- CONSTRUCTOR & PROPERTIES -----------------------------
	public House(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super(name, wvb);
		_animationPanel = (HouseAnimationPanel)bp.getBuildingAnimation();
	}
	public Place place() { return this; } // required in Home interface
	public JPanel animationPanel() { return _animationPanel; }
	
	
	
	// --------------------------------- METHODS ---------------------------------------
	public HouseOccupantRole tryGenerateHomeOccupantRole(PersonAgent person)
	{
		if(_occupiedSemaphore.tryAcquire())
		{
			// possibly add a function to set the occupant of this House
			HouseOccupantRole newRole = new HouseOccupantRole(person, this);
			newRole.setGui(new HouseOccupantGui(newRole));
			_animationPanel.addGui(newRole.gui());
			return newRole;
		}
		else return null;
	}
}
