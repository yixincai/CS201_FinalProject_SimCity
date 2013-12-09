package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinCashierRole;

public class YixinCashierGui extends JPanel implements Gui {

	private YixinCashierRole agent = null;

	private int xPos = 300, yPos = 0;//default waiter position
	public static int xGap = 20;
	public static int yGap = 20;

	private ImageIcon i = new ImageIcon(this.getClass().getResource("/image/cashier.jpg"));
	private Image image = i.getImage();
	public YixinCashierGui(YixinCashierRole agent) {
		this.agent = agent;
	}

	public void updatePosition() {
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.pink);
		if(agent.active){
			//g.fillRect(xPos, yPos, xGap, yGap);    	
			g.drawImage(image, xPos, yPos, xGap, yGap, this);
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

	public void LeaveRestaurant(){

	}
}
