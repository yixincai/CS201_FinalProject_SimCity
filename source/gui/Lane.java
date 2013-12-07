package gui;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.transportation.gui.CommuterGui;


public class Lane {
	Rectangle2D.Double rectangle;
	ArrayList<Line2D.Double> sides;
	public int xVelocity;
	public int yVelocity;
	boolean redLight = false;
	public int xOrigin;
	public int yOrigin;
	int width;
	int height;
	public boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<CommuterGui> vehicles;
	public ArrayList<Semaphore> permits;
	
	public Lane(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, Color sc ) {
		redLight = false;
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		sideColor = sc;
		
		//Make the lane surface
		rectangle = new Rectangle2D.Double( xOrigin, yOrigin, width, height );
		
		//Make the edges to the lane surface
		sides = new ArrayList<Line2D.Double>();
		if ( isHorizontal ) {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin+width, yOrigin ) );
			sides.add( new Line2D.Double( xOrigin, yOrigin+height, xOrigin+width, yOrigin+height ) );
		} else {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin, yOrigin+height ) );
			sides.add( new Line2D.Double( xOrigin+width, yOrigin, xOrigin+width, yOrigin+height ) );
		}
		
		permits = new ArrayList<Semaphore>();
		if (isHorizontal)
			for (int i=0;i<w/10;i++){
				permits.add(new Semaphore(1,true));
			}
		else
			for (int i=0;i<h/10;i++){
				permits.add(new Semaphore(1,true));
			}
		vehicles = new ArrayList<CommuterGui>();
	}
	
	public synchronized void addVehicle( CommuterGui v ) {
		//We need to set the proper origin for this new vehicle, given the lane starting geometry constraints
		//The +2 is due to my lanes being 20 pixels "wide" and vehicles being 16 pixels "wide". 
		if ( xVelocity > 0 ) {
			v.setXY(xOrigin, yOrigin); 
		} else if ( yVelocity > 0 ) {
			v.setXY( xOrigin, yOrigin ); 
		} else {
			if ( isHorizontal ) {
				v.setXY( xOrigin + width - 10, yOrigin );
			} else {
				v.setXY( xOrigin, yOrigin + height - 10 ) ;
			}
		}
		
		vehicles.add( v );
	}
	
	public void draw( Graphics2D g2 ) {
		g2.setColor( laneColor );
		g2.fill( rectangle );
		for ( int i=0; i<sides.size(); i++ ) {
			g2.setColor( sideColor );
			g2.draw( sides.get(i) );
		}
		
		for ( int i=vehicles.size()-1; i >= 0; i-- ) {
			CommuterGui v = vehicles.get(i);
			//if ( !redLight ) {
				v.move( xVelocity, yVelocity );
			//}
			
			double x = v.getX();
			double y = v.getY();

			//Remove the vehicle from the list if it is at the end of the lane
			if ( isHorizontal ) {
				//End of lane is xOrigin + width - vehicle width
				double endOfLane = xOrigin + width - 10;
				if ( xVelocity > 0 && x >= endOfLane ) {
					vehicles.get(i).releaseSemaphore();
					vehicles.remove(i);					
				} else if ( x <= xOrigin ) {
					vehicles.get(i).releaseSemaphore();
					vehicles.remove(i);
				}
			} else {
				//End of lane is xOrigin + height - vehicle height
				double endOfLane = yOrigin + height - 10;
				if ( yVelocity > 0 && y >= endOfLane ) {
					vehicles.get(i).releaseSemaphore();
					vehicles.remove(i);					
				} else if ( y <= yOrigin ) {
					vehicles.get(i).releaseSemaphore();
					vehicles.remove(i);
				}
			}
		}
		
//		for ( int i=0; i<vehicles.size(); i++ ) {
//			CommuterGui v = vehicles.get(i);
//			g2.setColor( Color.red );
//			g2.fill( v );
//		}
	}
	
	public void redLight() {
		redLight = true;
	}
	
	public void greenLight() {
		redLight = false;
	}
}
