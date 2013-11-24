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

import city.bank.gui.BankHostRoleGui;

public class ApartmentAnimationPanel extends JPanel implements ActionListener {
    private final int BEDX = 600;
    private final int BEDY = 300;
    
    private final int BEDWIDTH = 30;
    private final int BEDHEIGHT = 30;
    
    private final int BEDWALLX = 400;
    private final int BEDWALLY = 0;
    private final int BEDWALLWIDTH = 10;
    private final int BEDWALLHEIGHT = 160;
    
    private final int BEDWALLX2 = 400;
    private final int BEDWALLY2 = 200;
    
    private final int STOVEX = 200;
    private final int STOVEY = 30;
    private final int STOVEDIM = 20;
    
    private final int FRIDGEX = 220;
    private final int FRIDGEY = 30;
    private final int FRIDGEDIM = 20;

    private final int WINDOWX = 682;
    private final int WINDOWY = 360;
    private List<Gui> guis = new ArrayList<Gui>();
    
    public ApartmentAnimationPanel()
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

        g2.setColor(Color.CYAN);
        g2.fillRect(BEDX, BEDY, BEDWIDTH, BEDHEIGHT);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(BEDWALLX, BEDWALLY, BEDWALLWIDTH, BEDWALLHEIGHT);
        g2.fillRect(BEDWALLX2, BEDWALLY2, BEDWALLWIDTH, BEDWALLHEIGHT);
        
        g2.setColor(Color.CYAN);
        g2.fillRect(STOVEX, STOVEY, STOVEDIM, STOVEDIM);
        
        g2.setColor(Color.WHITE);
        g2.fillRect(FRIDGEX, FRIDGEY, FRIDGEDIM, FRIDGEDIM);
        

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
    
  /*  public void addGui(HomeBuyingRoleGui gui) {
        guis.add(gui);
    }

    public void addGui(RoleGui gui) {
        guis.add(gui);
    }
    
    public void addGui(HomeRoleGui gui) {
        guis.add(gui); */
}
