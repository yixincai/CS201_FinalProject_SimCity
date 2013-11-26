package city.restaurant.ryan.gui;

import restaurant.CashierAgent;

import java.awt.*;

public class CashierGui implements Gui{
	private RyanCashierRole agent = null;
	RestaurantGui gui;
	
	int xPos = 90; 
	int yPos = 20;
	int xDestination = 90;
	int yDestination = 20;
	
	CashierGui(RyanCashierRole agent, RestaurantGui gui, Dimension dimension){
		this.agent = agent;
		this.gui = gui;
		yPos = dimension.height;
		xPos = dimension.width;
		yDestination = dimension.height;
		xDestination = dimension.width;
	}
	
	public int getxPos(){
		return xPos;
	}
	
	public int getyPos(){
		return yPos;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}
