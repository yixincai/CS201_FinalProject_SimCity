package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame 
{
	private static int FRAMEX = 1024;
	private static int FRAMEY = 1024;
	
	 JPanel buildingPanels;
     CardLayout cardLayout;
     ControlPanel cPanel;
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
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
		//Building View
		cardLayout = new CardLayout();
		buildingPanels = new JPanel();
	    buildingPanels.setLayout( cardLayout ); 
	    buildingPanels.setBackground(Color.YELLOW);
	    
	    //World View
	    WorldView worldView = new WorldView();
	        
		//The code below will add an area for the two gui areas to go. BuildingView + WorldView
		JPanel guiArea = new JPanel();
		guiArea.setLayout(new BoxLayout(guiArea, BoxLayout.Y_AXIS));
		guiArea.setPreferredSize(new Dimension(2048/3, 720));
		guiArea.add(worldView);
		guiArea.add(buildingPanels);
		this.add(guiArea, Component.RIGHT_ALIGNMENT);
        
        //Create the BuildingPanel for each Building object
        ArrayList<Building> buildings = worldView.getBuildings();
        for ( int i=0; i<buildings.size(); i++ ) {
                Building b = buildings.get(i);
                BuildingPanel bp = new BuildingPanel(this,b,i);
                b.setBuildingPanel( bp );
                buildingPanels.add( bp, "Building " + i );
        }
        
      //The code below will add a tabbed panel to hold all the control panels.  Should take the right third of the window
  	  cPanel = new ControlPanel();
  	  this.add(cPanel, Component.LEFT_ALIGNMENT);
  	  		
  	  this.setVisible(true);
	
	}
	
	 public void displayBuildingPanel(BuildingPanel bp ) {
         System.out.println( bp.getName() );
         cardLayout.show( buildingPanels, bp.getName() );
         cPanel.updateBuildingInfo(bp);
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
