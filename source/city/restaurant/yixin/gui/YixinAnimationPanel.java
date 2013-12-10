package city.restaurant.yixin.gui;

import gui.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class YixinAnimationPanel extends JPanel implements ActionListener {
    private final int TABLEX = 450;
    private final int TABLEY1 = 40;
    private final int TABLEY2 = 110;
    private final int TABLEY3 = 180;
    private final int GAPX = 30;
    private final int GAPY = 30;
    private final int ICON_X = 32;
    private final int ICON_Y = 32;    
    private final int WINDOWX = 682;
    private final int WINDOWY = 360;
    private List<Gui> guis = new ArrayList<Gui>();
    private ImageIcon ifridge = new ImageIcon(this.getClass().getResource("/image/restaurant/fridge.png"));
    private Image fridgeimage = ifridge.getImage();
    private ImageIcon i2 = new ImageIcon(this.getClass().getResource("/image/host.png"));
    private Image plateimage = i2.getImage();
    private ImageIcon igrill = new ImageIcon(this.getClass().getResource("/image/restaurant/singlegrill.png"));
    private Image grillimage = igrill.getImage();
    private ImageIcon irevolving = new ImageIcon(this.getClass().getResource("/image/restaurant/revolvingStand.png"));
    private Image standimage = irevolving.getImage();
    private String WA = "Waiting Area";
    
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/YixinBackground.png"));
    Image floor = a.getImage();
    int xFlGap = 682;
    int yFlGap = 360;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/KitchenCounter.png"));
    Image counter = b.getImage();
    int xKCGap = 682;
    int yKCGap = 30;
    
    public YixinAnimationPanel()
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
        g.drawImage(floor, 0, 0, xFlGap, yFlGap, this);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 248, WINDOWX, 10);
        g.drawImage(counter, 0, 248, xKCGap, yKCGap, this);

        //Here is the table

        g2.setColor(Color.red);
        g2.fillRect(TABLEX, TABLEY1, GAPX, GAPY);//200 and 250 need to be table params
        g2.fillRect(TABLEX, TABLEY2, GAPX, GAPY);
        g2.fillRect(TABLEX, TABLEY3, GAPX, GAPY);
    	g.drawString(WA, 30, 10);

        //here is the cooking area 
        
    	g.drawImage(plateimage, 170, 248, 20, 20, this);
    	g.drawImage(standimage, 145, 248, 20, 20, this);
    	g.drawImage(grillimage, 300, 248, ICON_X, ICON_Y, this);
    	g.drawImage(fridgeimage, 430, 232, ICON_X, 48, this);
    	
//    	g2.fillRect(170, 250, ICON_X, ICON_Y);
//    	g2.fillRect(140, 250, ICON_X, ICON_Y);
//    	g2.fillRect(300, 250, ICON_X, ICON_Y);
//    	g2.fillRect(430, 250, ICON_X, ICON_Y);

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
