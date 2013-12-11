package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.bank.BankCustomerRole;

public class BankCustomerRoleGui extends JPanel implements Gui {

	private BankCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isRobber = false;
	private boolean isDead = false;
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
	
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/bank/Skull.png"));
	int xGap = 20;
	int yGap = 23;
	private Image skull = a.getImage();
	
	private ImageIcon b = new ImageIcon(this.getClass().getResource("/image/bank/Robber.png"));
	int xRGap = 25;
	int yRGap = 32;
	private Image robber = b.getImage();
	
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
		if(isPresent()){
			if(isDead){
				g.drawImage(skull, xPos, yPos, xGap, yGap, this);
			}
			else if(isRobber && !isDead){
				g.drawImage(robber, xPos, yPos, xRGap, yRGap, this);
			}
			else{
				g.drawImage(agent.getImage(), xPos, yPos, 20, 27, null);
			}
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
		isDead = true;
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
		isRobber = true;
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
