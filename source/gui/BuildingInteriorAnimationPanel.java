package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class BuildingInteriorAnimationPanel extends JPanel implements ActionListener
{
	
	public static final int WIDTH = 1024 * 2 /3;
	public static final int HEIGHT = 720 / 2;
	MainGui gui;
	JPanel _buildingAnimation;
	//Timer animationTime;
	
    String myName;
    
    //View for the inside of a building
	public BuildingInteriorAnimationPanel(MainGui mainGui, String name, JPanel buildingAnimation)
	{
		//animationTimer = new Timer();
		this.gui = mainGui;
		this._buildingAnimation = buildingAnimation;
		this.myName = name;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
