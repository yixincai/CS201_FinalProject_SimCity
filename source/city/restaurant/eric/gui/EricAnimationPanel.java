package city.restaurant.eric.gui;

import gui.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class EricAnimationPanel extends JPanel implements ActionListener
{

    private int _windowWidth;
    private int _windowHeight;

    private List<Gui> guis = new ArrayList<Gui>();
    
    private List<TableDim> tableDims = new ArrayList<TableDim>();
    
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/EricBackground.png"));
    Image background = a.getImage();
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/EricKitchen.png"));
    Image kbackground = b.getImage();
    int xKGap = 80;
    int yKGap = 80;
    
    ImageIcon c = new ImageIcon(this.getClass().getResource("/image/restaurant/WaitChair.png"));
    Image chair = c.getImage();
    int xCGap = 20;
    int yCGap = 20;
    
    ImageIcon d = new ImageIcon(this.getClass().getResource("/image/restaurant/Table.png"));
    Image table = d.getImage();
    int xTGap = 30;
    int yTGap = 30;
    
    ImageIcon e = new ImageIcon(this.getClass().getResource("/image/restaurant/RevolvingStand.png"));
    Dimension standPlace;
    Image revolvingStand = e.getImage();
    int xRGap = 20;
    int yRGap = 20;
    
    ImageIcon f = new ImageIcon(this.getClass().getResource("/image/restaurant/Plating1.png"));
    Dimension platePlace;
    Image plating = f.getImage();
    int xPGap = 20;
    int yPGap = 15;
    
    private class TableDim
    {
    	public int posx, posy, width, height;
    	public TableDim(int x, int y, int w, int h) { posx=x; posy=y; width=w; height=h;}
    }

    public EricAnimationPanel(int width, int height)
    {
    	_windowWidth = width;
    	_windowHeight = height;
    	
    	// Hard-coded 3 tables.
    	tableDims.add(new TableDim(EricAnimationConstants.TABLE0_POSX, EricAnimationConstants.TABLE0_POSY, EricAnimationConstants.TABLE_WIDTH, EricAnimationConstants.TABLE_HEIGHT));
    	tableDims.add(new TableDim(EricAnimationConstants.TABLE1_POSX, EricAnimationConstants.TABLE1_POSY, EricAnimationConstants.TABLE_WIDTH, EricAnimationConstants.TABLE_HEIGHT));
    	tableDims.add(new TableDim(EricAnimationConstants.TABLE2_POSX, EricAnimationConstants.TABLE2_POSY, EricAnimationConstants.TABLE_WIDTH, EricAnimationConstants.TABLE_HEIGHT));
    	
    	standPlace = new Dimension(EricAnimationConstants.REVOLVING_STAND_POSX,EricAnimationConstants.REVOLVING_STAND_POSY);
    	platePlace = new Dimension(EricAnimationConstants.REVOLVING_STAND_POSX+33,EricAnimationConstants.REVOLVING_STAND_POSY+7);
    	
    	setSize(_windowWidth, _windowHeight);
        setVisible(true);
 
    	Timer timer = new Timer(EricAnimationConstants.TIMER_PERIOD, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e)
	{

        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.updatePosition();
            }
        }
        
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g)
    {
        Graphics2D graphicsDrawing = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        graphicsDrawing.setColor(getBackground());
        graphicsDrawing.fillRect(0, 0, _windowWidth, _windowHeight );
        
        g.drawImage(background, 0, 0, _windowWidth, _windowHeight, null);
        
        g.drawImage(kbackground, 190, _windowHeight-105, xKGap, yKGap, null);
        
        g.drawImage(revolvingStand, standPlace.width, standPlace.height, xRGap, yRGap, null);
        
        g.drawImage(plating, platePlace.width, platePlace.height, xPGap, yPGap, null);

        //Here are the tables
        graphicsDrawing.setColor(Color.ORANGE);
        for(TableDim t : tableDims)
        {
        	g.drawImage(chair, t.posx, t.posy+5, xCGap, yCGap, this);
        	g.drawImage(table, t.posx+25, t.posy-5, xTGap, yTGap, this);
        }

        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.draw(graphicsDrawing);
            }
        }
    }

    public void addGui(Gui gui)
    {
        guis.add(gui);
    }
}
