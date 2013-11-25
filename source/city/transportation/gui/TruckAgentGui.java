package city.transportation.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import city.Directory;
import city.Place;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStopObject;
import city.transportation.TruckAgent;


public class TruckAgentGui implements Gui{
	
	int _xPos, _yPos;
	Market _market;
	int _xDestination, _yDestination;
	
	TruckAgent _truck;
	Restaurant _restaurant;
	
	boolean isPresent;
	boolean park;
	
	enum Point{Dock, PointA, PointB, Restaurant, PointC, PointD, PointE, Market, Park, none};
	Point _point = Point.Dock;
	
	List<Restaurant> restaurants;
	
	public TruckAgentGui(TruckAgent truck, Market market){
		_market = market;
		_xPos = _market.xPosition() - 10;
		_yPos = _market.yPosition();
		_xDestination = _market.xPosition() - 10;
		_yDestination = _market.yPosition();
		_truck = truck;
		isPresent = true;
		park = false;
		restaurants = Directory.restaurants();
	}
	
	public void goToMarket(Market market) { //Unused method clean it
		// TODO Auto-generated method stub
		isPresent = true;
		_point = Point.PointC;
		_xDestination = market.xPosition();
		_yDestination = market.yPosition();
	}

	public void goToMarketParkingLot(Market market) {
		// TODO Auto-generated method stub
		isPresent = true;
		park = true;
		_point = Point.PointC;
		_xDestination = _market.xPosition()- 35;
	}

	public void goToDockFromParkingLot(Market market) {
		// TODO Auto-generated method stub
		isPresent = true;
		_point = Point.Dock;
		_xDestination = market.xPosition()- 10;
		_yDestination = market.yPosition() + 15;
	}
	
	public void goToDock(Restaurant restaurant) {
		// TODO Auto-generated method stub
		isPresent = true;
		_point = Point.PointC;
		_xDestination = _market.xPosition()- 35;
	}

	public void goToDestination(Restaurant restaurant) {
		// TODO Auto-generated method stub
		isPresent = true;
		_restaurant = restaurant;
		_point = Point.PointA;
		_xDestination = _market.xPosition()- 35;
		_yDestination = _market.yPosition() + 15;
		
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
		
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.Dock){
			_point = Point.none;
			_truck.msgAtDestination();
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.PointA){
			_point = Point.PointB;
			_yDestination = _restaurant.yPosition() + 15;
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.PointB){
			_point = Point.Restaurant;
			if(_xPos < _restaurant.xPosition()){
				_xDestination = _restaurant.xPosition() - 10;
			}
			else{
				_xDestination = _restaurant.xPosition() + 40;
			}
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.Restaurant){
			_truck.msgAtDestination();
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.PointC){
			_point = Point.PointD;
			_yDestination = _market.yPosition() + 15;
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.PointD){
			_point = Point.PointE;
			_xDestination = _market.xPosition()- 10;
			_yDestination = _market.yPosition() + 15;
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.PointE){
			_point = Point.Market;
			//_truck.msgAtDestination();
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.Market && !park){
			_point = Point.none;
			_truck.msgAtDestination();
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.Market && park){
			_point = Point.Park;
			_xDestination = _market.xPosition()- 10;
			_yDestination = _market.yPosition();
			System.out.println("going to park");
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _point == Point.Park && park){
			//isPresent = false;
			_point = Point.none;
			park = false;
			_truck.msgAtDestination();
		}
	}
	
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.blue);
			g.fillRect(_xPos, _yPos, 10, 10);
		}
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}

}
