package city.transportation.gui;

import java.awt.Graphics2D;

import gui.Gui;
import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;

public class CommuterGui implements Gui {
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	
	CommuterRole _commuter;
	
	enum TransportationType{walking, driving, ridingBus, none};
	TransportationType _transportationType = TransportationType.none;
	
	//Constructor
	public CommuterGui(CommuterRole commuter, Place startingPlace) {
		_commuter = commuter;
	}
	
	
	
	//Walking gui
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		// set destination x & y to the destination that was passed in
		_transportationType = TransportationType.walking;
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationType = TransportationType.ridingBus;
	}
	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_transportationType = TransportationType.driving;
	}
	
	
	//at destination message
	public void atDestination(){
		_commuter.msgAtDestination(_destination);
		_transportationType = TransportationType.none;
	}
	
	public void getOnBus(){
		
	}
	
	public void getOffBus(){
		
	}
	
	
	//------------------------------------------Animation---------------------------------------
	@Override
	public void updatePosition() {
		if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
}
