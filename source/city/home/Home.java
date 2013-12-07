package city.home;

import city.interfaces.Person;
import city.Place;

public interface Home {
	public Place place();
	public HomeOccupantRole tryGenerateHomeOccupantRole(Person person);
}
