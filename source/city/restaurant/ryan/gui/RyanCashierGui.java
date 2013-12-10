package city.restaurant.ryan.gui;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.restaurant.ryan.RyanCashierRole;
import city.restaurant.ryan.gui.RyanCustomerGui.State;

public class RyanCashierGui extends JPanel implements Gui{
	private RyanCashierRole agent = null;
	
	int xPos = 200; 
	int yPos = -20;
	int xDestination = 200;
	int yDestination = -20;
	
	private enum Command {noCommand, LeaveRestaurant};
	Command command = Command.noCommand;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Cashier.png"));
    Image cashier = a.getImage();
    int xGap = 19;
    int yGap = 25;
	
	public RyanCashierGui(RyanCashierRole agent){
		this.agent = agent;
		xDestination = 200;
		yDestination = 20;
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
		if(agent.active){
			g.drawImage(cashier, xPos, yPos, xGap, yGap, this);
		}
	        
		
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
