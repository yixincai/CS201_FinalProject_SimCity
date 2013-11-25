package city.home.gui;

public class HouseOccupantGui extends HomeOccupantGui {

	@Override
	protected int bedX() {
		return HouseAnimationPanel.BEDX;
	}

	@Override
	protected int bedY() {
		return HouseAnimationPanel.BEDY;
	}

	@Override
	protected int kitchenX() {
		//TODO implement with Omar's new values for kitchen
		return 0;
	}

	@Override
	protected int kitchenY() {
		//TODO implement with Omar's new values for kitchen
		return 0;
	}

	@Override
	protected int idleX() {
		//TODO implement with Omar's new values for idle
		return 0;
	}

	@Override
	protected int idleY() {
		//TODO implement with Omar's new values for idle
		return 0;
	}

}
