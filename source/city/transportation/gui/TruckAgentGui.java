package city.transportation.gui;

import gui.Gui;
import gui.Lane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.*;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.TruckAgent;


public class TruckAgentGui implements Gui{

	int _xPos, _yPos;
	Market _market;
	int _xDestination, _yDestination;
	int _currentBlockX, _currentBlockY;//TODO set the block positions used by cars
	int _destinationBlockX, _destinationBlockY; 

	TruckAgent _truck;
	Restaurant _restaurant;

	boolean isPresent;
	List<Integer> route = new ArrayList<Integer>();
	List<Integer> intersections = new ArrayList<Integer>();
	enum Point{moving, none};
	Point _point = Point.none;
	private Timer _lookUpDelay = new Timer();

	private Semaphore _reachedDestination = new Semaphore(0, true);
	private Semaphore _delayForMoving = new Semaphore(0, true);
	List<Restaurant> restaurants;
	private int parkingSpot = 3;

	public TruckAgentGui(TruckAgent truck, Market market){
		_market = market;
		_xPos = _market.positionX() - 30;
		_yPos = _market.positionY();
		_xDestination = _market.positionX() - 30;
		_yDestination = _market.positionY();
		_currentBlockX = getBlockX(market.positionX());
		_currentBlockY = getBlockY(market.positionY());
		_truck = truck;
		isPresent = true;
		restaurants = Directory.restaurants();
	}

	//	public void goToMarket(Market market) { //Unused method clean it
	//		// TODO Auto-generated method stub
	//		isPresent = true;
	//		_point = Point.PointC;
	//		_xDestination = market.positionX();
	//		_yDestination = market.positionY();
	//	}
	public void releaseSemaphore(){ _reachedDestination.release(); };

	public void goToMarketParkingLot(Market market) {
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		route.clear();
		intersections.clear();
		setPresent(true);
		_destinationBlockX = getBlockX(market.positionX());
		_destinationBlockY = getBlockY(market.positionY());
		if (_destinationBlockX == 1){
			if (_currentBlockX == 1 && _currentBlockY == 1){
				route.add(11);
				intersections.add(1);
				route.add(7);
			}
			else if (_currentBlockX == 1 && _currentBlockY == 0){
				route.add(1);
				intersections.add(1);
				route.add(7);
			}
			else if (_currentBlockX == 2 && _currentBlockY == 1){
				route.add(5);
				intersections.add(3);
				route.add(2);
				intersections.add(2);
				route.add(1);
				intersections.add(1);
				route.add(7);
			} 
		}
		else if (_destinationBlockX == 2){
			if (_currentBlockX == 1 && _currentBlockY == 1){
				route.add(12);
				intersections.add(7);
				route.add(13);
				intersections.add(2);
				route.add(9);
			}
			else if (_currentBlockX == 1 && _currentBlockY == 0){
				route.add(1);
				intersections.add(1);
				route.add(4);
				intersections.add(2);
				route.add(9);
			}
			else if (_currentBlockX == 2 && _currentBlockY == 1){
				route.add(13);
				intersections.add(2);
				route.add(9);
			} 
		}

		for (int i=0; i< route.size();i++){
			Lane lane = Directory.lanes().get(route.get(i));
			int starting_position = 0;
			if (i == 0) {
				if (parkingSpot > 0)
					starting_position = parkingSpot;
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
				_point = Point.moving;
				waitForLaneToFinish();
				//free parking spaces
				if (i == 0 && j == starting_position){
					lane.parking_spaces.get(j).release();
				}
				if (i != 0 && j == starting_position)
					Directory.intersections().get(i-1).release();
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
						_point = Point.moving;
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
									_xDestination += 10;
								}
								else{
									_xDestination -= 10;
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
				while(!Directory.intersections().get(i).tryAcquire()){
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
				_point = Point.moving;
				waitForLaneToFinish();
				//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
		}
		_currentBlockX = _destinationBlockX;
		_currentBlockY = _destinationBlockY;		
	}
	//
	//	public void goToDockFromParkingLot(Market market) {
	//		// TODO Auto-generated method stub
	//		isPresent = true;
	//		_point = Point.Dock;
	//		_xDestination = market.positionX()- 10;
	//		_yDestination = market.positionY() + 15;
	//	}
	//	
	//	public void goToDock(Restaurant restaurant) {
	//		// TODO Auto-generated method stub
	//		isPresent = true;
	//		_point = Point.PointC;
	//		_xDestination = _market.positionX()- 35;
	//	}

	public void goToDestination(Restaurant restaurant) {
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		route.clear();
		intersections.clear();
		setPresent(true);
		_destinationBlockX = getBlockX(restaurant.positionX());
		_destinationBlockY = getBlockY(restaurant.positionY());
		if (_currentBlockX == 1){
			route.add(7);
			intersections.add(4);
			route.add(6);
			if (_destinationBlockX == 1 && _destinationBlockY == 1){
				intersections.add(1);
				route.add(10);
				intersections.add(6);
				route.add(11);
			}
			else if (_destinationBlockX == 1 && _destinationBlockY == 0){
				intersections.add(1);
				route.add(4);
				intersections.add(2);
				route.add(1);
			}
			else if (_destinationBlockX == 2 && _destinationBlockY == 1){
				intersections.add(1);
				route.add(4);
				intersections.add(2);
				route.add(5);
			} 
		}
		else if (_currentBlockX == 2){
			route.add(9);
			intersections.add(5);
			route.add(8);
			if (_destinationBlockX == 1 && _destinationBlockY == 1){
				intersections.add(2);
				route.add(12);
			}
			else if (_destinationBlockX == 1 && _destinationBlockY == 0){
				intersections.add(2);
				route.add(1);
			}
			else if (_destinationBlockX == 2 && _destinationBlockY == 1){
				intersections.add(2);
				route.add(12);
				intersections.add(7);
				route.add(13);
			} 
		}
		_currentBlockX = _destinationBlockX;
		_currentBlockY = _destinationBlockY;
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.lanes().get(route.get(i));
			int starting_position = 0;
			if (i == 0) {
				if (parkingSpot > 0)
					starting_position = parkingSpot;
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
				_point = Point.moving;
				waitForLaneToFinish();
				//free parking spaces
				if (i == 0 && j == starting_position){
					lane.parking_spaces.get(j).release();
				}
				if (i != 0 && j == starting_position)
					Directory.intersections().get(i-1).release();
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
						_point = Point.moving;
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
									_xDestination += 10;
								}
								else{
									_xDestination -= 10;
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
				while(!Directory.intersections().get(i).tryAcquire()){
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
				_point = Point.moving;
				waitForLaneToFinish();
				//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
		}
	}

	//Animation
	public void updatePosition() {
		if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--;
		if(_xPos == _xDestination &&  _yPos == _yDestination &&
				_point == Point.moving){
			_point = Point.none;
			//setPresent(false);
			releaseSemaphore();
			//_commuter.msgReachedDestination();
		}
	}

	public void draw(Graphics2D g) {
		//		g.setColor(new Color(156, 93, 82));
		//		g.fillRect(_market.positionX() - 32, _market.positionY(), 12, 14);
		//		
		//		g.setColor(Color.black);
		//		g.setFont(new Font("default", Font.PLAIN, 10));
		//		g.drawString("Truck", _market.positionX() - 12, _market.positionY()-5);
		//		
		if(isPresent){
			g.setColor(Color.cyan);
			g.fillRect(_xPos, _yPos, 10, 10);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean present){
		this.isPresent = present;
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
}
