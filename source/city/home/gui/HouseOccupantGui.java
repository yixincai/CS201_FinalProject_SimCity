package city.home.gui;

import city.home.ApartmentOccupantRole;
import city.home.HouseOccupantRole;

public class HouseOccupantGui extends HomeOccupantGui {
	
	public HouseOccupantGui(HouseOccupantRole role)
	{
		super(role);
	}

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
		return HouseAnimationPanel.STOVEX;
	}

	@Override
	protected int kitchenY() {
		return HouseAnimationPanel.STOVEY;
	}

	@Override
	protected int idleX() {
		return HouseAnimationPanel.TVX;
	}

	@Override
	protected int idleY() {
		return HouseAnimationPanel.TVY;
	}

}
