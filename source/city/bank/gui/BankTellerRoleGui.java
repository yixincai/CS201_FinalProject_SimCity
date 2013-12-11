package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.bank.BankCustomerRole;
import city.bank.BankTellerRole;

public class BankTellerRoleGui extends JPanel implements Gui {

	private int xCoord = 622;
	private int yCoord = 195;
	private int width = 20;
	private int height = 20;
	
	private BankTellerRole agent = null;
	
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/bank/BankTeller.png"));
	int xGap = 20;
	int yGap = 29;
	private Image teller = a.getImage();
	
	public BankTellerRoleGui(BankTellerRole t){
		agent = t;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent()){
			g.drawImage(teller, xCoord, yCoord, xGap, yGap, this);
		}
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
