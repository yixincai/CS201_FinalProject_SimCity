package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import javax.swing.BoxLayout;
import javax.swing.JFrame;


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
		
		//The code below will add a tabbed panel to hold all the control panels
		this.add(new ControlPanel());
		
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
