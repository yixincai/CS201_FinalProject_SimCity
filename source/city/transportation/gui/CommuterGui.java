package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

import gui.Gui;
import gui.Lane;
import city.Directory;
import city.Place;
import city.transportation.CommuterRole;
import city.transportation.BusStopObject;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	
	int _xPos, _yPos;
	int _currentBlockX = 0, _currentBlockY = 0;//TODO set the block positions used by cars
	int _destinationBlockX, _destinationBlockY; 
	List<Integer> route = new ArrayList<Integer>();
	List<Integer> intersections = new ArrayList<Integer>();
	Place _destination;
	int _xDestination, _yDestination;
	enum Command { none, walk, car}
	Command _transportationMethod = Command.none;
	boolean isPresent = true;
	private Semaphore _reachedDestination = new Semaphore(0, true);

	CommuterRole _commuter;
	
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
	
	public void releaseSemaphore(){ _reachedDestination.release(); };

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
	public void setXY(int x, int y){
		_xPos = x;
		_yPos = y;
	}
	
	public int getX(){
		return _xPos;
	}
	
	public int getY(){
		return _yPos;
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.walk;
		_xDestination = placeX(destination);
		_yDestination = placeY(destination);
	}
	
	public void driveToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		route.clear();
		setPresent(true);
//		_xDestination = placeX(destination);
//		_yDestination = placeY(destination);
		_destinationBlockX = getBlockX(_xDestination);
		_destinationBlockY = getBlockY(_yDestination);
//		while (_currentBlockX != _destinationBlockX){
//			if (_destinationBlockX > _currentBlockX){
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockX++;
//			}
//			else {
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockX--;
//			}
//		}
//		while (_currentBlockY != _destinationBlockY){
//			if (_destinationBlockY > _currentBlockY){
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockY++;
//			}
//			else {
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockY--;
//			}
//		}
		
		route.add(0);
		intersections.add(1);
		route.add(1);
		intersections.add(2);
		route.add(2);
		intersections.add(3);
		route.add(3);
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.lanes().get(route.get(i));
			if (i!=0)
				Directory.intersections().get(i-1).release();
			for (int j=0; j< lane.permits.size();j++){//TODO change size to the ending position and starting position
				while(!lane.permits.get(j).tryAcquire());
				//TODO change waiting to timer based
				if (lane.isHorizontal){
					if (lane.xVelocity>0){
						_xDestination = lane.xOrigin + 10 * lane.xVelocity * j;
						_yDestination = lane.yOrigin;
					}
					else {
						_xDestination = lane.xOrigin + 10 * lane.permits.size() + 10 * lane.xVelocity * (j+1);
						_yDestination = lane.yOrigin;
					}
				}
				else {
					if (lane.yVelocity>0){
						_yDestination = lane.yOrigin + 10 * lane.yVelocity * j;
						_xDestination = lane.xOrigin;
					}
					else{
						_yDestination = lane.yOrigin + 10 * lane.permits.size() + 10 * lane.yVelocity * (j+1);
						_xDestination = lane.xOrigin;
					}
				}
				_transportationMethod = Command.car;
				waitForLaneToFinish();
				if (j!=0)
					lane.permits.get(j-1).release();
			}
			
			if (i<route.size() - 1){
				Lane next_lane = Directory.lanes().get(route.get(i+1));
				while(!Directory.intersections().get(i).tryAcquire());
				if (next_lane.isHorizontal){
					if (next_lane.xVelocity>0){
						_xDestination = next_lane.xOrigin - 10;
					}
					else {
						_xDestination = next_lane.xOrigin + 10 * lane.permits.size();
					}
				}
				else {
					if (lane.yVelocity>0){
						_yDestination = lane.yOrigin - 10;
					}
					else{
						_yDestination = lane.yOrigin + 10 * lane.permits.size();
					}
				}
				_transportationMethod = Command.car;
				waitForLaneToFinish();
			//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
			lane.permits.get(lane.permits.size()-1).release();
		}
		setPresent(false);
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationMethod = Command.walk;
		_xDestination = busstop.positionX();
		_yDestination = busstop.positionY();
		setPresent(true);
	}
/*	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_goingSomewhere = true;
		_xDestination = car.getXPosition();
		_yDestination = car.getYPosition();
		setPresent(true);
	}
	
	public void atCar(){
		setPresent(false);
		_commuter.msgAtCar();
	}
	
*/	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
		_xPos = busstop.positionX();
		_yPos = busstop.positionY();
		setPresent(true);
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
		
		if (_transportationMethod == Command.car){
			if (_xPos < _xDestination)
				_xPos++;
			else if (_xPos > _xDestination)
				_xPos--;

			if (_yPos < _yDestination)
				_yPos++;
			else if (_yPos > _yDestination)
				_yPos--;
		}
		
		if(_xPos == _xDestination && _yPos == _yDestination &&
				(_transportationMethod == Command.car || _transportationMethod == Command.walk)){
			_transportationMethod = Command.none;
			//setPresent(false);
			releaseSemaphore();
			//_commuter.msgReachedDestination();
		}
	}
	
	public void move( int xv, int yv ) {
		_xPos+=xv;
		_yPos+=yv;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			if(_commuter.hasCar())
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 10, 10);
		}
	}
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.positionX();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.positionY();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
	
	private int getBlockX(int xPos){
		if (xPos >= 41 + 8 * 10 && xPos < 41 + 16 * 10)
			return 0;
		if (xPos >= 41 + 24 * 10 && xPos < 41 + 36 * 10)
			return 1;
		if (xPos >= 41 + 44 * 10 && xPos < 41 + 52 * 10)
			return 2;
		return -1;
	}
	
	private int getBlockY(int yPos){
		if (yPos >= 30 + 5 * 10 && yPos < 41 + 9 * 10)
			return 0;
		if (yPos >= 41 + 19 * 10 && yPos < 41 + 25 * 10)
			return 1;
		return -1;
	}
	private void waitForLaneToFinish() {
		try {
			_reachedDestination.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
