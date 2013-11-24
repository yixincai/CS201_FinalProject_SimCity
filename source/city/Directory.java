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
	private static List<PersonAgent> _personAgents = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	//Bus Fare (initialize in constructor?)
	private static double _busFare = 2;
	
	
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
	
	
	//Transportation Methods
	public static double getFare(){
		return _busFare;
	}
	public static void setFare(double newFare){
		_busFare = newFare;
	}
	
	public static BusStopObject getNearestBusStop(int xStart, int yStart)
	{
		BusStopObject temp = null;
		int Distance1 = -1;
		int Distance2;
		
		for(Place p : _places)
		{
			if(p instanceof BusStopObject)
			{
				Distance2 = Math.abs(xStart - p.xPosition()) + Math.abs(yStart - p.yPosition());	
				if(Distance1 < 0){
					Distance1 = Distance2;
					temp = (BusStopObject)p;
				}
				else if(Distance2 < Distance1){
					Distance1 = Distance2;
					temp = (BusStopObject)p;
				}
			}
		}
		return temp;
	}
	
	public static BusStopObject getNearestBusStopToDestination(Place destination)
	{
		BusStopObject temp = null;
		int Distance1 = -1;
		int Distance2;
		
		for(Place p : _places)
		{
			if(p instanceof BusStopObject)
			{
				Distance2 = Math.abs(destination.xPosition() - p.xPosition()) + Math.abs(destination.yPosition() - p.yPosition());	
				if(Distance1 < 0){
					Distance1 = Distance2;
					temp = (BusStopObject)p;
				}
				else if(Distance2 < Distance1){
					Distance1 = Distance2;
					temp = (BusStopObject)p;
				}
			}
		}
		return temp;
	}
}
