package city.transportation.gui;

import gui.Gui;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.Place;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;
import city.transportation.interfaces.Commuter;

public class BusAgentGui extends JPanel implements Gui {
	
	int _xPos, _yPos;
	BusStopObject _busStop;
	int _xDestination, _yDestination;
	int _xStarting, _yStarting, deathCount;
	
	BusAgent _bus;
	private String currentStatus = "Bus";
	List<Commuter> deathList;
	List<Commuter> carList;
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
		deathCount = 0;
		AlertLog.getInstance().logMessage(AlertTag.BUS, "Bus" ,"Going To Bus Stop");
		if (_bus._busStopNum == 0){
			deathList = _bus._busStops.get(5).getSuicideList();
			carList = _bus._busStops.get(5).getCarCrashList();
		}
		else
			deathList = _bus._busStops.get(_bus._busStopNum - 1).getSuicideList();
		System.out.println(_xPos + "  " + _yPos);
		_xStarting = _xPos;
		_yStarting = _yPos;
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
			if ((_xPos - _xStarting) % 10 == 0){
				deathCount = (_xPos - _xStarting)/ 10;
				if (deathList.size() != 0){
					if (deathCount < deathList.size())
						deathList.get(deathCount).msgYouAreAllowedToDie();
					else
						deathList.clear();
				}
			}
			_xPos++;
			direction = BusDirection.BusRight;
		}
		else if (_xPos > _xDestination){
			if ((_xStarting - _xPos) % 10 == 0){
				deathCount = (_xStarting - _xPos)/ 10 - 2;
				if (deathList.size() != 0 && deathCount >= 0){
					if (deathCount < deathList.size())
						deathList.get(deathCount).msgYouAreAllowedToDie();
					else
						deathList.clear();
				}
			}
			_xPos--;
			direction = BusDirection.BusLeft;
		}
		
		if (_yPos < _yDestination){
			if ((_yPos - _yStarting) % 10 == 0){
				deathCount = (_yPos - _yStarting)/ 10;
				if (deathList.size() != 0){
					if (deathCount < deathList.size())
						deathList.get(deathCount).msgYouAreAllowedToDie();
					else
						deathList.clear();
				}
			}
			_yPos++;
			direction = BusDirection.BusDown;
		}
		else if (_yPos > _yDestination){
			if ((_yStarting - _yPos) % 10 == 0){
				deathCount = (_yStarting - _yPos)/ 10 - 2;
				if (deathList.size() != 0 && deathCount >= 0){
					if (deathCount < deathList.size())
						deathList.get(deathCount).msgYouAreAllowedToDie();
					else
						deathList.clear();
				}
			}
			if (_yStarting - _yPos == 160 && carList.size() != 0){
				for (int i = 0; i<carList.size(); i++)
					carList.get(i).msgYouAreAllowedToDie();
				carList.clear();
			}
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
