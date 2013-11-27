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
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import city.Directory;
import city.Time;
import city.bank.Bank;
import city.home.Apartment;
import city.home.ApartmentBuilding;
import city.home.House;
import city.market.Market;
import city.restaurant.omar.OmarRestaurant;
import city.restaurant.ryan.RyanRestaurant;
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
		//Market
		WorldViewBuilding b3 = _worldView.addBuilding(10, 5, 40);
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market", b3, bp3, _worldView);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.getName());
		_buildingInteriorAnimationPanels.add(bp3);
		
		// Yixin's Restaurant:
		WorldViewBuilding b = _worldView.addBuilding(10, 1, 40);
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.getName());
		_buildingInteriorAnimationPanels.add(bp);
		
		//Omar's Restaurant
		WorldViewBuilding b9 = _worldView.addBuilding(8, 3, 40);
		BuildingInteriorAnimationPanel bp9 = new BuildingInteriorAnimationPanel(this, "Omar's Restaurant", new city.restaurant.omar.gui.OmarRestaurantAnimationPanel());
		b9.setBuildingPanel(bp9);
		OmarRestaurant or = new OmarRestaurant("Omar's Restaurant", b9, bp9);
		Directory.addPlace(or);
		_buildingCardLayoutPanel.add( bp9, bp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(or.getName());
		_buildingInteriorAnimationPanels.add(bp9);
		
		//Ryan Restaurant
		WorldViewBuilding bR = _worldView.addBuilding(8, 1, 40);
		BuildingInteriorAnimationPanel bpR = new BuildingInteriorAnimationPanel(this, "Ryan's Restaurant", new city.restaurant.ryan.gui.RyanAnimationPanel());
		bR.setBuildingPanel(bpR);
		RyanRestaurant rr = new RyanRestaurant("Ryan's Restaurant", bR, bpR);
		Directory.addPlace(rr);
		_buildingCardLayoutPanel.add( bpR, bpR.getName() );
		cPanel.currentBuildingPanel.addBuilding(rr.getName());
		_buildingInteriorAnimationPanels.add(bpR);
		
		//Bank
		WorldViewBuilding b2 = _worldView.addBuilding(10, 3, 40);
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.getName());
		_buildingInteriorAnimationPanels.add(bp2);
		
		//Initializing houses
		for(int i = 1; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(1, i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.getName());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing apartments
		for(int i = 1; i < 6; i++){
			 WorldViewBuilding b4 = _worldView.addBuilding(2, i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.getName());
			_buildingInteriorAnimationPanels.add(bp4);
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
		
		// TODO Here, add scanning the configuration file.
		
		cPanel.addPerson("noJobDude", 300, "None", true, "house");
		
		/*
		cPanel.addPerson("Omar", 300, "Bank Teller", true, "house");
		cPanel.addPerson("Omar1", 300, "Bank Host", true, "house");
		cPanel.addPerson("Omar2", 300, "Bank Customer", true, "house");
		cPanel.addPerson("Omar3", 300, "Bank Customer", true, "house");
		cPanel.addPerson("Omar4", 300, "Bank Customer", true, "apartment");
		cPanel.addPerson("Omar5", 300, "Bank Customer", true, "apartment");
		cPanel.addPerson("Omar9", 300, "Restaurant Host", true, "apartment");
		cPanel.addPerson("Omar10", 300, "Restaurant Cashier", true, "apartment");
		cPanel.addPerson("Omar11", 300, "Cook", true, "apartment");
		cPanel.addPerson("Omar12", 300, "Omar Waiter", true, "apartment");
		cPanel.addPerson("Omar13", 300, "Omar Customer", true, "apartment");
		cPanel.addPerson("Omar14", 300, "Market Customer", true, "apartment");
		*/
		
		
		cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
		cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
		cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
//		cPanel.addPerson("Yixin3", 300, "Waiter", true, "house");
//		cPanel.addPerson("Yixin4", 300, "Yixin Customer", true, "apartment");
//		cPanel.addPerson("Yixin5", 300, "Bank Teller", true, "apartment");
//		cPanel.addPerson("Yixin6", 300, "Bank Host", true, "apartment");
		cPanel.addPerson("Yixin7", 300, "Market Cashier", true, "apartment");
		cPanel.addPerson("Yixin8", 300, "Market Employee", true, "apartment");
		cPanel.addPerson("Yixin9", 300, "Restaurant Host", true, "apartment");
		cPanel.addPerson("Yixin10", 300, "Restaurant Cashier", true, "apartment");
		cPanel.addPerson("Yixin11", 300, "Cook", true, "apartment");
		cPanel.addPerson("Yixin12", 300, "Omar Waiter", true, "apartment");
		cPanel.addPerson("Yixin13", 300, "Omar Customer", true, "apartment");
//		cPanel.addPerson("Yixin14", 300, "Market Customer", true, "apartment");
//		cPanel.addPerson("Yixin15", 300, "Restaurant Cashier", true, "house");
//		cPanel.addPerson("Yixin16", 300, "Cook", true, "house");
//		cPanel.addPerson("Yixin17", 300, "Ryan Waiter", true, "house");
//		cPanel.addPerson("Yixin18", 300, "Ryan Customer", true, "apartment");
//		cPanel.addPerson("Yixin19", 300, "Restaurant Host", true, "house");
//		cPanel.addPerson("Yixin20", 300, "Restaurant Host", true, "house");
//		cPanel.addPerson("Yixin21", 300, "Restaurant Cashier", true, "house");
//		cPanel.addPerson("Yixin22", 300, "Cook", true, "house");
//		
		
		Time.startTimer();
	}
	
	public WorldView getWorldView() { return _worldView; }
	
	 public void displayBuildingPanel(BuildingInteriorAnimationPanel bp ) {
		//System.out.println("MainGui: showing building " + bp.getName() + "'s AnimationPanel." );
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
