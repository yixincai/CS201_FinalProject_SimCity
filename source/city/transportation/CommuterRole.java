package city.transportation;

import java.util.Random;

import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.CommuterGui;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public PersonAgent _person;
	public Place _destination;
	public Place _currentPlace;
	CarObject _car = new CarObject();
	BusAgent _bus;
	int _fare;
	CommuterGui gui = new CommuterGui(this);
	
	//Probably won't need -> not 100% sure though
	enum carState{noCar, hasCar, usingCar};
	carState _cState = carState.noCar; 
	
	enum travelState{choosing, 
		choseCar, driving, 
		choseWalking, walking, 
		choseBus, goingToBusStop, atBusStop, waitingAtBusStop, busIsHere, ridingBus, busIsAtDestination, gettingOffBus,
			atDestination, done, none};
	travelState _tState = travelState.done;
	
	Random _generator = new Random();
	
	//----------------------------------------------Constructor----------------------------------------
	public CommuterRole(PersonAgent person, Place place){
		super(person);
		_person = person;
		_currentPlace = place;
		// TODO Auto-generated constructor stub
	}
	
	//----------------------------------------------Command---------------------------------------------
	
	//----------------------------------------------Messages------------------------------------------
	public void msgGoToDestination(Place place){ //Command to go to destination
		_tState = travelState.atBusStop;
		_destination = place;
	}
	
	//Bus Transportation messages
	public void msgAtBusStop(BusStop busstop){ //GUI message
		_tState = travelState.atBusStop;
		_currentPlace = busstop;
	}
	public void msgGetOnBus(int fare, BusAgent bus){
		_tState = travelState.busIsHere;
		_bus = bus;
		_fare = fare;
	}
	public void msgGetOffBus(Place place){
		_tState = travelState.busIsAtDestination;
		_currentPlace = place;
	}
	
	//Msg At Destination from GUI
	public void msgAtDestination(Place place){
		_tState = travelState.atDestination;
		_currentPlace = place;
	}
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction() {
		//At Destination
		if(_destination == _currentPlace && _tState == travelState.atDestination){
			actAtDestination();
			return true;
		}
		if(_destination != _currentPlace && _tState == travelState.atDestination){
			actChooseTransportation();
			return true;
		}
		
		//Choosing
		if(_tState == travelState.choosing){
			actChooseTransportation();
			return true;
		}
		
		//Walking
		if(_tState == travelState.choseWalking){
			actWalking();
			return true;
		}
		
		//Riding Bus
		if(_tState == travelState.choseBus){
			actGoToBusStop();
			return true;
		}
		if(_tState == travelState.atBusStop){
			actAtBusStop();
			return true;
		}
		if(_tState == travelState.busIsHere && _bus != null && _person._money >= _fare){
			actGetOnBus();
			return true;
		}
		if(_tState == travelState.busIsHere && _bus != null && _person._money <= _fare){
			actChooseNewTransportation();
			return true;
		}
		if(_tState == travelState.busIsAtDestination){
			actGetOffBus();
			return true;
		}
		
		//Driving
		if(_tState == travelState.choseCar){
			actDriving();
			return true;
		}

		
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------Actions----------------------------------------
	//Choosing
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
	public void actChooseNewTransportation(){ //Choosing when previous form of transportation doesn't work (Mostly for bus)
		_tState = travelState.none;
	}
	
	//Walking
	public void actWalking(){
		_tState = travelState.walking;
		gui.walkToLocation(_destination);
	}
	
	//Bus
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
	public void actGetOnBus(){
		_tState = travelState.ridingBus;
		_person._money -= _fare;
		gui.getOnBus();
		_bus.msgGettingOnBoard(this, _destination, _fare);
	}
	public void actGetOffBus(){
		_tState = travelState.gettingOffBus;
		gui.getOffBus();
		_bus.msgGotOff(this);
		_bus = null;
		actWalking(); //Calls this function here because after you get off of the bus stop you walk to the destination
	}

	//Driving
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
