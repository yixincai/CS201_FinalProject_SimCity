package city.home.test.mock;

import city.home.HomeOccupantRole;
import city.interfaces.Person;

public class PersonMock implements Person {
	public boolean stateChangedCalled = false;
	public boolean cmdNoLongerHungryCalled = false;
	public void stateChanged() { stateChangedCalled = true; }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PersonMock";
	}
	@Override
	public void cmdNoLongerHungry() {
		cmdNoLongerHungryCalled = true;
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
