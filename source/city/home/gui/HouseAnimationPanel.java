package city.home.gui;

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

import city.bank.gui.BankCustomerRoleGui;
import city.bank.gui.BankHostRoleGui;
import city.bank.gui.BankTellerRoleGui;

@SuppressWarnings("serial")
public class HouseAnimationPanel extends JPanel implements ActionListener {
	
	// Note: I changed these to public so that they can be accessed by HouseOccupantGui and ApartmentOccupantGui --Eric
	
	public static final int STOVEX = 300;
	public static final int STOVEY = 100;
	public static final int STOVEDIM = 20;
	
	public static final int FRIDGEX = 320;
	public static final int FRIDGEY = 100;
	public static final int FRIDGEDIM = 20;
	
    public static final int BEDX = 600;
    public static final int BEDY = 300;
    public static final int BEDWIDTH = 30;
    public static final int BEDHEIGHT = 30;
    
    public static final int BEDWALLX = 400;
    public static final int BEDWALLY = 0;
    public static final int BEDWALLWIDTH = 10;
    public static final int BEDWALLHEIGHT = 160;
    
    public static final int BEDWALLX2 = 400;
    public static final int BEDWALLY2 = 200;
    
    private static final int TVX = 400;
    private static final int TVY = 100;
    
    public static final int WINDOWX = 682;
    public static final int WINDOWY = 360;
    
    private static List<Gui> guis = new ArrayList<Gui>();
    
    public HouseAnimationPanel()
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

        //Here is the table

        g2.setColor(Color.CYAN);
        g2.fillRect(BEDX, BEDY, BEDWIDTH, BEDHEIGHT);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(BEDWALLX, BEDWALLY, BEDWALLWIDTH, BEDWALLHEIGHT);
        g2.fillRect(BEDWALLX2, BEDWALLY2, BEDWALLWIDTH, BEDWALLHEIGHT);
        

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
  /*  public void addGui(HomeOccupantGui gui) {
        guis.add(gui);
    } */
}
