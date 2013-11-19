package gui;

/**
 * This CreatePersonPanel class is the panel that will go under the "New Person" tab in the control panel.
 * Here you will be able to add people to SimCity
 * @author Tanner Zigrang
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class CreatePersonPanel extends JPanel implements ActionListener 
{
	
	public CreatePersonPanel()
	{
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder("Create New Person"));
		String[] occupations = {"None", "Waiter", "Restaurant Cashier", "Cook", "Restaurant Host", 
								"Bank Teller", "Bank Host", "Market Cashier", "Market Employee"};
		JTextField nameField = new JTextField(15);
		JTextField moneyField = new JTextField(15);
		JComboBox occupationBox = new JComboBox(occupations);
		JLabel nameLabel = new JLabel("Name:");
		JLabel moneyLabel = new JLabel("Money:");
		JLabel occupationLabel = new JLabel("Occupation:");
		this.add(nameLabel);
		this.add(moneyLabel);
		this.add(occupationLabel);
		this.add(nameField);
		this.add(moneyField);
		this.add(occupationBox);
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
		layout.putConstraint(SpringLayout.NORTH, moneyField, 10, SpringLayout.SOUTH, nameField);
		//Position OccupationLabel
		layout.putConstraint(SpringLayout.WEST, occupationLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, occupationLabel, 15, SpringLayout.SOUTH, moneyLabel);
		//Position OccupationBox
		layout.putConstraint(SpringLayout.EAST, occupationBox, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, occupationBox, 10, SpringLayout.SOUTH, moneyField);
		
		JButton addButton = new JButton("Add");
		addButton.setPreferredSize(new Dimension(1024/3 - 17, 30));
		addButton.addActionListener(this);
		this.add(addButton);
		layout.putConstraint(SpringLayout.SOUTH, addButton, 0, SpringLayout.SOUTH, this);
	}
		

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Button clicked");
		// TODO Implement function to create a new person
		
	}

}
