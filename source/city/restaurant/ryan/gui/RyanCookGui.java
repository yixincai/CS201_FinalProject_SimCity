package city.restaurant.ryan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import city.restaurant.ryan.RyanCookRole;

public class RyanCookGui extends JPanel implements Gui{
	private RyanCookRole agent = null;
	//RestaurantGui gui;
	
	String food;
	int grillNum;
	int plateNum;
	boolean hasIngredients = false;
	
	int xPos = 0; 
	int yPos = 0;
	int xDestination = 0;
	int yDestination = 0;
	
	List<Grill> grills = new ArrayList<Grill>();
	List<Plate> plates = new ArrayList<Plate>();
	
	Dimension fridge = new Dimension(580, 80);
	Dimension grill = new Dimension(580, 180);
	Dimension revolvingStand = new Dimension(520, 50);
	Dimension plate = new Dimension(520, 125);
	Dimension home;
	
	enum Command{noCommand, goToStand, goToFridge, goToGrill, goToGrill1, goToPlating, LeaveRestaurant};
	Command command = Command.noCommand;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Chef.png"));
    Image cook = a.getImage();
    int xGap = 18;
    int yGap = 32;
    
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/Pot.png"));
    Image pot = b.getImage();
    int xPGap = 20;
    int yPGap = 20;
	
	public RyanCookGui(RyanCookRole agent){
		this.agent = agent;
//		home = dimension;
//		yPos = dimension.height;
//		xPos = dimension.width;
//		yDestination = dimension.height;
//		xDestination = dimension.width;
		
		yPos = 0;
		xPos = 550;
		yDestination = 130;
		xDestination = 550;
		home = new Dimension(550, 130);
		
		plates.add(new Plate(1, 500, 115));
		plates.add(new Plate(2, 500, 140));
		plates.add(new Plate(3, 500, 165));
		
		grills.add(new Grill(1, 600, 175));
		grills.add(new Grill(2, 600, 200));
		grills.add(new Grill(3, 600, 225));
		
	}
	
	public void getIngredients(String choice, int grill){
		food = choice.substring(0, Math.min(choice.length(), 2));
		grillNum = grill;
		command = Command.goToFridge;
		gotoFridge();
	}
	
	public void Plating(int gNumber, int pNumber){
		grillNum = gNumber;
		plateNum = pNumber;
		gotoGrill1();
	}
	
	public void ClearPlate(int pNumber){
		for(Plate temp: plates){
			if(temp.number == pNumber){
				temp.occupied = false;
			}
		}
	}
	

	public void goToRevolvingStand() {
		yDestination = revolvingStand.height;
		xDestination = revolvingStand.width;
		command = Command.goToStand;
	}
	
	public void gotoFridge(){
		yDestination = fridge.height;
		xDestination = fridge.width;
		command = Command.goToFridge;
	}
	
	public void gotoGrill(){
		yDestination = grill.height;
		xDestination = grill.width;
		command = Command.goToGrill;
		System.out.println("goToGrill");
	}
	
	public void gotoGrill1(){
		yDestination = grill.height;
		xDestination = grill.width;
		command = Command.goToGrill1;
		System.out.println("goToGrill");
	}
	
	public void gotoPlate(){
		yDestination = plate.height;
		xDestination = plate.width;
		command = Command.goToPlating;
	}
	
	public void gotoHome(){
		yDestination = home.height;
		xDestination = home.width;
	}
	
	public int getxPos(){
		return xPos;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	public void LeaveRestaurant(){
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;  
        
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.goToFridge){
				System.out.println("atfridge");
				hasIngredients = true;
				gotoGrill();
			}
			else if (command==Command.goToStand){
				System.out.println("atstand");
				agent.msgAtStand();
			}
			else if(command==Command.goToGrill){
				System.out.println("atgrill");
				command=Command.noCommand;
				hasIngredients = false;
				for(Grill temp: grills){
					if(temp.number == grillNum){
						temp.choice = food;
						temp.occupied = true;
					}
				}
				food = null;
				grillNum = 0;
				agent.msgFoodPutOnGrill();
			}
			else if(command==Command.goToGrill1){
				System.out.println("atgrill1");
				for(Grill temp: grills){
					if(temp.number == grillNum){
						temp.occupied = false;
						food = temp.choice;
						temp.choice = null;
					}
				}
				hasIngredients = true;
				gotoPlate();
			}
			else if(command == Command.goToPlating){
				System.out.println("plating");
				command=Command.noCommand;
				hasIngredients = false;
				for(Plate temp: plates){
					if(temp.number == plateNum){
						temp.choice = food;
						temp.occupied = true;
					}
				}
				food = null;
				grillNum = 0;
				plateNum = 0;
				agent.msgFoodPutOnPlate();
			}
			else if(command==Command.LeaveRestaurant) {
				System.out.println("leaving");
				agent.active = false;
				//gui.setCustomerEnabled(agent);
			}
			else{
				command=Command.noCommand;
			}
		}
        
        
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		if(agent.active){
			g.drawImage(cook, xPos, yPos, xGap, yGap, this);
		}
        
        if(hasIngredients){
        	g.setColor(Color.black);
			g.drawString((food), xPos, (yPos-5));
        }
        
        for(Grill temp: grills){
        	if(temp.occupied == true){
        		g.setColor(Color.black);
    			g.drawString((temp.choice), (temp.x+22), (temp.y-9));
    			g.drawImage(pot, temp.x, temp.y, xPGap, yPGap, this);
        	}
        }
        
        for(Plate temp: plates){
        	if(temp.occupied == true){
        		g.setColor(Color.black);
    			g.drawString((temp.choice), temp.x, (temp.y-5));
        	}
        }
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	class Grill{
		int number;
		final int x;
		final int y;
		boolean occupied = false;
		String choice;
		
		Grill(int number, int x, int y){
			this.number = number;
			this.x = x;
			this.y = y;
		}
	}
	
	class Plate{
		int number;
		final int x;
		final int y;
		boolean occupied = false;
		String choice;
		
		Plate(int number, int x, int y){
			this.number = number;
			this.x = x;
			this.y = y;
		}
	}

}
