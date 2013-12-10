package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import city.Directory;
import city.PersonAgent;

public class ConfigurationPanel extends JPanel implements ActionListener {

	ControlPanel cPanel;
	String[] configOptions = {"Scenario: Robber", "Scenario: Vehicle Accident", 
			"Scenario: Vehicle Hits Pedestrian", "Scenario: Weekend", "Scenario: Job Change", //end NN
			"Normative Scenario", "Scenario: Delivery Failure",
			"Scenario: Workspace Down", "Scenario: Bus Activity", 
			"Scenario: Market Delivery", "Scenario: One Customer", "Scenario: Three Customers"};
	JLabel configLabel;
	JComboBox configBox;
	JButton startButton;
	JPanel description;
	String config1Description;
	String config2Description;
	String config3Description;
	String config4Description;
	String config5Description;
	JLabel descriptionText;
	SpringLayout layout;
	private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	Timer timer;
	
	public ConfigurationPanel(ControlPanel cp)
	{
		//Initialization
		layout = new SpringLayout();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		cPanel = cp;
		this.setLayout(layout);
		configBox = new JComboBox(configOptions);
		configLabel = new JLabel("Configurations:");
		description = new JPanel();
		description.setBorder(BorderFactory.createTitledBorder("Config Description"));
		SpringLayout descriptionLayout = new SpringLayout();
		description.setLayout(descriptionLayout);
		startButton = new JButton("Start");

		config1Description = "<html><br>Scenario 1: Initialize a bank interaction.</br><br>Three bank customers,"
				+ " one bank host, and one bank teller are put into the world.</br><br>  They will go home and"
				+ " then head over to the bank and proceed to make deposits (since it's their first time"
				+ " to the bank, </br><br> naturally they can't withdraw or do any other bank actions.</br></html>";
		config2Description = "<html><br>Scenario 2: Initialize a restaurant to market interaction.</br><br>Several "
				+ "people with restaurant and market related roles will be added to the world</br><br>This scenario "
				+ "particularly involves the Market, Omar's Restaurant, and Yixin's Restaurant.</br><br>People will go home and"
				+ " then head over to their respective place (market/omarRestaurant/yixinRestaurant).</br><br>"
				+ "Because of non-normatives integrated in restaurant from our past project, customers may leave</br><br>"
				+ "when they find out that the restaurant is out of food, but the market interaction will still work</br></html>";
		config3Description = "<html><br>Scenario 3: Initialize a market with restaurant-market interaction. </br><br>One market "
				+ "customer goes to market to order 3 meals. </br><br>At the same time, the cook will check his inventory and ask "
				+ "the market for resupply. </br><br>The market cashier handles both the restaurant order and customer order, and ask "
				+ "the employee to pick up the intems from inventory. </br><br>The customer order will be handed to customer after they have "
				+ "paid the cashier. </br><br>Restaurant order will be delivered by the truck and come back.</br>";
		config4Description = "<html><br>Scenario 4: Initialize the market as well as a restaurant. A customer will go to the restaurant and order food. </br><br>"
				+ "He may leave because the restuarant does not have any food to provide or he does not have enough money to buy any food. </br><br>"
				+ "The cook in the restaurant can order form the new market cashier instead of the old market. </br><br>"
				+ "The waiter in the restaurant can either be a shared data waiter or a normal waiter. </br><br>The other behaviors are basically the "
				+ "same as the before in Yixin Restaurant.</br>";
		config5Description = "<html>Scenario 5: Initialize five people without jobs.  They will start as commuter roles, and then they will acquire a house "
				+ "or an apartment, going about doing an action.  These actions include cooking, sleeping, and relaxing "
				+ "(being idle).</html>";
		descriptionText = new JLabel();
		descriptionText.setText(config1Description);
		description.add(descriptionText);
		configBox.addActionListener(this);
		startButton.addActionListener(this);
		this.add(configLabel);
		this.add(configBox);
		this.add(description);
		this.add(startButton);

		//Now fix the layout
		//Configuration label
		layout.putConstraint(SpringLayout.WEST, configLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, configLabel, 10, SpringLayout.NORTH, this);
		//Configuration JComboBox
		layout.putConstraint(SpringLayout.EAST, configBox, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, configBox, 5, SpringLayout.NORTH, this);
		//Description Panel
		layout.putConstraint(SpringLayout.NORTH, description, 10, SpringLayout.SOUTH, configBox);
		layout.putConstraint(SpringLayout.WEST, description, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, description, -50, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, description, -5, SpringLayout.EAST, this);
		//Start Button
		layout.putConstraint(SpringLayout.WEST, startButton, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, startButton, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, startButton, -35, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.SOUTH, startButton, -5, SpringLayout.SOUTH, this);
		//Description Label
		descriptionLayout.putConstraint(SpringLayout.WEST, descriptionText, 10, SpringLayout.WEST, description);
		descriptionLayout.putConstraint(SpringLayout.EAST, descriptionText, -10, SpringLayout.EAST, description);

	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == configBox)
		{
			if(configBox.getSelectedIndex() == 0)
			{
				descriptionText.setText(config1Description);
			}
			else if(configBox.getSelectedIndex() == 1)
			{
				descriptionText.setText(config2Description);
			}
			else if(configBox.getSelectedIndex() == 2)
			{
				descriptionText.setText(config3Description);
			}
			else if(configBox.getSelectedIndex() == 3)
			{
				descriptionText.setText(config4Description);
			}
			else if(configBox.getSelectedIndex() == 4)
			{
				descriptionText.setText(config5Description);
			}

		}

