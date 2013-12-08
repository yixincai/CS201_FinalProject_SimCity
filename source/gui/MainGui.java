package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import gui.trace.AlertLog;
import gui.trace.AlertTag;
import gui.trace.TracePanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import city.Directory;
import city.Time;
import city.bank.Bank;
import city.home.*;
import city.market.Market;
import city.restaurant.omar.OmarRestaurant;
import city.restaurant.ryan.RyanRestaurant;
import city.restaurant.yixin.YixinRestaurant;
import city.transportation.*;
import city.transportation.gui.BusAgentGui;

public class MainGui extends JFrame 
{
	private static int FRAMEX = 1024;
	private static int FRAMEY = 820;
	Semaphore[][] grid;
	
	BuildingCardLayoutPanel _buildingCardLayoutPanel;
	ControlPanel cPanel;
	TracePanel tPanel;
	String name = "Main Gui";
	
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
		tPanel = new TracePanel();
		cPanel = new ControlPanel(this);
		    
		//The code below will add an area for the two gui areas to go. BuildingView + WorldView
		JPanel animationArea = new JPanel();
		animationArea.setLayout(new BoxLayout(animationArea, BoxLayout.Y_AXIS));
		animationArea.setPreferredSize(new Dimension(2048/3, 720));
		animationArea.add(_worldView);
		animationArea.add(_buildingCardLayoutPanel);
		this.add(animationArea, Component.LEFT_ALIGNMENT);
		
		//Bus Stops
		WorldViewBuilding b5 = _worldView.addBuilding(0, 0, 20);
		BusStopObject busStop0 = new BusStopObject("Bus Stop " + 0, b5);
		Directory.addPlace(busStop0);//
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 0 successfully added");
		WorldViewBuilding b6 = _worldView.addBuilding(58, 0, 20);
		BusStopObject busStop1 = new BusStopObject("Bus Stop " + 1, b6);
		Directory.addPlace(busStop1);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 1 successfully added");
		WorldViewBuilding b7 = _worldView.addBuilding(58, 28, 20);
		BusStopObject busStop2 = new BusStopObject("Bus Stop " + 2, b7);
		Directory.addPlace(busStop2);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 2 successfully added");
		WorldViewBuilding b8 = _worldView.addBuilding(0, 28, 20);
		BusStopObject busStop3 = new BusStopObject("Bus Stop " + 3, b8);
		Directory.addPlace(busStop3);
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Stop 3 successfully added");
		
		BusAgent bus = new BusAgent("Bus");
		BusAgentGui busGui = new BusAgentGui(bus, null);
		bus.setBusAgentGui(busGui);
		busGui.setPresent(true);
		_worldView.addGui(busGui);
		bus.startThread();
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Added");
		
		// Hard-coded instantiation of all the buildings in the city:
		//Market
		WorldViewBuilding b3 = _worldView.addBuilding(24, 5, 40);
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market 1", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market 1", b3, bp3, _worldView);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.name());
		_buildingInteriorAnimationPanels.add(bp3);
		
		WorldViewBuilding marketBuilding2 = _worldView.addBuilding(28, 5, 40);
		BuildingInteriorAnimationPanel marketBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Market 2", new city.market.gui.MarketAnimationPanel());
		marketBuilding2.setBuildingPanel(marketBuildingPanel2);
		Market market2 = new Market("Market 2", marketBuilding2, marketBuildingPanel2, _worldView);
		Directory.addPlace(market2);
		_buildingCardLayoutPanel.add( marketBuildingPanel2, marketBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(market2.name());
		_buildingInteriorAnimationPanels.add(marketBuildingPanel2);
		
		// Yixin's Restaurant:
		WorldViewBuilding b = _worldView.addBuilding(32, 5, 40);
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.name());
		_buildingInteriorAnimationPanels.add(bp);
		
		//Omar's Restaurant
		WorldViewBuilding b9 = _worldView.addBuilding(24, 19, 40);
		BuildingInteriorAnimationPanel bp9 = new BuildingInteriorAnimationPanel(this, "Omar's Restaurant", new city.restaurant.omar.gui.OmarRestaurantAnimationPanel());
		b9.setBuildingPanel(bp9);
		OmarRestaurant or = new OmarRestaurant("Omar's Restaurant", b9, bp9);
		Directory.addPlace(or);
		_buildingCardLayoutPanel.add( bp9, bp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(or.name());
		_buildingInteriorAnimationPanels.add(bp9);
		
		//Ryan Restaurant
		WorldViewBuilding bR = _worldView.addBuilding(28, 19, 40);
		BuildingInteriorAnimationPanel bpR = new BuildingInteriorAnimationPanel(this, "Ryan's Restaurant", new city.restaurant.ryan.gui.RyanAnimationPanel());
		bR.setBuildingPanel(bpR);
		RyanRestaurant rr = new RyanRestaurant("Ryan's Restaurant", bR, bpR);
		Directory.addPlace(rr);
		_buildingCardLayoutPanel.add( bpR, bpR.getName() );
		cPanel.currentBuildingPanel.addBuilding(rr.name());
		_buildingInteriorAnimationPanels.add(bpR);

		//TODO change to Eric's restaurant
		WorldViewBuilding restaurantBuilding4 = _worldView.addBuilding(32, 19, 40);
		BuildingInteriorAnimationPanel restaurantBuildingPanel4 = new BuildingInteriorAnimationPanel(this, "Eric's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding4.setBuildingPanel(restaurantBuildingPanel4);
		YixinRestaurant er = new YixinRestaurant("Eric's Restaurant", restaurantBuilding4, restaurantBuildingPanel4);
		Directory.addPlace(er);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel4, restaurantBuildingPanel4.getName() );
		cPanel.currentBuildingPanel.addBuilding(er.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel4);
		
		//TODO change to Tanner's restaurant
		WorldViewBuilding restaurantBuilding5 = _worldView.addBuilding(44, 5, 40);
		BuildingInteriorAnimationPanel restaurantBuildingPanel5 = new BuildingInteriorAnimationPanel(this, "Tanner's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding5.setBuildingPanel(restaurantBuildingPanel5);
		YixinRestaurant tr = new YixinRestaurant("Tanner's Restaurant", restaurantBuilding5, restaurantBuildingPanel5);
		Directory.addPlace(tr);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel5, restaurantBuildingPanel5.getName() );
		cPanel.currentBuildingPanel.addBuilding(tr.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel5);
		
		//Bank
		WorldViewBuilding b2 = _worldView.addBuilding(48, 5, 40);
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.name());
		_buildingInteriorAnimationPanels.add(bp2);
		
