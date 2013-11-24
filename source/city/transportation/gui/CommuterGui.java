package city.transportation.gui;

import java.awt.Color;
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
	boolean tFlag = false;
	boolean isPresent = true;
	
	CommuterRole _commuter;
	
	enum TransportationType{walking, driving, ridingBus, none};
	TransportationType _transportationType = TransportationType.none;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place startingPlace) {
		System.out.println("Created CommuterGui");
		_xPos = 300;
		_yPos = 0; 
		_commuter = commuter;
	}
	
	public double getDistanceToDestination(Place destination){
		double x, y;
		x = Math.abs(_xPos - destination.xPosition());
		y = Math.abs(_yPos - destination.yPosition());
		
		return x+y;
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		tFlag = true;
		System.out.println("destinationX: " + destination.xPosition());
		System.out.println("destinationY: " + destination.yPosition());
		_xDestination = destination.xPosition();
		_yDestination = destination.yPosition();
		_transportationType = TransportationType.walking;
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationType = TransportationType.ridingBus;
		tFlag = true;
	}
	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_transportationType = TransportationType.driving;
		tFlag = true;
	}
	
	
	//at destination message
	public void atDestination(){
		_commuter.msgAtDestination(_destination);
		_transportationType = TransportationType.none;
		tFlag = false;
		setPresent(false);
	}
	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
		_xPos = busstop.xPosition();
		_yPos = busstop.yPosition();
		setPresent(true);
	}
	
	public int getX(){
		return _xPos;
	}
	
	public int getY(){
		return _yPos;
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
		
		if(_xPos == _xDestination && _yPos == _yDestination && tFlag){
			atDestination();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 5, 5);
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
}
