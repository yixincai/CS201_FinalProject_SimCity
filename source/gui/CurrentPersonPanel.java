package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import city.Directory;
import city.PersonAgent;

public class CurrentPersonPanel extends JPanel implements ActionListener
{
	JPanel view;
	JPanel buttonPanel;
	// List<JButton> personButtonList; // not currently used, may be needed though
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JScrollPane peopleButtons;
	ControlPanel cPanel;
	
	public CurrentPersonPanel(ControlPanel cp)
	{
		cPanel = cp;
		this.setLayout(new BorderLayout());
		// personButtonList = new ArrayList<JButton>();
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(1024/3, 720/2));
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		nameField = new JLabel("Person Name:");
		moneyField = new JLabel("Person Money:");
		currentRoleField = new JLabel("Person Current Role:");
		infoPanel.add(nameField); //TODO Add getter for the current person's name
		infoPanel.add(moneyField); //TODO Add getter for the current person's money
		infoPanel.add(currentRoleField); //TODO Add getter for current person's role
		this.add(infoPanel, BorderLayout.NORTH);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createTitledBorder("People"));
		view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		peopleButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		peopleButtons.setViewportView(view);
		buttonPanel.add(peopleButtons, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.CENTER);
	}

	public void addPerson(String name)
	{
		JButton newPersonButton = new JButton(name);
		newPersonButton.setBackground(Color.white);
		Dimension paneSize = peopleButtons.getSize();
		newPersonButton.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.addActionListener(this);
		// personButtonList.add(newPersonButton);
		view.add(newPersonButton);
		this.updateInfo(newPersonButton);
		newPersonButton.setSelected(true);
		
	}
	
	public void updateInfo(JButton selected)
	{
		//TODO we will need to actually display the relevant information to the agent, not just the name
		//nameField.setText("Person Name: " + selected.getText());
		for(PersonAgent tempPerson : Directory.personAgents())
		{
			if(tempPerson.getName() == selected.getText())
			{
				nameField.setText("Person Name: " + tempPerson.getName());
				moneyField.setText("Person Money: " + tempPerson.money() + "0");
				currentRoleField.setText("Current Role: Need to implement toString() for the different roles.");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// note: this method is called when you click a button in the list of people.
		
		updateInfo((JButton) e.getSource());
	}

}