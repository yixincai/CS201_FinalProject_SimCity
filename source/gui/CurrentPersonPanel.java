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

public class CurrentPersonPanel extends JPanel implements ActionListener
{
	JPanel view;
	JPanel buttonPanel;
	List<JButton> buildingList;
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JScrollPane peopleButtons;
	
	public CurrentPersonPanel()
	{
		this.setLayout(new BorderLayout());
		buildingList = new ArrayList<JButton>();
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
		JButton newPerson = new JButton(name);
		newPerson.setBackground(Color.white);
		Dimension paneSize = peopleButtons.getSize();
		newPerson.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPerson.addActionListener(this);
		buildingList.add(newPerson);
		view.add(newPerson);
		
	}
	
	public void updateInfo(JButton selected)
	{
		//TODO we will need to actually display the relevant information to the agent, not just the name
		nameField.setText("Person Name: " + selected.getText());
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateInfo((JButton) e.getSource());
	}

}