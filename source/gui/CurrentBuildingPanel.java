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
import city.Place;

public class CurrentBuildingPanel extends JPanel implements ActionListener {
	
	JPanel view;
	JPanel buttonPanel;
	JPanel infoPanel;
	JLabel buildingName;
	JLabel buildingMoney;
	JScrollPane buildingButtons;
	ControlPanel cPanel;	
	BuildingInteriorAnimationPanel currentBuildingPanel = null;
	
	public CurrentBuildingPanel(ControlPanel cp)
	{
			cPanel = cp;
			this.setLayout(new BorderLayout());
			infoPanel = new JPanel();
			infoPanel.setPreferredSize(new Dimension(1024/3, 720/2));
			infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			buildingName = new JLabel("Building Name: ");
			buildingMoney = new JLabel("Building Money: ");
			infoPanel.add(buildingName); //Gives current building name
			infoPanel.add(buildingMoney); //TODO Add getter for the current building's money
			this.add(infoPanel, BorderLayout.NORTH);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());
			buttonPanel.setBorder(BorderFactory.createTitledBorder("Buildings"));
			view = new JPanel();
			view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
			buildingButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			buildingButtons.setViewportView(view);
			buttonPanel.add(buildingButtons, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.CENTER);
	}
	
	public void addBuilding(String name)
	{
		System.out.println(name + " BUTTON WHY ARENT YOU PRINTING!!");
		JButton newBuilding = new JButton(name);
		newBuilding.setBackground(Color.white);
		Dimension paneSize = buildingButtons.getSize();
		newBuilding.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuilding.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuilding.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuilding.addActionListener(this);
		view.add(newBuilding);
		this.updateInfo(newBuilding);
		newBuilding.setSelected(true);
	}
	
	public void updateInfo(JButton selected)
	{
		for(int i = 0; i < Directory.places().size(); i++)
		{
			Place tempPlace = Directory.places().get(i);
			if(tempPlace.getName() == selected.getText())
			{
				System.out.println(("Proof!!"));
				buildingName.setText("Building Name: " + Directory.places().get(i)._name);
				buildingMoney.setText("Building Money: Need a money field in places");
			}
		}
	}
	
	public void setBuildingPanel(BuildingInteriorAnimationPanel bp){
		this.currentBuildingPanel = bp;
		buildingName.setText("Building Name: " + bp.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateInfo((JButton)e.getSource());

	}

}
