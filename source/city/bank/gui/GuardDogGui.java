package city.bank.gui;

import gui.Gui;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Color;
import java.awt.Graphics2D;

import city.bank.BankCustomerRole;
import city.bank.GuardDog;

public class GuardDogGui implements Gui {

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
			g.setColor(myColor);
			g.fillRect(xPos, yPos, 10, 10);
	}

	@Override
	public boolean isPresent() {
		return true;
	}


}
