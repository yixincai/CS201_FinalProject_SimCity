package gui;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class WorldViewBuilding extends Rectangle2D.Double {
	BuildingInteriorAnimationPanel myBuildingPanel;
	BufferedImage image;

	// PROPERTIES
	public int positionX() { return (int)x; }
	public int positionY() { return (int)y; }

	public WorldViewBuilding( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}

	public WorldViewBuilding( int x, int y, int dim, BufferedImage image) {
		super( x*10 + 41, y*10 + 30, dim, dim );
		this.image = image;
	}

	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}

	public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
		myBuildingPanel = bp;
	}
}
