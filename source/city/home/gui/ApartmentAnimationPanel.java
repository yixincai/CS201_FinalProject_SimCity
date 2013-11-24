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
    private final int STOVEX = 570;
    private final int STOVEY = 300;
    private final int FRIDGEX = 550;
    private final int FRIDGEY = 300;
    
    private final int BEDX2 = 600;
    private final int BEDY2 = 100;
    private final int STOVEX2 = 570;
    private final int STOVEY2 = 100;
    private final int FRIDGEX2 = 550;
    private final int FRIDGEY2 = 100;
    
    private final int BEDX3 = 100;
    private final int BEDY3 = 300;
    private final int STOVEX3 = 130;
    private final int STOVEY3 = 300;
    private final int FRIDGEX3 = 150;
    private final int FRIDGEY3 = 300;
    
    private final int BEDX4 = 100;
    private final int BEDY4 = 100;
    private final int STOVEX4 = 130;
    private final int STOVEY4 = 100;
    private final int FRIDGEX4 = 150;
    private final int FRIDGEY4 = 100;
    
    private final int BEDWIDTH = 30;
    private final int BEDHEIGHT = 30;
    private final int STOVEDIM = 20;
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
        g2.fillRect(BEDX2, BEDY2, BEDWIDTH, BEDHEIGHT);
        g2.fillRect(BEDX3, BEDY3, BEDWIDTH, BEDHEIGHT);
        g2.fillRect(BEDX4, BEDY4, BEDWIDTH, BEDHEIGHT);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(STOVEX, STOVEY, STOVEDIM, STOVEDIM);
        g2.fillRect(STOVEX2, STOVEY2, STOVEDIM, STOVEDIM);
        g2.fillRect(STOVEX3, STOVEY3, STOVEDIM, STOVEDIM);
        g2.fillRect(STOVEX4, STOVEY4, STOVEDIM, STOVEDIM);
        
        g2.setColor(Color.WHITE);
        g2.fillRect(FRIDGEX, FRIDGEY, FRIDGEDIM, FRIDGEDIM);
        g2.fillRect(FRIDGEX2, FRIDGEY2, FRIDGEDIM, FRIDGEDIM);
        g2.fillRect(FRIDGEX3, FRIDGEY3, FRIDGEDIM, FRIDGEDIM);
        g2.fillRect(FRIDGEX4, FRIDGEY4, FRIDGEDIM, FRIDGEDIM);
        

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
