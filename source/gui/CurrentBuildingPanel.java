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
	private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	
	public CurrentBuildingPanel(ControlPanel cp)
	{
			this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
			this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
			cPanel = cp;
			this.setLayout(new BorderLayout());
			infoPanel = new JPanel();
			infoPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
			infoPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
			infoPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
			infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			buildingName = new JLabel("Building Name: ");
			buildingMoney = new JLabel("Building Money: ");
			infoPanel.add(buildingName); 
			infoPanel.add(buildingMoney); 
			this.add(infoPanel, BorderLayout.NORTH);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());
			buttonPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
			buttonPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
			buttonPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
			buttonPanel.setBorder(BorderFactory.createTitledBorder("Buildings"));
			view = new JPanel();
			view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
			buildingButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			buildingButtons.setViewportView(view);
			buttonPanel.add(buildingButtons, BorderLayout.CENTER);
			buttonPanel.validate();
			this.add(buttonPanel, BorderLayout.CENTER);
	}
	
	public void addBuilding(String name)
	{
		JButton newBuildingButton = new JButton(name);
		newBuildingButton.setBackground(Color.white);
		Dimension paneSize = buttonPanel.getPreferredSize();
		newBuildingButton.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuildingButton.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuildingButton.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newBuildingButton.addActionListener(this);
		view.add(newBuildingButton);
		this.updateInfo(newBuildingButton);
	}
	
	public void updateInfo(JButton selected)
	{
		List<Place> places = Directory.places();
		for(Place p : places)
		{
			if(p.getName() == selected.getText())
			{
				buildingName.setText("Building Name: " + p.getName());
			//	buildingMoney.setText("Building Money: Need a money field in places");
				p.getBuilding().displayBuilding();
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
