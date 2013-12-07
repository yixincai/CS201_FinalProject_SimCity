package city.restaurant.ryan.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RyanAnimationPanel extends JPanel implements ActionListener { //Add revolving stand

    private final int WINDOWX = 700;
    private final int WINDOWY = 700;
    List<Dimension> tables = new ArrayList<Dimension>();
    List<Dimension> kitchen = new ArrayList<Dimension>(); //For grill, and placing area
    List<Dimension> seats = new ArrayList<Dimension>();
    int numSeats = 5; 
    private int width = 50;
    private int height = 50;
    private int agentWidth = 20;
    private int agentHeight = 20;
    
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Fridge.png"));
    Dimension FridgePlace = new Dimension(600, 80);
    Image fridge = a.getImage();
    int xFGap = 25;
    int yFGap = 48;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/Grill.png"));
    Dimension GrillPlace = new Dimension(600, 145);
    Image grill = b.getImage();
    int xGGap = 28;
    int yGGap = 88;
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/restaurant/Plating.png"));
    Dimension PlatePlace = new Dimension(500, 85);
    Image plating = c.getImage();
    int xPGap = 20;
    int yPGap = 80;
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/restaurant/RevolvingStand.png"));
    Dimension StandPlace = new Dimension(498, 40);
    Image revolvingStand = d.getImage();
    int xRGap = 24;
    int yRGap = 32;
    
    private List<Gui> guis = new ArrayList<Gui>();

    public RyanAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        tables.add(new Dimension(150, 150));
        tables.add(new Dimension(250, 150));
        tables.add(new Dimension(350, 150));
        
        kitchen.add(new Dimension(500, 85));
        kitchen.add(new Dimension(600, 145));
        
        for(int i=1; i<=numSeats; i++){
        	seats.add(new Dimension( (10+25*(i-1)), 5));
        }
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        
        g2.setColor(Color.GRAY);
        g2.fillRect(180, 10, 10, 40);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(180, 50, 50, 10);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(500, 0, 20, 500);
        
        //g2.setColor(Color.RED);
        //g2.fillRect(90, 65, 20, 20);
        
        //Seats
        for(Dimension seat: seats){
        	g2.setColor(Color.ORANGE);
        	g2.fillRect(seat.width, seat.height, agentWidth, agentHeight);
        }
        
        //Kitchen Places
        g2.drawImage(fridge, FridgePlace.width, FridgePlace.height, xFGap, yFGap, this); //Fridge
        
        g2.drawImage(revolvingStand, StandPlace.width, StandPlace.height, xRGap, yRGap, this); //Revolving Stand
        
        g2.drawImage(grill, GrillPlace.width, GrillPlace.height, xGGap, yGGap, this); //Grill
        
        g2.drawImage(plating, PlatePlace.width, PlatePlace.height, xPGap, yPGap, this); //Plating
        
//        g2.setColor(Color.black);
//        g2.drawString("Grill", 620, 215);
//        g2.drawString("Plating", 490, 180);
        
        //Here is the table
        for(Dimension temp: tables){
        	g2.setColor(Color.ORANGE);
            g2.fillRect(temp.width, temp.height, width, height);
        }


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

    public void addGui(RyanCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(RyanHostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(RyanWaiterGui gui){
    	guis.add(gui);
    }
    
    public void addGui(RyanCashierGui gui){
    	guis.add(gui);
    }
    
    public void addGui(RyanCookGui gui){
    	guis.add(gui);
    }
}
