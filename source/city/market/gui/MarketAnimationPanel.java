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

        g2.setColor(Color.GREEN);
        g2.fillRect(counter_x, counter_y, counter_width, counter_height);
        g2.fillRect(inventory_x, inventory_y, inventory_width, inventory_height);
                
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
}
