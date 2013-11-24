package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.bank.Bank;
import city.home.ApartmentBuilding;
import city.home.House;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStopObject;

public class Directory {
	
	// Add location data for each place somehow.
	
	private static List<Place> _places = Collections.synchronizedList(new ArrayList<Place>());
	private static List<PersonAgent> _personAgents = Collections.synchronizedList(new ArrayList<PersonAgent>());
	
	//Bus Fare (initialize in constructor?)
	private static double _busFare = 2;
	
	private static double _openingTime = 8;
	private static double _closingTime = 20;
	
	
	// ------------------------------------ PROPERTIES ---------------------------------------
	public static double openingTime() { return _openingTime; }
	public static double closingTime() { return _closingTime; }
	
	/** Returns a new list of all places */
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
	/** Returns a new list of places that are markets */
	public static List<Market> markets()
	{
		List<Market> newList = new ArrayList<Market>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Market) newList.add((Market)p);
			}
		}
		return newList;
	}
	/** Returns a new list of places that are restaurants */
	public static List<Restaurant> restaurants()
	{
		List<Restaurant> newList = new ArrayList<Restaurant>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Restaurant) newList.add((Restaurant)p);
			}
		}
		return newList;
	}
	/** Returns a new list of places that are banks */
	public static List<Bank> banks()
	{
		List<Bank> newList = new ArrayList<Bank>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof Bank) newList.add((Bank)p);
			}
		}
		return newList;
	}
	/** Returns a new list of places that are apartment buildings */
	public static List<ApartmentBuilding> apartmentBuildings()
	{
		List<ApartmentBuilding> newList = new ArrayList<ApartmentBuilding>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof ApartmentBuilding) newList.add((ApartmentBuilding)p);
			}
		}
		return newList;
	}
	/** Returns a new list of places that are houses */
	public static List<House> houses()
	{
		List<House> newList = new ArrayList<House>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof House) newList.add((House)p);
			}
		}
		return newList;
	}
	public static List<BusStopObject> busStops()
	{
		List<BusStopObject> busstoplist = new ArrayList<BusStopObject>();
		synchronized(_places)
		{
			for(Place p : _places)
			{
				if(p instanceof BusStopObject) busstoplist.add((BusStopObject)p);
			}
		}
		return busstoplist;
	}
	/** Returns a new list of every person */
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
	
	
	//Transportation Methods (Bus)
	public static double getBusFare(){
		return _busFare;
	}
	public static void setBusFare(double newFare){
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
