package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.bank.BankTellerRole;
import city.bank.interfaces.BankTeller;

public class BankHostRoleGui implements Gui {

	static int width = 20;
	static int height = 20;
	static int xCoord = 382;
	static int yCoord = 360/2;
	
	
	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xCoord, yCoord, width, height);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public void DoCallTeller(BankTeller t) {
		// TODO Auto-generated method stub
		
	}
	
	public void DoLeaveBank() {
		// TODO
	}

}
