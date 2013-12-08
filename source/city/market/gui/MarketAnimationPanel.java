package city.market.gui;

import gui.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends JPanel implements ActionListener {
    private final int counter_x = 200;
    private final int counter_y = 0;
    private final int counter_width = 20;
    private final int counter_height = 300;
    private final int inventory_x = 400;
    private final int inventory_y = 0;
    private final int inventory_width = 180;
    private final int inventory_height = 50;
    private final int ICON_X = 50;
    private final int ICON_Y = 50;    
    private final int WINDOWX = 682;
    private final int WINDOWY = 360;
    List<Boolean> restaurant_order = new ArrayList<Boolean>();
    private List<Gui> guis = new ArrayList<Gui>();
    
    private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/market/MarketTile.png"));
    private Image marketTile = a.getImage();
    int xFGap = 682;
    int yFGap = 360;

    private ImageIcon b = new ImageIcon(this.getClass().getResource("/image/market/MarketCounter.png"));
    private Image counter = b.getImage();
    int xCGap = 20;
    int yCGap = 317;

    private ImageIcon c = new ImageIcon(this.getClass().getResource("/image/market/Inventory.png"));
    private Image inventory = c.getImage();
    int xIGap = 200;
    int yIGap = 70;

    private ImageIcon d = new ImageIcon(this.getClass().getResource("/image/market/Car.png"));
    private Image car = d.getImage();
    int xCarGap = 128;
    int yCarGap = 70;

    private ImageIcon e = new ImageIcon(this.getClass().getResource("/image/market/Bikes.png"));
    private Image bikes = e.getImage();
    int xBGap = 55;
    int yBGap = 60;

    private ImageIcon f = new ImageIcon(this.getClass().getResource("/image/market/Chest.png"));
    private Image chest = f.getImage();
    int xChGap = 42;
    int yChGap = 35;

    public MarketAnimationPanel()
    {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }
    
    public void showRestaurantOrder(int index){
    	restaurant_order.add(true);
    }

    public void hideRestaurantOrder(int index){
    	restaurant_order.remove(0);
    }
    
	public void actionPerformed(ActionEvent e) {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		
		repaint();  //Will have paintComponent called
	}
	
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table

        g.drawImage(marketTile, 0, 0, xFGap, yFGap, this);
        g.drawImage(counter, counter_x, counter_y, xCGap, yCGap, this);
        g.drawImage(inventory, inventory_x, inventory_y, xIGap, yIGap, this);
        g.drawImage(car, 450, 250, xCarGap, yCarGap, this);
        g.drawImage(bikes, 340, 250, xBGap, yBGap, this);
        g.drawImage(bikes, 270, 250, xBGap, yBGap, this);
        g.drawImage(chest, 340, 30, xChGap, yChGap, this);
        g.drawImage(chest, 270, 30, xChGap, yChGap, this);
                
        for (int i = 0; i < restaurant_order.size(); i++){
        	if (restaurant_order.get(i).booleanValue())
                g2.fillRect( 600, i*ICON_X , ICON_X, ICON_Y);        		
        }
        //here is the cooking area 

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void addGui(MarketCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(MarketCashierGui gui) {
        guis.add(gui);
    }
    
    public void addGui(MarketEmployeeGui gui) {
        guis.add(gui);
    }
    
    /*
	private ImageIcon a = new ImageIcon(this.getClass().getResource("/image/market/MarketTile.png"));
private Image marketTile = a.getImage();
int xFGap = 682;
int yFGap = 360;

private ImageIcon b = new ImageIcon(this.getClass().getResource("/image/market/MarketCounter.png"));
private Image counter = b.getImage();
int xCGap = 20;
int yCGap = 317;

private ImageIcon c = new ImageIcon(this.getClass().getResource("/image/market/Inventory.png"));
private Image inventory = c.getImage();
int xIGap = 200;
int yIGap = 70;

private ImageIcon d = new ImageIcon(this.getClass().getResource("/image/market/Car.png"));
private Image car = d.getImage();
int xCarGap = 128;
int yCarGap = 70;

private ImageIcon e = new ImageIcon(this.getClass().getResource("/image/market/Bikes.png"));
private Image bikes = e.getImage();
int xBGap = 55;
int yBGap = 60;

private ImageIcon f = new ImageIcon(this.getClass().getResource("/image/market/Chest.png"));
private Image chest = f.getImage();
int xChGap = 42;
int yChGap = 35;
	
	g.drawImage(marketTile, 0, 0, xFGap, yFGap, this);
    g.drawImage(counter, counter_x, counter_y, xCGap, yCGap, this);
    g.drawImage(inventory, inventory_x, inventory_y, xIGap, yIGap, this);
    g.drawImage(car, 400, 275, xCarGap, yCarGap, this);
    g.drawImage(bikes, 340, 250, xBGap, yBGap, this);
    g.drawImage(chest, 350, 40, xChGap, yChGap, this);
    g.drawImage(chest, 290, 40, xChGap, yChGap, this);
 
 */
}
