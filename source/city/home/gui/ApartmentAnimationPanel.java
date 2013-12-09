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
	//Bottom Right Bed
    public static final int BEDX = 600;
    public static final int BEDY = 180;
    public static final int STOVEX = 400;
    public static final int STOVEY = 180;
    public static final int FRIDGEX = 370;
    public static final int FRIDGEY = 172;
    public static final int TVX = 500;
    public static final int TVY = 250;
    public static final int FRONTDOORX = 500;
    public static final int FRONTDOORY = 380;
    public static final int ITEMX = 500;
    public static final int ITEMY = 200;
    
    //Top Right Bed
    public static final int BEDX2 = 600;
    public static final int BEDY2 = 15;
    public static final int STOVEX2 = 400;
    public static final int STOVEY2 = 15;
    public static final int FRIDGEX2 = 370;
    public static final int FRIDGEY2 = 7;
    public static final int TVX2 = 500;
    public static final int TVY2 = 90;
    public static final int FRONTDOORX2 = 500;
    public static final int FRONTDOORY2 = -20;
    public static final int ITEMX2 = 380;
    public static final int ITEMY2 = 380;
    
    //Bottom Left Bed
    public static final int BEDX3 = 20;
    public static final int BEDY3 = 180;
    public static final int STOVEX3 = 260;
    public static final int STOVEY3 = 180;
    public static final int FRIDGEX3 = 290;
    public static final int FRIDGEY3 = 172;
    public static final int TVX3 = 170;
    public static final int TVY3 = 250;
    public static final int FRONTDOORX3 = 170;
    public static final int FRONTDOORY3 = 380;
    public static final int ITEMX3 = 170;
    public static final int ITEMY3 = 200;
    
    //Top Left Bed
    public static final int BEDX4 = 20;
    public static final int BEDY4 = 15;
    public static final int STOVEX4 = 260;
    public static final int STOVEY4 = 15;
    public static final int FRIDGEX4 = 290;
    public static final int FRIDGEY4 = 7;
    public static final int TVX4 = 170;
    public static final int TVY4 = 90;
    public static final int FRONTDOORX4 = 170;
    public static final int FRONTDOORY4 = -20;
    public static final int ITEMX4 = 380;
    public static final int ITEMY4 = 380;
    
    public static final int BEDWIDTH = 30;
    public static final int BEDHEIGHT = 30;
    public static final int STOVEDIM = 20;
    public static final int FRIDGEDIM = 20;
    
    public static final int WALLX1 = 0;
    public static final int WALLY1 = 160;
    
    public static final int WALLX2 = 530;
    public static final int WALLY2 = 180;
    
    public static final int WALLX3 = 341;
    public static final int WALLY3 = 0;
    
    public static final int WALLX4 = 341;
    public static final int WALLY4 = 210;
    
    public static final int WALLDIMH = 10;
    public static final int WALLDIMV = 150;
    
    public static final int TVDIM = 10;

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
    
    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/home/FloorMat.png"));
    Image mat = e.getImage();
    int xMGap = 26;
    int yMGap = 16;
    
    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/home/TVGame.png"));
    Image game = f.getImage();
    int xGameGap = 32;
    int yGameGap = 29;
    
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
        g2.drawImage(bed, BEDX, BEDY, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX2, BEDY2, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX3, BEDY3, xBGap, yBGap, this);
		g2.drawImage(bed, BEDX4, BEDY4, xBGap, yBGap, this);
        
        //stoves
        g2.drawImage(grill, STOVEX, STOVEY, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX2, STOVEY2, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX3, STOVEY3, xGGap, yGGap, this);
        g2.drawImage(grill, STOVEX4, STOVEY4, xGGap, yGGap, this);
        
        //fridges
        g2.drawImage(fridge, FRIDGEX, FRIDGEY, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX2, FRIDGEY2, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX3, FRIDGEY3, xFGap, yFGap, this);
        g2.drawImage(fridge, FRIDGEX4, FRIDGEY4, xFGap, yFGap, this);
        
        //Mat
        g2.drawImage(mat, FRONTDOORX-7, FRONTDOORY-60, xMGap, yMGap, this);
        g2.drawImage(mat, FRONTDOORX2-7, FRONTDOORY2+20, xMGap, yMGap, this);
        g2.drawImage(mat, FRONTDOORX3-7, FRONTDOORY3-60, xMGap, yMGap, this);
        g2.drawImage(mat, FRONTDOORX4-7, FRONTDOORY4+20, xMGap, yMGap, this);
        
        //Items
        g2.drawImage(game, FRIDGEX, FRIDGEY, xFGap, yFGap, this);
        //g2.drawImage(fridge, FRIDGEX2, FRIDGEY2, xFGap, yFGap, this);
        g2.drawImage(game, FRIDGEX3, FRIDGEY3, xFGap, yFGap, this);
        //g2.drawImage(fridge, FRIDGEX4, FRIDGEY4, xFGap, yFGap, this);
        
        //walls
        g2.setColor(Color.BLUE);
        g2.fillRect(WALLX1, WALLY1, 700, WALLDIMH);
        g2.fillRect(WALLX3, WALLY3, WALLDIMH, 400);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(TVX, TVY, 10, 10);
        g2.fillRect(TVX2, TVY2, 10, 10);
        g2.fillRect(TVX3, TVY3, 10, 10);
        g2.fillRect(TVX4, TVY4, 10, 10);
        

        for(Gui gui : _guis) {
        	gui.draw(g2);
        }
    }
    
    public void addGui(HomeOccupantGui gui) {
        _guis.add(gui);
    }
}
