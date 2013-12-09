package city.transportation;

import gui.WorldViewBuilding;

import java.util.ArrayList;
import java.util.List;

import city.Place;
import city.transportation.interfaces.Commuter;

public class BusStopObject extends Place {
	List<Commuter> _waitList = new ArrayList<Commuter>();
	List<Commuter> _suicideList = new ArrayList<Commuter>();
	
	public BusStopObject(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void addCommuterRole(Commuter person){
		System.out.println("Added person to waitlist");
		_waitList.add(person);
	}
	
	public synchronized int addMyselfToDeathList(Commuter person){
		System.out.println("Someone want to commit suicide pretty soon" + _suicideList.size());
		_suicideList.add(person);
		return _suicideList.size();
	}
	
	public synchronized void removeCommuterRole(Commuter person){
		_waitList.remove(person);
	}

	public synchronized void removeDeadPeople(Commuter person){
		_suicideList.remove(person);
	}
	
	public List<Commuter> getList(){
		return _waitList;
	}
	
	public List<Commuter> getSuicideList(){
		return _suicideList;
	}
}
