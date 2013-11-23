package city.transportation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.BusAgentGui;
import city.transportation.interfaces.Bus;

//NOTES when taking bus, must decide who finds which busstop is nearest to destination (commuter role or busagent)

public class BusAgent extends Agent implements Bus{
	String _name;
	
	List<MyCommuter> passengers = new ArrayList<MyCommuter>();
	BusAgentGui _gui = new BusAgentGui();
	
	BusStopObject currentDestination;
	List<CommuterRole> currentBusStopList = new ArrayList<CommuterRole>();
	
	static int _fare;
	int _register;

	static int capacity;
	int numPeople = 0;
	int expectedPeople = 0;

	enum BusState{moving, atDestination, droppingoff, pickingup, notmoving};
	BusState bState = BusState.notmoving;
	
	enum PassengerState{onBus, offBus};

	class MyCommuter{
	    CommuterRole commuter;
	    Place destination;
	    PassengerState pState = PassengerState.onBus;
	    
	    MyCommuter(CommuterRole person, Place destination){
	    	this.commuter = person;
	    	this.destination = destination;
	    }
	}
	
	public BusAgent(String name, int fare){
		_name = name;
		_fare = fare;
	}
	
	public BusAgent(String name){
		_name = name;
		_fare = 1;
	}
	
	//----------------------------------------------Messages----------------------------------------
	public void msgAtDestination(BusStopObject busstop){
	    currentDestination = busstop;
	    
	    bState = BusState.atDestination;
	}

	public void msgGotOff(CommuterRole passenger){
	    passengers.remove(findCommuter(passenger)); //Fix this
	    numPeople--;
	}

	public void msgGettingOnBoard(CommuterRole person, Place destination, int payment){ //Check if payment is correct?
	    passengers.add(new MyCommuter(person, destination));
	    _register += payment;
	    numPeople++; //Fix this
	}
	
	
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction(){
		if(bState == BusState.atDestination){
			DropOff();
			return true;
		}
		
		if(bState == BusState.droppingoff && expectedPeople == numPeople){
			PickUp();
			return true;
		}
		
		if(bState == BusState.pickingup && expectedPeople == numPeople){
			Leave();
			return true;
		}
		
		return false;
	}
	
	//----------------------------------------------Actions----------------------------------------
	public void DropOff(){
	    bState = BusState.droppingoff;
	    for(MyCommuter commuter: passengers){
	        if(commuter.destination == currentDestination){
	        	commuter.commuter.msgGetOffBus(currentDestination);
	            expectedPeople--;
	        }
	    }
	}

	public void PickUp(){
		currentBusStopList = currentDestination.getList();
		bState = BusState.pickingup;
	    while(expectedPeople <= capacity){
	    	for(CommuterRole comm: currentBusStopList){
	    		comm.msgGetOnBus(_fare, this);
	            expectedPeople++;
	        }
	    }
	}

	public void Leave(){
	    bState = BusState.moving;
	    _gui.moveToNextDestination();
	}
	
	//-----------------------------------------Utilities-----------------------------------------
	public MyCommuter findCommuter(CommuterRole commuter){
		synchronized(passengers){
			for(MyCommuter passenger: passengers){
				if(passenger.commuter == commuter){
					return passenger;
				}
			}
			return null;
		}
	}
	
	public String getName(){
		return _name;
	}

	@Override
	public void setFare(int fare) {
		_fare = fare;
	}
	
}
