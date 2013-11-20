package city.transportation;

public class CarObject {

	//Position
	int _xPos;
	int _yPos;
	
	CarObject(){ }
	
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
