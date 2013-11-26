package city.restaurant.ryan.gui;

import java.awt.*;

import city.restaurant.ryan.RyanCashierRole;
import city.restaurant.ryan.gui.RyanCustomerGui.Command;
import city.restaurant.ryan.gui.RyanCustomerGui.State;

public class RyanCashierGui implements Gui{
	private RyanCashierRole agent = null;
	
	int xPos = 90; 
	int yPos = 20;
	int xDestination = 90;
	int yDestination = 20;
	
	private enum Command {noCommand, LeaveRestaurant};
	Command command = Command.noCommand;
	
	public RyanCashierGui(RyanCashierRole agent){
		this.agent = agent;
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
        
        if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.LeaveRestaurant) {
				agent.active = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
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
	
	public void LeaveRestaurant(){
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
