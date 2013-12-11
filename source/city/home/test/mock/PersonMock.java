package city.home.test.mock;

import java.awt.Image;

import city.home.HomeOccupantRole;
import city.interfaces.Person;

public class PersonMock implements Person {
	public boolean stateChangedCalled = false;
	public boolean cmdNoLongerHungryCalled = false;
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "PersonMock";
	}
	@Override
	public double money() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public HomeOccupantRole homeOccupantRole() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void cmdNoLongerHungry() {
		cmdNoLongerHungryCalled = true;
	}
	@Override
	public void cmdChangeMoney(double amountReceived) {
		// TODO Auto-generated method stub
		
	}
	public void stateChanged() { stateChangedCalled = true; }
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}
}
