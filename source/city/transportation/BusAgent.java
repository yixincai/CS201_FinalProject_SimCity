package city.transportation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.BusAgentGui;
import city.transportation.interfaces.Bus;

//NOTES when taking bus, must decide who finds which busstop is nearest to destination (commuter role or busagent)

public class BusAgent extends Agent implements Bus{
	String _name;
	
	List<MyCommuter> _passengers = new ArrayList<MyCommuter>();
	BusAgentGui _gui;
	
	List<BusStopObject> _busStops = new ArrayList<BusStopObject>();
	
	
	BusStopObject currentDestination;
	int _busStopNum;
	List<CommuterRole> currentBusStopList = new ArrayList<CommuterRole>();
	Semaphore busSem = new Semaphore(0, true);
	
	static double _fare;
	double _register;

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
	
	public BusAgent(String name){
		_name = name;
		_fare = Directory.getBusFare();
		_busStopNum = 0;
		_busStops = Directory.busStops();
	}
	
	public void setBusAgentGui(BusAgentGui gui){
		_gui = gui;
	}
	
	//----------------------------------------------Messages----------------------------------------
	public void msgAtDestination(BusStopObject busstop){
	//	System.out.println("msgAtDestination");
	    currentDestination = busstop;
	    bState = BusState.atDestination;
	    stateChanged();
	}

	public void msgGotOff(CommuterRole passenger){
	    _passengers.remove(findCommuter(passenger)); //Fix this
	    numPeople--;
	}

	public void msgGettingOnBoard(CommuterRole person, Place destination, double payment){ //Check if payment is correct?
	    _passengers.add(new MyCommuter(person, destination));
	    _register += payment;
	    numPeople++; //Fix this
	}
	
	
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction(){
		if(bState == BusState.notmoving){
			GoToFirstBusStop();
			return true;
		}
		
		if(bState == BusState.atDestination){
			DropOff();
			return true;
		}
		
		if(bState == BusState.droppingoff && expectedPeople == numPeople){
			PickUp();
			return true;
		}
		
		if(bState == BusState.pickingup && expectedPeople == numPeople){
			System.out.println("Leaving");
			Leave();
			return true;
		}
		
		return false;
	}
	
	//----------------------------------------------Actions----------------------------------------
	public void GoToFirstBusStop(){
		_gui.goToBusStop(_busStops.get(_busStopNum));
		try {
			busSem.acquire();
	  } catch (InterruptedException e) {
			e.printStackTrace();
	  }
	}

	public void DropOff(){
		System.out.println("Dropping off");
	    bState = BusState.droppingoff;
	    for(MyCommuter commuter: _passengers){
	        if(commuter.destination == currentDestination){
	        	commuter.commuter.msgGetOffBus(currentDestination);
	            expectedPeople--;
	        }
	    }
	    stateChanged();
	}

	public void PickUp(){
		System.out.println("Pick up");
		currentBusStopList = currentDestination.getList();
		bState = BusState.pickingup;
	    while(expectedPeople <= capacity && currentBusStopList.size() > 0){
	    	for(CommuterRole comm: currentBusStopList){
	    		comm.msgGetOnBus(_fare, this);
	            expectedPeople++;
	        }
	    }
	    stateChanged();
	}

	public void Leave(){
	    bState = BusState.moving;
	    _busStopNum++;
		
		if(_busStopNum >= _busStops.size()){
			_busStopNum = 0;
		}
		_gui.goToBusStop(_busStops.get(_busStopNum));
	}
	
	//-----------------------------------------Utilities-----------------------------------------
	public MyCommuter findCommuter(CommuterRole commuter){
		synchronized(_passengers){
			for(MyCommuter passenger: _passengers){
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
	
	public void updateBusStopList(){
		_busStops = Directory.busStops();
	}
	
	public void releaseSem(){
		busSem.release();
	}
	
}
