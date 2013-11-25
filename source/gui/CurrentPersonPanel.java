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
	JPanel infoPanel;
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JScrollPane peopleButtons;
	ControlPanel cPanel;
	private static int WIDTH = 1024/3;
	private static int HEIGHT = 720;
	
	public CurrentPersonPanel(ControlPanel cp)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		cPanel = cp;
		this.setLayout(new BorderLayout());
		infoPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		nameField = new JLabel("Person Name:");
		moneyField = new JLabel("Person Money:");
		currentRoleField = new JLabel("Person Current Role:");
		infoPanel.add(nameField); 
		infoPanel.add(moneyField); 
		infoPanel.add(currentRoleField); 
		this.add(infoPanel, BorderLayout.NORTH);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("People"));
		view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		peopleButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		peopleButtons.setViewportView(view);
		buttonPanel.add(peopleButtons, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.CENTER);
		this.validate();
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
		view.add(newPersonButton);
		this.updateInfo(newPersonButton);
	}
	
	public void updateInfo(JButton selected)
	{
		for(PersonAgent tempPerson : Directory.personAgents())
		{
			if(tempPerson.getName() == selected.getText())
			{
				nameField.setText("Person Name: " + tempPerson.getName());
				moneyField.setText("Person Money: " + tempPerson.money() + "0");
			//	currentRoleField.setText("Current Role: Need to implement toString() for the different roles.");
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