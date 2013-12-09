package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinHostRole;

public class YixinHostGui extends JPanel implements Gui {

	private YixinHostRole agent = null;

	private int xPos = 120, yPos = 0;//default waiter position
	public static int xGap = 20;
	public static int yGap = 20;

	//private ImageIcon i = new ImageIcon(this.getClass().getResource("image/host.png"));
	//private Image image = i.getImage();
	public YixinHostGui(YixinHostRole agent) {
		this.agent = agent;
	}

	public void LeaveRestaurant(){

	}

	public void updatePosition() {
	}

	public void draw(Graphics2D g) {
		if(agent.active){
			g.setColor(Color.BLUE);
			g.fillRect(xPos, yPos, xGap, yGap);
			g.drawString("Host", xPos, yPos - 10);
			//g.drawImage(image, xPos, yPos, xGap, yGap, this);
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
