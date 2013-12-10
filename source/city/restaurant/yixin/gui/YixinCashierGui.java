package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinCashierRole;

public class YixinCashierGui extends JPanel implements Gui {

	private YixinCashierRole agent = null;

	private int xPos = 300, yPos = 0;//default waiter position

	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Cashier.png"));
    Image cashier = a.getImage();
    int xGap = 19;
    int yGap = 25;
    
	public YixinCashierGui(YixinCashierRole agent) {
		this.agent = agent;
	}

	public void updatePosition() {
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.pink);
		if(agent.active){
			//g.fillRect(xPos, yPos, xGap, yGap);    	
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

	public void LeaveRestaurant(){

	}
}
