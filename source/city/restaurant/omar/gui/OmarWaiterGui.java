package city.restaurant.omar.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.restaurant.omar.OmarCustomerRole;
import city.restaurant.omar.OmarSharedDataWaiterRole;
import city.restaurant.omar.OmarWaiterRole;
import city.restaurant.omar.Table;
import city.restaurant.ryan.RyanSharedDataWaiterRole;

public class OmarWaiterGui extends JPanel implements Gui {

	private OmarWaiterRole agent = null;
	private boolean gotAct = false;
	
	OmarRestaurantAnimationPanel gui;  //maybe not

	private int xPos = 50, yPos = 50;//default waiter position
	private int xDestination = 50, yDestination = 50;//default start position
	private String currentStatus = "Idle";
	
	private static int TOFFSET = 20;
	private static int ORPOS = 50;
	private static int COOKLOCX = 530;
	private static int COOKLOCY = 240;

	private static int BREAKX = 100;
	private static int BREAKY = 200;
	
	private static int CASHIERX = 0;
	private static int CASHIERY = 200;
	
	private static int REVOLVINGX = 510; // can change if needed
	private static int REVOLVINGY = 300;
	
	private int HOMEX = 0;
	private int HOMEY = 0;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/NormalWaiter.png"));
    Image normal = a.getImage();
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/SharedDataWaiter.png"));
    Image shared = b.getImage();
    int xGap = 17;
    int yGap = 27;

	    public OmarWaiterGui(OmarWaiterRole agent, OmarRestaurantAnimationPanel gui) {
	        this.agent = agent;
	        this.gui = gui;
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
		        
		        if(xPos == xDestination && yPos == yDestination && gotAct){
		        	gotAct = false;
			    	agent.msgArrived();
			    	DoGoIdle();
			    	return;
			    }
	    }
	  

	    public void draw(Graphics2D g) {
	    	if(agent.active){
    			g.drawImage(normal, xPos, yPos, xGap, yGap, this);
    			
		        if(xPos == HOMEX && yPos == HOMEY){
		        	g.drawString("Home", xPos + 5, yPos - 5);
		        } else {
		        	g.drawString(currentStatus, xPos + 5, yPos - 5);
		        }
	    	}
	    }

	    public boolean isPresent() {
	        return true;
	    }
	    
	    public void setCurrentStatus(String newStatus){
	    	currentStatus = newStatus;
	    }
	    
	    public void DoGetCustomer(OmarCustomerRole customer, Table table){
	    	/*xDestination = -1 * TOFFSET + 100;
	    	yDestination = -1 * TOFFSET + 30; */
	    	
	    	setCurrentStatus("Customer");
	    	xDestination = customer.getGui().getX() - 20;
	    	yDestination = customer.getGui().getY() + 20;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoBringToTable(OmarCustomerRole customer, Table table) {
	    	setCurrentStatus("Going to Table");
	    	xDestination = table.getX() + TOFFSET;
	    	yDestination = table.getY() - TOFFSET;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoTakeCustomerOrder(OmarCustomerRole c, Table table){
	    	setCurrentStatus("Taking Order");
	    	xDestination = table.getX() + TOFFSET;
	    	yDestination = table.getY() - TOFFSET;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGiveOrderToCook(){
	    	setCurrentStatus("Giving Order To Cook");
	    	xDestination = COOKLOCX;
	    	yDestination = COOKLOCY;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGoToRevolvingStand(){
	    	setCurrentStatus("Rev. Stand");
	    	xDestination = REVOLVINGX;
	    	yDestination = REVOLVINGY;
	    			
	    	gotAct = true;
	    }
	    
	    public void DoGetFoodFromCook(){
	    	setCurrentStatus("Getting Food");
	    	xDestination = 540;
	    	yDestination = 220;
	    	
	    	gotAct = true;
	    }
	    
	    public void DoGiveCustomerFood(OmarCustomerRole c, Table table){
	    	setCurrentStatus("Food");
	    	xDestination = table.getX() + TOFFSET;
	    	yDestination = table.getY() - TOFFSET;
	    	
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
	    	xDestination = table.getX() + TOFFSET;
	    	yDestination = table.getY() - TOFFSET;
	    	
	    	gotAct = true;
	    }

	    public void DoGoIdle() {
	    	setCurrentStatus("Idle");
	        xDestination = HOMEX;
	        yDestination = HOMEY;
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
	    //gui.setWaiterBreakBoxEnabled(this.agent);
	    }
	    
	    public void setHomePosition(int x, int y){
	    	HOMEX = x;
	    	HOMEY = y;
	    	setCurrentStatus("Idle");
	    	xDestination = HOMEX;
	    	yDestination = HOMEY;
	    	
	    	gotAct = true;
	    }

}
