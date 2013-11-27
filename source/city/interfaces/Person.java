package city.interfaces;

import city.home.HomeOccupantRole;
import agent.Role;

public interface Person {

	Object getName();

	void stateChanged();

	void cmdNoLongerHungry();

	HomeOccupantRole homeOccupantRole();

	void cmdChangeMoney(double amountReceived);
	
}
