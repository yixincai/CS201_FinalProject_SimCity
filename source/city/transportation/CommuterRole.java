package city.transportation;

import java.util.Random;

import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.CommuterRoleGui;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public Place _destination;
	public Place _currentPlace;
	CarObject _car = new CarObject();
	CommuterRoleGui gui = new CommuterRoleGui(this);
	
	//Probably won't need -> not 100% sure though
	enum carState{noCar, hasCar, usingCar};
	carState _cState = carState.noCar; 
	
	enum travelState{choosing, choseCar, driving, choseWalking, walking, choseBus, goingToBusStop, atBusStop, waitingAtBusStop, ridingBus, atDestination, done};
	travelState _tState = travelState.done;
	
	Random _generator = new Random();
	
	//----------------------------------------------Constructor----------------------------------------
	public CommuterRole(PersonAgent person, Place place){
		super(person);
		_currentPlace = place;
		// TODO Auto-generated constructor stub
	}
	
	//----------------------------------------------Command---------------------------------------------
	
	//----------------------------------------------Messages------------------------------------------
	public void msgGoToDestination(Place place){
		_destination = place;
		_tState = travelState.choosing;
	}
	
	public void msgAtBusStop(BusStop busstop){
		_currentPlace = busstop;
		_tState = travelState.atBusStop;
	}
	
	public void msgGetOnBus(int fare){
		
	}
	
	public void msgGetOffBus(){
		
	}
	
	public void msgAtDestination(Place place){
		_currentPlace = place;
		_tState = travelState.atDestination;
	}
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction() {
		if(_destination == _currentPlace && _tState == travelState.atDestination){
			actAtDestination();
			return true;
		}
		if(_destination != _currentPlace && _tState == travelState.atDestination){
			actChooseTransportation();
			return true;
		}

		if(_tState == travelState.choosing){
			actChooseTransportation();
			return true;
		}
		
		if(_tState == travelState.choseWalking){
			actWalking();
			return true;
		}
		if(_tState == travelState.choseBus){
			actGoToBusStop();
			return true;
		}
		if(_tState == travelState.atBusStop){
			actAtBusStop();
		}
		if(_tState == travelState.choseCar){
			actDriving();
			return true;
		}

		
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------Actions----------------------------------------
	public void actChooseTransportation(){
		int choice = 0;
		
		if(_car == null){
			choice = _generator.nextInt(3);
		}
		else if(_car != null){
			choice = _generator.nextInt(2);
		}
		
		if(choice == 0){
			_tState = travelState.choseWalking;
		}
		if(choice == 1){
			_tState = travelState.choseBus;
		}
		if(choice == 2){
			_tState = travelState.choseCar;
		}
		
	}
	
	public void actWalking(){
		_tState = travelState.walking;
		gui.walkToLocation(_destination);
	}
	
	public void actGoToBusStop(){
		BusStop busStop;

		_tState = travelState.goingToBusStop;
		busStop = Directory.getNearestBusStop(_currentPlace);
		gui.goToBusStop(busStop);
	}
	
	public void actAtBusStop(){
		BusStop busStop;

		_tState = travelState.waitingAtBusStop;
		busStop = Directory.getNearestBusStop(_currentPlace);
		
		busStop.addPerson(this);
	}
	
	public void actRidingBus(){
		_tState = travelState.ridingBus;
		
	}

	public void actDriving(){
		_tState = travelState.driving;
		gui.goToCar(_car, _destination);
	}
	
	public void actAtDestination(){
		_tState = travelState.done;
	}
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		//_person.atDestination(_currentPlace);
		active = false;
		
	}
	
	//----------------------------------------------Setter----------------------------------------
	public void setCar(CarObject car){_car = car;}
	
	public Place destination() { return _destination; }
	public void setDestination(Place place) { _destination = place; }
	
	public Place currentPlace() { return _currentPlace; }
	

}
