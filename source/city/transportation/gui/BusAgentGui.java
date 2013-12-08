package city.transportation.gui;

import gui.Gui;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;

public class BusAgentGui extends JPanel implements Gui {
	
	int _xPos, _yPos;
	BusStopObject _busStop;
	int _xDestination, _yDestination;
	
	BusAgent _bus;
	private String currentStatus = "Bus";
	
	boolean moving;
	boolean isPresent;
	
	enum BusDirection{BusUp, BusDown, BusRight, BusLeft, BusNone};
	BusDirection direction = BusDirection.BusNone;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/transportation/BusUp.png"));
    Image busUp = a.getImage();
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/transportation/BusDown.png"));
    Image busDown = b.getImage();
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/transportation/BusLeft.png"));
    Image busLeft = c.getImage();
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/transportation/BusRight.png"));
    Image busRight = d.getImage();
    
    int xBGap = 25;
    int yBGap = 48;
	
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
		AlertLog.getInstance().logMessage(AlertTag.BUS, "Bus" ,"Going To Bus Stop");
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
		if (_xPos < _xDestination){
			_xPos++;
			direction = BusDirection.BusRight;
		}
		else if (_xPos > _xDestination){
			_xPos--;
			direction = BusDirection.BusLeft;
		}
		
		if (_yPos < _yDestination){
			_yPos++;
			direction = BusDirection.BusDown;
		}
		else if (_yPos > _yDestination){
			_yPos--;
			direction = BusDirection.BusUp;
		}
		
		if(_xPos == _xDestination && _yPos == _yDestination && moving){
			atBusStop();
			_bus.releaseSem();
			moving = false;
		}
	}
	
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.BLUE);
			if(direction == BusDirection.BusDown){
				g.drawImage(busDown, _xPos, _yPos, 16, 37, this);
			}
			else if(direction == BusDirection.BusUp){
				g.drawImage(busUp, _xPos, _yPos, 16, 37, this);
			}
			else if(direction == BusDirection.BusRight){
				g.drawImage(busRight, _xPos, _yPos, 36, 23, this);
			}
			else if(direction == BusDirection.BusLeft){
				g.drawImage(busLeft, _xPos, _yPos, 37, 23, this);
			}
			
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
