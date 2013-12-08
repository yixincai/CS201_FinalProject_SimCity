package city.restaurant.eric;

import city.restaurant.eric.Menu;

public class Check
{
	private Menu.Item _foodItem;
	public String choice() { return _foodItem.choice(); }
	
	public double price() { return _foodItem.price(); }
	
	private int _table;
	public int table() { return _table; }
	
	public Check(String choice, int table)
	{
		// Given the choice of food, find the correct MenuItem and therefore price
		for(Menu.Item i : Menu.allEntrees())
		{
			if(i.choice().equals(choice))
			{
				_foodItem = new Menu.Item(i);
			}
		}
		
		_table = table;
	}
	
	public Check(Check value) // copy constructor
	{
		_foodItem = new Menu.Item(value._foodItem);
		_table = value._table;
	}
}
