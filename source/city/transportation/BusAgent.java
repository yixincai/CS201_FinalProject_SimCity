package city.transportation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.transportation.gui.BusAgentGui;

public class BusAgent {
	List<MyCommuter> passengers = new ArrayList<MyCommuter>();
	BusAgentGui _gui = new BusAgentGui();
	
	BusStop currentDestination;
	List<CommuterRole> currentBusStopList = new ArrayList<CommuterRole>();
	
	static int fare;
	int register;

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
	
	public BusAgent(){
		
	}
	
	//----------------------------------------------Messages----------------------------------------
	public void msgAtDestination(BusStop busstop){
	    currentDestination = busstop;
	    
	    bState = BusState.atDestination;
	}

	public void msgGotOff(CommuterRole passenger){
	    passengers.remove(findCommuter(passenger)); //Fix this
	    numPeople--;
	}

	public void msgGettingOnBoard(CommuterRole person, Place destination, int payment){
	    passengers.add(new MyCommuter(person, destination));
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
	    		comm.msgGetOnBus(fare, this);
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
	
}
