package city.home;

import gui.WorldViewBuilding;
import city.Place;

public class House extends Place implements Home {
	
	// ------------------------- CONSTRUCTOR & PROPERTIES -----------------------------
	public House(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}
	public Place place() {
		return this;
	}
	
}
