package city.interfaces;

import city.home.HomeOccupantRole;

public interface Person {

	public String name();

	public void cmdNoLongerHungry();

	public HomeOccupantRole homeOccupantRole();

	public double money();
	public void cmdChangeMoney(double amountChanged);

	public void stateChanged();
	
}
