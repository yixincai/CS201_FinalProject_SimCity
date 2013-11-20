package city.market.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.market.MarketCashierRole;

public class MarketCashierGui extends JPanel implements Gui {

    private MarketCashierRole role = null;

    private int xPos = 300, yPos = 30;//default waiter position
    public static int xGap = 40;
    public static int yGap = 40;
    
    private ImageIcon i = new ImageIcon("image/cashier.jpg");
    private Image image = i.getImage();
    public MarketCashierGui(MarketCashierRole agent) {
        this.role = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {
    	g.drawImage(image, xPos, yPos, xGap, yGap, this);
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
