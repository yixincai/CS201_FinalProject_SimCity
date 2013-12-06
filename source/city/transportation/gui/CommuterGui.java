package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import gui.Gui;
import gui.astar.AStarNode;
import gui.astar.AStarTraversal;
import gui.astar.Position;
import city.Place;
import city.transportation.*;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	
//	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	enum Command { none, walk, car}
	Command _transportationMethod = Command.none;
	boolean isPresent = true;
	
	CommuterRole _commuter;
	AStarTraversal _aStarTraversal;
	
	Position currentPosition;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace, AStarTraversal aStarTraversal) {
		System.out.println("Created CommuterGui");
		// Note: placeX and placeY can safely receive values of null
	/*	_xPos = placeX(initialPlace);
		_yPos = placeY(initialPlace); */
		_commuter = commuter;
		_aStarTraversal = aStarTraversal;
		currentPosition = convertPixelToGridSpace(placeX(initialPlace), placeY(initialPlace));
	}
	
	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - currentPosition.getX());
		double y = Math.abs(placeY(destination) - currentPosition.getY());
		
		return x+y;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
	public int getX(){
		return currentPosition.getX();
	}
	
	public int getY(){
		return currentPosition.getY();
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.walk;
		Position destinationP = convertPixelToGridSpace(placeX(destination), placeY(destination) - 10); // offset by 10
		guiMoveFromCurrentPositionTo(destinationP);
	}
	
	public void driveToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.car;
		Position destinationP = convertPixelToGridSpace(placeX(destination), placeY(destination) - 10);
		guiMoveFromCurrentPositionTo(destinationP);
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationMethod = Command.walk;
		Position destinationP = convertPixelToGridSpace(busstop.positionX(), busstop.positionY() - 10);
		guiMoveFromCurrentPositionTo(destinationP);
		setPresent(true);
	}
/*	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_goingSomewhere = true;
		_xDestination = car.getXPosition();
		_yDestination = car.getYPosition();
		setPresent(true);
	}
	
	public void atCar(){
		setPresent(false);
		_commuter.msgAtCar();
	}
	
*/	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
	/*	currentPosition = busstop.positionX(); // setx
		currentPosition = busstop.positionY(); // sety */
		setPresent(true);
	}
	
	//------------------------------------------Animation---------------------------------------
	@Override
	public void updatePosition() {
		/*if (currentPosition.getX() < _xDestination)
			xPos++;
		else if (currentPosition.getX() > _xDestination)
			_xPos--;

		if (currentPosition.getY() < _yDestination)
			_yPos++;
		else if (currentPosition.getY() > _yDestination)
			_yPos--; */
		
		
		
	/*	if (_transportationMethod == Command.car){
			if (_xPos < _xDestination)
				_xPos++;
			else if (_xPos > _xDestination)
				_xPos--;

			if (_yPos < _yDestination)
				_yPos++;
			else if (_yPos > _yDestination)
				_yPos--;
		} */
		
		if(currentPosition.getX() == _xDestination &&  currentPosition.getY() == _yDestination &&
				(_transportationMethod == Command.car || _transportationMethod == Command.walk)){
			_transportationMethod = Command.none;
			setPresent(false);
			_commuter.msgReachedDestination();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			if(_transportationMethod == Command.car)
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);
			g.fillRect(currentPosition.getX(), currentPosition.getY(), 5, 5);
		}
	}
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.positionX();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.positionY();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
	
/*	void move(int destx, int desty){
		_xDestination = destx;
		_yDestination = desty;
	} */
	
	void guiMoveFromCurrentPositionTo(Position to){
        AStarNode aStarNode = (AStarNode)_aStarTraversal.generalSearch(currentPosition, to);
        List<Position> path = aStarNode.getPath();
        Boolean firstStep   = true;
        Boolean gotPermit   = true;

        for (Position tmpPath: path) {
            //The first node in the path is the current node. So skip it.
            if (firstStep) {
                firstStep   = false;
                continue;
            }

            //Try and get lock for the next step.
            int attempts    = 1;
            gotPermit       = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY()).moveInto(_aStarTraversal.getGrid());

            //Did not get lock. Lets make n attempts.
            while (!gotPermit && attempts < 3) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

                //Wait for 1sec and try again to get lock.
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit   = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY()).moveInto(_aStarTraversal.getGrid());
                attempts ++;
            }

            //Did not get lock after trying n attempts. So recalculating path.            
            if (!gotPermit) {
                guiMoveFromCurrentPositionTo(to);
                break;
            }

            //Got the required lock. Lets move.
            currentPosition.release(_aStarTraversal.getGrid());
            currentPosition = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY ());
          //  move(currentPosition.getX(), currentPosition.getY());
        }
        /*
        boolean pathTaken = false;
        while (!pathTaken) {
            pathTaken = true;
            //print("A* search from " + currentPosition + "to "+to);
            AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
            if (a == null) {//generally won't happen. A* will run out of space first.
                System.out.println("no path found. What should we do?");
                break; //dw for now
            }
            //dw coming. Get the table position for table 4 from the gui
            //now we have a path. We should try to move there
            List<Position> ps = a.getPath();
            Do("Moving to position " + to + " via " + ps);
            for (int i=1; i<ps.size();i++){//i=0 is where we are
                //we will try to move to each position from where we are.
                //this should work unless someone has moved into our way
                //during our calculation. This could easily happen. If it
                //does we need to recompute another A* on the fly.
                Position next = ps.get(i);
                if (next.moveInto(aStar.getGrid())){
                    //tell the layout gui
                    guiWaiter.move(next.getX(),next.getY());
                    currentPosition.release(aStar.getGrid());
                    currentPosition = next;
                }
                else {
                    System.out.println("going to break out path-moving");
                    pathTaken = false;
                    break;
                }
            }
        }
        */
    }
	
	Position convertPixelToGridSpace(int x, int y){
		return new Position(x/11, y/12);
	}
}
