package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class ConfigurationPanel extends JPanel implements ActionListener {
	
	ControlPanel cPanel;
	String[] configOptions = {"Robber", "Configuration 2", "Scenario C", 
			"Configuration 4", "Configuration 5", "Scenario A", "Scenario B",
					"Scenario E", "Scenario F", "Scenario G", "Scenario J", "Ryan Restaurant"};
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
			cPanel.addPerson("Teller", 300, "Bank Teller", true, "house");
			cPanel.addPerson("Host", 300, "Bank Host", true, "house");
			cPanel.addPerson("Omar", 300, "Bank Customer", true, "house");
			cPanel.addPerson("Omar2", 300, "Bank Customer", true, "house");
			cPanel.addPerson("Omar3", 300, "Bank Customer", true, "apartment");
		} else if(configBox.getSelectedIndex() == 1){
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
		else if (configBox.getSelectedIndex() == 2){
			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("Yixin Cashier", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("Yixin Employee", 300, "Market Employee", true, "apartment");
			cPanel.addPerson("Yixin Customer", 300, "Market Customer", true, "apartment");
		} else if (configBox.getSelectedIndex() == 3){
			cPanel.addPerson("Yixin", 300, "Restaurant Host", true, "house");
			cPanel.addPerson("Yixin1", 300, "Restaurant Cashier", true, "house");
			cPanel.addPerson("Yixin2", 300, "Cook", true, "house");
			cPanel.addPerson("Yixin3", 300, "Waiter", true, "house");
			cPanel.addPerson("Yixin4", 300, "Yixin Customer", true, "apartment");
			cPanel.addPerson("Yixin7", 300, "Market Cashier", true, "apartment");
			cPanel.addPerson("Yixin8", 300, "Market Employee", true, "apartment");
		} else if (configBox.getSelectedIndex() == 4){
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Omar", 300, "None", true, "house");
			cPanel.addPerson("Yixin", 300, "None", true, "house");
			cPanel.addPerson("Ryan", 300, "None", true, "apartment");
			cPanel.addPerson("Tanner", 300, "None", true, "apartment");
		} else if(configBox.getSelectedIndex() == 5){
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
			cPanel.addPerson("OmarWaiter", 300, "Omar Waiter", true, "apartment");
			
			cPanel.addPerson("Eric", 300, "None", true, "house"); //need to have each one take different transportation
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 7){
			//Three people need to take bus
			cPanel.addPerson("Eric", 300, "None", true, "house"); //need to have each one take different transportation
			cPanel.addPerson("Eric", 300, "None", true, "house");
			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 8){
			//TODO somehow close places
			cPanel.addPerson("Eric", 300, "None", true, "house");
		} else if(configBox.getSelectedIndex() == 9){
			//TODO Scenario G
		} else if(configBox.getSelectedIndex() == 10){
			
		} else if(configBox.getSelectedIndex() == 11){
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
