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
import city.bank.BankCustomerRole;
import city.home.House;
import city.restaurant.yixin.YixinCustomerRole;
import city.transportation.CommuterRole;
import city.transportation.gui.CommuterGui;

public class ControlPanel extends JTabbedPane {
	
	CreatePersonPanel newPersonPanel;
	CurrentPersonPanel currentPersonPanel;
	ConfigurationPanel configPanel;
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
		this.addTab("Current Person", null, currentPersonPanel, "Info about the currently selected person.");
		this.addTab("Current Building", null, currentBuildingPanel, "Info about the currently selected building.");
		this.addTab("New Person", null, newPersonPanel, "Create a new citizen of SimCity201.");
		this.addTab("Configuration", null, configPanel, "Configure SimCity to a preset scenario");
		this.setPreferredSize(new Dimension(1024/3, 720));
	}
	
	public void updateBuildingInfo(BuildingInteriorAnimationPanel bp){
		currentBuildingPanel.setBuildingPanel(bp);
		this.setSelectedComponent(currentBuildingPanel);
	}

	public void addPerson(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType) //TODO finish with new person instantiation stuff
	{
		//DEBUG
			for(House h : Directory.houses()) {
				System.out.println("Found house with name: " + h.getName());
			}
		currentPersonPanel.addPerson(name);
		PersonAgent newPerson = new PersonAgent(name, money, occupationType, housingType);
		Directory.addPerson(newPerson);
		// The old way:
		// CommuterRole newCommuterRole = new CommuterRole(newPerson, null);
		// newPerson.setCommuterRole(newCommuterRole);
		// CommuterGui newCommuterGui = new CommuterGui(newCommuterRole);
		// newCommuterRole.setGui(newCommuterGui);
		// mainGui.getWorldView().addGui(newCommuterGui);
		mainGui.getWorldView().addGui(newPerson.commuterRole().gui());
		this.setSelectedComponent(currentPersonPanel);
		newPerson.startThread();
	}
}
