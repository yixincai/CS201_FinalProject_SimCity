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
import city.Time;

public class ConfigurationPanel extends JPanel implements ActionListener {

	ControlPanel cPanel;
	String[] configOptions = {"Scenario: Robber",
			"Scenario: Vehicle Accident",
			"Scenario: Vehicle Hits Pedestrian",
			"Scenario: Weekend",
			"Scenario: Job Change",
			"Normative Scenario",
			"Scenario: Delivery Failure",
			"Scenario: Workspace Down",
			"Scenario: Bus Activity",
			"Scenario: Market Delivery",
			"Scenario: One Customer",
			"Scenario: Three Customers"};
	JLabel configLabel;
	JComboBox configBox;
	JButton startButton;
	JPanel description;
	String config1Description;
	String config2Description;
	String config3Description;
	String config4Description;
	String config5Description;
	String config6Description;
	String config7Description;
	String config8Description;
	String config9Description;
	String config10Description;
	String config11Description;
	String config12Description;
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

		config1Description = "<html><br>Scenario: Robber: Initialize two banks and four bank robbers.</br>"
				+ "<br>Robbers will go to the bank and get killed by the guard dog.</br></html>";
		config2Description = "<html><br>Scenario Vehicle Accident: Initialize a person with a car.</br>"
				+ "<br>The car will go to the bus lane and get run over by the bus.</br></html>";
		config3Description = "<html><br>Scenario: Vehicle Hits Pedestrian: Initialize some CS201 students. </br>"
				+ "<br> They are going to the bus lane and get run over by the bus one by one.</br></html>";
		config4Description = "<html><br>Scenario: Weekend: Initialize the city </br></html>";
		config5Description = "<html>Scenario: Job Change:  </br></html>";
		config6Description = "<html><br>Normative Scenario: Populate the city with 50 people. </br>"
				+ "<br> People will either stay at home or go to work or become customers.</br></html>";
		config7Description = "<html><br>Scenario: Delivery Failure: Initialize Yixin Restaurant without cashier. </br>"
				+ "<br>The truck will go to the market and fail to deliver.</br>"
				+ "<br>Then create a cashier to the restaurant. </br>"
				+ "<br>Truck will successfully deliver the food again.</br></html>";
		config8Description = "<html><br>Scenario: Workspace Down:  </br></html>";
		config9Description = "<html><br>Scenario: Bus Activity: Initialize some non CS201 students. </br>"
				+ "<br> They are going to the bus stop and go to their work spaces.</br></html>";
		config10Description = "<html><br>Scenario: Market Delivery: Initialize a market and all restaurants. </br>"
				+ "<br> The truck will deliver to all restaurants.</br></html>";
		config11Description = "<html><br>Scenario: One Customer: Initialize the city with all workers.</br>"
				+ "<br>One customer is also created and visits all workspaces one by one.</br></html>";
		config12Description = "<html><br>Scenario: One Customer: Initialize the city with all workers.</br>"
				+ "<br>Three customer are created and they visit all workspaces in different orders.</br></html>";
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
			Time.setToFriday();
			//TODO add people
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
			//TODO change timer to be way longer
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
	}

}
