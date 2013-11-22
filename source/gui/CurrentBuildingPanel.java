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

public class CurrentBuildingPanel extends JPanel implements ActionListener {
		
	BuildingPanel currentBuildingPanel = null;
	JPanel infoPanel;
	JLabel buildingName = new JLabel("Building Name: ");
	JLabel buildingMoney = new JLabel("Building Money: ");
	
	public CurrentBuildingPanel()
	{
			this.setLayout(new BorderLayout());
			List<JButton> buildingList = new ArrayList<JButton>();
			
			infoPanel = new JPanel();
			infoPanel.setPreferredSize(new Dimension(1024/3, 720/2));
			setBackground( Color.LIGHT_GRAY );
			infoPanel.setBorder(BorderFactory.createTitledBorder("Current Building"));
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			infoPanel.add(buildingName); //Gives current building name
			infoPanel.add(buildingMoney); //TODO Add getter for the current building's money
			
			this.add(infoPanel, BorderLayout.NORTH);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());
			buttonPanel.setBorder(BorderFactory.createTitledBorder("Buildings"));
			JPanel view = new JPanel();
			view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
			JScrollPane peopleButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			peopleButtons.setViewportView(view);
			buttonPanel.add(peopleButtons, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.CENTER);
	}
	
	public void setBuildingPanel(BuildingPanel bp){
		this.currentBuildingPanel = bp;
		buildingName.setText("Building Name: " + bp.getName());
	}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub

		}

}
