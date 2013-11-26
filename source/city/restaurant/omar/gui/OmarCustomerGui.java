package city.restaurant.omar.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.restaurant.omar.OmarCustomerRole;

public class OmarCustomerGui implements Gui {

	private OmarCustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean sentencedToDeath = false;
	private Color myColor = Color.GREEN;
	
	private String currentStatus = "Hungry";

	private OmarRestaurantAnimationPanel gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public OmarCustomerGui(OmarCustomerRole c, OmarRestaurantAnimationPanel gui){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 100;
		yDestination = 30;
		
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

		if (xPos == xDestination && yPos == yDestination) {
			if(sentencedToDeath){
				setColor(Color.RED);
				setCurrentStatus("DEAD");
			}
			
			if(xDestination == 15 && yDestination == 200){
				agent.msgArrived();
			}
			if (command==Command.GoToSeat) {agent.msgAnimationFinishedGoToSeat();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}
	
	public void leftWaitingList(){
		command = Command.LeaveRestaurant;
		xDestination = -40;
		yDestination = -40;
	}

	public void draw(Graphics2D g) {
		g.setColor(myColor);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.drawString(currentStatus, xPos + 5, yPos - 5);
	}
	
	public void setCurrentStatus(String newStatus){
		currentStatus = newStatus;
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber, int tableNum) {
    		xDestination = tableNum*100 + 100;
    		yDestination = 100;
    		
    	;
		command = Command.GoToSeat;
	}
	
	public void DoGoToCashier() {
		setCurrentStatus("Paying");
		xDestination = 15;
		yDestination = 200;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoDie(){
		setCurrentStatus("Paying with Life");
		xDestination = 450;
		yDestination = 200;
		
		sentencedToDeath = true;
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
}
