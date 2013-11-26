package city.transportation;

import city.Place;
import city.transportation.gui.CarObjectGui;

public class CarObject {
	
	public Place location;
	
	CommuterRole _commuter;
	CarObjectGui _gui;
	
	public CarObject(CommuterRole commuter){
		_commuter = commuter;
	}
	
	public void setCarGui(CarObjectGui gui){
		_gui = gui;
	}
	
	public void goToDestination(Place destination){
		_gui.goToDestination(destination);
	}
	
	public void atDestination(){
		_commuter.msgAtDestination(null);
	}

}
