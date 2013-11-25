package city.home;

import agent.Role;
import city.PersonAgent;
import city.Place;

import java.util.List;

import agent.Role;
import city.Directory;
import city.PersonAgent;

public class ApartmentRenterRole extends HomeBuyingRole
{
	// ---------------------------------- DATA -------------------------------------
	Apartment  _apartment;
	enum Command { NONE, START_A_RENTAL, CHECK_RATE, MOVE_IN, PAY_RENT }
	Command _command;
	
	
	
	// -------------------------- CONSTRUCTOR & PROPERTIES ----------------------------
	public ApartmentRenterRole(PersonAgent person) { super(person); }
	public ApartmentRenterRole(PersonAgent person, Apartment apartment)
	{
		super(person);
		_apartment = apartment;
	}
	public boolean haveHome() { if(_apartment != null) { return true; } return false; }
	@Override
	public Place place() { return _apartment.apartmentBuilding(); }
	public void setApartment(Apartment a) { _apartment = a; }
	
	
	
	// ----------------------------- COMMANDS -----------------------------------
	public void cmdStartARental() // from Person
	{
		_command = Command.START_A_RENTAL;
		stateChanged();
	}
	public void cmdFinishAndLeave() { } // nothing to do
	public void cmdPayRent() {
		
	}
	
	// ------------------------------- MESSAGES ---------------------------------
	public void msg() {}
	
	
	
	// ------------------------------- SCHEDULER ---------------------------------
	public boolean pickAndExecuteAnAction() {
		if(_command == Command.START_A_RENTAL) {
			actStartARental();
			return true;
		}
		return false;
	}
	
	
	
	// ---------------------------------- ACTIONS ----------------------------------
	private void actStartARental()
	{
//		ApartmentBuilding apartmentBuilding;
//		List<Place> apartmentBuildings = Directory.apartmentBuildings();
//		
//		for(Place p : apartmentBuildings)
//		{
//			ApartmentBuilding ab = (ApartmentBuilding)p;
//			if(!ab.full())
//			{
//				apartmentBuilding = ab;
//				break;
//			}
//		}
//		apartmentBuilding.landlord().msgIWouldLikeToStartRenting(this, apartmentBuilding);
//		_command = Command.NONE;
	}

}
