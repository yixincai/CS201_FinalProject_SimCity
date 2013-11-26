package city.home;

import java.util.ArrayList;
import java.util.List;

import city.Directory;
import city.PersonAgent;
import city.Place;
import agent.Role;

public class LandlordRole extends Role {
	
	// ------------------------------------ DATA -------------------------------------
	public enum MyApartmentState { VACANT, REQUESTED, ASKED_TENANT, READY_TO_LEASE, OCCUPIED }
	class MyApartment {
		Apartment apartment;
		
		ApartmentRenterRole tenant = null;
		double weeklyRate = 40; // hard-coded for now
		double owedAmount = 0;
		MyApartmentState state = MyApartmentState.VACANT;
		boolean justPaid = false;
	}
	List<MyApartment> _myApartments = new ArrayList<MyApartment>();
	
	public enum MyHouseState { VACANT, REQUESTED, ASKED_OWNER, CHECK_OWED_AMOUNT, OWNED }
	class MyHouse
	{
		House house;
		HouseOwnerRole owner = null;
		double price = 400;
		double owedAmount = 0;
		MyHouseState state = MyHouseState.VACANT;
	}
	List<MyHouse> _myHouses = new ArrayList<MyHouse>();
	
	
	
	// ------------------------------ CONSTRUCTOR & PROPERTIES ---------------------------
	public LandlordRole(PersonAgent person)
	{
		super(person);
		
		// Populate the _myApartments list
		List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
		for(ApartmentBuilding b : apartmentBuildings)
		{
			List<Apartment> apartments = b.apartments();
			for(Apartment a : apartments)
			{
				MyApartment m = new MyApartment();
				m.apartment = a;
				_myApartments.add(m);
			}
		}
		
		// Populate the _myHouses list
		List<House> houses = Directory.houses();
		for(House h : houses)
		{
			MyHouse m = new MyHouse();
			m.house = h;
			_myHouses.add(m);
		}
	}
	@Override
	public Place place() { return _person.homeOccupantRole().place(); } // works from home basically.
	
	
	
	// ------------------------------------ COMMANDS ------------------------------------
	@Override
	public void cmdFinishAndLeave() { } // do nothing
	
	// ------------------------------------- MESSAGES ---------------------------------
	// -------------- STARTING A RENTAL ---------------
	public void msgIWouldLikeToStartRenting(ApartmentRenterRole sender, ApartmentBuilding a)
	{
		for(MyApartment m : _myApartments)
		{
			if(m.apartment.apartmentBuilding() == a)
			{
				if(m.state == MyApartmentState.VACANT)
				{
					m.state = MyApartmentState.REQUESTED;
					m.tenant = sender;
				}
			}
		}
		stateChanged();
	}
	public void msgIAcceptRate(ApartmentRenterRole sender)
	{
		for(MyApartment m : _myApartments)
		{
			if(m.tenant == sender)
			{
				m.state = MyApartmentState.READY_TO_LEASE;
			}
		}
		stateChanged();
	}
	
	// -------------- RECEIVING RENT ---------------
	
	// -------------- SELLING A HOUSE ---------------
	
	
	
	// ------------------------------------ SCHEDULER --------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
