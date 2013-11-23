package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.bank.Bank;
import city.home.ApartmentBuilding;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStopObject;

public class Directory {
	
	// Add location data for each place somehow.
	
	private static List<Place> _places = Collections.synchronizedList(new ArrayList<Place>());
	
	
	
	// ------------------------------------ PROPERTIES ---------------------------------------
	/** Makes a deep copy of the list of places and returns it */
	public static List<Place> places()
	{
		List<Place> newList = new ArrayList<Place>();
		for(Place p : _places)
		{
			newList.add(p);
		}
		return newList;
	}
	/** Returns a list of places that are markets */
	public static List<Place> markets()
	{
		List<Place> newList = new ArrayList<Place>();
		for(Place p : _places)
		{
			if(p instanceof Market) newList.add(p);
		}
		return newList;
	}
	/** Returns a list of places that are restaurants */
	public static List<Place> restaurants()
	{
		List<Place> newList = new ArrayList<Place>();
		for(Place p : _places)
		{
			if(p instanceof Restaurant) newList.add(p);
		}
		return newList;
	}
	/** Returns a list of places that are banks */
	public static List<Place> banks()
	{
		List<Place> newList = new ArrayList<Place>();
		for(Place p : _places)
		{
			if(p instanceof Bank) newList.add(p);
		}
		return newList;
	}
	/** Returns a list of places that are apartment buildings */
	public static List<Place> apartmentBuildings()
	{
		List<Place> newList = new ArrayList<Place>();
		for(Place p : _places)
		{
			if(p instanceof ApartmentBuilding) newList.add(p);
		}
		return newList;
	}
	
	
	
	//TODO add accessor for the list; accessor will use a synchronized(_places) { } block
	
	public static void addPlace(Place place)
	{
		_places.add(place);
	}
	
	
	
	public static BusStopObject getNearestBusStop(Place fromPlace)
	{
		for(Place p : _places)
		{
			if(p instanceof BusStopObject)
			{
				
			}
		}
		return null;
	}
}
