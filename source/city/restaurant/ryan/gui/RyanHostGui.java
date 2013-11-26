package city.restaurant.ryan.gui;

import java.awt.*;

import city.restaurant.ryan.RyanCustomerRole;
import city.restaurant.ryan.RyanHostRole;

public class RyanHostGui implements Gui {

    private RyanHostRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static int xTable = 200;
    public static int yTable = 250;
    
    public boolean offScreen = false;

    public RyanHostGui(RyanHostRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
        
        if(xPos == -20 && yPos== -20){
        	offScreen = true;
        }
        else
        	offScreen = false;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(RyanCustomerRole customer, int x, int y) {
    	xTable = x;
    	yTable = y;
        xDestination = xTable + 20;
        yDestination = yTable - 20;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public boolean isOffScrreen(){
    	return offScreen;
    }
    
    public String getAgentName(){
    	return agent.getName();
    }
}
