package city;

import gui.Lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.bank.Bank;
import city.home.ApartmentBuilding;
import city.home.House;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStopObject;

public class Directory {
	// -------------------------------------- DATA ----------------------------------------------
	
	// Add location data for each place somehow.
	
	private static List<Place> _places = Collections.synchronizedList(new ArrayList<Place>());
	private static List<PersonAgent> _personAgents = Collections.synchronizedList(new ArrayList<PersonAgent>());
	private static ArrayList<Lane> lanes = new ArrayList<Lane>();
	private static ArrayList<Semaphore> intersections = new ArrayList<Semaphore>();
	//Bus Fare (initialize in constructor?)
	private static double _busFare = 2;
	
	private static double _openingTime = 0; // These are set to 0 and 24 for testing
	private static double _closingTime = 24;

	// ------------------------------------ PROPERTIES ---------------------------------------
	public static double openingTime() { return _openingTime; }
	public static double closingTime() { return _closingTime; }
	
	public static ArrayList<Lane> lanes(){
		return lanes;
	}
	
	public static ArrayList<Semaphore> intersections(){
		return intersections;
	}
	
	public static void addLanes(Lane lane){
		lanes.add(lane);
	}
	
	public static void addIntersections(Semaphore sem){
		intersections.add(sem);
	}
	
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
				Distance2 = Math.abs(xStart - p.positionX()) + Math.abs(yStart - p.positionY());	
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
				Distance2 = Math.abs(destination.positionX() - p.positionX()) + Math.abs(destination.positionY() - p.positionY());	
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
