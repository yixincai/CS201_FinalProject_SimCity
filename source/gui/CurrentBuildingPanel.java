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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import city.Directory;
import city.Place;
import city.Workplace;
import city.bank.Bank;
import city.restaurant.Restaurant;
import city.restaurant.eric.EricRestaurant;
import city.restaurant.eric.gui.EricAnimationPanel;
import city.restaurant.omar.OmarRestaurant;
import city.restaurant.omar.gui.OmarRestaurantAnimationPanel;
import city.restaurant.ryan.RyanRestaurant;
import city.restaurant.ryan.gui.RyanAnimationPanel;
import city.restaurant.yixin.YixinRestaurant;
import city.restaurant.yixin.gui.YixinAnimationPanel;

public class CurrentBuildingPanel extends JPanel implements ActionListener {
	
	JPanel view;
	JPanel buttonPanel;
	JPanel infoPanel;
	JLabel buildingName;
	JScrollPane buildingButtons;
	ControlPanel cPanel;
	
	Place _currentlySelectedBuilding;
	JButton clearInventoryButton;
	JButton closeWorkplaceButton;
	private final Dimension buttonDimension = new Dimension(340, 30);
	
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
			
			 clearInventoryButton = new JButton("Clear Inventory");
			 clearInventoryButton.setMinimumSize(buttonDimension);
			 clearInventoryButton.setMaximumSize(buttonDimension);
			 clearInventoryButton.setVisible(false);
			 
			 clearInventoryButton.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent e) {
	            	 if(_currentlySelectedBuilding != null){
	            		 ((Restaurant) _currentlySelectedBuilding).clearInventory();
	            	 }
	             }
	         });
			 
			 closeWorkplaceButton = new JButton("Close Workplace");
			 closeWorkplaceButton.setMinimumSize(buttonDimension);
			 closeWorkplaceButton.setMaximumSize(buttonDimension);
			 closeWorkplaceButton.setVisible(false);
			 
			 closeWorkplaceButton.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent e) {
	            	 if(_currentlySelectedBuilding != null && _currentlySelectedBuilding instanceof Workplace){
	            		((Workplace) _currentlySelectedBuilding).msgTimeToClose();
	            	 }
	             }
	         });
			 
			 
			 
			buildingName = new JLabel("Building Name: ");
			infoPanel.add(buildingName); 
			infoPanel.add(clearInventoryButton);
			infoPanel.add(closeWorkplaceButton);
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
		newBuildingButton.setPreferredSize(new Dimension(paneSize.width - 50, paneSize.height/10));
		newBuildingButton.setMinimumSize(new Dimension(paneSize.width - 50, paneSize.height/10));
		newBuildingButton.setMaximumSize(new Dimension(paneSize.width - 50, paneSize.height/10));
		newBuildingButton.addActionListener(this);
		view.add(newBuildingButton);
		this.updateInfo(newBuildingButton);
	}
	
	public void updateInfo(JButton selected)
	{
		updateInfo(selected.getText());
	}
	
	public void updateInfo(String selectedBuilding)
	{
		List<Place> places = Directory.places();
		for(Place p : places)
		{
			if(p.name().contains(selectedBuilding))
			{
				_currentlySelectedBuilding = p;
				if(p instanceof OmarRestaurant || p instanceof YixinRestaurant || 
						p instanceof EricRestaurant || p instanceof RyanRestaurant){
					clearInventoryButton.setVisible(true);
				} else {
					clearInventoryButton.setVisible(false);
				}
				if(p instanceof Workplace){
					closeWorkplaceButton.setVisible(true);
				} else{
					closeWorkplaceButton.setVisible(false);
				}
				buildingName.setText("Building Name: " + p.name());
			//	buildingMoney.setText("Building Money: Need a money field in places");
				p.worldViewBuilding().displayBuilding();
			}
		}
	}
	
	public void setBuildingPanel(BuildingInteriorAnimationPanel bp){
		this.currentBuildingPanel = bp;
		//updateInfo(bp.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateInfo((JButton)e.getSource());
	}

}
