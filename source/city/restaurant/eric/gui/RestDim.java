package city.restaurant.eric.gui;

public class RestDim
{
	public RestDim(int xVal, int yVal) { set(xVal, yVal); }
	public RestDim(RestDim value) { set(value); }
	public int x = 0;
	public int y = 0;
	public void set(int xVal, int yVal) { x = xVal; y = yVal; }
	public void set(RestDim value) { x = value.x; y = value.y; }
	public boolean equals(RestDim value) { return (x == value.x) && (y == value.y); }
}
