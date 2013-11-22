package city.market;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.market.MarketCashierRole.*;
import city.market.gui.*;
import city.market.interfaces.MarketEmployee;
import agent.Role;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	public MarketEmployeeGui gui;
	
	Market market;
	List<CustomerOrder> pickUpOrders;
	List<RestaurantOrder> deliverOrders;
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	public MarketEmployeeRole(PersonAgent p, Market m){
		super(p);
		this.market = m;
	}
	
	public void msgAnimationFinished() {
		//from animation
		atDestination.release();
		stateChanged();
	}
	
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();
	}

	public void msgPickOrder(CustomerOrder mc){
		pickUpOrders.add(mc);		
		stateChanged();
	}

	public void msgPickOrder(RestaurantOrder rc){
		deliverOrders.add(rc);
		stateChanged();
	}

	public boolean pickAndExecuteAnAction(){
		if(pickUpOrders.size()!=0){
			pickUpOrders(pickUpOrders.get(0));
			pickUpOrders.remove(0);
			return true;
		}
		if(deliverOrders.size()!=0){
			deliverFood(deliverOrders.get(0));
			deliverOrders.remove(0);
			return true;
		}
		if (pickUpOrders.size() == 0 && deliverOrders.size() == 0 && role_state == RoleState.WantToLeave){
			cmdFinishAndLeave();
			role_state = RoleState.none;
			return true;
		}
		DoGoHome();
		return false;
	}
	
	public void pickUpOrders(CustomerOrder mc){
		for (Item item : mc.orderFulfillment)
			DoPickUp(item.name);
		DoGoToCashier();
		market.MarketCashier.msgHereAreGoods(mc);
	}
	
	public void deliverFood(RestaurantOrder mc){
		for (Item item : mc.orderFulfillment){
			DoPickUp(item.name);
		}
		DoGoToTruck();
		//Transportation.Truck.msgDeliverToCook(mc.r, mc.orderFulfillment, mc.bill);
	}
	
	public void DoPickUp(String item){
		gui.PickUp(item);
	}
	
	public void DoGoToCashier(){
		gui.GoToCashier();
	}
	
	public void DoGoToTruck(){
		gui.GoToTruck();
	}
	
	public void DoGoHome(){
		gui.GoHome();
	}
}