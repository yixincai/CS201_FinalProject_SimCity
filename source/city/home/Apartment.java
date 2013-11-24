package city.home;

import city.Place;

public class Apartment implements Home
{
	//private ApartmentGui _gui;
	//public void setGui(ApartmentGui gui) { _gui = gui; }
	
	private ApartmentBuilding _apartmentBuilding;
	
	private int _number = -1;
	
	// ---------------------------- CONSTRUCTOR & PROPERTIES ---------------------------
	public Apartment(ApartmentBuilding apartmentBuilding) {
		_apartmentBuilding = apartmentBuilding;
	}
	public void setNumber(int number) { _number = number; }
	public int number() { return _number; }
	@Override
	public Place place() { return _apartmentBuilding; }
	public ApartmentBuilding apartmentBuilding() { return _apartmentBuilding; }
}
