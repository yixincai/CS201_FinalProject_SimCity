package city.market;
import java.util.*;

import city.PersonAgent;
import city.market.MarketCashierRole.CustomerOrder;
import city.market.MarketCashierRole.RestaurantOrder;
import city.market.interfaces.MarketEmployee;
import agent.Role;

public class MarketEmployeeRole extends Role implements MarketEmployee{
	Market market;
	List<CustomerOrder> pickUpOrders;
	List<RestaurantOrder> deliverOrders;
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;
	
	public MarketEmployeeRole(PersonAgent p, Market m){
		super(p);
		this.market = m;
	}

	public void msgPickOrder(CustomerOrder mc){
		pickUpOrders.add(mc);
	}

	public void msgPickOrder(RestaurantOrder rc){
		deliverOrders.add(rc);
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
			finishCommandAndLeave();
			role_state = RoleState.none;
			return true;
		}
		//DoGoHome();
		return false;
	}
	
	public void pickUpOrders(CustomerOrder mc){
		for (Item item : mc.orderFulfillment)
			//DoPickUp(item);
		//DoGoToCashier();
		market.MarketCashier.msgHereAreGoods(mc);
	}
	
	public void deliverFood(RestaurantOrder mc){
		for (Item item : mc.orderFulfillment){
			//DoPickUp(item);
		}
		//DoGoToTruck();
		//Transportation.Truck.msgDeliverToCook(mc.r, mc.orderFulfillment, mc.bill);
	}
	
	protected void finishCommandAndLeave() {
		//gui.DoLeaveMarket();
		active = false;
	}
}
