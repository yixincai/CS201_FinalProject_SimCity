package city.transportation.interfaces;

import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CommuterRole;

public interface Bus {
	
	public abstract void msgAtDestination(BusStopObject busstop);

	public abstract void msgGotOff(CommuterRole passenger);

	public abstract void msgGettingOnBoard(CommuterRole person, Place destination, int payment);
	
	public abstract void setFare(int fare);
	
	public abstract String getName();
}
