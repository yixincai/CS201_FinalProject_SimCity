package city.restaurant.tanner;

import java.awt.Point;

import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;

public class Table 
{
	Point position;
	int tableNumber;
	public TannerRestaurantCustomer occupant;
	
	public Table(int tn, int xPos, int yPos)
	{
		position = new Point(xPos,yPos);
		tableNumber = tn;
		occupant = null;
		TannerRestaurant.tableMap.put(tn, this);
	}
	
	//Getters
	public Point getPosition()
	{
		return position;
	}
}
