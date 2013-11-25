package city.restaurant.omar;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Order {
	private OmarWaiterRole w;
	private OmarCustomerRole c;
	private String choice;
	private int tableNumber;
	private double cookTime;
	Timer foodTimer;
	public enum OrderStatus{ pending, cooking, cooked, pickup};
	OrderStatus status;

	public Order(OmarWaiterRole w, int tableNumber, OmarCookRole c, OmarCustomerRole customer){
		this.w = w;
		this.choice = customer.choice;
		this.tableNumber = tableNumber;
		this.cookTime = c.cookInventory.get(choice).cookTime;
		this.c = customer;
		status = OrderStatus.pending;
	}
	
	public OmarWaiterRole getWaiter(){
		return w;
	}
	
	public void isCooking(){
		foodTimer = new Timer((int)cookTime, new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				status = OrderStatus.cooked;
				foodTimer.stop();}});
		foodTimer.start();
	}
	
	public void setWaiter(OmarWaiterRole w){
		this.w = w;
	}
	
	public String getChoice(){
		return choice;
	}
	
	public void setChoice(String choice){
		this.choice = choice;
	}
	
	public int getTableNumber(){
		return tableNumber;
	}
	
	public void setTableNumber(int tableNumber){
		this.tableNumber = tableNumber;
	}
	
	public OmarCustomerRole getCustomer(){
		return c;
	}
	
	public String toString(){
		return choice;
	}
}
