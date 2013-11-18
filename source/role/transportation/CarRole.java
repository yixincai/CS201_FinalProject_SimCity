package role.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;

public class CarRole {
	List<String> destinations;
	Dimension currentPos;
	PersonAgent person;
	Semaphore isMoving;
	CarGui gui;
	enum carState{notMoving, turnedOn, moving};
	carState cState = carState.notMoving;
	
	CarRole(PersonAgent person){
		this.person = person;
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
	    Gui.goToDestination(destination);
	    isMoving.acquire(); //release sent by gui
	    cState = carState.notMoving;
	    person.atDestination(destination);
	    destinations.remove(destination);
	}
	
}
