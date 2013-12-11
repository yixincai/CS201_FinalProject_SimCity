package city;

import gui.WorldViewBuilding;

public abstract class Workplace extends Place{

	int numberOfCustomers = 0;
	boolean allowedToOpen = true;
	public Workplace(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}

	protected abstract void cmdTimeToClose();
	
	//form gui
	public void msgTimeToClose(){
		allowedToOpen = false;
		cmdTimeToClose();
	}
	
	public synchronized void msgIAmComing(){
		numberOfCustomers++;
	}
	
	public synchronized void msgIAmLeaving(){
		numberOfCustomers--;
	}
	
	public int getNumberOfCustomers(){
		return numberOfCustomers;
	}
}
