package city.transportation.interfaces;

import gui.trace.AlertTag;
import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;

public interface Commuter {
	
	public void cmdGoToDestination(Place place);
	
	//Bus Transportation messages
	public void msgAtBusStop(BusStopObject busstop);
	
	public void msgGetOnBus(double fare, Bus bus);
	
	public void msgGetOffBus(Place place);
	
	//Msg At Destination from GUI
	public void msgAtDestination(Place place);
	
	public void msgYouAreAllowedToDie();

}
