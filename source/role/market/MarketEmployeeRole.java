package role.market;
import role.market.MarketCashierRole.CustomerOrder;
import role.market.MarketCashierRole.RestaurantOrder;
import role.market.interfaces.MarketEmployee;

import java.util.*;

public class MarketEmployeeRole implements MarketEmployee{
	Market m;
	List<CustomerOrder> pickUpOrders;
	List<RestaurantOrder> deliverOrders;

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
		//DoGoHome();
		return false;
	}
	
	public void pickUpOrders(CustomerOrder mc){
		for (Item item : mc.orderFulfillment)
			//DoPickUp(item);
		//DoGoToCashier();
		m.MarketCashier.msgHereAreGoods(mc);
	}
	
	public void deliverFood(RestaurantOrder mc){
		for (Item item : mc.orderFulfillment){
			//DoPickUp(item);
		}
		//DoGoToTruck();
		//Transportation.Truck.msgDeliverToCook(mc.r, mc.orderFulfillment, mc.bill);
	}
}
