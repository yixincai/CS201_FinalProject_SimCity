package gui;

/**
 * This class is the container class for each of the three types of control panels needed in the application
 * 1) New Person Panel
 * 2) Current Person Panel
 * 3) Current Building Panel
 * @author Tanner Zigrang
 */

import gui.astar.AStarTraversal;

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

	public void addPerson(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType) //TODO finish with new person instantiation stuff
	{
		PersonAgent newPerson;
		if(!occupationType.contains("Restaurant")){
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
			}/* if(occupationType.contains("Bank")){
				newPerson.addActionToDo("Bank Customer");
			}  */
		}
		currentPersonPanel.addPerson(name);
		Directory.addPerson(newPerson);
		mainGui.getWorldView().addGui(newPerson.commuterRole().gui());
		this.setSelectedComponent(currentPersonPanel);
		newPerson.startThread();
	}
}
