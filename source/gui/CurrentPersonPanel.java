package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import city.Directory;
import city.PersonAgent;

public class CurrentPersonPanel extends JPanel implements ActionListener, PersonInfoRefreshable
{
	JPanel view;
	JPanel buttonPanel;
	
	JPanel infoPanel;
	
	JLabel actionLabel;
	JTextField action;
	JButton addSelectedActions;
	
	PersonAgent _currentlySelectedPerson;
	
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JLabel nextRoleField;
	JLabel occupationField;
	JScrollPane peopleButtons;
	ControlPanel cPanel;
	private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	private final Dimension buttonDimension = new Dimension(340, 30);
	private final Dimension textDimension = new Dimension(400, 30);
	
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
		nameField = new JLabel("Name:");
		moneyField = new JLabel("Money:");
		currentRoleField = new JLabel("Current Role:");
		nextRoleField = new JLabel("Next Role:");
		occupationField = new JLabel("Occupation:");
		infoPanel.add(nameField);
		infoPanel.add(moneyField);
		infoPanel.add(currentRoleField);
		infoPanel.add(nextRoleField);
		infoPanel.add(occupationField);
		
		actionLabel = new JLabel("Give Command Here:");
		 action = new JTextField("");
		 action.setPreferredSize(textDimension);
        
         
		 addSelectedActions = new JButton("Add Actions");
		 addSelectedActions.setMinimumSize(buttonDimension);
		 addSelectedActions.setMaximumSize(buttonDimension);
		 
		 addSelectedActions.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
            	 if(_currentlySelectedPerson != null){
	                 _currentlySelectedPerson.addActionToDo(action.getText());
	                 action.setText("");
            	 }
             }
         });
		 
		 infoPanel.add(actionLabel);
		 infoPanel.add(action);
		 infoPanel.add(addSelectedActions);
         
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
			if(tempPerson.name() == selected.getText())
			{
				if(_currentlySelectedPerson != null){
					_currentlySelectedPerson.commuterRole().gui().setSelected(false);
				}
				_currentlySelectedPerson = tempPerson;
				tempPerson.commuterRole().gui().setSelected(true);
				refreshCurrentPersonInfo();
			}
		}
	}
	
	/** Only refreshes if the passed-in person is currently selected */
	public void refreshInfo(PersonAgent person)
	{
		if(_currentlySelectedPerson == person) refreshCurrentPersonInfo();
	}
	
	public void refreshCurrentPersonInfo()
	{
		nameField.setText("Person Name: " + _currentlySelectedPerson.name());
		moneyField.setText("Person Money: " + _currentlySelectedPerson.money() + "0");
		currentRoleField.setText("Current Role: " + _currentlySelectedPerson.currentRole().typeToString());
		nextRoleField.setText("Next Role: " + _currentlySelectedPerson.nextRoleTypeToString());
		occupationField.setText("Occupation: " + _currentlySelectedPerson.occupationTypeToString());
	}
	
	public void updatePerson(PersonAgent tempPerson){
		if(_currentlySelectedPerson != null){
			_currentlySelectedPerson.commuterRole().gui().setSelected(false);
		}
		_currentlySelectedPerson = tempPerson;
		tempPerson.commuterRole().gui().setSelected(true);
		refreshCurrentPersonInfo();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// note: this method is called when you click a button in the list of people.
		
		updateInfo((JButton) e.getSource());
	}

}