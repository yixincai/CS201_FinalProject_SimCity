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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import city.PersonAgent;

@SuppressWarnings("serial")
public class CreatePersonPanel extends JPanel implements ActionListener, ChangeListener
{
	String[] occupations = {"None", "Waiter", "Restaurant Cashier", "Cook", "Restaurant Host", 
								"Bank Teller", "Bank Host", "Market Cashier", "Market Employee", 
								"OmarRestaurant Customer", "YixinRestaurantCustomer", "EricRestaurantCustomer",
								"RyanRestaurant Customer"};
	JComboBox occupationBox = new JComboBox(occupations);
	JLabel moneyField = new JLabel("$0.00");
	JTextField nameField = new JTextField(15);
	JLabel nameLabel = new JLabel("Name:");
	JLabel moneyLabel = new JLabel("Money:");
	JSlider moneySlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
	ControlPanel cPanel;
	JRadioButton weekdays = new JRadioButton("Weekdays");
	JRadioButton weekends = new JRadioButton("Weekends");
	ButtonGroup radioButtons = new ButtonGroup();
	JRadioButton house = new JRadioButton("House");
	JRadioButton apartment = new JRadioButton("Apartment");
	ButtonGroup housing = new ButtonGroup();
	JLabel workSchedule = new JLabel("Work Schedule:");
	JLabel housingSituation = new JLabel("Housing:");
	private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	
	public CreatePersonPanel(ControlPanel cp)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		cPanel = cp;
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder("Create New Person"));
		radioButtons.add(weekdays);
		radioButtons.add(weekends);
		housing.add(house);
		housing.add(apartment);
		house.setSelected(true);
		moneySlider.setMajorTickSpacing(25);
		moneySlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> sliderLabels = new Hashtable<Integer, JLabel>();
		sliderLabels.put(PersonAgent.POOR_LEVEL, new JLabel("Poor"));
		sliderLabels.put(PersonAgent.RICH_LEVEL, new JLabel("Rich"));
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
		this.add(weekends);
		this.add(weekdays);
		this.add(workSchedule);
		this.add(house);
		this.add(apartment);
		this.add(housingSituation);
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
		//Positon Weekends RadioButton
		layout.putConstraint(SpringLayout.NORTH, weekends, 15, SpringLayout.SOUTH, occupationBox);
		layout.putConstraint(SpringLayout.WEST, weekends, -100, SpringLayout.EAST, this);
		//Position Weekdays RadioButton
		layout.putConstraint(SpringLayout.NORTH, weekdays, 2, SpringLayout.SOUTH, weekends);
		layout.putConstraint(SpringLayout.WEST, weekdays, -100, SpringLayout.EAST, this);
		//Position WorkSchedule Label
		layout.putConstraint(SpringLayout.NORTH, workSchedule, 32, SpringLayout.SOUTH, occupationLabel);
		layout.putConstraint(SpringLayout.WEST, workSchedule, 5, SpringLayout.WEST, this);
		//Position housing label
		layout.putConstraint(SpringLayout.NORTH, housingSituation, 45, SpringLayout.SOUTH, workSchedule);
		layout.putConstraint(SpringLayout.WEST, housingSituation, 5, SpringLayout.WEST, this);
		//Position house radio button
		layout.putConstraint(SpringLayout.NORTH, house, 15, SpringLayout.SOUTH, weekdays);
		layout.putConstraint(SpringLayout.WEST, house, -100, SpringLayout.EAST, this);
		//Position apartment radio button
		layout.putConstraint(SpringLayout.NORTH, apartment, 2, SpringLayout.SOUTH, house);
		layout.putConstraint(SpringLayout.WEST, apartment, -100, SpringLayout.EAST, this);
		
		
		
		
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
		String housingSelection;
		if(house.isSelected())
		{
			housingSelection = new String("house");
		}
		else
		{
			housingSelection = new String("apartment");
		}
		cPanel.addPerson(nameField.getText(), moneySlider.getValue(), (String)occupationBox.getSelectedItem(), weekdays.isSelected(), housingSelection);
		nameField.setText("");
		occupationBox.setSelectedIndex(0);
		moneySlider.setValue(0);
		moneyField.setText("$0.00");
		weekends.setSelected(false);
		weekdays.setSelected(false);
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
