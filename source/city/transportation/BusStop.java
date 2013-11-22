package city.transportation;

import java.util.List;

import city.Place;

public class BusStop extends Place {
	List<CommuterRole> _waitList;
	
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
