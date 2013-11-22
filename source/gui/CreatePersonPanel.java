package gui;

/**
 * This CreatePersonPanel class is the panel that will go under the "New Person" tab in the control panel.
 * Here you will be able to add people to SimCity
 * @author Tanner Zigrang
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import city.Constants;

public class CreatePersonPanel extends JPanel implements ActionListener, ChangeListener
{
	String[] occupations = {"None", "Waiter", "Restaurant Cashier", "Cook", "Restaurant Host", 
								"Bank Teller", "Bank Host", "Market Cashier", "Market Employee"};
	JComboBox occupationBox = new JComboBox(occupations);
	JLabel moneyField = new JLabel("$0.00");
	JTextField nameField = new JTextField(15);
	JLabel nameLabel = new JLabel("Name:");
	JLabel moneyLabel = new JLabel("Money:");
	JSlider moneySlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
	
	public CreatePersonPanel()
	{
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder("Create New Person"));

		moneySlider.setMajorTickSpacing(25);
		moneySlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> sliderLabels = new Hashtable<Integer, JLabel>();
		sliderLabels.put(city.Constants.POOR_LEVEL, new JLabel("Poor"));
		sliderLabels.put(city.Constants.RICH_LEVEL, new JLabel("Rich"));
		moneySlider.setPaintLabels(true);
		moneySlider.setLabelTable(sliderLabels);
		moneySlider.addChangeListener(this);
		JLabel occupationLabel = new JLabel("Occupation:");
		this.add(nameLabel);
		this.add(moneyLabel);
		this.add(occupationLabel);
		this.add(nameField);
		this.add(moneyField);
		this.add(occupationBox);
		this.add(moneySlider);
		//Position NameLabel
		layout.putConstraint(SpringLayout.WEST, nameLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, this);
		//Position NameField
		layout.putConstraint(SpringLayout.EAST, nameField, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, nameField, 10, SpringLayout.NORTH, this);
		//Position MoneyLabel
		layout.putConstraint(SpringLayout.WEST, moneyLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, moneyLabel, 15, SpringLayout.SOUTH, nameLabel);
		//Position MoneyField
		layout.putConstraint(SpringLayout.EAST, moneyField, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, moneyField, 18, SpringLayout.SOUTH, nameLabel);
		//Position OccupationLabel
		layout.putConstraint(SpringLayout.WEST, occupationLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, occupationLabel, 20, SpringLayout.SOUTH, moneySlider);
		//Position OccupationBox
		layout.putConstraint(SpringLayout.EAST, occupationBox, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, occupationBox, 15, SpringLayout.SOUTH, moneySlider);
		//Position MoneySlider
		layout.putConstraint(SpringLayout.NORTH, moneySlider, 15, SpringLayout.SOUTH, moneyLabel);
		layout.putConstraint(SpringLayout.WEST, moneySlider, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, moneySlider, -5, SpringLayout.EAST, this);
		
		JButton addButton = new JButton("Add");
		//addButton.setPreferredSize(new Dimension(1024/3 - 17, 30));
		addButton.addActionListener(this);
		this.add(addButton);
		layout.putConstraint(SpringLayout.SOUTH, addButton, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, addButton, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, addButton, -5, SpringLayout.EAST, this);
	}
		

	//This is the listener for the button
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Button clicked");
		// TODO Implement function to create a new person
		
	}


	//This is the listener for the slider
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		JSlider sourceSlider = (JSlider)e.getSource();
		double money = (double)sourceSlider.getValue();
		moneyField.setText("$" + money + "0");
	}

}
