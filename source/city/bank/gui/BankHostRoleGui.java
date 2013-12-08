package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.bank.BankCustomerRole;
import city.bank.BankHostRole;
import city.bank.BankTellerRole;
import city.bank.interfaces.BankTeller;

public class BankHostRoleGui extends JPanel implements Gui {

	private BankHostRole agent = null;
	
	private int width = 20;
	private int height = 20;
	private int xCoord = 382;
	private int yCoord = 180;
	
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/bank/BankHost.png"));
	int xGap = 20;
	int yGap = 34;
	private Image host = a.getImage();
	
	public BankHostRoleGui(BankHostRole h){
		agent = h;
	}
	
	@Override
	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent()){
			g.drawImage(host, xCoord, yCoord, xGap, yGap, this);
		}
	}	

	@Override
	public boolean isPresent() { 
		if(agent.active){
			return true;
		} else{
			return false;
		}
	}

	public void DoCallTeller(BankTeller t) {
		// TODO Auto-generated method stub
		
	}
	
	public void DoLeaveBank() {
		
	}

}
