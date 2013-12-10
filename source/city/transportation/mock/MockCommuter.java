package city.transportation.mock;

import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;
import city.transportation.interfaces.Bus;
import city.transportation.interfaces.Commuter;

public class MockCommuter extends Mock implements Commuter{

	public MockCommuter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cmdGoToDestination(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBusStop(BusStopObject busstop) {
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

	@Override
	public void msgGetOnBus(double fare, Bus bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYouAreAllowedToDie() {
		// TODO Auto-generated method stub
		
	}

}
