package city.transportation;

import java.util.Random;

import agent.Role;
import city.PersonAgent;
import city.Place;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role {
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public Place _destination;
	public Place _currentPlace;
	CarObject _car = new CarObject();
	
	//Probably won't need -> not 100% sure though
	enum carState{noCar, hasCar, usingCar};
	carState _cState = carState.noCar; 
	
	enum travelState{choosing, choseCar, driving, choseWalking, walking, choseBus, ridingBus, atDestination, done};
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
	
	public void msgAtDestination(Place place){
		
	}
	
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction() {
		if(_destination == _currentPlace){
			return true;
		}
		else{
			if(_tState == travelState.choosing){
				chooseTransportation();
				return true;
			}
			
			if(_tState == travelState.choseWalking){
				actWalking();
				return true;
			}
			if(_tState == travelState.choseBus){
				actRidingBus();
				return true;
			}
			if(_tState == travelState.choseCar){
				actDriving();
				return true;
			}
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------Actions----------------------------------------
	public void chooseTransportation(){
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
		
	}
	
	public void actRidingBus(){
		_tState = travelState.ridingBus;
	}

	public void actDriving(){
		_tState = travelState.driving;
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
