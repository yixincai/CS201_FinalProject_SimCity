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
public class HouseAnimationPanel extends JPanel implements ActionListener {
        
        // Note: I changed these to public so that they can be accessed by HouseOccupantGui and ApartmentOccupantGui --Eric
        
        public static final int STOVEX = 500;
        public static final int STOVEY = 20;
        public static final int STOVEDIM = 20;
        
        public static final int FRIDGEX = 530;
        public static final int FRIDGEY = 12;
        public static final int FRIDGEDIM = 20;
        
        public static final int TABLEX = 600;
        public static final int TABLEY = 80;
        
        public static final int CHAIRX = 580;
        public static final int CHAIRY = 87;
        
        public static final int BEDX = 570;
        public static final int BEDY = 210;
        public static final int BEDWIDTH = 30;
        public static final int BEDHEIGHT = 30;
        
        public static final int BEDWALLX = 400;
        public static final int BEDWALLY = 0;
        public static final int BEDWALLWIDTH = 10;
        public static final int BEDWALLHEIGHT = 160;
        
        public static final int BEDWALLX2 = 400;
        public static final int BEDWALLY2 = 200;
        
        public static final int TVX = 250;
        public static final int TVY = 170;
        public static final int TVDIM = 10;

        public static final int FRONTDOORX = 100;
        public static final int FRONTDOORY = 340;
        
        public static final int WINDOWX = 682;
        public static final int WINDOWY = 360;
        
        private List<Gui> _guis = new ArrayList<Gui>();
        
        ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Fridge.png"));
    Image fridge = a.getImage();
    int xFGap = 25;
    int yFGap = 48;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/SingleGrill.png"));
    Image grill = b.getImage();
    int xGGap = 28;
    int yGGap = 40;
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/home/Bed.png"));
    Image bed = c.getImage();
    int xBGap = 33;
    int yBGap = 72;
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/home/HomeTable.png"));
    Image table = d.getImage();
    int xTGap = 44;
    int yTGap = 31;
    
    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/home/HomeChair.png"));
    Image chair = e.getImage();
    int xCGap = 12;
    int yCGap = 14;
    
    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/home/TV.png"));
    Dimension TVPlace = new Dimension(225, 20);
    Image TV = f.getImage();
    int xTVGap = 56;
    int yTVGap = 58;
    
    ImageIcon g = new ImageIcon(this.getClass().getResource("/image/home/DisplayCabinet.png"));
    Dimension DCPlace = new Dimension(100, 40);
    Image dcabinet = g.getImage();
    int xDGap = 72;
    int yDGap = 42;
    
    ImageIcon h = new ImageIcon(this.getClass().getResource("/image/home/FloorMat.png"));
    Image mat = h.getImage();
    int xMGap = 52;
    int yMGap = 32;
//    
//    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/home/HomeChair.png"));
//    Image chair = e.getImage();
//    int xCGap = 12;
//    int yCGap = 14;
//    
//    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/home/HomeChair.png"));
//    Image chair = e.getImage();
//    int xCGap = 12;
//    int yCGap = 14;
    
    
    
    ImageIcon x = new ImageIcon(this.getClass().getResource("/image/home/HomeFloor.png"));
    Image floor = x.getImage();
    int xFlGap = WINDOWX;
    int yFlGap = WINDOWY;
    
    ImageIcon y = new ImageIcon(this.getClass().getResource("/image/home/KitchenFloor1.png"));
    Dimension kitchenPlace = new Dimension(400, 0);
    Image kfloor = y.getImage();
    int xKFlGap = 300;
    int yKFlGap = 180;
    
    ImageIcon z = new ImageIcon(this.getClass().getResource("/image/home/BedroomFloor.png"));
    Dimension bedroomPlace = new Dimension(400, 180);
    Image bfloor = z.getImage();
    int xBFlGap = 300;
    int yBFlGap = 160;
    
    
        
        public HouseAnimationPanel()
        {
                setSize(WINDOWX, WINDOWY);
                setVisible(true);
 
                Timer timer = new Timer(10, this );
                timer.start();
        }

        public void actionPerformed(ActionEvent e) {
                
                for(Gui gui : _guis) {
                        gui.updatePosition();
                }
                
                repaint();  //Will have paintComponent called
        }
        
        public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;

                //Clear the screen by painting a rectangle the size of the frame
                g2.setColor(getBackground());
                g2.fillRect(0, 0, WINDOWX, WINDOWY );
                g2.drawImage(floor, 0, 0, xFlGap, yFlGap, this);
                g2.drawImage(kfloor, kitchenPlace.width, kitchenPlace.height, xKFlGap, yKFlGap, this);
                g2.drawImage(bfloor, bedroomPlace.width, bedroomPlace.height, xBFlGap, yBFlGap, this);

                //Here is the table

                g2.drawImage(bed, BEDX, BEDY, xBGap, yBGap, this);
                
                g2.drawImage(grill, STOVEX, STOVEY, xGGap, yGGap, this);
                
                g2.drawImage(fridge, FRIDGEX, FRIDGEY, xFGap, yFGap, this);
                
                g2.drawImage(table, TABLEX, TABLEY, xTGap, yTGap, this);
                
                g2.drawImage(chair, CHAIRX, CHAIRY, xCGap, yCGap, this);
                
                g2.drawImage(TV, TVPlace.width, TVPlace.height, xTVGap, yTVGap, this);
                
                g2.drawImage(dcabinet, DCPlace.width, DCPlace.height, xDGap, yDGap, this);
                
                g2.drawImage(mat, FRONTDOORX, FRONTDOORY-40, xMGap, yMGap, this);

                for(Gui gui : _guis) {
                        gui.draw(g2);
                }
        }
        
        public void addGui(HomeOccupantGui gui) {
                _guis.add(gui);
        }
}