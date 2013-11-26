package city.transportation.mock;

import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CommuterRole;
import city.transportation.interfaces.Bus;
import city.transportation.interfaces.Commuter;

public class MockBus extends Mock implements Bus{

	public MockBus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgAtDestination(BusStopObject busstop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFare(int fare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGotOff(Commuter passenger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGettingOnBoard(Commuter person, Place destination,
			double _fare) {
		// TODO Auto-generated method stub
		
	}

}
