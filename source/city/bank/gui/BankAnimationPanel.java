package city.bank.gui;

import gui.Gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
	    
	    public BankAnimationPanel()
	    {
	    	setSize(WINDOWX, WINDOWY);
	        setVisible(true);
	 
	    	/*Timer timer = new Timer(10, this );
	    	timer.start(); */
	    }

		public void actionPerformed(ActionEvent e) {
			repaint();  //Will have paintComponent called
		}
		
	    public void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D)g;

	        //Clear the screen by painting a rectangle the size of the frame
	        g2.setColor(getBackground());
	        g2.fillRect(0, 0, WINDOWX, WINDOWY );

	        //Here is the table

	        g2.setColor(Color.BLACK);
	        g2.fillRect(TELLERDESKX, TELLERDESKY, TELLERDESKWIDTH, TELLERDESKHEIGHT);//200 and 250 need to be table params

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
	    
	    public void addGui(BankCustomerRoleGui gui) {
	        guis.add(gui);
	    }

	    public void addGui(BankHostRoleGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(BankTellerRoleGui gui) {
	        guis.add(gui);
	    }
}
