package gui;

/**
 * This is the BuildingWindow class.  It uses a CardLayout to cycle through the individual building GUIs.
 * @author Tanner Zigrang
 */

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BuildingWindow extends JPanel 
{
	private static final int PANELX = 1024 * 2 / 3;
	private static final int PANELY = 720 / 2;
	
	public BuildingWindow()
	{
		this.setLayout(new CardLayout());
		this.setBorder(BorderFactory.createTitledBorder("Building"));
		this.setPreferredSize(new Dimension(PANELX, PANELY));
	}

}
