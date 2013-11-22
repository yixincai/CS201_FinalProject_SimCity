package city.market.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.market.*;

public class MarketEmployeeGui extends JPanel implements Gui {

    private MarketEmployeeRole role = null;

    private int xPos = 300, yPos = 30;//default waiter position
    public static int xGap = 40;
    public static int yGap = 40;
    private int xDestination = 300, yDestination = 30;//default waiter position
	private enum Command {noCommand, GoToSeat};
	private Command command=Command.noCommand;
    
    private ImageIcon i = new ImageIcon("image/cashier.jpg");
    private Image image = i.getImage();  
    
    public MarketEmployeeGui(MarketEmployeeRole agent) {
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
        	System.out.println("release semaphore");
        	command = Command.noCommand;
        	role.msgAnimationFinished();
        }
    }

    public void draw(Graphics2D g) {
    	g.drawImage(image, xPos, yPos, xGap, yGap, this);
    }
    
	public void PickUp(String item){
		if (item.equals("Meal")){
			//xDestination = ??;
		}
		else if (item.equals("Car")){
			//xDestination = ??;
		}
		else if (item.equals("Steak")){
			//xDestination = ??;
		}
		else if (item.equals("Chicken")){
			//xDestination = ??;
		}
		else if (item.equals("Salad")){
			//xDestination = ??;
		}
		else if (item.equals("Pizza")){
			//xDestination = ??;
		}
		command=Command.GoToSeat;		
	}
	
	public void GoToCashier(){
		//xDestination = ??;
		command=Command.GoToSeat;
	}
	
	public void GoToTruck(){
		//xDestination = ??;
		command=Command.GoToSeat;
	}
	
	public void GoHome(){
		//xDestination = ??;
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
