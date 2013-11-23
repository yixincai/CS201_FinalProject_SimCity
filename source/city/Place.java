package city;

import gui.WorldViewBuilding;

/**
 * This class embodies a logical location; it could be a restaurant, a bank, a market, a house, an apartment building, a city park, etc.
 * The Restaurant, Bank, Market, House, Apartment classes should extend this.
 */
public abstract class Place {
	public String _name;
	public String getName() { return _name; }
	public void setName(String name) { _name = name; } 
	WorldViewBuilding _worldViewBuilding;
	
	// ------------------------------------ CONSTRUCTOR -----------------------------------
	public Place(String name, WorldViewBuilding worldViewBuilding)
	{
		_name = name;
		_worldViewBuilding = worldViewBuilding;
	}
}
