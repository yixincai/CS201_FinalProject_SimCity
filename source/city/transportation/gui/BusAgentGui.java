package city.transportation.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;

public class BusAgentGui implements Gui {
	
	int _xPos, _yPos;
	BusStopObject _busStop;
	int _xDestination, _yDestination;
	
	BusAgent _bus;
	private String currentStatus = "Bus";
	
	boolean moving;
	boolean isPresent;
	
	//-------------------------------Constructor-------------------------------
	public BusAgentGui(BusAgent bus, Place startingPlace) {
		System.out.println("Created CommuterGui");
		_xPos = 20;
		_yPos = 200; 
		_bus = bus;
		_busStop = null;
		isPresent = false;
		moving = false;
	}
	
	//-------------------------------GUI Calls------------------------------------
	public void goToBusStop(BusStopObject busstop){
		System.out.println("Going To Bus Stop");
		_busStop = busstop;
		_xDestination = busstop.positionX();
		_yDestination = busstop.positionY();
		moving = true;
	}
	
	public void atBusStop(){
		moving = false;
		_bus.msgAtDestination(_busStop);
	}
	
	//-------------------------------Animation Stuff-------------------------------
	public void updatePosition() {
		if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--;
		
		if(_xPos == _xDestination && _yPos == _yDestination && moving){
			atBusStop();
			_bus.releaseSem();
			moving = false;
		}
	}
	
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 30, 30);
			g.setColor(Color.BLUE);
			if(_xPos == 20){
				g.drawString(currentStatus, _xPos + 35, _yPos + 20);
			} else if(_xPos == 620) {
				g.drawString(currentStatus, _xPos - 25, _yPos + 15);
			} else if(_yPos == 20){
				g.drawString(currentStatus, _xPos + 5, _yPos + 40);
			} else if(_yPos == 320){
				g.drawString(currentStatus, _xPos + 5, _yPos - 5);
			}
		}
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
}
