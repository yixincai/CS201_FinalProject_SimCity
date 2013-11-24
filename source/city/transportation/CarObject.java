package city.transportation;

import city.Place;

public class CarObject {
	
	public Place location;
	
	//Position
	int _xPos;
	int _yPos;
	
	public CarObject(){ }
	
	CarObject(int x, int y){
		_xPos = x;
		_yPos = y;
	}
	
	public void setPosition(int x, int y){
		_xPos = x;
		_yPos = y;
	}
	
	public int getXPosition(){
		return _xPos;
	}
	
	public int getYPosition(){
		return _yPos;
	}

}
