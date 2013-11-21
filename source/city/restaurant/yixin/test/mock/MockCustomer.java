package city.restaurant.yixin.test.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.restaurant.yixin.Menu;
import city.restaurant.yixin.interfaces.YixinCashier;
import city.restaurant.yixin.interfaces.YixinCustomer;
import city.restaurant.yixin.interfaces.YixinWaiter;
import agent.Mock;

public class MockCustomer extends Mock implements YixinCustomer {

	public EventLog log = new EventLog();

	public MockCustomer(String name) {
		super(name);
	}
	
	public void gotHungry(){};
	
	public void msgNoSeat(){};

	public void msgFollowMe(YixinWaiter w, int tablenumber, Menu menu){};
	
	public void msgNoFood(Menu menu){};

	public void msgWhatWouldYouLike(){};
	
	public void msgHereIsYourFood(String choice){};
	
	public void msgHereIsTheCheck(double money, YixinCashier cashier){};
	
	public void msgHereIsTheChange(double change){
		log.add(new LoggedEvent("Received HereIsTheChange from cashier. Change = "+ change));
	}
	
	public void msgYouDoNotHaveEnoughMoney(double debt){
		log.add(new LoggedEvent("Received YouDoNotHaveEnoughMoney from cashier. Debt = "+ debt));
	}
	
	public void msgAnimationFinishedGoToSeat(){};
	
	public void msgAnimationFinishedGoToCashier(){};
	
	public void msgAnimationFinishedLeaveRestaurant(){}

}
