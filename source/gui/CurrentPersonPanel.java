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

import city.PersonAgent;

public class CurrentPersonPanel extends JPanel implements ActionListener
{
	JPanel view;
	JPanel buttonPanel;
	List<JButton> buildingList;
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JScrollPane peopleButtons;
	ControlPanel cPanel;
	
	public CurrentPersonPanel(ControlPanel cp)
	{
		cPanel = cp;
		this.setLayout(new BorderLayout());
		buildingList = new ArrayList<JButton>();
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(1024/3, 720/2));
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
		JButton newPerson = new JButton(name);
		newPerson.setBackground(Color.white);
		Dimension paneSize = peopleButtons.getSize();
		newPerson.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.addActionListener(this);
		buildingList.add(newPerson);
		view.add(newPerson);
		this.updateInfo(newPerson);
		newPerson.setSelected(true);
		
	}
	
	public void updateInfo(JButton selected)
	{
		//TODO we will need to actually display the relevant information to the agent, not just the name
		//nameField.setText("Person Name: " + selected.getText());
		for(int i = 0; i < cPanel.people.size(); i++)
		{
			PersonAgent tempPerson = cPanel.people.get(i);
			if(tempPerson.name() == selected.getText())
			{
				nameField.setText("Person Name: " + tempPerson.name());
				moneyField.setText("Person Money: " + tempPerson.money() + "0");
				currentRoleField.setText("Need to implement toString() for the different roles.");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateInfo((JButton) e.getSource());
	}

}