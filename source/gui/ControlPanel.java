package gui;

/**
 * This class is the container class for each of the three types of control panels needed in the application
 * 1) New Person Panel
 * 2) Current Person Panel
 * 3) Current Building Panel
 * @author Tanner Zigrang
 */

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ControlPanel extends JTabbedPane {
	
	public ControlPanel()
	{
		//This is all placeholder code just to get the panels into tabs.  Each tab will have its own class eventually.
		CreatePersonPanel NewPersonPanel = new CreatePersonPanel();
		JPanel CurrentPersonPanel = new JPanel();
		JPanel CurrentBuildingPanel = new JPanel();
		this.addTab("Current Person", null, CurrentPersonPanel, "Not Yet Implemented");
		this.addTab("Current Building", null, CurrentBuildingPanel, "Not Yet Implemented");
		this.addTab("New Person", null, NewPersonPanel, "Not Yet Implemented");
		this.setPreferredSize(new Dimension(1024/3, 720));
	}

}