		if(e.getSource() == startButton)
		{
			this.startConfiguration();
		}

	}

	public void startConfiguration()
	{
		if(configBox.getSelectedIndex() == 0){
			cPanel.addPerson("BankTeller1", 50, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost1", 50, "Bank Host", true, "house");
			cPanel.addPerson("BankTeller2", 300, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost2", 300, "Bank Host", true, "house");
			cPanel.addPerson("Omar", 50, "Bank Customer Robber", true, "house");
			cPanel.addPerson("Omar2", 50, "Bank Customer Robber", true, "house");
			cPanel.addPerson("Omar3", 300, "Bank Customer Robber", true, "apartment");
			cPanel.addPerson("Omar4", 300, "Bank Customer Robber", true, "apartment");
		}  else if(configBox.getSelectedIndex() == 1){
			cPanel.addPerson("CS201 Student 1", 300, "Restaurant Host", true, "house");
		}   else if(configBox.getSelectedIndex() == 2){
			cPanel.addPerson("CS201 Student 2", 150, "Restaurant Cashier", true, "house");
			cPanel.addPerson("CS201 Student 3", 150, "Cook", true, "house");
			cPanel.addPerson("CS201 Student 4", 150, "Waiter", true, "house");
			cPanel.addPerson("CS201 Student 5", 150, "Yixin Customer", true, "apartment");
			cPanel.addPerson("CS201 Student 6", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("CS201 Student 7", 150, "Market Employee", true, "apartment");
			cPanel.addPerson("CS201 Student 8", 150, "Restaurant Host", true, "house");
			cPanel.addPerson("CS201 Student 9", 150, "Restaurant Cashier", true, "house");
		} else if(configBox.getSelectedIndex() == 3){
			//TODO Weekend Behavior
		} else if(configBox.getSelectedIndex() == 4){
			//TODO Job Changing
		} else if(configBox.getSelectedIndex() == 5){
			//markets
			cPanel.addPerson("MarketCashier1", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee1", 50, "Market Employee", true, "apartment");
			cPanel.addPerson("MarketCashier2", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee2", 50, "Market Employee", true, "apartment");
			//banks
			cPanel.addPerson("BankTeller1", 150, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost1", 50, "Bank Host", true, "apartment");
			cPanel.addPerson("BankTeller2", 300, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost2", 50, "Bank Host", true, "apartment");
			//yixin restaurant
			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			//omar restaurant
			cPanel.addPerson("OmarHost", 300, "OmarRestaurant Host", true, "house");
			cPanel.addPerson("OmarCashier", 300, "OmarRestaurant Cashier", true, "apartment");
			cPanel.addPerson("OmarCook", 50, "OmarRestaurant Cook", true, "apartment");
			cPanel.addPerson("OmarSharedDataWaiter", 150, "OmarRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("OmarNormalWaiter", 150, "OmarRestaurant NormalWaiter", true, "apartment");

			//ryan restaurant
			cPanel.addPerson("RyanHost", 300, "RyanRestaurant Host", true, "house");
			cPanel.addPerson("RyanCashier", 300, "RyanRestaurant Cashier", true, "apartment");
			cPanel.addPerson("RyanCook", 50, "RyanRestaurant Cook", true, "apartment");
			cPanel.addPerson("RyanSharedDataWaiter", 150, "RyanRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("RyanNormalWaiter", 150, "RyanRestaurant NormalWaiter", true, "apartment");
			
			//eric restaurant
//			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
//			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
//			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
//			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
//			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
				
			//add home occupant role
			cPanel.addPerson("HomeOccupant1", 300, "None", true, "apartment");
			cPanel.addPerson("HomeOccupant2", 150, "None", true, "apartment");
			cPanel.addPerson("HomeOccupant3", 50, "None", true, "apartment");
			cPanel.addPerson("HomeOccupant4", 50, "None", true, "apartment");
			
			//add customers
			cPanel.addPerson("YixinRestaurantCustomer1", 300, "YixinRestaurant Customer", true, "apartment");
			cPanel.addPerson("OmarRestaurantCustomer1", 300, "OmarRestaurant Customer", true, "apartment");
			cPanel.addPerson("RyanRestaurantCustomer1", 300, "RyanRestaurant Customer", true, "apartment");
//			cPanel.addPerson("EricRestaurantCustomer", 50, "EricRestaurant Customer", true, "apartment");
			cPanel.addPerson("YixinRestaurantCustomer2", 150, "YixinRestaurant Customer", true, "apartment");
			cPanel.addPerson("OmarRestaurantCustomer2", 150, "OmarRestaurant Customer", true, "apartment");
			cPanel.addPerson("RyanRestaurantCustomer2", 150, "RyanRestaurant Customer", true, "apartment");
//			cPanel.addPerson("EricRestaurantCustomer", 50, "EricRestaurant Customer", true, "apartment");
			cPanel.addPerson("YixinRestaurantCustomer3", 50, "YixinRestaurant Customer", true, "apartment");
			cPanel.addPerson("OmarRestaurantCustomer3", 50, "OmarRestaurant Customer", true, "apartment");
			cPanel.addPerson("RyanRestaurantCustomer3", 50, "RyanRestaurant Customer", true, "apartment");
//			cPanel.addPerson("EricRestaurantCustomer", 50, "EricRestaurant Customer", true, "apartment");

			cPanel.addPerson("MarketCustomer1", 1000, "Market Customer", true, "apartment");
			cPanel.addPerson("MarketCustomer2", 1000, "Market Customer", true, "apartment");
			cPanel.addPerson("MarketCustomer3", 1000, "Market Customer", true, "apartment");
			
			cPanel.addPerson("BankCustomer1", 1000, "Bank Customer", true, "apartment");
			cPanel.addPerson("BankCustomer2", 1000, "Bank Customer", true, "apartment");
			cPanel.addPerson("BankCustomer3", 1000, "Bank Customer", true, "apartment");
		}
		else if (configBox.getSelectedIndex() == 6){
			cPanel.addPerson("MarketCashier1", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee1", 50, "Market Employee", true, "apartment");

			//yixin restaurant
			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			timer = new Timer(30000, new ActionListener() { 
				public void actionPerformed(ActionEvent e){
					cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
					timer.stop();}});
			timer.start();
		}
		else if (configBox.getSelectedIndex() == 7){
			//TODO bring workspaces down
		}
		else if (configBox.getSelectedIndex() == 8){
			cPanel.addPerson("Student 2", 150, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Student 3", 150, "Cook", true, "house");
			cPanel.addPerson("Student 4", 150, "Waiter", true, "house");
			cPanel.addPerson("Student 5", 150, "Yixin Customer", true, "apartment");
			cPanel.addPerson("Student 6", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("Student 7", 150, "Market Employee", true, "apartment");
			cPanel.addPerson("Student 8", 150, "Restaurant Host", true, "house");
			cPanel.addPerson("Student 9", 150, "Restaurant Cashier", true, "house");			
		}
		else if (configBox.getSelectedIndex() == 9){
			cPanel.addPerson("MarketCashier1", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee1", 300, "Market Employee", true, "apartment");
			
			//yixin restaurant
			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
			cPanel.addPerson("YixinSharedDataWaiter", 50, "YixinRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("YixinNormalWaiter", 50, "YixinRestaurant NormalWaiter", true, "apartment");
			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
			
			//omar restaurant
			cPanel.addPerson("OmarHost", 300, "OmarRestaurant Host", true, "house");
			cPanel.addPerson("OmarCashier", 300, "OmarRestaurant Cashier", true, "apartment");
			cPanel.addPerson("OmarCook", 50, "OmarRestaurant Cook", true, "apartment");
			cPanel.addPerson("OmarSharedDataWaiter", 50, "OmarRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("OmarNormalWaiter", 50, "OmarRestaurant NormalWaiter", true, "apartment");
			//TODO change route to ryan's restaurant
			//ryan restaurant
			cPanel.addPerson("RyanHost", 300, "RyanRestaurant Host", true, "house");
			cPanel.addPerson("RyanCashier", 300, "RyanRestaurant Cashier", true, "apartment");
			cPanel.addPerson("RyanCook", 50, "RyanRestaurant Cook", true, "apartment");
			cPanel.addPerson("RyanSharedDataWaiter", 50, "RyanRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("RyanNormalWaiter", 50, "RyanRestaurant NormalWaiter", true, "apartment");	
		}
		else if(configBox.getSelectedIndex() == 10){
			//markets
			cPanel.addPerson("MarketCashier1", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee1", 50, "Market Employee", true, "apartment");
			cPanel.addPerson("MarketCashier2", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee2", 50, "Market Employee", true, "apartment");
			//banks
			cPanel.addPerson("BankTeller1", 150, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost1", 50, "Bank Host", true, "apartment");
			cPanel.addPerson("BankTeller2", 300, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost2", 50, "Bank Host", true, "apartment");
			//yixin restaurant
			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			//omar restaurant
			cPanel.addPerson("OmarHost", 300, "OmarRestaurant Host", true, "house");
			cPanel.addPerson("OmarCashier", 300, "OmarRestaurant Cashier", true, "apartment");
			cPanel.addPerson("OmarCook", 50, "OmarRestaurant Cook", true, "apartment");
			cPanel.addPerson("OmarSharedDataWaiter", 150, "OmarRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("OmarNormalWaiter", 150, "OmarRestaurant NormalWaiter", true, "apartment");

			//ryan restaurant
			cPanel.addPerson("RyanHost", 300, "RyanRestaurant Host", true, "house");
			cPanel.addPerson("RyanCashier", 300, "RyanRestaurant Cashier", true, "apartment");
			cPanel.addPerson("RyanCook", 50, "RyanRestaurant Cook", true, "apartment");
			cPanel.addPerson("RyanSharedDataWaiter", 150, "RyanRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("RyanNormalWaiter", 150, "RyanRestaurant NormalWaiter", true, "apartment");
			
			//eric restaurant
//			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
//			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
//			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
//			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
//			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			//add customers
			cPanel.addPerson("GeneralCustomer", 1000, "Market Customer", true, "apartment");
			List<PersonAgent> personList = Directory.personAgents();
			PersonAgent currentPerson = personList.get(personList.size() - 1);
			//TODO go to another market
			currentPerson.addActionToDo("Bank Deposit");
			currentPerson.addActionToDo("Yixin Restaurant");
			currentPerson.addActionToDo("Omar Restaurant");
			currentPerson.addActionToDo("Ryan Restaurant");
			//currentPerson.addActionToDo("Eric Restaurant");
		}
		else if(configBox.getSelectedIndex() == 11){
			//markets
			cPanel.addPerson("MarketCashier1", 150, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee1", 50, "Market Employee", true, "apartment");
			cPanel.addPerson("MarketCashier2", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("MarketEmployee2", 50, "Market Employee", true, "apartment");
			//banks
			cPanel.addPerson("BankTeller1", 150, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost1", 50, "Bank Host", true, "apartment");
			cPanel.addPerson("BankTeller2", 300, "Bank Teller", true, "house");
			cPanel.addPerson("BankHost2", 50, "Bank Host", true, "apartment");
			//yixin restaurant
			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			//omar restaurant
			cPanel.addPerson("OmarHost", 300, "OmarRestaurant Host", true, "house");
			cPanel.addPerson("OmarCashier", 300, "OmarRestaurant Cashier", true, "apartment");
			cPanel.addPerson("OmarCook", 50, "OmarRestaurant Cook", true, "apartment");
			cPanel.addPerson("OmarSharedDataWaiter", 150, "OmarRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("OmarNormalWaiter", 150, "OmarRestaurant NormalWaiter", true, "apartment");

			//ryan restaurant
			cPanel.addPerson("RyanHost", 300, "RyanRestaurant Host", true, "house");
			cPanel.addPerson("RyanCashier", 300, "RyanRestaurant Cashier", true, "apartment");
			cPanel.addPerson("RyanCook", 50, "RyanRestaurant Cook", true, "apartment");
			cPanel.addPerson("RyanSharedDataWaiter", 150, "RyanRestaurant SharedDataWaiter", true, "apartment");
			cPanel.addPerson("RyanNormalWaiter", 150, "RyanRestaurant NormalWaiter", true, "apartment");
			
			//eric restaurant
//			cPanel.addPerson("YixinHost", 300, "YixinRestaurant Host", true, "house");
//			cPanel.addPerson("YixinCashier", 300, "YixinRestaurant Cashier", true, "apartment");
//			cPanel.addPerson("YixinCook", 50, "YixinRestaurant Cook", true, "apartment");
//			cPanel.addPerson("YixinSharedDataWaiter", 150, "YixinRestaurant SharedDataWaiter", true, "apartment");
//			cPanel.addPerson("YixinNormalWaiter", 150, "YixinRestaurant NormalWaiter", true, "apartment");
			
			//add customers
			cPanel.addPerson("GeneralCustomer1", 1000, "Market Customer", true, "apartment");
			cPanel.addPerson("GeneralCustomer2", 1000, "Bank Customer", true, "apartment");
			cPanel.addPerson("GeneralCustomer3", 1000, "Yixin Restaurant Customer", true, "apartment");

			List<PersonAgent> personList = Directory.personAgents();
			PersonAgent currentPerson = personList.get(personList.size() - 1);
			//TODO go to another market
			currentPerson.addActionToDo("Omar Restaurant");
			currentPerson.addActionToDo("Ryan Restaurant");
			currentPerson.addActionToDo("Yixin Restaurant");
			currentPerson.addActionToDo("Bank Deposit");

			currentPerson = personList.get(personList.size() - 2);
			//TODO go to another market
			currentPerson.addActionToDo("Ryan Restaurant");
			currentPerson.addActionToDo("Yixin Restaurant");
			currentPerson.addActionToDo("Omar Restaurant");
			currentPerson.addActionToDo("Market Customer");
			
			currentPerson = personList.get(personList.size() - 3);
			//TODO go to another market
			currentPerson.addActionToDo("Ryan Restaurant");
			currentPerson.addActionToDo("Bank Deposit");
			currentPerson.addActionToDo("Market Customer");
			currentPerson.addActionToDo("Omar Restaurant");
			//currentPerson.addActionToDo("Eric Restaurant");
		}
			else if(configBox.getSelectedIndex() == 2){
			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("YixinMarketCashier", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("YixinMarketEmployee", 300, "Market Employee", true, "apartment");
			cPanel.addPerson("YixinRestaurantHost", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("RestaurantCashier", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Cook", 300, "Cook", true, "apartment");
			cPanel.addPerson("OmarWaiter", 300, "Omar Waiter", true, "apartment");
			/*	cPanel.addPerson("OmarCustomer", 300, "Omar Customer", true, "apartment");
			cPanel.addPerson("OmarCustomer2", 300, "Omar Customer", true, "apartment");
			cPanel.addPerson("OmarCustomer3", 300, "Omar Customer", true, "apartment");
			cPanel.addPerson("OmarCustomer4", 300, "Omar Customer", true, "apartment"); */
		}
		else if (configBox.getSelectedIndex() == 3){
			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("Yixin Cashier", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("Yixin Employee", 300, "Market Employee", true, "apartment");
			cPanel.addPerson("Yixin Customer", 300, "Market Customer", true, "apartment");
		} else if (configBox.getSelectedIndex() == 4){
			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("Yixin3", 300, "Waiter", true, "house");
			cPanel.addPerson("Yixin4", 50, "Yixin Customer", true, "apartment");
			cPanel.addPerson("Yixin7", 50, "Market Cashier", true, "apartment");
			cPanel.addPerson("Yixin8", 50, "Market Employee", true, "apartment");
		} else if (configBox.getSelectedIndex() == 5){
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Omar", 300, "None", true, "house");
			cPanel.addPerson("Yixin", 300, "None", true, "house");
			cPanel.addPerson("Ryan", 300, "None", true, "apartment");
			cPanel.addPerson("Tanner", 300, "None", true, "apartment");
		} else if(configBox.getSelectedIndex() == 6){
			cPanel.addPerson("Teller", 300, "Bank Teller", true, "house");
			cPanel.addPerson("Host", 300, "Bank Host", true, "house");

			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("YixinMarketCashier", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("YixinMarketEmployee", 300, "Market Employee", true, "apartment");
			cPanel.addPerson("YixinRestaurantHost", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("RestaurantCashier", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Cook", 300, "Cook", true, "apartment");
			cPanel.addPerson("OmarWaiter", 300, "Omar Waiter", true, "apartment"); // add other restaurants

			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 7){
			cPanel.addPerson("Teller", 300, "Bank Teller", true, "house");
			cPanel.addPerson("Host", 300, "Bank Host", true, "house");

			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("YixinMarketCashier", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("YixinMarketEmployee", 300, "Market Employee", true, "apartment");
			cPanel.addPerson("YixinRestaurantHost", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("RestaurantCashier", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Cook", 300, "Cook", true, "apartment");
			cPanel.addPerson("OmarWaiter", 300, "Omar Waiter", true, "apartment");

			cPanel.addPerson("Eric", 300, "None", true, "house"); //need to have each one take different transportation
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 8){
			//Three people need to take bus
			cPanel.addPerson("Eric", 300, "None", true, "house"); //need to have each one take different transportation
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 12){
			cPanel.addPerson("Ryan", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("Ryan1", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Ryan2", 300, "Cook", true, "apartment");
			cPanel.addPerson("Ryan3", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("Ryan4", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Ryan5", 300, "Cook", true, "apartment");
			cPanel.addPerson("Ryan6", 300, "Restaurant Host", true, "apartment");
			cPanel.addPerson("Ryan7", 300, "Restaurant Cashier", true, "apartment");
			cPanel.addPerson("Ryan8", 300, "Cook", true, "apartment");
			cPanel.addPerson("Ryan9", 300, "Ryan Waiter", true, "house");
			cPanel.addPerson("Ryan10", 300, "Ryan Customer", true, "apartment");
			cPanel.addPerson("Ryan11", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("Ryan12", 300, "Market Employee", true, "apartment");
		} 

	}

}
