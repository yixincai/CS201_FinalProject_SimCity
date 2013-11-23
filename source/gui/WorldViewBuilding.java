package gui;
import java.awt.geom.*;


@SuppressWarnings("serial")
public class WorldViewBuilding extends Rectangle2D.Double {
        BuildingInteriorAnimationPanel myBuildingPanel;

        public WorldViewBuilding( int x, int y, int width, int height ) {
                super( x, y, width, height );
        }
        
        public WorldViewBuilding( int x, int y, int dim) {
        	super( x*100 + 50, y*60 + 50, dim, dim );
        }
        
        public void displayBuilding() {
                myBuildingPanel.displayBuildingPanel();
        }
        
        public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
                myBuildingPanel = bp;
        }
}
