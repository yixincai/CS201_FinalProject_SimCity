package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

import gui.*;
import gui.astar.*;
import city.*;
import city.transportation.*;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;


	int _xPos, _yPos;
	boolean _selected = false;
	int _currentBlockX = 0, _currentBlockY = 0;//TODO set the block positions used by cars
	int _destinationBlockX, _destinationBlockY; 
	List<Integer> route = new ArrayList<Integer>();
	List<Integer> intersections = new ArrayList<Integer>();
	Place _destination;
	int _xDestination, _yDestination;
	enum Command { none, waitForAnimation}
	Command _transportationMethod = Command.none;
	enum PedestrianState { none, waitForAnimation}
	PedestrianState _showPedestrian = PedestrianState.none;	
	boolean isPresent = true;
	private Semaphore _reachedDestination = new Semaphore(0, true);
	private Semaphore _delayForMoving = new Semaphore(0, true);
	private Timer _lookUpDelay = new Timer();
	private int parkingSpot = -1;
	private int startingSpot = -1;
	private int landingSpot = 0;	
	CommuterRole _commuter;
	public boolean dead = false;
	private Semaphore deathSem = new Semaphore(0, true);
	
	private ImageIcon b = new ImageIcon(this.getClass().getResource("/image/bank/Skull.png"));
	private Image skull = b.getImage();
	int xGap = 10;
	int yGap = 10;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace) {
		// Note: placeX and placeY can safely receive values of null
		Lane lane;
		if (commuter._person.money()>=200)
			lane = Directory.lanes().get(_currentBlockX + 3 * _currentBlockY);
		else
			lane = Directory.sidewalks().get(_currentBlockX + 3 * _currentBlockY);
		if (lane.isHorizontal){
			if (lane.xVelocity>0){
				_xDestination = lane.xOrigin;
				_yDestination = lane.yOrigin + 10;
			}
			else {
				_xDestination = lane.xOrigin + 10 * (lane.permits.size() - 1);
				_yDestination = lane.yOrigin - 10;
			}
		}
		else {
			if (lane.yVelocity>0){
				_yDestination = lane.yOrigin;
				_xDestination = lane.xOrigin - 10;
			}
			else{
				_yDestination = lane.yOrigin + 10 * (lane.permits.size() - 1);
				_xDestination = lane.xOrigin + 10;
			}
		}
		_xPos = _xDestination;
		_yPos = _yDestination;
		_commuter = commuter;
		//currentPosition = new Position(_xPos, _yPos);
	}

	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - _xPos);//currentPosition.getX());
		double y = Math.abs(placeY(destination) - _yPos);//currentPosition.getY());

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
		return _xPos;//currentPosition.getX();
	}

	public int getY(){
		return _yPos;//currentPosition.getY();
	}

	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		route.clear();
		intersections.clear();
		setPresent(true);
		_destinationBlockX = getBlockX(placeX(destination));
		_destinationBlockY = getBlockY(placeY(destination));
		if (_destinationBlockX == _currentBlockX && _destinationBlockY == _currentBlockY){
			setPresent(false);
			return;
		}
		route.add(_currentBlockX + 3 * _currentBlockY);
		if (_currentBlockY == 0){
			if ( _destinationBlockX > _currentBlockX){ //going right
				intersections.add(_currentBlockX + _currentBlockY);
				_currentBlockY++;
				route.add(_currentBlockX + 3 * _currentBlockY);
				while (_currentBlockX < _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY > _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}
			else{//going left or down
				while (_currentBlockX > _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY < _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}	
		}
		else if (_currentBlockY == 1){
			if ( _destinationBlockX > _currentBlockX){ //going right
				while (_currentBlockX < _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY > _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}
			else{//going left or up
				intersections.add(_currentBlockX + _currentBlockY);
				_currentBlockY--;
				route.add(_currentBlockX + 3 * _currentBlockY);
				while (_currentBlockX > _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY < _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}	
		}

		//TODO determine landing spot
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.sidewalks().get(route.get(i));
			int starting_position = 0;
			if (i == 0) {
				if (startingSpot > 0)
					starting_position = startingSpot;
			}
			for (int j=starting_position; j< lane.permits.size();j++){//TODO change size to the ending position and starting position
				while(!lane.permits.get(j).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 10);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
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
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//free parking spaces
				if (i != 0 && j == starting_position){
					Directory.intersections().get(intersections.get(i-1)).release();
				}
				//release the former spot
				if (j!=starting_position)
					lane.permits.get(j-1).release();
				//find parking spaces in last lane
				if (i == route.size() - 1){
					if (j == landingSpot){
						//move to the spot, release and record
						if (lane.isHorizontal){
							if (lane.xVelocity>0){
								_yDestination += 10;
							}
							else {
								_yDestination -=  10;
							}
						}
						else {
							if (lane.yVelocity>0){
								_xDestination -= 10;
							}
							else{
								_xDestination += 10;
							}
						}
						_transportationMethod = Command.waitForAnimation;
						waitForLaneToFinish();
						setPresent(false);
						lane.permits.get(j).release();
						startingSpot = j;
						return;
					}
				}
			}

			if (i<route.size() - 1){
				Lane next_lane = Directory.sidewalks().get(route.get(i+1));
				while(!Directory.intersections().get(intersections.get(i)).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 10);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				};
				if (next_lane.isHorizontal){
					if (next_lane.xVelocity>0){
						_xDestination = next_lane.xOrigin - 10;
						_yDestination = next_lane.yOrigin;
					}
					else {
						_xDestination = next_lane.xOrigin + 10 * next_lane.permits.size();
						_yDestination = next_lane.yOrigin;
					}
				}
				else {
					if (next_lane.yVelocity>0){
						_yDestination = next_lane.yOrigin - 10;
						_xDestination = next_lane.xOrigin;
					}
					else{
						_yDestination = next_lane.yOrigin + 10 * next_lane.permits.size();
						_xDestination = next_lane.xOrigin;
					}
				}
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
		}
		//set automatically
		_currentBlockX = _destinationBlockX;
		_currentBlockY = _destinationBlockY;	
	}

	/*	
	//Car gui
	public void goToCar(){
	}

	public void getOffCar(){
	}
	 */	

	public void driveToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		route.clear();
		intersections.clear();
		setPresent(true);
		_destinationBlockX = getBlockX(placeX(destination));
		_destinationBlockY = getBlockY(placeY(destination));
		if (_destinationBlockX == _currentBlockX && _destinationBlockY == _currentBlockY){
			return;
		}
		route.add(_currentBlockX + 3 * _currentBlockY);
		if (_currentBlockY == 0){
			if ( _destinationBlockX > _currentBlockX){ //going right
				intersections.add(_currentBlockX + _currentBlockY);
				_currentBlockY++;
				route.add(_currentBlockX + 3 * _currentBlockY);
				while (_currentBlockX < _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY > _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}
			else{//going left or down
				while (_currentBlockX > _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY < _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}	
		}
		else if (_currentBlockY == 1){
			if ( _destinationBlockX > _currentBlockX){ //going right
				while (_currentBlockX < _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY > _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}
			else{//going left or up
				intersections.add(_currentBlockX + _currentBlockY);
				_currentBlockY--;
				route.add(_currentBlockX + 3 * _currentBlockY);
				while (_currentBlockX > _destinationBlockX){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockX--;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
				if (_currentBlockY < _destinationBlockY){
					intersections.add(_currentBlockX + _currentBlockY);
					_currentBlockY++;
					route.add(_currentBlockX + 3 * _currentBlockY);
				}
			}	
		}
		//		route.add(3);
		//		intersections.add(1);
		//		route.add(4);
		//		intersections.add(2);
		//		route.add(5);
		//		intersections.add(3);
		//		route.add(2);
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.lanes().get(route.get(i));
			int starting_position = 0;
			if (i == 0) {
				if (parkingSpot > 0)
					starting_position = parkingSpot;
			}
			for (int j=starting_position; j< lane.permits.size();j++){//TODO change starting position
				while(!lane.permits.get(j).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 10);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
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
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//free parking spaces
				if (i == 0 && j == starting_position){
					lane.parking_spaces.get(j).release();
				}
				if (i != 0 && j == starting_position){
					Directory.intersections().get(intersections.get(i-1)).release();
				}
				//release the former spot
				if (j!=starting_position)
					lane.permits.get(j-1).release();
				//find parking spaces in last lane
				if (i == route.size() - 1){
					if (lane.parking_spaces.get(j).tryAcquire()){
						//move to the spot, release and record
						if (lane.isHorizontal){
							if (lane.xVelocity>0){
								_yDestination += 10;
							}
							else {
								_yDestination -=  10;
							}
						}
						else {
							if (lane.yVelocity>0){
								_xDestination -= 10;
							}
							else{
								_xDestination += 10;
							}
						}
						_transportationMethod = Command.waitForAnimation;
						waitForLaneToFinish();
						lane.permits.get(j).release();
						parkingSpot = j;
						return;
					}
					else {
						//go ahead and try another
						if (j == lane.permits.size() - 1){
							setPresent(false);
							if (lane.isHorizontal){
								if (lane.xVelocity>0){
									_yDestination += 10;
								}
								else {
									_yDestination -=  10;
								}
							}
							else {
								if (lane.yVelocity>0){
									_xDestination -= 10;
								}
								else{
									_xDestination += 10;
								}
							}
							_xPos = _xDestination;
							_yPos = _yDestination;
							lane.permits.get(j).release();
						}
					}
				}
			}

			if (i<route.size() - 1){
				Lane next_lane = Directory.lanes().get(route.get(i+1));
				while(!Directory.intersections().get(intersections.get(i)).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 500);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				};
				if (_commuter.wantToDie && intersections.get(i) == 0){
					_xDestination = 1 * 10 + 41;
					_yDestination = 12 * 10 + 30;
					_transportationMethod = Command.waitForAnimation;
					waitForLaneToFinish();
					lane.permits.get(lane.permits.size()-1).release();
					Directory.intersections().get(intersections.get(i)).release();
					Directory.busStops().get(5).addMyselfToCrashList(_commuter);
					try{
						deathSem.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
					return;
				}
				if (next_lane.isHorizontal){
					if (next_lane.xVelocity>0){
						_xDestination = next_lane.xOrigin - 10;
						_yDestination = next_lane.yOrigin;
					}
					else {
						_xDestination = next_lane.xOrigin + 10 * next_lane.permits.size();
						_yDestination = next_lane.yOrigin;
					}
				}
				else {
					if (next_lane.yVelocity>0){
						_yDestination = next_lane.yOrigin - 10;
						_xDestination = next_lane.xOrigin;
					}
					else{
						_yDestination = next_lane.yOrigin + 10 * next_lane.permits.size();
						_xDestination = next_lane.xOrigin;
					}
				}
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
		}
		//TODO if we have parking area do not set present to false but show in parking lot
		//setPresent(false);
		//set automatically
		_currentBlockX = _destinationBlockX;
		_currentBlockY = _destinationBlockY;		
	}

	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		//TODO set position and destination
		route.clear();
		if (_currentBlockX == 0 && _currentBlockY == 0){
			_xPos = 41 + 15 * 10;
			_yPos = 30 + 5 * 10;
			route.add(0);
			route.add(1);
		}
		else if (_currentBlockX == 1 && _currentBlockY == 0){
			_xPos = 41 + 30 * 10;
			_yPos = 30 + 5 * 10;
			route.add(5);
		}
		else if (_currentBlockX == 2 && _currentBlockY == 0){
			_xPos = 41 + 44 * 10;
			_yPos = 30 + 5 * 10;
			route.add(6);
			route.add(7);
		}
		if (_currentBlockX == 0 && _currentBlockY == 1){
			_xPos = 41 + 15 * 10;
			_yPos = 30 + 24 * 10;
			route.add(16);
			route.add(17);
		}
		else if (_currentBlockX == 1 && _currentBlockY == 1){
			_xPos = 41 + 30 * 10;
			_yPos = 30 + 24 * 10;
			route.add(15);
		}
		else if (_currentBlockX == 2 && _currentBlockY == 1){
			_xPos = 41 + 44 * 10;
			_yPos = 30 + 24 * 10;
			route.add(10);
			route.add(11);
		}
		_xDestination = _xPos;
		_yDestination = _yPos;
		setPresent(true);
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.busSidewalks().get(route.get(i));
			for (int j=0; j< lane.permits.size();j++){//TODO change starting position
				while(!lane.permits.get(j).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 10);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
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
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//release the former spot in current lane
				if (j!=0)
					lane.permits.get(j-1).release();
				//release the former spot in last lane
				else if (i != 0){
					ArrayList<Semaphore> former_lane_permits = Directory.busSidewalks().get(route.get(i-1)).permits;
					former_lane_permits.get(former_lane_permits.size() - 1).release();
				}
				//find parking spaces in last lane
				if (i == route.size() - 1 && j == lane.permits.size() - 1){
					//move to the spot, release and record
					if (lane.isHorizontal){
						if (lane.xVelocity>0){
							_xDestination += 10;
						}
						else {
							_xDestination -=  10;
						}
					}
					else {
						if (lane.yVelocity>0){
							_yDestination += 10;
						}
						else{
							_yDestination -= 10;
						}
					}
					_transportationMethod = Command.waitForAnimation;
					waitForLaneToFinish();
					lane.permits.get(j).release();
					if (!_commuter.wantToDie)
						setPresent(false);
					return;
				}
			}
		}
	}

	public void goDie(){
		setPresent(true);
		if (_currentBlockX == 0 && _currentBlockY == 0){
			_yDestination -= 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_xDestination += _commuter.deadListNumber * 10;
		}
		else if (_currentBlockX == 1 && _currentBlockY == 0){
			_yDestination -= 10;
			_xDestination += 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_xDestination += _commuter.deadListNumber * 10;
		}
		else if (_currentBlockX == 2 && _currentBlockY == 0){
			_xDestination += 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_yDestination += _commuter.deadListNumber * 10;
		}
		if (_currentBlockX == 0 && _currentBlockY == 1){
			_xDestination -= 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_yDestination -= _commuter.deadListNumber * 10;
		}
		else if (_currentBlockX == 1 && _currentBlockY == 1){
			_xDestination -= 10;
			_yDestination += 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_xDestination -= _commuter.deadListNumber * 10;
		}
		else if (_currentBlockX == 2 && _currentBlockY == 1){
			_yDestination += 10;
			_transportationMethod = Command.waitForAnimation;
			waitForLaneToFinish();
			_xDestination -= _commuter.deadListNumber * 10;
		}
		_transportationMethod = Command.waitForAnimation;
		waitForLaneToFinish();
	}

	public void getOnBus(){
		setPresent(false);
	}

	public void getOffBus(BusStopObject busstop){
		if (busstop.positionX() < 41 + 20 * 10 && busstop.positionY() < 30 + 14 * 10){
			_currentBlockX = 0;
			_currentBlockY = 0;
		}
		if (busstop.positionX() > 41 + 20 * 10 && busstop.positionX() < 41 + 40 * 10 && busstop.positionY() < 30 + 14 * 10){
			_currentBlockX = 1;
			_currentBlockY = 0;
		}
		if (busstop.positionX() > 41 + 40 * 10 && busstop.positionY() < 30 + 14 * 10){
			_currentBlockX = 2;
			_currentBlockY = 0;
		}
		if (busstop.positionX() < 41 + 20 * 10 && busstop.positionY() > 30 + 14 * 10){
			_currentBlockX = 0;
			_currentBlockY = 1;
		}
		if (busstop.positionX() > 41 + 20 * 10 && busstop.positionX() < 41 + 40 * 10 && busstop.positionY() > 30 + 14 * 10){
			_currentBlockX = 1;
			_currentBlockY = 1;
		}
		if (busstop.positionX() > 41 + 40 * 10 && busstop.positionY() > 30 + 14 * 10){
			_currentBlockX = 2;
			_currentBlockY = 1;
		}
		route.clear();
		if (_currentBlockX == 0 && _currentBlockY == 0){
			_xPos = 41 + 2 * 10;
			_yPos = 30 + 2 * 10;
			route.add(2);
			route.add(3);
		}
		else if (_currentBlockX == 1 && _currentBlockY == 0){
			_xPos = 41 + 29 * 10;
			_yPos = 30 + 1 * 10;
			route.add(4);
		}
		else if (_currentBlockX == 2 && _currentBlockY == 0){
			_xPos = 41 + 57 * 10;
			_yPos = 30 + 2 * 10;
			route.add(8);
			route.add(9);
		}
		if (_currentBlockX == 0 && _currentBlockY == 1){
			_xPos = 41 + 2 * 10;
			_yPos = 30 + 27 * 10;
			route.add(18);
			route.add(19);
		}
		else if (_currentBlockX == 1 && _currentBlockY == 1){
			_xPos = 41 + 29 * 10;
			_yPos = 30 + 28 * 10;
			route.add(14);
		}
		else if (_currentBlockX == 2 && _currentBlockY == 1){
			_xPos = 41 + 57 * 10;
			_yPos = 30 + 27 * 10;
			route.add(12);
			route.add(13);
		}
		_xDestination = _xPos;
		_yDestination = _yPos;
		setPresent(true);
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.busSidewalks().get(route.get(i));
			for (int j=0; j< lane.permits.size();j++){//TODO change starting position
				while(!lane.permits.get(j).tryAcquire()){
					_lookUpDelay.schedule(new TimerTask(){
						@Override
						public void run() {
							_delayForMoving.release();
						}
					}, 10);

					try{
						_delayForMoving.acquire();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
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
				_transportationMethod = Command.waitForAnimation;
				waitForLaneToFinish();
				//release the former spot in current lane
				if (j!=0)
					lane.permits.get(j-1).release();
				//release the former spot in last lane
				else if (i != 0){
					ArrayList<Semaphore> former_lane_permits = Directory.busSidewalks().get(route.get(i-1)).permits;
					former_lane_permits.get(former_lane_permits.size() - 1).release();
				}
				//find parking spaces in last lane
				if (i == route.size() - 1 && j == lane.permits.size() - 1){
					//move to the spot, release and record
					if (lane.isHorizontal){
						if (lane.xVelocity>0){
							_xDestination += 10;
						}
						else {
							_xDestination -=  10;
						}
					}
					else {
						if (lane.yVelocity>0){
							_yDestination += 10;
						}
						else{
							_yDestination -= 10;
						}
					}
					_transportationMethod = Command.waitForAnimation;
					waitForLaneToFinish();
					lane.permits.get(j).release();
					//TODO maybe change this
					setPresent(false);
					if (_currentBlockX == 0 && _currentBlockY == 0){
						_xPos = 41 + 0 * 10;
						_yPos = 30 + 0 * 10;
					}
					else if (_currentBlockX == 1 && _currentBlockY == 0){
						_xPos = 41 + 29 * 10;
						_yPos = 30 + 0 * 10;
					}
					else if (_currentBlockX == 2 && _currentBlockY == 0){
						_xPos = 41 + 58 * 10;
						_yPos = 30 + 0 * 10;
					}
					if (_currentBlockX == 0 && _currentBlockY == 1){
						_xPos = 41 + 0 * 10;
						_yPos = 30 + 14 * 10;
					}
					else if (_currentBlockX == 1 && _currentBlockY == 1){
						_xPos = 41 + 29 * 10;
						_yPos = 30 + 14 * 10;
					}
					else if (_currentBlockX == 2 && _currentBlockY == 1){
						_xPos = 41 + 58 * 10;
						_yPos = 30 + 14 * 10;
					}
					_xDestination = _xPos;
					_yDestination = _yPos;
					return;
				}
			}
		}
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

		if (_commuter.hasCar()){
			if (_xPos < _xDestination)
				_xPos++;
			else if (_xPos > _xDestination)
				_xPos--;

			if (_yPos < _yDestination)
				_yPos++;
			else if (_yPos > _yDestination)
				_yPos--;
		}

		if(_xPos == _xDestination &&  _yPos == _yDestination &&
				(_transportationMethod == Command.waitForAnimation)){
			_transportationMethod = Command.none;
			releaseSemaphore();
		}
	}

	public void move( int xv, int yv ) {
		_xPos+=xv;
		_yPos+=yv;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			if(_selected){
				g.setColor(Color.RED);
				g.fillRect(_xPos - 2, _yPos - 2, xGap + 4, yGap + 4);
			}
			
			if (dead){
				g.drawImage(skull,_xPos,_yPos, xGap, yGap, null);
				return;
			}
			if(_commuter.hasCar()){
				g.setColor(Color.ORANGE);
				g.fillRect(_xPos, _yPos, 10, 10);
			}
			else{
				g.setColor(Color.magenta);
				g.fillRect(_xPos, _yPos, 10, 10);
			}
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

	/*	void move(int destx, int desty){
		_xDestination = destx;
		_yDestination = desty;
	} */


	Position convertPixelToGridSpace(int x, int y){
		return new Position((x-41)/10, (y-30)/10);
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
		if (yPos >= 30 + 5 * 10 && yPos < 30 + 9 * 10)
			return 0;
		if (yPos >= 30 + 19 * 10 && yPos < 30 + 25 * 10)
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
	
	public void setSelected(boolean selected){
		_selected = selected;
	}
}
