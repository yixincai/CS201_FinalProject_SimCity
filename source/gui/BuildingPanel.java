package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import city.restaurant.yixin.gui.AnimationPanel;

public class BuildingPanel extends JPanel implements ActionListener
{
	
	private static final int PANELX = 1024 * 2 /3;
	private static final int PANELY = 720 / 2;
	MainGui gui;
	
	Rectangle2D myRectangle;
    String myName;
    
    //View for the inside of a building
	public BuildingPanel(MainGui mgui, Rectangle2D r, int i)
	{
		this.gui = mgui;
		this.myName = "Building " + i;
		this.myRectangle = r;
		this.setPreferredSize(new Dimension(PANELX, PANELY));
		this.setLayout(new BorderLayout());
		List<JButton> buildingList = new ArrayList<JButton>();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		//buttonPanel.setBorder(BorderFactory.createTitledBorder("Building"));
		JPanel view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		buttonPanel.add(new AnimationPanel());
		this.add(buttonPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}

    public void displayBuildingPanel() {
            gui.displayBuildingPanel( this ); 
    }
    
    public String getName() {
        return myName;
    }	

}
