package city.transportation.interfaces;

import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CommuterRole;

public interface Bus {
	public void msgAtDestination(BusStopObject busstop);

	public void msgGotOff(CommuterRole passenger);

	public void msgGettingOnBoard(CommuterRole person, Place destination, int payment);
}
