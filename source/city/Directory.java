package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.bank.Bank;
import city.home.ApartmentBuilding;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStop;

public class Directory {
	
	// Add location data for each place somehow.
	
	private static List<Place> _places = Collections.synchronizedList(new ArrayList<Place>());
	private static List<PersonAgent> _personAgents = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	
	
	// ------------------------------------ PROPERTIES ---------------------------------------
	/** Makes a deep copy of the list of places and returns it */
	public static List<Place> places()
	{
		List<Place> newList = new ArrayList<Place>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				newList.add(p);
			}
		}
		return newList;
	}
	/** Returns a list of places that are markets */
	public static List<Place> markets()
	{
		List<Place> newList = new ArrayList<Place>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Market) newList.add(p);
			}
		}
		return newList;
	}
	/** Returns a list of places that are restaurants */
	public static List<Place> restaurants()
	{
		List<Place> newList = new ArrayList<Place>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Restaurant) newList.add(p);
			}
		}
		return newList;
	}
	/** Returns a list of places that are banks */
	public static List<Place> banks()
	{
		List<Place> newList = new ArrayList<Place>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Bank) newList.add(p);
			}
		}
		return newList;
	}
	/** Returns a list of places that are apartment buildings */
	public static List<Place> apartmentBuildings()
	{
		List<Place> newList = new ArrayList<Place>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof ApartmentBuilding) newList.add(p);
			}
		}
		return newList;
	}
	
	public static List<PersonAgent> personAgents()
	{
		List<PersonAgent> newList = new ArrayList<PersonAgent>();
		synchronized(_personAgents)
		{
			for(PersonAgent p : _personAgents)
			{
				newList.add(p);
			}
		}
		return newList;
	}
	
	
	
	public static void addPlace(Place place)
	{
		_places.add(place);
	}
	
	public static void addPerson(PersonAgent personAgent)
	{
		_personAgents.add(personAgent);
	}
	
	
	
	public static BusStop getNearestBusStop(Place fromPlace)
	{
		for(Place p : _places)
		{
			if(p instanceof BusStop)
			{
				
			}
		}
		return null;
	}
}
