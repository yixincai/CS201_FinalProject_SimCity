package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 *
 */

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class WorldView extends JPanel implements MouseListener 
{
	private static int WINDOWX = 1024 * 2 / 3;
	private static int WINDOWY = 720 / 2;
	
	ArrayList<Building> buildings;
	
	public WorldView()
	{
		
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		this.setBorder(BorderFactory.createTitledBorder("World View"));
		 buildings = new ArrayList<Building>();

         for ( int i=0; i<2; i++ ) {
                 for ( int j=0; j<5; j++ ) {
                         Building b = new Building( i*40+ 50, j*40 + 50, 20, 20 );
                         buildings.add( b );
                 }
         }
		 

         addMouseListener( this );
	}

	public void paintComponent( Graphics g ) {
		System.out.println("hello1");
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor( Color.black );
		                        
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
		    g2.fill( b );
		}
	}
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse Clicked in WorldView");

        for ( int i=0; i<buildings.size(); i++ ) {
                Building b = buildings.get(i);
                if ( b.contains( e.getX(), e.getY() ) ) {
                        b.displayBuilding();
                }
        }	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}