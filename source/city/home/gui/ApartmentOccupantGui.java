package city.home.gui;

import city.home.ApartmentOccupantRole;

public class ApartmentOccupantGui extends HomeOccupantGui {
	
	// -------------------------------- CONSTRUCTOR & PROPERTIES --------------------------------
	public ApartmentOccupantGui(ApartmentOccupantRole role)
	{
		super(role);
	}
	// Note: this method can't throw an exception because when this class is instantiated, it has to take in an ApartmentOccupantRole (if it doesn't, an exception will be thrown in the constructor), so _role must be an instanceof ApartmentOccupantRole
	private ApartmentOccupantRole apartmentOccupantRole() { return (ApartmentOccupantRole)_role; }

	@Override
	protected int bedX() {
		switch(apartmentOccupantRole().apartmentNumber())
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
		switch(apartmentOccupantRole().apartmentNumber())
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
		switch(apartmentOccupantRole().apartmentNumber())
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
		switch(apartmentOccupantRole().apartmentNumber())
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
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.TVX;
		case 1:
			return ApartmentAnimationPanel.TVX2;
		case 2:
			return ApartmentAnimationPanel.TVX3;
		case 3:
			return ApartmentAnimationPanel.TVX4;
		}
		return 0;
	}

	@Override
	protected int idleY() {
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.TVY;
		case 1:
			return ApartmentAnimationPanel.TVY2;
		case 2:
			return ApartmentAnimationPanel.TVY3;
		case 3:
			return ApartmentAnimationPanel.TVY4;
		}
		return 0;
	}

	@Override
	protected int frontDoorX() {
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.FRONTDOORX;
		case 1:
			return ApartmentAnimationPanel.FRONTDOORX2;
		case 2:
			return ApartmentAnimationPanel.FRONTDOORX3;
		case 3:
			return ApartmentAnimationPanel.FRONTDOORX4;
		}
		return 0;
	}

	@Override
	protected int frontDoorY() {
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.FRONTDOORY;
		case 1:
			return ApartmentAnimationPanel.FRONTDOORY2;
		case 2:
			return ApartmentAnimationPanel.FRONTDOORY3;
		case 3:
			return ApartmentAnimationPanel.FRONTDOORY4;
		}
		return 0;
	}
	@Override
	protected int chairX() {
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.TVX;
		case 1:
			return ApartmentAnimationPanel.TVX2;
		case 2:
			return ApartmentAnimationPanel.TVX3;
		case 3:
			return ApartmentAnimationPanel.TVX4;
		}
		return 0;
	}
	
	@Override
	protected int chairY() {
		switch(apartmentOccupantRole().apartmentNumber())
		{
		case 0:
			return ApartmentAnimationPanel.TVY;
		case 1:
			return ApartmentAnimationPanel.TVY2;
		case 2:
			return ApartmentAnimationPanel.TVY3;
		case 3:
			return ApartmentAnimationPanel.TVY4;
		}
		return 0;
	}

}
