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
	private static ArrayList<Lane> sidewalks = new ArrayList<Lane>();
	private static ArrayList<Lane> busSidewalks = new ArrayList<Lane>();	
	private static ArrayList<Semaphore> intersections = new ArrayList<Semaphore>();
	//Bus Fare (initialize in constructor?)
	private static double _busFare = 2;
	private static List<BusStopObject> _busStops = new ArrayList<BusStopObject>();
	private static List<Market> _markets = new ArrayList<Market>();
	private static List<Bank> _banks = new ArrayList<Bank>();
	private static List<Restaurant> _restaurants = new ArrayList<Restaurant>();
	private static List<House> _houses = new ArrayList<House>();
	private static List<ApartmentBuilding> _apartmentBuildings = new ArrayList<ApartmentBuilding>();
	
	public static final double OPENING_TIME = 8;
	public static final double CLOSING_TIME = 16;
	public static final double RESTAURANT_OPENING_TIME = 12;
	public static final double RESTAURANT_CLOSING_TIME = 23;

	// ------------------------------------ PROPERTIES ---------------------------------------
	public static boolean cityAtCapacity() { return _personAgents.size() >= ( houses().size() + (apartmentBuildings().size() * 4) ); } // if city is running slow, change this because houses() and apartmentBuildings() both iterate through _places every time they're called. Use a home counter that's incremented whenever a house or apartment building is created
	
	// WorldView stuff, like lanes and sidewalks and intersections
	public static ArrayList<Lane> lanes(){
		return lanes;
	}
	public static ArrayList<Lane> sidewalks(){
		return sidewalks;
	}
	public static ArrayList<Lane> busSidewalks(){
		return busSidewalks;
	}
	public static ArrayList<Semaphore> intersections(){
		return intersections;
	}
	public static void addLanes(Lane lane){
		lanes.add(lane);
	}
	public static void addSidewalk(Lane lane){
		sidewalks.add(lane);
	}
	public static void addBusSidewalk(Lane lane){
		busSidewalks.add(lane);
	}
	public static void addIntersections(Semaphore sem){
		intersections.add(sem);
	}
	
	/** Returns a new list of all places */
	public static List<Place> places()
	{
		synchronized(_places)
		{
			return new ArrayList<Place>(_places);
		}
	}
	/** Returns a new list of places that are markets */
	public static List<Market> markets()
	{
		synchronized(_markets)
		{
			return new ArrayList<Market>(_markets);
		}
	}
	/** Returns a new list of places that are restaurants */
	public static List<Restaurant> restaurants()
	{
		synchronized(_restaurants)
		{
			return new ArrayList<Restaurant>(_restaurants);
		}
	}
	/** Returns a new list of places that are banks */
	public static List<Bank> banks()
	{
		synchronized(_banks)
		{
			return new ArrayList<Bank>(_banks);
		}
	}
	/** Returns a new list of places that are apartment buildings */
	public static List<ApartmentBuilding> apartmentBuildings()
	{
		synchronized(_apartmentBuildings)
		{
			return new ArrayList<ApartmentBuilding>(_apartmentBuildings);
		}
	}
	/** Returns a new list of places that are houses */
	public static List<House> houses()
	{
		synchronized(_houses)
		{
			return new ArrayList<House>(_houses);
		}
	}
	public static List<BusStopObject> busStops()
	{
		synchronized(_busStops)
		{
			return new ArrayList<BusStopObject>(_busStops);
		}
	}
	/** Returns a new list of every person */
	public static List<PersonAgent> personAgents()
	{
		synchronized(_personAgents)
		{
			return new ArrayList<PersonAgent>(_personAgents);
		}
	}
	
	
	
	// This is how we used to do it.
	//	public static void addPlace(Place place)
	//	{
	//		_places.add(place);
	//	}
	
	public static void addBusStop(BusStopObject busStop)
	{
		_busStops.add(busStop);
		_places.add(busStop);
	}
	public static void addMarket(Market market)
	{
		_markets.add(market);
		_places.add(market);
	}
	public static void addBank(Bank bank)
	{
		_banks.add(bank);
		_places.add(bank);
	}
	public static void addRestaurant(Restaurant restaurant)
	{
		_restaurants.add(restaurant);
		_places.add(restaurant);
	}
	public static void addHouse(House house)
	{
		_houses.add(house);
		_places.add(house);
	}
	public static void addApartmentBuilding(ApartmentBuilding apartmentBuilding)
	{
		_apartmentBuildings.add(apartmentBuilding);
		_places.add(apartmentBuilding);
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
		List<BusStopObject> busStops = busStops();

		if (xStart < 41 + 20 * 10 && yStart < 30 + 14 * 10){
			return busStops.get(0);
		}
		if (xStart > 41 + 20 * 10 && xStart < 41 + 40 * 10 && yStart < 30 + 14 * 10){
			return busStops.get(1);
		}
		if (xStart > 41 + 40 * 10 && yStart < 30 + 14 * 10){
			return busStops.get(2);
		}
		if (xStart < 41 + 20 * 10 && yStart > 30 + 14 * 10){
			return busStops.get(5);
		}
		if (xStart > 41 + 20 * 10 && xStart < 41 + 40 * 10 && yStart > 30 + 14 * 10){
			return busStops.get(4);
		}
		if (xStart > 41 + 40 * 10 && yStart > 30 + 14 * 10){
			return busStops.get(3);
		}
		return busStops.get(0);
//		BusStopObject temp = null;
//		int Distance1 = -1;
//		int Distance2;
//		for(Place p : _places)
//		{
//			if(p instanceof BusStopObject)
//			{
//				Distance2 = Math.abs(xStart - p.positionX()) + Math.abs(yStart - p.positionY());	
//				if(Distance1 < 0){
//					Distance1 = Distance2;
//					temp = (BusStopObject)p;
//				}
//				else if(Distance2 < Distance1){
//					Distance1 = Distance2;
//					temp = (BusStopObject)p;
//				}
//			}
//		}
//		return temp;
	}
	
	public static BusStopObject getNearestBusStopToDestination(Place destination)
	{
		List<BusStopObject> busStops = busStops();

		if (destination.positionX() < 41 + 20 * 10 && destination.positionY() < 30 + 14 * 10){
			return busStops.get(0);
		}
		if (destination.positionX() > 41 + 20 * 10 && destination.positionX() < 41 + 40 * 10 && destination.positionY() < 30 + 14 * 10){
			return busStops.get(1);
		}
		if (destination.positionX() > 41 + 40 * 10 && destination.positionY() < 30 + 14 * 10){
			return busStops.get(2);
		}
		if (destination.positionX() < 41 + 20 * 10 && destination.positionY() > 30 + 14 * 10){
			return busStops.get(5);
		}
		if (destination.positionX() > 41 + 20 * 10 && destination.positionX() < 41 + 40 * 10 && destination.positionY() > 30 + 14 * 10){
			return busStops.get(4);
		}
		if (destination.positionX() > 41 + 40 * 10 && destination.positionY() > 30 + 14 * 10){
			return busStops.get(3);
		}
		return busStops.get(0);
//		BusStopObject temp = null;
//		int Distance1 = -1;
//		int Distance2;
//		
//		for(Place p : _places)
//		{
//			if(p instanceof BusStopObject)
//			{
//				Distance2 = Math.abs(destination.positionX() - p.positionX()) + Math.abs(destination.positionY() - p.positionY());	
//				if(Distance1 < 0){
//					Distance1 = Distance2;
//					temp = (BusStopObject)p;
//				}
//				else if(Distance2 < Distance1){
//					Distance1 = Distance2;
//					temp = (BusStopObject)p;
//				}
//			}
//		}
//		return temp;
	}
}
