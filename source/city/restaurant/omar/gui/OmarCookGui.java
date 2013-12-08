package city.restaurant.omar.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.restaurant.omar.OmarCookRole;

public class OmarCookGui extends JPanel implements Gui {

	private static int XGRILL0 = 550; //change numbers to grill positions
	private static int YGRILL0 = 200;
	
	private static int XGRILL1 = 590;
	private static int YGRILL1 = 200;
	
	private static int XGRILL2 = 630;
	private static int YGRILL2 = 200;
	
	private static int REVOLVINGX = 550;
	private static int REVOLVINGY = 300;
	
	private static int GRILLOFFSETY = 20;
	
	private OmarCookRole agent = null;
	private Color myColor = Color.CYAN;
	private boolean flag = false;
	
	private String currentStatus = "";
	private String pickupStatus = "";

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Chef.png"));
    Image cook = a.getImage();
    int xGap = 18;
    int yGap = 32;
	
	public OmarCookGui(OmarCookRole c){
		agent = c;
		xPos = 590;
		yPos = 240;
		xDestination = 590;
		yDestination = 240;
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
			if(!(xPos == 590 && yPos == 240)){
				agent.msgArrived();
			}
				flag = false;
				if(xPos == 590 && yPos == 140){
					setPickupStatus("PICKUP");
				}
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(cook, xPos, yPos, xGap, yGap, this);
		
		g.drawString(currentStatus, xPos + 5, yPos - 5);
		g.drawString(pickupStatus, 590, 175);
	}
	
	public void setPickupStatus(String pickupStatus){
		this.pickupStatus = pickupStatus;
	}
	
	public void setCurrentStatus(String newStatus){
		currentStatus = newStatus;
	}
	
	public void DoGoToFridge(){
		xDestination = 610;
		yDestination = 255;
		
		flag = true;
	}
	
	public void DoGoToRevolvingStand(){
		xDestination = REVOLVINGX;
		yDestination = REVOLVINGY;
		
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
		xDestination = 590;
		yDestination = 240;
		flag = true;
	}
	
	public void DoMoveFoodToPlatingArea(){
		setPickupStatus("PICKUP");
		xDestination = 590;
		yDestination = 140;
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

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
}
