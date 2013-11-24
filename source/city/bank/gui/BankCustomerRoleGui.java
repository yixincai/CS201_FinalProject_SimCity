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
	private int lineX = 382;
	private int lineY = 180;
	private int tellerX = 572;
	private int tellerY = 195;
	private int exitX = 321;
	private int exitY = 380;
	
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
					agent.releaseSemaphore();
					flag = true;
			}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.green);
		g.fillRect(xPos, yPos, 20, 20);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void DoGoToLine() {
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
		// TODO Auto-generated method stub
		//DONT DO TANNER, but please set up the animation panel to look nice.  Desk on the right as i showed
	}

}
