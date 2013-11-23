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
    private final int TABLEX3 = 100;
    private final int TABLEY = 150;
    private final int GAPX = 50;
    private final int GAPY = 50;
    private final int ICON_X = 30;
    private final int ICON_Y = 30;    
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private List<Gui> guis = new ArrayList<Gui>();
    private ImageIcon ifridge = new ImageIcon("image/fridge.png");
    private Image fridgeimage = ifridge.getImage();
    private ImageIcon i2 = new ImageIcon("image/host.png");
    private Image plateimage = i2.getImage();
    private ImageIcon igrill = new ImageIcon("image/grill.jpg");
    private Image grillimage = igrill.getImage();
    private ImageIcon irevolving = new ImageIcon("image/revolvingStand.jpg");
    private Image standimage = irevolving.getImage();
    int testNumber; //to make sure card layout is working
    
    public AnimationPanel(int i) {
    	testNumber = i;
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

        Color randomColor = new Color(0);
        if(testNumber == 0)
        {
        	randomColor = Color.blue;
        }
        else if(testNumber == 1)
        {
        	randomColor = Color.black;
        }
        else if(testNumber == 2)
        {
        	randomColor = Color.green;
        }
        else if(testNumber == 3)
        {
        	randomColor = Color.cyan;
        }
        else if(testNumber == 4)
        {
        	randomColor = Color.magenta;
        }
        else if(testNumber == 5)
        {
        	randomColor = Color.orange;
        }
        else if(testNumber == 6)
        {
        	randomColor = Color.pink;
        }
        else if(testNumber == 7)
        {
        	randomColor = Color.white;
        }
        else if(testNumber == 8)
        {
        	randomColor = Color.yellow;
        }
        g2.setColor(randomColor);
        g2.fillRect(TABLEX1, TABLEY, GAPX, GAPY);//200 and 250 need to be table params

        g2.fillRect(TABLEX2, TABLEY, GAPX, GAPY);
        g2.fillRect(TABLEX3, TABLEY, GAPX, GAPY);
        //here is the cooking area 
    	g.drawImage(plateimage, 70, 250, ICON_X, ICON_Y, this);
    	g.drawImage(standimage, 40, 250, ICON_X, ICON_Y, this);
    	g.drawImage(grillimage, 200, 250, ICON_X, ICON_Y, this);
    	g.drawImage(fridgeimage, 330, 250, ICON_X, ICON_Y, this);
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
