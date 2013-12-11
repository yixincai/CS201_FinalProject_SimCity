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
    	
    	setSize(_windowWidth, _windowHeight);
        setVisible(true);
 
    	Timer timer = new Timer(EricAnimationConstants.TIMER_PERIOD, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e)
	{
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g)
    {
        Graphics2D graphicsDrawing = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        graphicsDrawing.setColor(getBackground());
        graphicsDrawing.fillRect(0, 0, _windowWidth, _windowHeight );

        //Here are the tables
        graphicsDrawing.setColor(Color.ORANGE);
        for(TableDim t : tableDims)
        {
            graphicsDrawing.fillRect(t.posx, t.posy, t.width, t.height);
        }
        


        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.updatePosition();
            }
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
