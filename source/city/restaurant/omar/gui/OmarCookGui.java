package city.restaurant.omar.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class OmarCookGui {

	private static int XGRILL0 = 500; //change numbers to grill positions
	private static int YGRILL0 = 400;
	
	private static int XGRILL1 = 540;
	private static int YGRILL1 = 400;
	
	private static int XGRILL2 = 580;
	private static int YGRILL2 = 400;
	
	private static int GRILLOFFSETY = 20;
	
	private CookAgent agent = null;
	private Color myColor = Color.CYAN;
	private boolean flag = false;
	
	private String currentStatus = "";
	private String pickupStatus = "";

	private RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;

	public OmarCookGui(CookAgent c, RestaurantGui gui){
		agent = c;
		xPos = 540;
		yPos = 440;
		xDestination = 540;
		yDestination = 440;
		
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

		if (xPos == xDestination && yPos == yDestination && flag) {
			if(!(xPos == 540 && yPos == 440)){
				agent.msgArrived();
			}
				flag = false;
				if(xPos == 540 && yPos == 340){
					setPickupStatus("PICKUP");
				}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(myColor);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.drawString(currentStatus, xPos + 5, yPos - 5);
		g.drawString(pickupStatus, 520, 340);
	}
	
	public void setPickupStatus(String pickupStatus){
		this.pickupStatus = pickupStatus;
	}
	
	public void setCurrentStatus(String newStatus){
		currentStatus = newStatus;
	}
	
	public void DoGoToFridge(){
		xDestination = 560;
		yDestination = 470;
		
		flag = true;
	}
	
	public void DoGoToGrill(int tableNum) {
		setCurrentStatus("Cking");
		int grillNum = (int)(Math.random() * 3);
    		if(grillNum == 0){
    			xDestination = XGRILL0;
    			yDestination = YGRILL0 + GRILLOFFSETY;
    		} else if(grillNum == 1){
    			xDestination = XGRILL1;
    			yDestination = YGRILL1 + GRILLOFFSETY;
    		} else {
    			xDestination = XGRILL2;
    			yDestination = YGRILL2 + GRILLOFFSETY;
    		}
    		flag = true;
	}
	
	public void DoGoBackToRest(){
		setCurrentStatus("");
		xDestination = 540;
		yDestination = 440;
		flag = true;
	}
	
	public void DoMoveFoodToPlatingArea(){
		setPickupStatus("PICKUP");
		xDestination = 540;
		yDestination = 340;
		flag = true;
	}
	
	
	//utilities
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	private void setColor(Color c){
		this.myColor = c;
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}
