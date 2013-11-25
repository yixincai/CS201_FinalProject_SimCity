package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import gui.Gui;
import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	boolean tFlag = false;
	boolean isPresent = true;
	
	CommuterRole _commuter;
	
	enum TransportationType{walking, driving, ridingBus, none};
	TransportationType _transportationType = TransportationType.none;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace) {
		System.out.println("Created CommuterGui");
		// Note: placeX and placeY can safely receive values of null
		_xPos = placeX(initialPlace);
		_yPos = placeY(initialPlace);
		_commuter = commuter;
	}
	
	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - _xPos);
		double y = Math.abs(placeY(destination) - _yPos);
		
		return x+y;
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		tFlag = true;
		System.out.println("destination X: " + placeX(destination));
		System.out.println("destination Y: " + placeY(destination));
		_xDestination = placeX(destination);
		_yDestination = placeY(destination);
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
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.xPosition();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.yPosition();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
}
