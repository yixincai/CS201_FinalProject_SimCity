package city.interfaces;

import city.home.HomeOccupantRole;

public interface Person {

	String name();

	void stateChanged();

	void cmdNoLongerHungry();

	HomeOccupantRole homeOccupantRole();

	void cmdChangeMoney(double amountReceived);
	
}
