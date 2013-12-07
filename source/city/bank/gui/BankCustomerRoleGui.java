package city.bank.gui;

import gui.Gui;


import java.awt.Color;
import java.awt.Graphics2D;

import city.bank.BankCustomerRole;

public class BankCustomerRoleGui implements Gui {

	private BankCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean flag = true;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private int lineX = 352;
	private int lineY = 180;
	private int tellerX = 572;
	private int tellerY = 195;
	private int exitX = 321;
	private int exitY = 380;
	
	private int robberX = 0;
	private int robberY = 0;
	
	Color myColor = Color.green;
	
	public BankCustomerRoleGui(BankCustomerRole c){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 100; // change these
		yDestination = 30;
	}
	
	@Override
	public void updatePosition() {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;

			if (xPos == xDestination && yPos == yDestination && !flag) {
				if(xPos == tellerX + 5 && yDestination == tellerY - 5){
					DoRun();
				} else{
					agent.releaseSemaphore();
					flag = true;
				}
			}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent()){
			g.setColor(myColor);
			g.fillRect(xPos, yPos, 20, 20);
		}
	}

	@Override
	public boolean isPresent() {
		if(agent.active){
			return true;
		}
		
		return false;
	}
	
	public void dead(){
		myColor = Color.red;
		xDestination = xPos;
		yDestination = yPos;
		flag = true;
	}

	public void DoGoToLine() {
		isPresent = true;
		xDestination = lineX;
		yDestination = lineY;
		flag = false;
	}

	public void DoGoToTeller(int tellerNum) {
		xDestination = tellerX;
		yDestination = tellerY;
		flag = false;
	}

	public void DoLeaveBank() {
		xDestination = exitX;
		yDestination = exitY;
		flag = false;
	}

	public void DoRobBank() {
		myColor = Color.orange;
		xDestination = tellerX + 5;
		yDestination = tellerY - 5;
		flag = false;
	}
	
	public void DoRun() {
		xDestination = robberX;
		yDestination = robberY;
		flag = false;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}

}
