package city.restaurant.yixin.gui;

import gui.Gui;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import city.restaurant.yixin.YixinCookRole;

public class YixinCookGui extends JPanel implements Gui{

	private YixinCookRole agent;
	private ImageIcon i = new ImageIcon("source/image/cook.jpg");
	private Image image = i.getImage();
	private String food = "";
	private boolean show_food = false;

	public YixinCookGui(YixinCookRole c){
		this.agent = c;
	}

	private int xPos = 200, yPos = 290, xPlate = 200, xCooking = 300, xFridge = 400;//default cook position
	private int xDestination = 200, yDestination = 290;
	public static int xGap = 30;
	public static int yGap = 30;
	private enum Command {noCommand, GoToSeat};
	private Command command=Command.noCommand;

	public void DoGoCookFood(){
		xDestination = xCooking;
		command = Command.GoToSeat;
	}

	public void DoCookFood(String choice){
		this.food = choice;
		show_food = true;
	}

	public void DoFinishFood(){
		this.food = "";
		show_food = false;
	}

	public void DoGoToFridge(){
		xDestination = xFridge;
		command = Command.GoToSeat;
	}

	public void DoPutPlate(){
		xDestination = xPlate;
		command = Command.GoToSeat;
	}

	public void DoGoToRevolvingStand(){
		xDestination = xPlate - 50;
		command = Command.GoToSeat;
	}

	public void DoGoHome(){
		xDestination = xCooking;
	}

	public void LeaveRestaurant(){
		xDestination = 0;
		yDestination = 0;
		command = Command.GoToSeat;
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
			agent.releaseSemaphore();
		}
	}

	public void draw(Graphics2D g) {
		if(agent.active){
			g.setColor(Color.BLUE);
			g.fillRect(xPos, yPos, xGap, yGap);    	
			//g.drawImage(image, xPos, yPos, xGap, yGap, this);
			if (show_food)
				g.drawString(food, xCooking, yPos - 50);
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
