package city.restaurant.eric;

import java.util.ArrayList;
import java.util.List;

public class Menu
{
	public static class Item
	{
		private String _choice;
		public String choice() { return _choice; }
		private double _price;
		public double price() { return _price; }
		public Item(String choiceVal, double priceVal) { _choice = choiceVal; _price = priceVal; }
		public Item(Item value) { _choice = value.choice(); _price = value.price(); }
	}
	
	// ---------------------- Properties ----------------------
	private static List<Item> ALL_ENTREES = null;
	public static final List<Item> allEntrees() { return ALL_ENTREES; }
	private List<Item> _entrees = new ArrayList<Item>();
	
	// could conceivably add lists for other types of food i.e. appetizers, drinks, desserts, sides, etc.
	
	// ------------------------- Constructor ---------------------------
	static {
		ALL_ENTREES = new ArrayList<Item>();
		
		ALL_ENTREES.add(new Item("Steak",     15.99));
		ALL_ENTREES.add(new Item("Chicken",   10.99));
		ALL_ENTREES.add(new Item("Salad",      5.99));
		ALL_ENTREES.add(new Item("Pizza",      8.99));
	}
	public Menu()
	{
		// Initialize this particular Menu's list.
		for(Item e : ALL_ENTREES)
		{
			_entrees.add(new Item(e));
		}
	}
	
	public List<Item> entrees()
	{
		return _entrees;
	}
	
	public void removeEntree(String choice)
	{
		Item temp = null;;
		for(Item e : _entrees)
		{
			if(e.choice().equals(choice))
			{
				temp = e;
				break;
			}
		}
		if(temp != null)
		{
			_entrees.remove(temp);
		}
	}
	
	//public void restockEntree(String choice) // needed when the cook can get more stuff from the market.
}
