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

import javax.swing.ImageIcon;
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
    
    private static int TIMER = 10;

    private List<Gui> guis = new ArrayList<Gui>();
    OmarHostRole host;
    
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Fridge.png"));
    Dimension FridgePlace = new Dimension(630, 255);
    Image fridge = a.getImage();
    int xFGap = 25;
    int yFGap = 48;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/SingleGrill.png"));
    Image grill = b.getImage();
    int xGGap = 28;
    int yGGap = 40;
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/restaurant/RevolvingStand.png"));
    Dimension StandPlace = new Dimension(530, 300);
    Image revolvingStand = c.getImage();
    int xRGap = 20;
    int yRGap = 30;
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/restaurant/BrownFloor.png"));
    Image floor = d.getImage();
    int xFlGap = WINDOWX;
    int yFlGap = WINDOWY;
    
    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/restaurant/Table1.png"));
    Image table = e.getImage();
    int xTGap = 28;
    int yTGap = 32;
    
    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/restaurant/Chair1.png"));
    Image chair = f.getImage();
    int xCGap = 20;
    int yCGap = 22;
    
    ImageIcon g = new ImageIcon(this.getClass().getResource("/image/restaurant/WaitArea.png"));
    Image waitArea = g.getImage();
    int xWGap = 50;
    int yWGap = 50;
    
    ImageIcon h = new ImageIcon(this.getClass().getResource("/image/restaurant/PlayArea.png"));
    Image playArea = h.getImage();
    int xPAGap = 86;
    int yPAGap = 123;

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
        g2.fillRect(WINDOWC, WINDOWC, WINDOWX, WINDOWY );
        g2.drawImage(floor, 0, 0, xFlGap, yFlGap, this);

        //Waiting Area
        g2.drawImage(waitArea, 100, 0, xWGap, yWGap, this);
        g2.setColor(Color.BLACK);
        g2.drawString("Waiting Area", 155, 30);
        
        //Cooking Area
        g2.setColor(Color.GRAY);
        g2.fillRect(550, 200, 170, 170);
        g2.setColor(Color.BLACK);
        
        //Plating Area
        g2.setColor(Color.PINK);
        g2.fillRect(550, 180, 150, 20);
        g2.fillRect(530, 180, 20, 170);
        
        //RevolvingStand
        g2.drawImage(revolvingStand, StandPlace.width, StandPlace.height, xRGap, yRGap, this);
        g2.setColor(Color.BLACK);
        g2.drawString("Revolving Stand", 560, 325);
        
        //Grills
        g2.setColor(Color.DARK_GRAY);
        g2.drawImage(grill, 550, 200, xGGap, yGGap, this);
        g2.drawImage(grill, 590, 200, xGGap, yGGap, this);
        g2.drawImage(grill, 630, 200, xGGap, yGGap, this);
        
        //Fridge
        g2.drawImage(fridge, FridgePlace.width, FridgePlace.height, xFGap, yFGap, this);
        
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        
        for(int i = 0; i < host.getTableList().size(); i++){
        	g2.drawImage(chair, host.getTableList().get(i).getX(), host.getTableList().get(i).getY(), xCGap, yCGap, this);
        	g2.drawImage(table, host.getTableList().get(i).getX(), host.getTableList().get(i).getY()+25, xTGap, yTGap, this);
        //  g2.fillRect(host.getTableList().get(i).getX(), host.getTableList().get(i).getY(), TABLEWIDTH, TABLEHEIGHT);
        //	tables.add(host.getTableList().get(i));
        }
        
        g2.drawImage(playArea, 570, 20, xPAGap, yPAGap, this);
        
        g2.setColor(Color.MAGENTA); // add waiter guis

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
    
    public void addGui(OmarCashierGui gui) {
    	guis.add(gui);
    }
    
    public void setTableList(ArrayList<Table> list){
    	tables = list;
    }
}
