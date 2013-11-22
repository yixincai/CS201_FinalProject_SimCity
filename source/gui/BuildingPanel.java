package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BuildingPanel extends JPanel implements ActionListener
{
	MainGui gui;
	
	Rectangle2D myRectangle;
    String myName;
    JPanel infoPanel;
    
	public BuildingPanel(MainGui mgui, Rectangle2D r, int i)
	{
		this.gui = mgui;
		this.myName = "Building " + i;
		this.myRectangle = r;
		
		this.setLayout(new BorderLayout());
		List<JButton> buildingList = new ArrayList<JButton>();
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(1024/3, 720/2 - 15));
		setBackground( Color.LIGHT_GRAY );
		infoPanel.setBorder(BorderFactory.createTitledBorder("Current Building"));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(new JLabel("Building Name:")); //TODO Add getter for the current building's name
		infoPanel.add(new JLabel("Bulding Money: ")); //TODO Add getter for the current building's money
		this.add(infoPanel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Building"));
		JPanel view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		JScrollPane buildingButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		buildingButtons.setViewportView(view);
		buttonPanel.add(buildingButtons, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
	}

    public void displayBuildingPanel() {
            gui.displayBuildingPanel( this ); 
    }
    
    public String getName() {
        return myName;
}

}