//		WorldViewBuilding bankBuilding2 = _worldView.addBuilding(44, 19, 40);
//		BuildingInteriorAnimationPanel bankBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Bank 2", new city.bank.gui.BankAnimationPanel());
//		bankBuilding2.setBuildingPanel(bankBuildingPanel2);
//		Bank bank2 = new Bank("Bank 2", bankBuilding2, bankBuildingPanel2);
//		Directory.addPlace(bank2);
//		_buildingCardLayoutPanel.add( bankBuildingPanel2, bankBuildingPanel2.getName() );
//		cPanel.currentBuildingPanel.addBuilding(bank2.name());
//		_buildingInteriorAnimationPanels.add(bankBuildingPanel2);
		
		//Initializing houses
		for(int i = 1; i < 3; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 13 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing apartments
		for(int i = 1; i < 3; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(10, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(10, 13 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing more houses
		for(int i = 1; i < 3; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + (i + 5), new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i + 5, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 13 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + (i + 5), new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i + 5, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing more apartments
		for(int i = 1; i < 3; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(14, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(14, 13 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		
		Lane l1 = new Lane( 8*10+41, 15*10+30, 80, 10, 1, 0, true, Color.green, Color.black );
		Directory.addLanes(l1);
		Lane l2 = new Lane( 24*10+41, 15*10+30, 120, 10, 1, 0, true, Color.green, Color.black );
		Directory.addLanes(l2);
		Lane l3 = new Lane( 44*10+41, 15*10+30, 80, 10, 1, 0, true, Color.green, Color.black );
		Directory.addLanes(l3);
		Lane l6 = new Lane( 44*10+41, 12*10+30, 80, 10, -1, 0, true, Color.yellow, Color.black );
		Directory.addLanes(l6);
		Lane l5 = new Lane( 24*10+41, 12*10+30, 120, 10, -1, 0, true, Color.yellow, Color.black );
		Directory.addLanes(l5);
		Lane l4 = new Lane( 8*10+41, 12*10+30, 80, 10, -1, 0, true, Color.yellow, Color.black );
		Directory.addLanes(l4);
		//intersections
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
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
		int xdim = 60;
	    int ydim = 30;
	    grid = new Semaphore[xdim][ydim];
	    for (int i=0; i<xdim; i++)
	    	for (int j=0; j<ydim; j++)
	    		grid[i][j] = new Semaphore(1,true);
	    //set access to all buildings to false
	    for (int i=8; i<16; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=8; i<16; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=24; i<36; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=24; i<36; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=44; i<52; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=44; i<52; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();
		//The code below will add a tabbed panel to hold all the control panels.  Should take the right third of the window
		
		this.add(cPanel, Component.RIGHT_ALIGNMENT);
		this.add(tPanel, Component.RIGHT_ALIGNMENT);
		this.pack();		
		this.setVisible(true);
		
		// //Ryan Restaurant interaction
		// cPanel.addPerson("Ryan", 300, "Restaurant Host", true, "apartment");
		// cPanel.addPerson("Ryan1", 300, "Restaurant Cashier", true, "apartment");
		// cPanel.addPerson("Ryan2", 300, "Cook", true, "apartment");
		
		// cPanel.addPerson("Ryan3", 300, "Restaurant Host", true, "apartment");
		// cPanel.addPerson("Ryan4", 300, "Restaurant Cashier", true, "apartment");
		// cPanel.addPerson("Ryan5", 300, "Cook", true, "apartment");
		
		// cPanel.addPerson("Ryan6", 300, "Restaurant Host", true, "apartment");
		// cPanel.addPerson("Ryan7", 300, "Restaurant Cashier", true, "apartment");
		// cPanel.addPerson("Ryan8", 300, "Cook", true, "apartment");
		// cPanel.addPerson("Ryan9", 300, "Ryan Waiter", true, "house");
		// cPanel.addPerson("Ryan10", 300, "Ryan Customer", true, "apartment");
		
		// cPanel.addPerson("Ryan11", 300, "Market Cashier", true, "apartment");
		// cPanel.addPerson("Ryan12", 300, "Market Employee", true, "apartment");
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
