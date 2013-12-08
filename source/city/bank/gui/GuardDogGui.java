package city.bank.gui;

import gui.Gui;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.bank.BankCustomerRole;
import city.bank.GuardDog;

public class GuardDogGui extends JPanel implements Gui {

	private GuardDog agent = null;
	private boolean isPresent = true;
	private boolean flag = true;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean attacking = false;
	private BankCustomerRole hit;
	
	private int RESTX = 100;
	private final int RESTY = 30;
	
	Color myColor = Color.DARK_GRAY;
	
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/bank/Dog.png"));
	int xGap = 18;
	int yGap = 30;
	private Image dog = a.getImage();
	
	public GuardDogGui(GuardDog guardDog){
		agent = guardDog;
		xPos = RESTX;
		yPos = RESTY;
		xDestination = RESTX;
		yDestination = RESTY;
	}
	
	public void updatePosition() { //moves twice as fast as everyone else
		if(attacking){
			xDestination = this.hit.getGui().getX();
			yDestination = this.hit.getGui().getY(); 
		}
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			
			if(attacking){
				if (xPos < xDestination)
					xPos++;
				else if (xPos > xDestination)
					xPos--;
	
				if (yPos < yDestination)
					yPos++;
				else if (yPos > yDestination)
					yPos--;
			}

			if (xPos == xDestination && yPos == yDestination && !flag) {
					AlertLog.getInstance().logDebug(AlertTag.BANK, "Guard Dog", "Target Neutralized");
					hit.dead();
					agent.releaseSem();
					flag = true;
					attacking = false;
					xDestination = RESTX;
					yDestination = RESTY;
			}
	}
	
	public void DoAttack(BankCustomerRole hit){
		this.hit = hit;
		xDestination = hit.getGui().getX();
		yDestination = hit.getGui().getY();
		attacking = true;
		flag = false;
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(dog, xPos, yPos, xGap, yGap, this);
	}

	@Override
	public boolean isPresent() {
		return true;
	}


}
