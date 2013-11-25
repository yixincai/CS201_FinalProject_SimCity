package city.restaurant.omar.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.restaurant.omar.OmarCustomerRole;
import city.restaurant.omar.OmarWaiterRole;
import city.restaurant.omar.Table;

public class OmarWaiterGui implements Gui {

	private OmarWaiterRole agent = null;
	private boolean gotAct = false;
	
	RestaurantGui gui;

	private int xPos = 50, yPos = 50;//default waiter position
	private int xDestination = 50, yDestination = 50;//default start position
	private String currentStatus = "Idle";
	
	private static int TOFFSET = 20;
	private static int ORPOS = 50;
	private static int COOKLOCX = 480;
	private static int COOKLOCY = 440;

	private static int BREAKX = 100;
	private static int BREAKY = 400;
	
	private static int CASHIERX = 0;
	private static int CASHIERY = 200;
	
	private int HOMEX = 0;
	private int HOMEY = 0;

	    public OmarWaiterGui(OmarWaiterRole agent, RestaurantGui gui) {
	        this.agent = agent;
	        this.gui = gui;
	    }

	    public void updatePosition() {
	    	if(gotAct){
		        if (xPos < xDestination)
		            xPos++;
		        else if (xPos > xDestination)
		            xPos--;
	
		        if (yPos < yDestination)
		            yPos++;
		        else if (yPos > yDestination)
		            yPos--;
		        
		        if(xPos == xDestination && yPos == yDestination){
		        	gotAct = false;
			    	agent.msgArrived();
			    	return;
			    }
	    	}
	    }

	    public void draw(Graphics2D g) {
	        g.setColor(Color.MAGENTA);
	        g.fillRect(xPos, yPos, TOFFSET, TOFFSET);
	        
	        if(xPos == HOMEX && yPos == HOMEY){
	        	g.drawString("Home", xPos + 5, yPos - 5);
	        } else {
	        	g.drawString(currentStatus, xPos + 5, yPos - 5);
	        }
	    }

	    public boolean isPresent() {
	        return true;
	    }
	    
	    public void setCurrentStatus(String newStatus){
	    	currentStatus = newStatus;
	    }
	    
	    public void DoGetCustomer(OmarCustomerRole customer, Table table){
	    	xDestination = -1 * TOFFSET + 100;
	    	yDestination = -1 * TOFFSET + 30;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoBringToTable(OmarCustomerRole customer, Table table) {
	    	xDestination = table.getY() + TOFFSET;
	    	yDestination = table.getX() - TOFFSET;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoTakeCustomerOrder(OmarCustomerRole c, Table table){
	    	xDestination = table.getY() + TOFFSET;
	    	yDestination = table.getX() - TOFFSET;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGiveOrderToCook(){
	    	xDestination = COOKLOCX;
	    	yDestination = COOKLOCY;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGetFoodFromCook(){
	    	xDestination = 540;
	    	yDestination = 320;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGiveCustomerFood(OmarCustomerRole c, Table table){
	    	xDestination = table.getY() + TOFFSET;
	    	yDestination = table.getX() - TOFFSET;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGetCheck(){
	    	setCurrentStatus("Gting Check");
	    	xDestination = CASHIERX;
	    	yDestination = CASHIERY;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGiveCorrectBillToCustomer(OmarCustomerRole c, Table table){
	    	setCurrentStatus("Check");
	    	xDestination = table.getY() + TOFFSET;
	    	yDestination = table.getX() - TOFFSET;
	    	
	    	gotAct = true;
	    }

	    public void DoGoIdle() {
	        xDestination = HOMEX;
	        yDestination = HOMEY;
	        
	        gotAct = true;
	    }
	    
	    public void DoGoOnBreak() {
	    	setCurrentStatus("On Break");
	    	xDestination = BREAKX;
	    	yDestination = BREAKY;
	    }

	    public int getX() {
	        return xPos;
	    }

	    public int getY() {
	        return yPos;
	    }
	    
	    public void setWaiterBreakBoxEnabled(){
	    	gui.setWaiterBreakBoxEnabled(this.agent);
	    }
	    
	    public void setHomePosition(int x, int y){
	    	HOMEX = x;
	    	HOMEY = y;
	    	
	    	xDestination = HOMEX;
	    	yDestination = HOMEY;
	    	
	    	gotAct = true;
	    }

}
