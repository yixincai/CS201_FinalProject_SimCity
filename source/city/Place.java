package city;

import gui.WorldViewBuilding;
import gui.BuildingInteriorAnimationPanel;

/**
 * This class embodies a logical location; it could be a restaurant, a bank, a market, a house, an apartment building, a city park, etc.
 * The Restaurant, Bank, Market, House, Apartment classes should extend this.
 */
public abstract class Place {
	
	public Place(String name)
	{
		_name = name;
	}
	public String _name;
	public String getName() { return _name; }
	public void setName(String name) { _name = name; } 
}
