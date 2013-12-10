package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import gui.trace.*;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
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

@SuppressWarnings("serial")
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
		try {
			ImageAtlas.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		WorldViewBuilding bs0 = _worldView.addBuilding(0, 0, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop0 = new BusStopObject("Bus Stop " + 0, bs0);
		Directory.addPlace(busStop0);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 0 successfully added");
		WorldViewBuilding bs1 = _worldView.addBuilding(29, 0, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop1 = new BusStopObject("Bus Stop " + 1, bs1);
		Directory.addPlace(busStop1);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 1 successfully added");
		WorldViewBuilding bs2 = _worldView.addBuilding(58, 0, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop2 = new BusStopObject("Bus Stop " + 2, bs2);
		Directory.addPlace(busStop2);
		AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Bus Stop 2 successfully added");
		WorldViewBuilding bs3 = _worldView.addBuilding(58, 28, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop3 = new BusStopObject("Bus Stop " + 3, bs3);
		Directory.addPlace(busStop3);
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Stop 3 successfully added");
		WorldViewBuilding bs4 = _worldView.addBuilding(29, 28, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop4 = new BusStopObject("Bus Stop " + 4, bs4);
		Directory.addPlace(busStop4);
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Stop 4 successfully added");
		WorldViewBuilding bs5 = _worldView.addBuilding(0, 28, 20, ImageAtlas.mapAtlas.get("Bus Stop"));
		BusStopObject busStop5 = new BusStopObject("Bus Stop " + 5, bs5);
		Directory.addPlace(busStop5);
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Stop 5 successfully added");
		
		BusAgent bus = new BusAgent("Bus");
		BusAgentGui busGui = new BusAgentGui(bus, null);
		bus.setBusAgentGui(busGui);
		busGui.setPresent(true);
		_worldView.addGui(busGui);
		bus.startThread();
		AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Bus Added");
		
		// Hard-coded instantiation of all the buildings in the city:
		//Market
		WorldViewBuilding b3 = _worldView.addBuilding(24, 5, 40, ImageAtlas.mapAtlas.get("Market"));
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market 1", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market 1", b3, bp3, _worldView);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.name());
		_buildingInteriorAnimationPanels.add(bp3);
		
		//Bank
		WorldViewBuilding b2 = _worldView.addBuilding(28, 5, 40, ImageAtlas.mapAtlas.get("Bank"));
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.name());
		_buildingInteriorAnimationPanels.add(bp2);
		
		// Yixin's Restaurant:
		WorldViewBuilding b = _worldView.addBuilding(32, 5, 40, ImageAtlas.mapAtlas.get("YixinRestaurant"));
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.name());
		_buildingInteriorAnimationPanels.add(bp);
		
		//Omar's Restaurant
		WorldViewBuilding b9 = _worldView.addBuilding(24, 19, 40, ImageAtlas.mapAtlas.get("OmarRestaurant"));
		BuildingInteriorAnimationPanel bp9 = new BuildingInteriorAnimationPanel(this, "Omar's Restaurant", new city.restaurant.omar.gui.OmarRestaurantAnimationPanel());
		b9.setBuildingPanel(bp9);
		OmarRestaurant or = new OmarRestaurant("Omar's Restaurant", b9, bp9);
		Directory.addPlace(or);
		_buildingCardLayoutPanel.add( bp9, bp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(or.name());
		_buildingInteriorAnimationPanels.add(bp9);
		
		//Ryan Restaurant
		WorldViewBuilding bR = _worldView.addBuilding(28, 19, 40, ImageAtlas.mapAtlas.get("RyanRestaurant"));
		BuildingInteriorAnimationPanel bpR = new BuildingInteriorAnimationPanel(this, "Ryan's Restaurant", new city.restaurant.ryan.gui.RyanAnimationPanel());
		bR.setBuildingPanel(bpR);
		RyanRestaurant rr = new RyanRestaurant("Ryan's Restaurant", bR, bpR);
		Directory.addPlace(rr);
		_buildingCardLayoutPanel.add( bpR, bpR.getName() );
		cPanel.currentBuildingPanel.addBuilding(rr.name());
		_buildingInteriorAnimationPanels.add(bpR);

		//TODO change to Eric's restaurant
		WorldViewBuilding restaurantBuilding4 = _worldView.addBuilding(32, 19, 40, ImageAtlas.mapAtlas.get("EricRestaurant"));
		BuildingInteriorAnimationPanel restaurantBuildingPanel4 = new BuildingInteriorAnimationPanel(this, "Eric's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding4.setBuildingPanel(restaurantBuildingPanel4);
		YixinRestaurant er = new YixinRestaurant("Eric's Restaurant", restaurantBuilding4, restaurantBuildingPanel4);
		Directory.addPlace(er);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel4, restaurantBuildingPanel4.getName() );
		cPanel.currentBuildingPanel.addBuilding(er.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel4);

		//another market
		WorldViewBuilding marketBuilding2 = _worldView.addBuilding(44, 5, 40, ImageAtlas.mapAtlas.get("Market"));
		BuildingInteriorAnimationPanel marketBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Market 2", new city.market.gui.MarketAnimationPanel());
		marketBuilding2.setBuildingPanel(marketBuildingPanel2);
		Market market2 = new Market("Market 2", marketBuilding2, marketBuildingPanel2, _worldView);
		Directory.addPlace(market2);
		_buildingCardLayoutPanel.add( marketBuildingPanel2, marketBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(market2.name());
		_buildingInteriorAnimationPanels.add(marketBuildingPanel2);

		//another bank
		WorldViewBuilding bankBuilding2 = _worldView.addBuilding(48, 5, 40, ImageAtlas.mapAtlas.get("Bank"));
		BuildingInteriorAnimationPanel bankBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Bank 2", new city.bank.gui.BankAnimationPanel());
		bankBuilding2.setBuildingPanel(bankBuildingPanel2);
		Bank bank2 = new Bank("Bank 2", bankBuilding2, bankBuildingPanel2);
		Directory.addPlace(bank2);
		_buildingCardLayoutPanel.add( bankBuildingPanel2, bankBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank2.name());
		_buildingInteriorAnimationPanels.add(bankBuildingPanel2);
		
		//TODO change to Tanner's restaurant
		WorldViewBuilding restaurantBuilding5 = _worldView.addBuilding(44, 19, 40, ImageAtlas.mapAtlas.get("YixinRestaurant"));
		BuildingInteriorAnimationPanel restaurantBuildingPanel5 = new BuildingInteriorAnimationPanel(this, "Tanner's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding5.setBuildingPanel(restaurantBuildingPanel5);
		YixinRestaurant tr = new YixinRestaurant("Tanner's Restaurant", restaurantBuilding5, restaurantBuildingPanel5);
		Directory.addPlace(tr);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel5, restaurantBuildingPanel5.getName() );
		cPanel.currentBuildingPanel.addBuilding(tr.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel5);
		
		//Initializing houses
		for(int i = 1; i < 3; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 3 + 2*i, 20, ImageAtlas.mapAtlas.get("House"));
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 13 + 2*i, 20, ImageAtlas.mapAtlas.get("House"));
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
			WorldViewBuilding b4 = _worldView.addBuilding(10, 3 + 2*i, 20, ImageAtlas.mapAtlas.get("Apartment"));
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(10, 13 + 2*i, 20, ImageAtlas.mapAtlas.get("Apartment"));
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
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 3 + 2*i, 20, ImageAtlas.mapAtlas.get("House"));
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + (i + 5), new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i + 5, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 13 + 2*i, 20, ImageAtlas.mapAtlas.get("House"));
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
			WorldViewBuilding b4 = _worldView.addBuilding(14, 3 + 2*i, 20, ImageAtlas.mapAtlas.get("Apartment"));
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(14, 13 + 2*i, 20, ImageAtlas.mapAtlas.get("Apartment"));
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//vehicle lanes
		Lane l3 = new Lane( 8*10+41, 15*10+30, 80, 10, 1, 0, true, Color.black, Color.yellow );
		Lane l4 = new Lane( 24*10+41, 15*10+30, 120, 10, 1, 0, true, Color.black, Color.yellow );
		Lane l5 = new Lane( 44*10+41, 15*10+30, 80, 10, 1, 0, true, Color.black, Color.yellow );
		Lane l2 = new Lane( 44*10+41, 12*10+30, 80, 10, -1, 0, true, Color.black, Color.yellow );
		Lane l1 = new Lane( 24*10+41, 12*10+30, 120, 10, -1, 0, true, Color.black, Color.yellow );
		Lane l0 = new Lane( 8*10+41, 12*10+30, 80, 10, -1, 0, true, Color.black, Color.yellow );
		Directory.addLanes(l0);
		Directory.addLanes(l1);
		Directory.addLanes(l2);
		Directory.addLanes(l3);
		Directory.addLanes(l4);
		Directory.addLanes(l5);
		
		//truck lanes
		Lane tl0 = new Lane( 19*10+41, 5*10+30, 10, 40, 0, 1, false, Color.black, Color.yellow );
		Lane tl1 = new Lane( 20*10+41, 5*10+30, 10, 40, 0, -1, false, Color.black, Color.yellow );
		Lane tl2 = new Lane( 39*10+41, 5*10+30, 10, 40, 0, 1, false, Color.black, Color.yellow );
		Lane tl3 = new Lane( 40*10+41, 5*10+30, 10, 40, 0, -1, false, Color.black, Color.yellow );
		Lane tl4 = new Lane( 19*10+41, 19*10+30, 10, 40, 0, 1, false, Color.black, Color.yellow );
		Lane tl5 = new Lane( 20*10+41, 19*10+30, 10, 40, 0, -1, false, Color.black, Color.yellow );
		Lane tl6 = new Lane( 39*10+41, 19*10+30, 10, 40, 0, 1, false, Color.black, Color.yellow );
		Lane tl7 = new Lane( 40*10+41, 19*10+30, 10, 40, 0, -1, false, Color.black, Color.yellow );
		Directory.addLanes(tl0);
		Directory.addLanes(tl1);
		Directory.addLanes(tl2);
		Directory.addLanes(tl3);
		Directory.addLanes(tl4);
		Directory.addLanes(tl5);
		Directory.addLanes(tl6);
		Directory.addLanes(tl7);

		//intersections
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));
		Directory.intersections().add(new Semaphore(1,true));

		//sidewalk
		//left
		Lane sw0 = new Lane( 8*10+41, 9*10+30, 80, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane sw1 = new Lane( 24*10+41, 9*10+30, 120, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane sw2 = new Lane( 44*10+41, 9*10+30, 80, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		//right
		Lane sw3 = new Lane( 8*10+41, 18*10+30, 80, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane sw4 = new Lane( 24*10+41, 18*10+30, 120, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane sw5 = new Lane( 44*10+41, 18*10+30, 80, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane sw6 = new Lane( 16*10+41, 5*10+30, 10, 40, 0, 1, false, Color.lightGray, Color.lightGray );
		//up
		Lane sw7 = new Lane( 23*10+41, 5*10+30, 10, 40, 0, -1, false, Color.lightGray, Color.lightGray );
		//down
		Lane sw8 = new Lane( 36*10+41, 5*10+30, 10, 40, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane sw9 = new Lane( 43*10+41, 5*10+30, 10, 40, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane sw10 = new Lane( 16*10+41, 19*10+30, 10, 40, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane sw11 = new Lane( 23*10+41, 19*10+30, 10, 40, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane sw12 = new Lane( 36*10+41, 19*10+30, 10, 40, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane sw13 = new Lane( 43*10+41, 19*10+30, 10, 40, 0, -1, false, Color.lightGray, Color.lightGray );
		Directory.addSidewalk(sw0);
		Directory.addSidewalk(sw1);
		Directory.addSidewalk(sw2);
		Directory.addSidewalk(sw3);
		Directory.addSidewalk(sw4);
		Directory.addSidewalk(sw5);
		Directory.addSidewalk(sw6);
		Directory.addSidewalk(sw7);
		Directory.addSidewalk(sw8);
		Directory.addSidewalk(sw9);
		Directory.addSidewalk(sw10);
		Directory.addSidewalk(sw11);
		Directory.addSidewalk(sw12);
		Directory.addSidewalk(sw13);
		
		//bus pedestrian lanes
		Lane bl0 = new Lane( 15*10+41, 2*10+30, 10, 30, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl1 = new Lane( 3*10+41, 2*10+30, 120, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl2 = new Lane( 2*10+41, 3*10+30, 10, 20, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl3 = new Lane( 3*10+41, 4*10+30, 60, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl4 = new Lane( 29*10+41, 2*10+30, 10, 30, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl5 = new Lane( 30*10+41, 2*10+30, 10, 30, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl6 = new Lane( 44*10+41, 2*10+30, 10, 30, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl7 = new Lane( 45*10+41, 2*10+30, 120, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl8 = new Lane( 57*10+41, 3*10+30, 10, 20, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl9 = new Lane( 51*10+41, 4*10+30, 60, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl10 = new Lane( 44*10+41, 25*10+30, 10, 30, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl11 = new Lane( 45*10+41, 27*10+30, 120, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl12 = new Lane( 57*10+41, 25*10+30, 10, 20, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl13 = new Lane( 51*10+41, 25*10+30, 60, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl14 = new Lane( 30*10+41, 25*10+30, 10, 30, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl15 = new Lane( 29*10+41, 25*10+30, 10, 30, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl16 = new Lane( 15*10+41, 25*10+30, 10, 30, 0, 1, false, Color.lightGray, Color.lightGray );
		Lane bl17 = new Lane( 3*10+41, 27*10+30, 120, 10, -1, 0, true, Color.lightGray, Color.lightGray );
		Lane bl18 = new Lane( 2*10+41, 25*10+30, 10, 20, 0, -1, false, Color.lightGray, Color.lightGray );
		Lane bl19 = new Lane( 3*10+41, 25*10+30, 60, 10, 1, 0, true, Color.lightGray, Color.lightGray );
		
		Directory.addBusSidewalk(bl0);
		Directory.addBusSidewalk(bl1);
		Directory.addBusSidewalk(bl2);
		Directory.addBusSidewalk(bl3);
		Directory.addBusSidewalk(bl4);
		Directory.addBusSidewalk(bl5);
		Directory.addBusSidewalk(bl6);
		Directory.addBusSidewalk(bl7);
		Directory.addBusSidewalk(bl8);
		Directory.addBusSidewalk(bl9);
		Directory.addBusSidewalk(bl10);
		Directory.addBusSidewalk(bl11);
		Directory.addBusSidewalk(bl12);
		Directory.addBusSidewalk(bl13);
		Directory.addBusSidewalk(bl14);
		Directory.addBusSidewalk(bl15);
		Directory.addBusSidewalk(bl16);
		Directory.addBusSidewalk(bl17);
		Directory.addBusSidewalk(bl18);
		Directory.addBusSidewalk(bl19);
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
