package city.restaurant.omar.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.restaurant.omar.OmarHostRole;
import city.restaurant.omar.Table;

public class OmarRestaurantAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 682;
    private final int WINDOWY = 360;
    private static int WINDOWC = 0;
    
    private Image bufferImage;
    private Dimension bufferSize;
    
    private ArrayList<Table> tables;
    
    private static int TABLEHEIGHT = 50;
    private static int TABLEWIDTH = 50;
    
    private static int TIMER = 5;

    private List<Gui> guis = new ArrayList<Gui>();
    OmarHostRole host;

    public OmarRestaurantAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMER, this );
    	timer.start();
    }
    
    public void setHost(OmarHostRole h){
    	this.host = h;
    	setTableList(h.getTableList());
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(WINDOWC, WINDOWC, WINDOWX, WINDOWY );

        //Waiting Area
        g2.setColor(Color.YELLOW);
        g2.fillRect(100, 0, 50, 50);
        g2.setColor(Color.BLACK);
        g2.drawString("Waiting Area", 155, 30);
        
        //Cooking Area
        g2.setColor(Color.GRAY);
        g2.fillRect(550, 200, 100, 75);
        g2.setColor(Color.BLACK);
        g2.drawString("Cooking Area", 560, 300);
        
        //Plating Area
        g2.setColor(Color.PINK);
        g2.fillRect(550, 130, 100, 75);
        g2.setColor(Color.BLACK);
        g2.drawString("Plating Area", 560, 110);
        
        //Grills
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(550, 200, 20, 20);
        g2.fillRect(590, 200, 20, 20);
        g2.fillRect(630, 200, 20, 20);
        
        //Fridge
        g2.setColor(Color.lightGray);
        g2.fillRect(630, 255, 20, 20);
        g2.drawString("Fridge", 620, 285);
        
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        
        for(int i = 0; i < host.getTableList().size(); i++){
        	g2.fillRect(host.getTableList().get(i).getX(), host.getTableList().get(i).getY(), TABLEWIDTH, TABLEHEIGHT);
        //	tables.add(host.getTableList().get(i));
        }
        
        g2.setColor(Color.MAGENTA); // add waiter guis

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(OmarCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(OmarWaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(OmarCookGui gui) {
    	guis.add(gui);
    }
    
    public void setTableList(ArrayList<Table> list){
    	tables = list;
    }
}
