package city.transportation.mock;

import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;
import city.transportation.interfaces.Commuter;

public class MockCommuter extends Mock implements Commuter{

	public MockCommuter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGoToDestination(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBusStop(BusStopObject busstop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetOnBus(int fare, BusAgent bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetOffBus(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination(Place place) {
		// TODO Auto-generated method stub
		
	}

}
