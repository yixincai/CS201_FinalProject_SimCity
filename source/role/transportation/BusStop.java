package role.transportation;

import java.util.List;

import agent.PersonAgent;

public class BusStop {
	List<PersonAgent> _waitList;
	
	public void addPerson(PersonAgent person){
		_waitList.add(person);
	}
	
	public void removePerson(PersonAgent person){
		_waitList.remove(person);
	}
	
	public List getList(BusAgent bus){
		return _waitList;
	}
}
