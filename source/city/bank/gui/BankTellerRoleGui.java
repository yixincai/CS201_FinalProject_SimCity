package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;

public class BankTellerRoleGui implements Gui {

	private int xCoord = 622;
	private int yCoord = 195;
	private int width = 20;
	private int height = 20;
	
	private BankTellerRole agent = null;
	
	public BankTellerRoleGui(BankTellerRole t){
		agent = t;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xCoord, yCoord, width, height);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		if(agent.active){
			return true;
		}
		
		return false;
	}

	public void DoCallSecurity() {
		// TODO Auto-generated method stub
		
	}
	
	public void DoLeaveBank() {
		
	}

}
