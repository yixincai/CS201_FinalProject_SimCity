package city.transportation;

import gui.astar.AStarTraversal;
import gui.trace.AlertTag;

import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.CommuterGui;
import city.transportation.interfaces.Bus;
import city.transportation.interfaces.Commuter;

/** There is one CommuterRole per person, and the CommuterRole is the one that gets the person to different places in the world view. */

//NOTES when taking bus, must decide who finds which busstop is nearest to destination (commuter role or busagent)
//		Connect to directory to find fare

public class CommuterRole extends Role implements Commuter{
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public PersonAgent _person;
	public Place _destination;
	public Place _currentPlace;
	BusStopObject _busStop;
	//	Semaphore commuterSem = new Semaphore(0, true);

	public Bus _bus;
	public double _fare;
	CommuterGui _gui;
	public boolean hasCar;
	public boolean wantToDie = false;
	public int deadListNumber = 0;
	//TODO	if someone want to die, change money > 100, hasCar = false (he will take bus), wantToDie = true;
	public enum TravelState{choosing, choseCar, goToCar, atCar, choseWalking, walking, 
		choseBus, goingToBusStop, atBusStop, waitingAtBusStop, busIsHere, ridingBus, busIsAtDestination, 
		atDestination, done, none};

		public TravelState _tState = TravelState.none;

		Random _generator = new Random();

		//Transportation Hacks
		enum PrefTransport{none, walk, bus, car};
		PrefTransport pTransport = PrefTransport.none;

		//Probably won't need -> not 100% sure though
		enum CarState{noCar, hasCar, usingCar};
		CarState _cState = CarState.noCar; 

		private Semaphore _reachedDestination = new Semaphore(0, true);

		//----------------------------------------------CONSTRUCTOR & PROPERTIES----------------------------------------
		public CommuterRole(PersonAgent person, Place initialPlace){
			super(person);
			_person = person;
			_currentPlace = initialPlace;
			_destination = null;
//			if ((new Random()).nextInt(2) == 0)
				hasCar = true;
//			else 
//				hasCar = false;
			_gui = new CommuterGui(this, initialPlace);
		}

		public CommuterGui gui() { return _gui; }
		public void setGui(CommuterGui gui) { _gui = gui; }

		public Place destination() { return _destination; }
		public void setDestination(Place place) { cmdGoToDestination(place); }

		public void setCar(boolean car) { hasCar = car; }
		public boolean hasCar(){ return hasCar; }

		public Place place() { return currentPlace(); } // could replace this to return a home-like location that the CommuterRole defaults back to when PersonAgent sets its _nextRole to its _commuterRole.  This would of course require more changes to work correctly.
		public Place currentPlace() { return _currentPlace; }
		public void setCurrentPlace(Place place) { _currentPlace = place; }

		public void setBusStop(BusStopObject busStop){_busStop = busStop;}

		//----------------------------------------------Command---------------------------------------------
		public void cmdGoToDestination(Place destination){ //Command to go to destination
			if(destination != null) {
				print(AlertTag.WORLDVIEW,"Will go to " + destination.name() + " at (" + destination.positionX() + "," + destination.positionY() + ").");
			}
			else {
				print(AlertTag.WORLDVIEW,"Will go to null destination.");
			}
			_tState = TravelState.choosing;
			_destination = destination;
			stateChanged();
		}
		@Override
		public void cmdFinishAndLeave() { }



		//----------------------------------------------Messages------------------------------------------
		public void msgReachedDestination() {
			_reachedDestination.release();
			// no stateChanged() because this message is to release the semaphore
		}

		//Bus Transportation messages
		public void msgAtBusStop(BusStopObject busstop){ //GUI message
			_tState = TravelState.atBusStop;
			_currentPlace = busstop;
			stateChanged();
			print(AlertTag.WORLDVIEW,"Going to bus stop " + busstop.name());
		}
		public void msgGetOnBus(double fare, Bus bus){
			_tState = TravelState.busIsHere;
			_bus = bus;
			_fare = fare;
			stateChanged();
			print(AlertTag.WORLDVIEW,"Getting on bus " + bus.name());
		}

		@Override
		public void msgGetOffBus(Place busstop){
			_currentPlace = busstop;
			_tState = TravelState.busIsAtDestination;
			_busStop = (BusStopObject)busstop;
			stateChanged();
			print(AlertTag.WORLDVIEW,"Getting off bus " + _bus.name());
		}

		//Suicide messages
		public void msgYouAreAllowedToDie(){
			_gui.dead = true;
			print(AlertTag.WORLDVIEW, "Hit by the car.");
		}		

