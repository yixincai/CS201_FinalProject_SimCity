package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainGui extends JFrame 
{
	
	/**
	 * Constructor for the MainGui window
	 */
	public MainGui()
	{
		//The code below is for setting up the default window settings
		this.setSize(1024, 720);
		this.setLocationRelativeTo(null);
		this.setTitle("SimCity201 - Team 18");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
		//The code below will add a tabbed panel to hold all the control panels.  Should take the right third of the window
		ControlPanel cpanel = new ControlPanel();
		cpanel.setPreferredSize(new Dimension(1024/3, 720));
		this.add(cpanel, Component.RIGHT_ALIGNMENT);
		
		//The code below will add an area for the two gui areas to go.
		JPanel guiArea = new JPanel();
		guiArea.setBorder(BorderFactory.createTitledBorder("Animations"));
		guiArea.setPreferredSize(new Dimension(2048/3, 720));
		this.add(guiArea, Component.CENTER_ALIGNMENT);
		
	}
	
	/**
	 * Main routine to create an instance of the MainGui window
	 */
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		MainGui gui = new MainGui();
	}
}
