package gui;
import java.awt.geom.*;


public class Building extends Rectangle2D.Double {
        BuildingAnimationPanel myBuildingPanel;

        public Building( int x, int y, int width, int height ) {
                super( x, y, width, height );
        }
        
        public void displayBuilding() {
                myBuildingPanel.displayBuildingPanel();
        }
        
        public void setBuildingPanel( BuildingAnimationPanel bp ) {
                myBuildingPanel = bp;
        }
}
