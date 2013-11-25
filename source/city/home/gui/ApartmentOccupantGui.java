package city.home.gui;

import city.home.ApartmentOccupantRole;

public class ApartmentOccupantGui extends HomeOccupantGui {
	
	public ApartmentOccupantGui(ApartmentOccupantRole role)
	{
		_role = role;
	}
	
	private ApartmentOccupantRole _role;

	@Override
	protected int bedX() {
		switch(_role.apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.BEDX;
		case 1:
			return ApartmentAnimationPanel.BEDX2;
		case 2:
			return ApartmentAnimationPanel.BEDX3;
		case 3:
			return ApartmentAnimationPanel.BEDX4;
		}
		return 0;
	}

	@Override
	protected int bedY() {
		switch(_role.apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.BEDY;
		case 1:
			return ApartmentAnimationPanel.BEDY2;
		case 2:
			return ApartmentAnimationPanel.BEDY3;
		case 3:
			return ApartmentAnimationPanel.BEDY4;
		}
		return 0;
	}

	@Override
	protected int kitchenX() {
		switch(_role.apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.STOVEX;
		case 1:
			return ApartmentAnimationPanel.STOVEX2;
		case 2:
			return ApartmentAnimationPanel.STOVEX3;
		case 3:
			return ApartmentAnimationPanel.STOVEX4;
		}
		return 0;
	}

	@Override
	protected int kitchenY() {
		switch(_role.apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.STOVEY;
		case 1:
			return ApartmentAnimationPanel.STOVEY2;
		case 2:
			return ApartmentAnimationPanel.STOVEY3;
		case 3:
			return ApartmentAnimationPanel.STOVEY4;
		}
		return 0;
	}

	@Override
	protected int idleX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int idleY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
