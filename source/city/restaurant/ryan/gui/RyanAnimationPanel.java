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
    List<Dimension> seats = new ArrayList<Dimension>(); //Waiting area
    List<Dimension> chairs = new ArrayList<Dimension>(); //Table Area
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
    
    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/restaurant/WaitChair.png"));
    Image waitChair = e.getImage();
    int xWGap = 20;
    int yWGap = 23;
    
    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/restaurant/Chair.png"));
    Image chairimage = f.getImage();
    int xCGap = 20;
    int yCGap = 18;
    
    ImageIcon g = new ImageIcon(this.getClass().getResource("/image/restaurant/Table.png"));
    Image table = g.getImage();
    int xTGap = 50;
    int yTGap = 53;
    
    ImageIcon h = new ImageIcon(this.getClass().getResource("/image/restaurant/KitchenFloor.png"));
    Image kitchenFloor = h.getImage();
    int xKFGap = 175;
    int yKFGap = 360;
    
    ImageIcon i = new ImageIcon(this.getClass().getResource("/image/restaurant/WoodFloor.png"));
    Image restaurantFloor = i.getImage();
    int xRFGap = 500;
    int yRFGap = 360;
    
    ImageIcon j = new ImageIcon(this.getClass().getResource("/image/restaurant/CashierCounter.png"));
    Image cashierCounter = j.getImage();
    int xCCounterGap = 63;
    int yCCounterGap = 51;
    
    ImageIcon k = new ImageIcon(this.getClass().getResource("/image/restaurant/Counter.png"));
    Image counter = k.getImage();
    int xCounterGap = 20;
    int yCounterGap = 350;
    
    private List<Gui> guis = new ArrayList<Gui>();

    public RyanAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        tables.add(new Dimension(150, 170));
        tables.add(new Dimension(250, 170));
        tables.add(new Dimension(350, 170));
        
        chairs.add(new Dimension(150, 150));
        chairs.add(new Dimension(180, 150));
        chairs.add(new Dimension(250, 150));
        chairs.add(new Dimension(280, 150));
        chairs.add(new Dimension(350, 150));
        chairs.add(new Dimension(380, 150));
        chairs.add(new Dimension(150, 225));
        chairs.add(new Dimension(180, 225));
        chairs.add(new Dimension(250, 225));
        chairs.add(new Dimension(280, 225));
        chairs.add(new Dimension(350, 225));
        chairs.add(new Dimension(380, 225));
        
        kitchen.add(new Dimension(500, 85));
        kitchen.add(new Dimension(600, 145));
        
        for(int i=1; i<=numSeats; i++){
        	seats.add(new Dimension( (10+25*(i-1)), 5));
        }
 
    	Timer timer = new Timer(10, this );
    	timer.start();
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
        
        g2.drawImage(restaurantFloor, 0, 0, xRFGap, yRFGap, this);
        g2.drawImage(kitchenFloor, 520, 0, xKFGap, yKFGap, this);
        g2.drawImage(cashierCounter, 180, 20, xCCounterGap, yCCounterGap, this);
        g2.drawImage(counter, 500, 0, xCounterGap, yCounterGap, this);
        
        //Seats
        for(Dimension seat: seats){
        	g2.drawImage(waitChair, seat.width, seat.height, xWGap, yWGap, this);
        }
        
        for(Dimension chair: chairs){
        	g2.drawImage(chairimage, chair.width, chair.height+10, xCGap, yCGap, this);
        }
        
        //Kitchen Places
        g2.drawImage(fridge, FridgePlace.width, FridgePlace.height, xFGap, yFGap, this); //Fridge
        
        g2.drawImage(revolvingStand, StandPlace.width, StandPlace.height, xRGap, yRGap, this); //Revolving Stand
        
        g2.drawImage(grill, GrillPlace.width, GrillPlace.height, xGGap, yGGap, this); //Grill
        
        g2.drawImage(plating, PlatePlace.width, PlatePlace.height, xPGap, yPGap, this); //Plating
        
        //Here is the table
        for(Dimension temp: tables){
        	g2.drawImage(table, temp.width, temp.height+10, xTGap, yTGap, this);
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
