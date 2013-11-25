package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.bank.BankTellerRole;
import city.bank.interfaces.BankTeller;

public class BankHostRoleGui implements Gui {

	private int width = 20;
	private int height = 20;
	private int xCoord = 382;
	private int yCoord = 180;
	
	
	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xCoord, yCoord, width, height);
	}

	@Override
	public boolean isPresent() { //Looking at Yixin's gui it seems like we just return true. 
		//in the restaurant, though, isPresent is set to true when they get hungry.
		// TODO Auto-generated method stub
		return true;
	}

	public void DoCallTeller(BankTeller t) {
		// TODO Auto-generated method stub
		
	}
	
	public void DoLeaveBank() {
		
	}

}
