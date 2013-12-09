package city.restaurant.yixin.gui;

import gui.Gui;
import java.awt.*;
import javax.swing.*;

import city.restaurant.yixin.YixinCustomerRole;

public class YixinCustomerGui extends JPanel implements Gui{

	private YixinCustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;

	//private HostAgent host;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoToCashier};
	private Command command=Command.noCommand;
	private int count = 0;

	public static final int TableX = 500;
	public static final int TableY1 = 60, TableY2 = 130, TableY3 = 200;
	private final int GAPX = 30;
	private final int GAPY = 30;
	private final int OriginX = -60;
	private final int OriginY = -60;

	private ImageIcon i = new ImageIcon(this.getClass().getResource("/image/customer.jpg"));
	private Image image = i.getImage();

	private String choice;
	private boolean show_choice = false;

	public YixinCustomerGui(YixinCustomerRole c, int count){ 
		agent = c;
		xPos = OriginX;
		yPos = OriginY;
		xDestination = OriginX;
		yDestination = OriginY;
		//maitreD = m;
		this.count = count;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat)
				agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			}
			else if (command==Command.GoToCashier) 
				agent.msgAnimationFinishedGoToCashier();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		if(agent.active){
			g.setColor(Color.BLUE);
			//g.fillRect(xPos, yPos, GAPX, GAPY);    	
			g.drawImage(image, xPos, yPos, GAPX, GAPY, this);
			if (show_choice)
				g.drawString(this.choice, xDestination, yDestination + 40);
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.cmdGotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if (seatnumber == 1){
			xDestination = TableX;
			yDestination = TableY1;
		}
		else if(seatnumber == 2){
			xDestination = TableX;
			yDestination = TableY2;
		}
		else if (seatnumber == 3){
			xDestination = TableX;
			yDestination = TableY3;
		}
		command = Command.GoToSeat;
		show_choice = false;
	}

	public void DoGoToCashier() {
		show_choice = false;
		choice = "";
		xDestination = 300;
		yDestination = 0 + GAPY;
		command = Command.GoToCashier;
	}

	public void DoGoWaiting() {
		show_choice = false;
		choice = "";
		xDestination = 30;
		yDestination = count*30+15;
		command = Command.GoToCashier;
	}

	public void DoGoToJail() {
		xDestination = 50;
		yDestination = 170;
	}

	public void showOrderFood(String choice){
		this.choice = choice + "?";
		show_choice = true;
	}

	public void eatFood(String choice){
		this.choice = choice;
		show_choice = true;
	}

	public void DoExitRestaurant() {
		xDestination = OriginX;
		yDestination = OriginY;
		command = Command.LeaveRestaurant;
	}

}
