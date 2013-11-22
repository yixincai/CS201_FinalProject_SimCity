package city.restaurant.yixin.gui;

import gui.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
    private final int TABLEX1 = 200;
    private final int TABLEX2 = 300;
    private final int TABLEY = 150;
    private final int GAPX = 50;
    private final int GAPY = 50;
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private List<Gui> guis = new ArrayList<Gui>();

    private int TABLEX3 = 100, TABLEY3 = 150;
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
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

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX1, TABLEY, GAPX, GAPY);//200 and 250 need to be table params

        g2.fillRect(TABLEX2, TABLEY, GAPX, GAPY);
        g2.fillRect(TABLEX3, TABLEY3, GAPX, GAPY);        
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
    
    public void addGui(YixinCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(YixinHostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(YixinWaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(YixinCookGui gui) {
        guis.add(gui);
    }
    
    public void addGui(YixinCashierGui gui) {
        guis.add(gui);
    }
}
