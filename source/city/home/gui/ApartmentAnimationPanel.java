package city.home.gui;

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

@SuppressWarnings("serial")
public class ApartmentAnimationPanel extends JPanel implements ActionListener {
	
	//TODO change constant names to be 0-indexed
	
    public static final int BEDX = 600;
    public static final int BEDY = 300;
    public static final int STOVEX = 570;
    public static final int STOVEY = 300;
    public static final int FRIDGEX = 540;
    public static final int FRIDGEY = 292;
    public static final int TVX = 382;
    public static final int TVY = 310;
    
    public static final int BEDX2 = 600;
    public static final int BEDY2 = 30;
    public static final int STOVEX2 = 570;
    public static final int STOVEY2 = 30;
    public static final int FRIDGEX2 = 550;
    public static final int FRIDGEY2 = 30;
    public static final int TVX2 = 382;
    public static final int TVY2 = 50;
    
    public static final int BEDX3 = 20;
    public static final int BEDY3 = 300;
    public static final int STOVEX3 = 60;
    public static final int STOVEY3 = 300;
    public static final int FRIDGEX3 = 80;
    public static final int FRIDGEY3 = 300;
    public static final int TVX3 = 300;
    public static final int TVY3 = 310;
    
    public static final int BEDX4 = 20;
    public static final int BEDY4 = 30;
    public static final int STOVEX4 = 60;
    public static final int STOVEY4 = 30;
    public static final int FRIDGEX4 = 80;
    public static final int FRIDGEY4 = 30;
    public static final int TVX4 = 300;
    public static final int TVY4 = 50;
    
    public static final int BEDWIDTH = 30;
    public static final int BEDHEIGHT = 30;
    public static final int STOVEDIM = 20;
    public static final int FRIDGEDIM = 20;
    
    public static final int WALLX1 = 0;
    public static final int WALLY1 = 180;
    
    public static final int WALLX2 = 530;
    public static final int WALLY2 = 180;
    
    public static final int WALLX3 = 341;
    public static final int WALLY3 = 0;
    
    public static final int WALLX4 = 341;
    public static final int WALLY4 = 210;
    
    public static final int WALLDIMH = 10;
    public static final int WALLDIMV = 150;
    
    public static final int TVDIM = 10;

    public static final int FRONTDOORX = 0;
    public static final int FRONTDOORY = 0;

    public static final int WINDOWX = 682;
    public static final int WINDOWY = 360;
    
    private List<Gui> _guis = new ArrayList<Gui>();
    
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Fridge.png"));
    Dimension FridgePlace = new Dimension(600, 80);
    Image fridge = a.getImage();
    int xFGap = 25;
    int yFGap = 48;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/SingleGrill.png"));
    Image grill = b.getImage();
    int xGGap = 28;
    int yGGap = 40;
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/home/Bed1.png"));
    Image bed = c.getImage();
    int xBGap = 33;
    int yBGap = 72;
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/home/ApartmentFloor.png"));
    Image floor = d.getImage();
    int xFlGap = WINDOWX;
    int yFlGap = WINDOWY;
    
    public ApartmentAnimationPanel()
    {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
    	Timer timer = new Timer(10, this );
    	timer.start(); 
    }

	public void actionPerformed(ActionEvent e) {
		
		  for(Gui gui : _guis) {
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
        g2.drawImage(floor, 0, 0, xFlGap, yFlGap, this);

		//Here is the table
		
		g2.drawImage(grill, STOVEX, STOVEY, xGGap, yGGap, this);
		
		g2.drawImage(fridge, FRIDGEX, FRIDGEY, xFGap, yFGap, this);

        //beds
        g2.setColor(Color.CYAN);
        g2.drawImage(bed, BEDX, BEDY, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX2, BEDY2, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX3, BEDY3, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX4, BEDY4, xBGap, yBGap, this);
        
        //stoves
        g2.setColor(Color.BLACK);
        g2.drawImage(grill, STOVEX, STOVEY, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX2, STOVEY2, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX3, STOVEY3, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX4, STOVEY4, xGGap, yGGap, this);
        
        //fridges
        g2.setColor(Color.WHITE);
        g2.drawImage(fridge, FRIDGEX, FRIDGEY, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX2, FRIDGEY4, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX3, FRIDGEY3, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX4, FRIDGEY2, xFGap, yFGap, this);
        
        //walls
        g2.setColor(Color.BLUE);
        g2.fillRect(WALLX1, WALLY1, WALLDIMV, WALLDIMH);
        g2.fillRect(WALLX2, WALLY2, WALLDIMV, WALLDIMH);
        g2.fillRect(WALLX3, WALLY3, WALLDIMH, WALLDIMV);
        g2.fillRect(WALLX4, WALLY4, WALLDIMH, WALLDIMV);

        for(Gui gui : _guis) {
        	gui.draw(g2);
        }
    }
    
    public void addGui(HomeOccupantGui gui) {
        _guis.add(gui);
    }
}
