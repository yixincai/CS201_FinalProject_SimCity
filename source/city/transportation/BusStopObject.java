package city.transportation;

import gui.WorldViewBuilding;

import java.util.ArrayList;
import java.util.List;

import city.Place;

public class BusStopObject extends Place {
	
	public BusStopObject(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
		// TODO Auto-generated constructor stub
	}

	List<CommuterRole> _waitList = new ArrayList<CommuterRole>();
	
	
	
	public void addPerson(CommuterRole person){
		_waitList.add(person);
	}
	
	public void removePerson(CommuterRole person){
		_waitList.remove(person);
	}
	
	public List getList(){
		return _waitList;
	}
}