		//Car Messages
		public void msgAtCar(){
			_tState = TravelState.atCar;
			stateChanged();
			print(AlertTag.WORLDVIEW,"At car");
		}

		//Msg At Destination from GUI
		public void msgAtDestination(Place place){
			_tState = TravelState.atDestination;
			_currentPlace = place;
			//active = false;
			stateChanged();
		}

		//----------------------------------------------Scheduler----------------------------------------
		public boolean pickAndExecuteAnAction() {
			try{
				//Choosing
				if(_tState == TravelState.choosing){
					actChooseTransportation();
					return true;
				}

				//Walking
				if(_tState == TravelState.choseWalking){
					actWalking();
					return true;
				}

				//Driving Car
				if(_tState == TravelState.choseCar){
					actDriving();
					return true;
				}

				//Riding Bus
				if(_tState == TravelState.choseBus){
					actGoToBusStop();
					return true;
				}
				if(_tState == TravelState.busIsHere && _bus != null && _person._money >= _fare){
					actGetOnBus();
					return true;
				}
				if(_tState == TravelState.busIsHere && _bus != null && _person._money <= _fare){
					//actWalking();
					return false;//true;
				}
				if(_tState == TravelState.busIsAtDestination){
					actGetOffBus();
					return true;
				}
			}
			catch (ConcurrentModificationException e){

			}
			return false;
		}

		//----------------------------------------------Actions----------------------------------------
		//Choosing
		public void actChooseTransportation(){
			print(AlertTag.WORLDVIEW,"Choosing mode of transport");
			if (_person.name().contains("CS201"))
				wantToDie = true;
			else
				wantToDie = false;
			if (_person._money >= 200)
				hasCar = true;
			else
				hasCar = false;
			if (hasCar){
				_tState = TravelState.choseCar;
			}
			else{
				if (_person._money >= 100){
					_tState = TravelState.choseBus;
				}
				else
					_tState = TravelState.choseWalking;
			}

			// note: when testing, you can force one of these (default value is TravelState.none)
			if(pTransport == PrefTransport.walk){
				_tState = TravelState.choseWalking;
			}
			if(pTransport == PrefTransport.bus){
				_tState = TravelState.choseBus;
			}
			if(pTransport == PrefTransport.car){
				_tState = TravelState.choseCar;
			}
		}

		public void actChooseNewTransportation(){ //Choosing when previous form of transportation doesn't work (Mostly for bus)
			_tState = TravelState.none;
		}

		//Driving
		public void actDriving(){
			_gui.driveToLocation(_destination);
			_tState = TravelState.done;
			active = false;
		}

		//Walking
		public void actWalking(){
			_tState = TravelState.done;
			_gui.walkToLocation(_destination);
			active = false;
		}

		//Bus
		public void actGoToBusStop(){
			_busStop = Directory.getNearestBusStop(_gui.getX(), _gui.getY()); //Unit Testing will skip this for now
			BusStopObject _nextStop = Directory.getNearestBusStopToDestination(_destination);
			if (wantToDie){
				_gui.goToBusStop(_busStop);
				deadListNumber = _busStop.addMyselfToDeathList(this);
				_gui.goDie();
				_tState = TravelState.waitingAtBusStop;
			}
			else{
				if (_nextStop == _busStop){
					_tState = TravelState.done;
					active = false;
				}
				else{
				_gui.goToBusStop(_busStop);
				_busStop.addCommuterRole(this);
				_busStop = Directory.getNearestBusStopToDestination(_destination);
				_tState = TravelState.waitingAtBusStop;
				}
			}
		}
		public void actGetOnBus(){
			_tState = TravelState.ridingBus;
			_person._money -= _fare;
			_gui.getOnBus();
			_bus.msgGettingOnBoard(this, _busStop, _fare);
		}
		public void actGetOffBus(){
			_gui.getOffBus(_busStop);
			_bus.msgGotOff(this);
			_bus = null;
			_tState = TravelState.done;
			active = false;
		}



		//----------------------------------------------Hacks----------------------------------------
		public void setPreferredTransportation(int choice){
			if(choice == 0){
				pTransport = PrefTransport.walk;
			}
			else if(choice == 1){
				pTransport = PrefTransport.bus;
			}
			else if(choice == 2){
				pTransport = PrefTransport.car;
			}
			else{
				pTransport = PrefTransport.none;
			}
		}

		// ------------------------------------------ UTILITIES -----------------------------------
		private void waitForGuiToReachDestination() {
			try {
				_reachedDestination.acquire();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
}
