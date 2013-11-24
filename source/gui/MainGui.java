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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import city.Directory;
import city.Time;
import city.bank.Bank;
import city.market.Market;
import city.restaurant.yixin.YixinRestaurant;
import city.transportation.BusAgent;
import city.transportation.BusStopObject;
import city.transportation.gui.BusAgentGui;

public class MainGui extends JFrame 
{
	private static int FRAMEX = 1024;
	private static int FRAMEY = 720;
	
     BuildingCardLayoutPanel _buildingCardLayoutPanel;
     ControlPanel cPanel;
     
     List<BuildingInteriorAnimationPanel> _buildingInteriorAnimationPanels = new ArrayList<BuildingInteriorAnimationPanel>();
     
     WorldView _worldView;
	/**
	 * Constructor for the MainGui window
	 */
	public MainGui()
	{
		//The code below is for setting up the default window settings
		this.setSize(FRAMEX, FRAMEY);
		this.setLocationRelativeTo(null);
		this.setTitle("SimCity201 - Team 18");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
		//Building View
		_buildingCardLayoutPanel = new BuildingCardLayoutPanel();
	    
	    //World View
	    _worldView = new WorldView();
	    
	    //Control Panel
	    cPanel = new ControlPanel(this);
	        
		//The code below will add an area for the two gui areas to go. BuildingView + WorldView
		JPanel animationArea = new JPanel();
		animationArea.setLayout(new BoxLayout(animationArea, BoxLayout.Y_AXIS));
		animationArea.setPreferredSize(new Dimension(2048/3, 720));
		animationArea.add(_worldView);
		animationArea.add(_buildingCardLayoutPanel);
		this.add(animationArea, Component.LEFT_ALIGNMENT);
		
		//Bus Stops
		WorldViewBuilding b5 = _worldView.addBuilding(0, 0, 30);
		BusStopObject busStop0 = new BusStopObject("Bus Stop " + 0, b5);
		Directory.addPlace(busStop0);
		WorldViewBuilding b6 = _worldView.addBuilding(12, 0, 30);
		BusStopObject busStop1 = new BusStopObject("Bus Stop " + 1, b6);
		Directory.addPlace(busStop1);
		WorldViewBuilding b7 = _worldView.addBuilding(12, 6, 30);
		BusStopObject busStop2 = new BusStopObject("Bus Stop " + 2, b7);
		Directory.addPlace(busStop2);
		WorldViewBuilding b8 = _worldView.addBuilding(0, 6, 30);
		BusStopObject busStop3 = new BusStopObject("Bus Stop " + 3, b8);
		Directory.addPlace(busStop3);
		
		BusAgent bus = new BusAgent("Bus");
		BusAgentGui busGui = new BusAgentGui(bus, null);
		bus.setBusAgentGui(busGui);
		busGui.setPresent(true);
		_worldView.addGui(busGui);
		bus.startThread();
		
		// Hard-coded instantiation of all the buildings in the city:
		// Yixin's Restaurant:
		WorldViewBuilding b = _worldView.addBuilding(10, 1, 40);
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.getName()); // unsure if this is needed or not; I deleted it earlier
        _buildingInteriorAnimationPanels.add(bp);
        
        //Bank
        WorldViewBuilding b2 = _worldView.addBuilding(10, 3, 40);
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.getName());
        _buildingInteriorAnimationPanels.add(bp2);
        
        WorldViewBuilding b3 = _worldView.addBuilding(10, 5, 40);
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market", b3, bp3);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.getName());
        _buildingInteriorAnimationPanels.add(bp3);
        
        //Going to be houses
        for(int i = 0; i < 4; i+=2){
        	for(int j = 0; j < 5; j++){
                WorldViewBuilding b4 = _worldView.addBuilding(i, j, 20);
        		BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "BankHouse", new city.bank.gui.BankAnimationPanel());
        		b4.setBuildingPanel(bp4);
        		Bank bankTemp = new Bank("Bank", b4, bp4);
        		Directory.addPlace(bankTemp);
        		_buildingCardLayoutPanel.add( bp4, bp4.getName() );
        		cPanel.currentBuildingPanel.addBuilding(bankTemp.getName());
                _buildingInteriorAnimationPanels.add(bp4);
        	}
        }
        
        
        
        /*
        //Create the BuildingPanel for each Building object
        ArrayList<WorldViewBuilding> worldViewBuildings = _worldView.getBuildings();
        for ( int i=0; i<worldViewBuildings.size(); i++ )
        {
                WorldViewBuilding b = worldViewBuildings.get(i);
                BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this,i);
                b.setBuildingPanel( bp );
                _buildingCardLayoutPanel.add( bp, "Building " + i );
        }
        
        
		 for (int i = 3; i < 6; i++) {
			WorldViewBuilding b = new WorldViewBuilding( i, 1, 40 );
			
			worldViewBuildings.add( b );
		 }
		 for ( int i=0; i<2; i++ ) {
			 for ( int j=0; j<5; j++ ) {
				 if(i == 1 && j == 2) continue;
				 WorldViewBuilding b = new WorldViewBuilding( i, j, 30 );
				 
				 worldViewBuildings.add( b );
			 }
		 }
		 */
        
      //The code below will add a tabbed panel to hold all the control panels.  Should take the right third of the window
  	  
  	  this.add(cPanel, Component.RIGHT_ALIGNMENT);
  	  this.pack();		
  	  this.setVisible(true);
	
  	  Time.startTimer();
	}
	
	public WorldView getWorldView() { return _worldView; }
	
	 public void displayBuildingPanel(BuildingInteriorAnimationPanel bp ) {
         System.out.println( bp.getName() );
         ((CardLayout) _buildingCardLayoutPanel.getLayout()).show(_buildingCardLayoutPanel, bp.getName());
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
