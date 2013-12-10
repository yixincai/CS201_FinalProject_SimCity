package city.market.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.market.*;

public class MarketCustomerGui extends JPanel implements Gui {

    private MarketCustomerRole role = null;

    private int xPos = -50, yPos = -50;//default waiter position
    public static int xGap = 40;
    public static int yGap = 40;
    private int xDestination = -50, yDestination = -50;//default waiter position
	private enum Command {noCommand, GoToSeat};
	private Command command=Command.noCommand;
    
    private ImageIcon i = new ImageIcon("image/cashier.jpg");
    private Image image = i.getImage();
    
    public MarketCustomerGui(MarketCustomerRole agent) {
        this.role = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;
        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        if (yPos == yDestination && xPos == xDestination && command == Command.GoToSeat){
        	//System.out.println("release semaphore");
        	command = Command.noCommand;
        	role.msgAnimationFinished();
        }
    }

    public void draw(Graphics2D g) {
		if(role.active){
			g.drawImage(role.getImage(), xPos, yPos, 20, 27, null);
			//g.drawImage(image, xPos, yPos, xGap, yGap, this);
		}
    }
    
	public void GoToCashier(){
		xDestination = 200 - xGap;
		yDestination = 130;
		command = Command.GoToSeat;
	}

	public void GoToWaitingArea(){
		xDestination = 50;
		yDestination = 50;
		command = Command.GoToSeat;
	}
	
	public void LeaveMarket(){
		xDestination = -50;
		yDestination = -50;
		command = Command.GoToSeat;
	}

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
