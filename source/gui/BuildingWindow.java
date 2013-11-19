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
	public BuildingWindow()
	{
		this.setLayout(new CardLayout());
		this.setBorder(BorderFactory.createTitledBorder("Building"));
		this.setPreferredSize(new Dimension(2048/3, 1440/5));
	}

}
