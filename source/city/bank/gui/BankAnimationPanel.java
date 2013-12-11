package city.bank.gui;

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


public class BankAnimationPanel extends JPanel implements ActionListener {
	    private final int TELLERDESKX = 582;
	    private final int TELLERDESKY = 60;
	    private final int TELLERDESKWIDTH = 30;
	    private final int TELLERDESKHEIGHT = 270;
   
	    private final int WINDOWX = 682;
	    private final int WINDOWY = 360;
	    private List<Gui> guis = new ArrayList<Gui>();
	    GuardDogGui dogGui;
	    
	    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/bank/BankCarpet.png"));
	    Image carpet = a.getImage();
	    int xFGap = 700;
	    int yFGap = 400;
	    
	    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/bank/TellerFloor.png"));
	    Image desk = b.getImage();
	    int xDGap = 100;
	    int yDGap = 350;
	    
	    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/bank/LineCarpet.png"));
	    Dimension LinePlace = new Dimension(50, 190);
	    Image line = c.getImage();
	    int xLGap = 375;
	    int yLGap = 30;
	    
	    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/bank/HostDesk.png"));
	    Dimension HDeskPlace = new Dimension(378, 165);
	    Image hdesk = d.getImage();
	    int xHDGap = 37;
	    int yHDGap = 25;
	    
	    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/bank/BankDesk.png"));
	    static int xDeskPlace = -20;
	    Image Bdesk = e.getImage();
	    int xBDGap = 48;
	    int yBDGap = 63;
	    static int BDesk = 3;
	    
	    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/bank/DogHouse.png"));
	    Image DogHouse = f.getImage();
	    int xDHouse = 25;
	    int yDHouse = 25;
	    
	    
	    
	    public BankAnimationPanel()
	    {
	    	setSize(WINDOWX, WINDOWY);
	        setVisible(true);
	 
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
	        
	        g2.drawImage(carpet, 0, 0, xFGap, yFGap, this);
	        g2.drawImage(desk, TELLERDESKX, 0, xDGap, yDGap, this);
	        
	        g2.drawImage(line, LinePlace.width, LinePlace.height, xLGap, yLGap, this);
	        
	        for(int i = 1; i <=BDesk; i++){
	        	g2.drawImage(Bdesk, xDeskPlace+(150*i), 260, xBDGap, yBDGap, this);
	        }
	        
	        g2.drawImage(DogHouse, 90, 10, xHDGap, yHDGap, this);

	        //Here is the table

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
	        
	        g2.drawImage(hdesk, HDeskPlace.width, HDeskPlace.height, xHDGap, yHDGap, this);
	        
	    }
	    
	    public void addGui(BankCustomerRoleGui gui) {
	        guis.add(gui);
	    }

	    public void addGui(BankHostRoleGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(BankTellerRoleGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(GuardDogGui gui){
	    	guis.add(gui);
	    }
}
