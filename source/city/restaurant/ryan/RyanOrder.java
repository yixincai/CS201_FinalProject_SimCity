package city.restaurant.ryan;

import java.util.Timer;
import java.util.TimerTask;

import city.restaurant.ryan.RyanCookRole.DishState;

public class RyanOrder{
	RyanWaiterRole waiter;
	RyanCustomerRole customer;
	String choice;
	int gNumber;
	int pNumber;
	DishState dishState = DishState.check;
	Timer timer = new Timer();
	
	public RyanOrder(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
		this.waiter = waiter;
		this.customer = customer;
		this.choice = choice;
	}
	
//	public void cookTimer(int time){
//		System.out.println("Timer on");
//		timer.schedule(new TimerTask() {
//			public void run() {
//				System.out.println("Timer off");
//				dishState = DishState.cooked;
//				msgFinished();
//			}
//		},
//		time);
//	}
	
}
