package gui;

/**
 * This class is the container class for each of the three types of control panels needed in the application
 * 1) New Person Panel
 * 2) Current Person Panel
 * 3) Current Building Panel
 * @author Tanner Zigrang
 */

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import city.Directory;
import city.PersonAgent;
import city.transportation.CommuterRole;

public class ControlPanel extends JTabbedPane {
	
	CreatePersonPanel newPersonPanel;
	CurrentPersonPanel currentPersonPanel;
	CurrentBuildingPanel currentBuildingPanel;
	MainGui mainGui;
	
	public ControlPanel(MainGui mGui)
	{
		mainGui = mGui;
		//This is all placeholder code just to get the panels into tabs.  Each tab will have its own class eventually.
		newPersonPanel = new CreatePersonPanel(this);
		currentPersonPanel = new CurrentPersonPanel(this);
		currentBuildingPanel = new CurrentBuildingPanel();
		this.addTab("Current Person", null, currentPersonPanel, "Info about the currently selected person.");
		this.addTab("Current Building", null, currentBuildingPanel, "Info about the currently selected building.");
		this.addTab("New Person", null, newPersonPanel, "Create a new citizen of SimCity201.");
		this.setPreferredSize(new Dimension(1024/3, 720));
	}
	
	public void updateBuildingInfo(BuildingInteriorAnimationPanel bp){
		currentBuildingPanel.setBuildingPanel(bp);
		this.setSelectedComponent(currentBuildingPanel);
	}

	public void addPerson(String name, double money, String occupation, boolean weekday_notWeekend, String shift) 
	{
		
		currentPersonPanel.addPerson(name);
		PersonAgent newPerson = new PersonAgent(name, money, occupation);
		newPerson.setCommuterRole(new CommuterRole(newPerson, null));
		newPerson.setShift(shift, weekday_notWeekend);
		Directory.addPerson(newPerson);
		this.setSelectedComponent(currentPersonPanel);
		// TODO this is where we should actually create the new Person agent.  We should discuss how we want to handle the agents.
	}
}
