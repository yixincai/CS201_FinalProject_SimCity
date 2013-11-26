package city.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.Place;
import city.home.gui.ApartmentAnimationPanel;
import city.home.gui.HouseAnimationPanel;
import city.interfaces.PlaceWithAnimation;

public class ApartmentBuilding extends Place implements PlaceWithAnimation {
	
	// ---------------------------------------- DATA ----------------------------------
	private List<Apartment> _apartments = new ArrayList<Apartment>();
	private ApartmentAnimationPanel _animationPanel;
	private LandlordRole _landlord;
	private static Semaphore _landlordSemaphore = new Semaphore(1, true);

	// ---------------------------- CONSTRUCTOR & PROPERTIES ------------------------------
	public ApartmentBuilding(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel bp) {
		super(name, worldViewBuilding);
		for(int floorNumber = 0; floorNumber < 1; floorNumber++) // starting out with only one floor, which is floor 0.
		{
			for(int roomNumber = 0; roomNumber < 4; roomNumber++) // starting out with a small number of rooms so we can fit them on the animation
			{
				Apartment a = new Apartment(this, floorNumber*100 + roomNumber);
				_apartments.add(a);
			}
		}
		_animationPanel = (ApartmentAnimationPanel)bp.getBuildingAnimation();
		_landlord = new LandlordRole(null);
	}
	/** Returns a new list of the apartments.  Makes and populates a new list every time it is called. */
	public List<Apartment> apartments()
	{
		List<Apartment> newList = new ArrayList<Apartment>();
		for(Apartment a : _apartments) { newList.add(a); }
		return newList;
	}
	public LandlordRole landlord() { return _landlord; }
	public ApartmentAnimationPanel animationPanel() { return _animationPanel; }
	public LandlordRole tryAcquireLandlordRole() {
		if(_landlordSemaphore.tryAcquire()) {
			return _landlord;
		}
		else return null;
	}
}
