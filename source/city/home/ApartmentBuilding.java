package city.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import gui.WorldViewBuilding;
import city.Place;

public class ApartmentBuilding extends Place {
	
	// ---------------------------------------- DATA ----------------------------------
	private List<Apartment> _apartments = new ArrayList<Apartment>();
	
	private static Semaphore _landlordSemaphore = new Semaphore(1, true); //TODO make the tryacquire thingy

	// ---------------------------- CONSTRUCTOR & PROPERTIES ------------------------------
	public ApartmentBuilding(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
		for(int floorNumber = 0; floorNumber < 1; floorNumber++) // starting out with only one floor, which is floor 0.
		{
			for(int roomNumber = 0; roomNumber < 4; roomNumber++) // starting out with a small number of rooms so we can fit them on the animation
			{
				Apartment a = new Apartment(this);
				a.setNumber(floorNumber*100 + roomNumber);
				_apartments.add(a);
			}
		}
	}
	/** Returns a new list of the apartments.  Makes and populates a new list every time it is called. */
	public List<Apartment> apartments()
	{
		List<Apartment> newList = new ArrayList<Apartment>();
		for(Apartment a : _apartments) { newList.add(a); }
		return newList;
	}
	
}
