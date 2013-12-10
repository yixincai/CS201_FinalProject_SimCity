package gui;

/**
 * This class is the container class for each of the three types of control panels needed in the application
 * 1) New Person Panel
 * 2) Current Person Panel
 * 3) Current Building Panel
 * @author Tanner Zigrang
 */

import gui.astar.AStarTraversal;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Dimension;

import javax.swing.JTabbedPane;

import city.Directory;
import city.PersonAgent;

@SuppressWarnings("serial")
public class ControlPanel extends JTabbedPane {
	
	CreatePersonPanel newPersonPanel;
	CurrentPersonPanel currentPersonPanel;
	ConfigurationPanel configPanel;
	LogControlPanel errorControlPanel;
	public CurrentBuildingPanel currentBuildingPanel;
	MainGui mainGui;
	
	public ControlPanel(MainGui mGui)
	{
		mainGui = mGui;
		//This is all placeholder code just to get the panels into tabs.  Each tab will have its own class eventually.
		newPersonPanel = new CreatePersonPanel(this);
		currentPersonPanel = new CurrentPersonPanel(this);
		currentBuildingPanel = new CurrentBuildingPanel(this);
		configPanel = new ConfigurationPanel(this);
		errorControlPanel = new LogControlPanel(mainGui.tPanel);
		this.addTab("Current Person", null, currentPersonPanel, "Info about the currently selected person.");
		this.addTab("Current Building", null, currentBuildingPanel, "Info about the currently selected building.");
		this.addTab("New Person", null, newPersonPanel, "Create a new citizen of SimCity201.");
		this.addTab("Configuration", null, configPanel, "Configure SimCity to a preset scenario");
		this.addTab("Log Info", null, errorControlPanel, "Log Control");
		this.setPreferredSize(new Dimension(1024/3, 720));
	}
	
	public void updateBuildingInfo(BuildingInteriorAnimationPanel bp){
		currentBuildingPanel.setBuildingPanel(bp);
		this.setSelectedComponent(currentBuildingPanel);
	}

	/** @return false if the city is at capacity, true if a person was successfully added. */
	public boolean addPerson(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType)
	{
		if(Directory.cityAtCapacity()) {
			AlertLog.getInstance().logWarning(AlertTag.GENERAL_CITY, "Control Panel", "Failed to add person because city is at capacity");
			return false;
		}
		
		PersonAgent newPerson;
		if(!(occupationType.contains("Customer"))){
			newPerson = new PersonAgent(name, money, occupationType, weekday_notWeekend, housingType);
		} else{
			newPerson = new PersonAgent(name,money,"None",weekday_notWeekend,housingType);
			if(occupationType.contains("Omar")){
				newPerson.addActionToDo("OmarRestaurant");
			} if(occupationType.contains("Yixin")){
				newPerson.addActionToDo("YixinRestaurant");
			} if(occupationType.contains("Eric")){
				newPerson.addActionToDo("EricRestaurant");
			} if(occupationType.contains("Ryan")){
				newPerson.addActionToDo("RyanRestaurant");
			} if(occupationType.contains("Bank")){
				if (occupationType.contains("Robber")){
					newPerson.addActionToDo("BankRobber");
				} else{
					newPerson.addActionToDo("BankCustomer");
				}
			} if(occupationType.contains("Market")){
				newPerson.addActionToDo("MarketCustomer");
			}   
		}
		currentPersonPanel.addPerson(name);
		Directory.addPerson(newPerson);
		mainGui.getWorldView().addGui(newPerson.commuterRole().gui());
		this.setSelectedComponent(currentPersonPanel);
		newPerson.startThread();
		return true;
	}
}
