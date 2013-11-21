package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.transportation.BusStop;

public class Directory {
	
	// Add location data for each place somehow.
	
	private static List<Place> _places = Collections.synchronizedList(new ArrayList<Place>());
	
	
	
	//TODO add accessor for the list; accessor will use a synchronized(_places) { } block
	
	public static void addPlace(Place place)
	{
		_places.add(place);
	}
	
	
	
	public static BusStop getNearestBusStop(Place fromPlace)
	{
		//TODO
		return null;
	}
}
