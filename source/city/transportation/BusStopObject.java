package city.transportation;

import gui.WorldViewBuilding;

import java.util.ArrayList;
import java.util.List;

import city.Place;
import city.transportation.interfaces.Commuter;

public class BusStopObject extends Place {
	List<Commuter> _waitList = new ArrayList<Commuter>();
	
	public BusStopObject(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
		// TODO Auto-generated constructor stub
	}
	
	public void addCommuterRole(Commuter person){
		_waitList.add(person);
	}
	
	public void removeCommuterRole(Commuter person){
		_waitList.remove(person);
	}
	
	public List<Commuter> getList(){
		return _waitList;
	}
}
