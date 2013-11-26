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
	String[] configOptions = {"Configuration 1", "Configuration 2", "Configruation 3", "Configruation 4", "Configuration 5"};
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
		startButton = new JButton("Start");
		config1Description = "Scenario 1: Initialize a bank interaction.\n Three bank customers,"
				+ "one bank host, and one bank teller are put into the world.\nThey will go home and"
				+ "then head over to the bank and proceed to make deposits (since it's their first time\n"
				+ "to the bank, naturally they can't withdraw or do any other bank actions.";
		config2Description = "WRITE CONFIGURATION 2 DESCRIPTION HERE";
		config3Description = "WRITE CONFIGURATION 3 DESCRIPTION HERE";
		config4Description = "WRITE CONFIGURATION 4 DESCRIPTION HERE";
		config5Description = "WRITE CONFIGURATION 5 DESCRIPTION HERE";
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
		}
		System.out.println("Config Start Button Pressed.");
	}

}
