package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinHostRole;

public class YixinHostGui extends JPanel implements Gui {

	private YixinHostRole agent = null;

	private int xPos = 120, yPos = 0;//default waiter position

	private ImageIcon i = new ImageIcon(this.getClass().getResource("/image/restaurant/Host.png"));
	private Image image = i.getImage();
	public static int xGap = 21;
	public static int yGap = 30;
	
	public YixinHostGui(YixinHostRole agent) {
		this.agent = agent;
	}

	public void LeaveRestaurant(){

	}

	public void updatePosition() {
	}

	public void draw(Graphics2D g) {
		if(agent.active){
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

}
