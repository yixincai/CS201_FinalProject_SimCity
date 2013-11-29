package gui;
import java.awt.geom.*;


@SuppressWarnings("serial")
public class WorldViewBuilding extends Rectangle2D.Double {
	BuildingInteriorAnimationPanel myBuildingPanel;

	// PROPERTIES
	public int positionX() { return (int)x; }
	public int positionY() { return (int)y; }

	public WorldViewBuilding( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}

	public WorldViewBuilding( int x, int y, int dim) {
		super( x*50 + 20, y*50 + 20, dim, dim );
	}

	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}

	public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
		myBuildingPanel = bp;
	}
}
