package city.transportation.gui;

import java.awt.Graphics2D;

import gui.Gui;
import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;

public class CommuterRoleGui implements Gui {
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	
	CommuterRole _commuter;
	
	enum transportUsed{walking, driving, ridingBus, none};
	transportUsed tUsed = transportUsed.none;
	
	//Constructor
	public CommuterRoleGui(CommuterRole commuter){
		_commuter = commuter;
	}
	
	//Walking gui
	public void walkToLocation(Place destination){
		tUsed = transportUsed.walking;
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		tUsed = transportUsed.ridingBus;
	}
	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		tUsed = transportUsed.driving;
	}
	
	
	//at destination message
	public void atDestination(){
		
		_commuter.msgAtDestination(_destination);
		tUsed = transportUsed.none;
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
