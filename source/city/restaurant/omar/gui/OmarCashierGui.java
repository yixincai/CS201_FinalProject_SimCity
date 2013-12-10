package city.restaurant.omar.gui;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import gui.Gui;

import city.restaurant.omar.*;

public class OmarCashierGui extends JPanel implements Gui{
		
	int xPos = 0; 
	int yPos = 200;
	int xDestination = 0;
	int yDestination = 200;
	OmarCashierRole agent;
	
	private enum Command {noCommand, LeaveRestaurant};
	Command command = Command.noCommand;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Cashier.png"));
    Image cashier = a.getImage();
    int xGap = 19;
    int yGap = 25;
	
	public OmarCashierGui(OmarCashierRole agent){
		this.agent = agent;
		xDestination = 0;
		yDestination = 200;
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
