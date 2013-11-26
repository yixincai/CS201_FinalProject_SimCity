package city.restaurant.ryan.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.gui.CustomerGui.State;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WaiterGui implements Gui {

    private RyanWaiterRole agent = null;
    
	private boolean holding = false;
	private boolean check = false;
	private String food;
     
	RestaurantGui gui;

    private int xPos = -20, yPos = 150;//default waiter position
    private int xDestination = -20, yDestination = 200;//default start position

    public int xTable;
    public int yTable;
    
    Dimension CashierPosition = new Dimension(200, 65);
    Dimension ChefPosition = new Dimension(475, 115);
    Dimension HomePosition;
    
    public boolean offScreen = false;

    public WaiterGui(RyanWaiterRole agent, RestaurantGui gui, int x, int y) {
        this.agent = agent;
        this.gui = gui;
        xDestination = x;
        yDestination = y;
        HomePosition = new Dimension(x,y);
    }
    
    public void OffBreak(){
    	gui.setWaiterEnabled(agent);
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
        
        if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

        if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xTable + 20) && (yDestination == yTable - 20)) {
        	agent.msgAtTable();
        }
        
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == ChefPosition.width) & (yDestination == ChefPosition.height)){
        	agent.msgAtChef();
        }
        
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == CashierPosition.width) & (yDestination == CashierPosition.height)){
        	agent.msgAtCashier();
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
        
        if(holding == true){
			g.setColor(Color.black);
			g.drawString((food), xPos, (yPos-5));
		}
        
        if(check == true){
        	g.setColor(Color.black);
			g.drawString("Check", xPos, (yPos-5));
        }
    }

    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToPosition(Dimension seatPos){
    	xDestination = seatPos.width +20;
    	yDestination = seatPos.height +20;
    }

    public void DoGoToTable(int tableNumber) {
    	if(tableNumber == 1){
    		xTable = 150;
    		yTable = 150;
    	}
    	else if(tableNumber == 2){
    		xTable = 250;
    		yTable = 150;
    	}
    	else if(tableNumber == 3){
    		xTable = 350;
    		yTable = 150;
    	}
        xDestination = xTable + 20;
        yDestination = yTable - 20;
    }
    
    public void DoGoToChef(){
    	xDestination = ChefPosition.width;
    	yDestination = ChefPosition.height;
    }
    
    public void DoGoToCashier(){
    	xDestination = CashierPosition.width;
    	yDestination = CashierPosition.height;
    }
    
    public void DoGoToFront(){
    	xDestination = -20;
		yDestination = -20;
    }
    
    public void setPosition(int choice){
    	switch(choice){
    		case 1 :xDestination = -20;
    				yDestination = -20;
    				break;
    		default:
    			xDestination = HomePosition.width;
    			yDestination = HomePosition.height;
    			break;
    	}
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
    
    public void hasFood(){
    	holding = true;
    }
    
    public void noFood(){
    	holding = false;
    	food = null;
    }
    
    public void thisFood(String choice){
    	food = choice.substring(0, Math.min(choice.length(), 2));
    }
    
    public void hasCheck(){
    	check = true;
    }
    
    public void noCheck(){
    	check = false;
    }
    
    public String getAgentName(){
    	return agent.getName();
    }
}