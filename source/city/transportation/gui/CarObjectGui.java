package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Place;
import city.transportation.CarObject;
import gui.Gui;

public class CarObjectGui implements Gui {
	
	CarObject _car;
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	boolean isPresent;
	boolean driving = false;
	
	CarObjectGui(){
		
	}
	
	CarObjectGui(int x, int y){
		_xPos = x;
		_yPos = y;
	}
	
	public void goToDestination(int x, int y){
		driving = true;
		_xDestination = x;
		_yDestination = y;
	}
	
	public void goToDestination(Place destination){
		driving = true;
		_xDestination = destination.xPosition();
		_yDestination = destination.yPosition();
	}
	
	public void atDestination(){
		driving = false;
		_car.atDestination();
	}
	
	
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
		
		if(_xPos == _xDestination && _yPos == _yDestination && driving){
			atDestination();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.red);
			g.fillRect(_xPos, _yPos, 10, 10);
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

}
