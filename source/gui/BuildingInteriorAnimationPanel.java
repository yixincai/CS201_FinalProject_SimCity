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

import city.restaurant.yixin.gui.YixinAnimationPanel;

@SuppressWarnings("serial")
public class BuildingInteriorAnimationPanel extends JPanel implements ActionListener
{
	
	private static final int PANELX = 1024 * 2 /3;
	private static final int PANELY = 720 / 2;
	MainGui gui;
	JPanel _buildingAnimation;
	
    String myName;
    
    //View for the inside of a building
	public BuildingInteriorAnimationPanel(MainGui mainGui, String name, JPanel buildingAnimation)
	{
		this.gui = mainGui;
		this._buildingAnimation = buildingAnimation;
		this.myName = name;
		this.setPreferredSize(new Dimension(PANELX, PANELY));
		this.setLayout(new BorderLayout());
		
		
		add(buildingAnimation, BorderLayout.CENTER);
	}
	
	public JPanel getBuildingAnimation() { return _buildingAnimation; }

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
