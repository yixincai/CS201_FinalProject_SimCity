package city.restaurant.ryan.gui;

import java.awt.*;

import city.restaurant.ryan.RyanCustomerRole;

public class RyanCustomerGui implements Gui{

	private RyanCustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;

	//private HostAgent host;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, WaitAreaSeat, GoToSeat, GoToCashier, LeaveRestaurant};
	public enum State{nothing, choosing, ordering, ordered, eating, paying, running, leaving};
	private Command command=Command.noCommand;
	private State state = State.nothing;
	
	String choice;

	public int xTable;
	public int yTable;
	
	Dimension CashierPos = new Dimension(200, 65);

	public RyanCustomerGui(RyanCustomerRole c){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		if(state == State.running){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
		}

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.WaitAreaSeat) agent.msgAnimationFinishedGoToWaitArea();
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if(command==Command.GoToCashier) agent.msgAnimationFinishedGoToCashier();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				agent.active = false;
				//gui.setCustomerEnabled(agent);
				agent.cmdFinishAndLeave();
				state = State.nothing;
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		if(agent.active){
			g.drawImage(agent.getImage(), xPos, yPos, 20, 27, null);
		}
		
		if(state == State.choosing){
			g.setColor(Color.black);
			g.drawString("Choosing", xPos, (yPos-5));
		}
		if(state == State.ordering){
			g.setColor(Color.black);
			g.drawString("Ready to Order!", xPos, (yPos-5));
		}
		if(state == State.ordered){
			g.setColor(Color.black);
			g.drawString((choice +"?"), xPos, (yPos-5));
		}
		if(state == State.eating){
			g.setColor(Color.black);
			g.drawString((choice), xPos, (yPos+35));
		}
		if(state == State.paying){
			g.setColor(Color.black);
			g.drawString("Paying", xPos, (yPos-5));
		}
		if(state == State.leaving){
			g.setColor(Color.black);
			g.drawString("Leaving", xPos, (yPos-5));
		}
		if(state == State.running){
			g.setColor(Color.black);
			g.drawString("o_o", xPos, (yPos-5));
		}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		//agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToWaitArea(int x, int y){
		xDestination = x;
		yDestination = y;
		command = Command.WaitAreaSeat;
	}

	public void DoGoToSeat(int tableNumber) {//later you will map seatnumber to table coordinates.
		if(tableNumber == 1){
    		xTable = 150;
    		yTable = 150;
    	}
    	else if(tableNumber == 2){
    		xTable = 250;
    		yTable = 150;
    	}
    	else if(tableNumber ==3){
    		xTable = 350;
    		yTable = 150;
    	}
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}
	
    public void DoGoToCashier(){
    	xDestination = CashierPos.width;
    	yDestination = CashierPos.height;
    	command = Command.GoToCashier;
    }

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = 75;
		command = Command.LeaveRestaurant;
	}
	
	public void setState(int option){
		switch(option){
			case 1: state = State.ordering;
					break;
			case 2: state = State.ordered;
					break;
			case 3: state = State.eating;
					break;
			case 4: state = State.paying;
					break;
			case 5: state = State.leaving;
					break;
			case 6: state = State.choosing;
					break;
			case 7: state = State.running;
					break;
				default: state = State.nothing;
		}
	}
	
	public void setChoice(String choice){
		this.choice = choice.substring(0, Math.min(choice.length(), 2));
	}
	
	public String getAgentName(){
    	return agent.getName();
    }
}
