package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 * @author Tanner Zigrang
 */

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WorldView extends JPanel 
{
	public WorldView()
	{
		this.setBorder(BorderFactory.createTitledBorder("World View"));
		this.setPreferredSize(new Dimension(2048/3, 2160/5));
	}
}