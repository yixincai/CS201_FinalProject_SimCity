package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import city.Directory;
import city.Place;
import city.market.Market;
import city.restaurant.Restaurant;
import city.transportation.BusStopObject;
import city.transportation.TruckAgent;


public class TruckAgentGui {
	
	int _xPos, _yPos;
	Market _market;
	int _xDestination, _yDestination;
	
	TruckAgent _truck;
	
	boolean isPresent;
	boolean park;
	
	List<Restaurant> restaurants;
	
	TruckAgentGui(TruckAgent truck, Market market){
		_market = market;
		_truck = truck;
		isPresent = false;
		restaurants = Directory.restaurants();
	}
	
	public void goToMarket(Market market) {
		// TODO Auto-generated method stub
		isPresent = true;
		_xDestination = market.xPosition();
		_yDestination = market.yPosition();
	}

	public void goToMarketParkingLot(Market market) {
		// TODO Auto-generated method stub
		isPresent = true;
		park = true;
		_xDestination = market.xPosition();
		_yDestination = market.yPosition();
	}

	public void goToDock(Market market) {
		// TODO Auto-generated method stub
		isPresent = true;
		_xDestination = market.xPosition();
		_yDestination = market.yPosition();
	}

	public void goToDestination(Restaurant restaurant) {
		// TODO Auto-generated method stub
		isPresent = true;
		_xDestination = restaurant.xPosition();
		_yDestination = restaurant.yPosition();
		
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
		
		if(_xPos == _xDestination && _yPos == _yDestination && park){
			isPresent = false;
		}
	}
	
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 5, 5);
		}
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}

}
