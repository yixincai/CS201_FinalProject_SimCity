package city.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Place;
import city.PersonAgent;
import city.transportation.gui.CarObjectGui;

public class CarRole {
	List<String> destinations;
	Dimension currentPos;
	CommuterRole commuter;
	Semaphore isMoving;
	CarObjectGui gui;
	enum carState{notMoving, turnedOn, moving};
	carState cState = carState.notMoving;
	
	CarRole(CommuterRole person){
		commuter = person;
	}
	
	public Place place() {
		//return _currentPlace; //TODO need to implement _currentPlace (in constructor, data member, etc) 
		return null;
	}
	
	public void msgGotInCar(PersonAgent person, String destination){
	    destinations.add(destination);
	    cState = cState.turnedOn;
	}
	public void msgAtDestination(){ //gui message
	    isMoving.release();
	}
	
	public boolean pickAndExecuteAnAction(){
		
		if(!destinations.isEmpty() && cState == carState.turnedOn){
			GoToDestination(destinations.get(0));
			return true;
		}
		
		return false;
	}
	
	public void GoToDestination(String destination){
	    cState = carState.moving;
	//    gui.goToDestination(destination);
	//    isMoving.acquire(); //release sent by gui
	    cState = carState.notMoving;
///	    commuter.atDestination(destination);
	    destinations.remove(destination);
	}
	
}
