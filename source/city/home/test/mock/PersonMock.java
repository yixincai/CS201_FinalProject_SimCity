package city.home.test.mock;

import city.home.HomeOccupantRole;
import city.interfaces.Person;

public class PersonMock implements Person {
	boolean stateChangedCalled = false;
	public void stateChanged() { stateChangedCalled = true; }
	@Override
	public Object getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void cmdNoLongerHungry() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HomeOccupantRole homeOccupantRole() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void cmdChangeMoney(double amountReceived) {
		// TODO Auto-generated method stub
		
	}
}
