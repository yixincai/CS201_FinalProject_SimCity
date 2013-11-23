package gui;

/**
 * This class is the container class for each of the three types of control panels needed in the application
 * 1) New Person Panel
 * 2) Current Person Panel
 * 3) Current Building Panel
 * @author Tanner Zigrang
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import city.PersonAgent;

public class ControlPanel extends JTabbedPane {
	
	CreatePersonPanel newPersonPanel;
	CurrentPersonPanel currentPersonPanel;
	public CurrentBuildingPanel currentBuildingPanel;
	MainGui mainGui;
	public List<PersonAgent> people = new ArrayList<PersonAgent>();
	
	public ControlPanel(MainGui mGui)
	{
		mainGui = mGui;
		//This is all placeholder code just to get the panels into tabs.  Each tab will have its own class eventually.
		newPersonPanel = new CreatePersonPanel(this);
		currentPersonPanel = new CurrentPersonPanel(this);
		currentBuildingPanel = new CurrentBuildingPanel(this);
		this.addTab("Current Person", null, currentPersonPanel, "Info about the currently selected person.");
		this.addTab("Current Building", null, currentBuildingPanel, "Info about the currently selected building.");
		this.addTab("New Person", null, newPersonPanel, "Create a new citizen of SimCity201.");
		this.setPreferredSize(new Dimension(1024/3, 720));
	}
	
	public void updateBuildingInfo(BuildingInteriorAnimationPanel bp){
		currentBuildingPanel.setBuildingPanel(bp);
		this.setSelectedComponent(currentBuildingPanel);
	}

	public void addPerson(String name, double money, String occupation, boolean weekday_Notweekend, String shift) 
	{
		
		currentPersonPanel.addPerson(name);
		PersonAgent newPerson = new PersonAgent(name, money, occupation);
		newPerson.setShift(shift, weekday_Notweekend);
		people.add(newPerson);
		this.setSelectedComponent(currentPersonPanel);
		// TODO this is where we should actually create the new Person agent.  We should discuss how we want to handle the agents.
	}
}
