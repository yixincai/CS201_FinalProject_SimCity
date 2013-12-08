package city.market.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.market.MarketCashierRole;

public class MarketCashierGui extends JPanel implements Gui {

    private MarketCashierRole role = null;

    private int xPos = 230, yPos = 130;//default waiter position
    private int xDestination = 230, yDestination = 130;//default waiter position
	private enum Command {noCommand, GoToSeat};
	private Command command=Command.noCommand;
	
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/market/MarketCashier.png"));
	int xGap = 17;
	int yGap = 24;
	private Image cashier = a.getImage();
	
    public MarketCashierGui(MarketCashierRole agent) {
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
    
	public void LeaveMarket(){
		xDestination = -40;
		yDestination = -40;
		command = Command.GoToSeat;
	}
	
	public void enterMarket(){
		//xDestination = ??;
		command = Command.GoToSeat;
	}

    public void draw(Graphics2D g) {
		if(role.active){
			g.drawImage(cashier, xPos, yPos, xGap, yGap, this);
		}
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
