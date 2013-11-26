package city.market;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.market.MarketCashierRole.*;
import city.market.gui.*;
import city.market.interfaces.MarketEmployee;
import agent.Role;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	public MarketEmployeeGui gui;
	
	public Market market;
	public List<CustomerOrder> pickUpOrders = new ArrayList<CustomerOrder>();
	public List<RestaurantOrder> deliverOrders = new ArrayList<RestaurantOrder>();
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	public MarketEmployeeRole(PersonAgent p, Market m){
		super(p);
		this.market = m;
	}
	
	public void setGui(MarketEmployeeGui g) {
		gui = g;
	}
	
	public void msgAnimationFinished() {
		//from animation
		atDestination.release();
		stateChanged();
	}
	
	public void cmdFinishAndLeave() {
		print("person tells employee to finish and leave");
		role_state = RoleState.WantToLeave;
		stateChanged();
	}

	public void msgPickOrder(CustomerOrder mc){
		print("employee received customer order");
		pickUpOrders.add(mc);		
		stateChanged();
	}

	public void msgPickOrder(RestaurantOrder rc){
		print("employee received restaurant order");		
		deliverOrders.add(rc);
		stateChanged();
	}

	public boolean pickAndExecuteAnAction(){
		if(pickUpOrders.size()!=0){
			pickUpOrders(pickUpOrders.get(0));
			pickUpOrders.remove(0);
			return true;
		}
		print("--------------------------------------------");

		if(deliverOrders.size()!=0){
			deliverFood(deliverOrders.get(0));
			deliverOrders.remove(0);
			return true;
		}
		if (pickUpOrders.size() == 0 && deliverOrders.size() == 0 && role_state == RoleState.WantToLeave){
			cmdFinishAndLeave();
			role_state = RoleState.none;
			active = false;
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
		market.truck.msgDeliverToCook(mc.orderFulfillment, mc.r);
	}
	
	public void DoPickUp(String item){
		gui.PickUp(item);
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoGoToCashier(){
		gui.GoToCashier();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoGoToTruck(){
		gui.GoToTruck();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoGoHome(){
		gui.GoHome();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return market;
	}
}