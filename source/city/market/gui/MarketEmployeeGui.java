package city.market.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.market.*;

public class MarketEmployeeGui extends JPanel implements Gui {

    private MarketEmployeeRole role = null;

    private int xPos = 400, yPos = 100;//default waiter position
    public static int xGap = 30;
    public static int yGap = 30;
    private int xDestination = 400, yDestination = 100;//default waiter position
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
        	//System.out.println("release semaphore");
        	command = Command.noCommand;
        	role.msgAnimationFinished();
        }
    }

    public void draw(Graphics2D g) {
		if(role.active){
			g.fillRect(xPos, yPos, xGap, yGap);
			//g.drawImage(image, xPos, yPos, xGap, yGap, this);
		}
    }
    
	public void PickUp(String item){
		if (item.equals("Meal")){
			System.out.println("Getting meal");
			xDestination = 400;
			yDestination = 50;
		}
		else if (item.equals("Car")){
			System.out.println("Getting car");
			xDestination = 430;
			yDestination = 50;
		}
		else if (item.equals("Steak")){
			System.out.println("Getting Steak");
			xDestination = 460;
			yDestination = 50;
		}
		else if (item.equals("Chicken")){
			System.out.println("Getting chicken");
			xDestination = 490;
			yDestination = 50;
		}
		else if (item.equals("Salad")){
			System.out.println("Getting salad");
			xDestination = 520;
			yDestination = 50;
		}
		else if (item.equals("Pizza")){
			System.out.println("Getting pizza");
			xDestination = 450;
			yDestination = 50;
		}
		command=Command.GoToSeat;		
	}
	
	public void GoToCashier(){
		xDestination = 275;
		yDestination = 130;
		command=Command.GoToSeat;
	}
	
	public void GoToTruck(){
		xDestination = 650 - xGap;
		command=Command.GoToSeat;
	}
	
	public void GoHome(){
		xDestination = 400;
		yDestination = 100;
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
