package city.restaurant.omar;

import java.util.HashMap;
import java.util.Map;

public class Menu {
	Map<String, Double> menuItems;
	
	public Menu(){
		menuItems = new HashMap<String, Double>();
		
		menuItems.put("Pizza", 12.0);
		menuItems.put("Hot Dog", 15.0);
		menuItems.put("Burger", 20.0);
		menuItems.put("Filet Mignon", 35.0);
	}
	
}
