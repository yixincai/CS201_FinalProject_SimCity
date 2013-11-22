package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 * @author Tanner Zigrang
 */

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class WorldView extends JPanel 
{
	private static int WINDOWX = 682;
	private static int WINDOWY = 432;
	
	public WorldView()
	{
		this.setBorder(BorderFactory.createTitledBorder("World View"));
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		
		//initializing buildings
		/* JButton marketWorldViewButton = new JButton();
		marketWorldViewButton.setBounds(20, 20, 50, 50);
		add(marketWorldViewButton);
		
		JButton yixinRestaurantWorldViewButton = new JButton();
		add(yixinRestaurantWorldViewButton);
		
		JButton bankWorldViewButton = new JButton();
		add(bankWorldViewButton);
		
		JButton homeWorldViewButton = new JButton();
		add(homeWorldViewButton);
		
		JButton apartmentWorldViewButton = new JButton();
		add(apartmentWorldViewButton); */
		
		//initialize roads
	}
}