package city.restaurant.yixin;

import java.util.ArrayList;
import java.util.List;

import agent.Role;
import city.restaurant.Restaurant;


public class YixinRestaurant extends Restaurant{
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	
	boolean open;
	public YixinCashierRole Cashier;
	public YixinHostRole Host;
	public YixinCookRole Cook;
	public List<YixinWaiterRole> Waiters = new ArrayList<YixinWaiterRole>();
	
	public YixinRestaurant(){
		super();
		Cashier = new YixinCashierRole(null,this);
		Host = new YixinHostRole(null,this,"Fiona");
		Cook = new YixinCookRole(null,this);
	}
		
	public void updateMarketStatus(){
		if (Cashier == null || Host == null || Cook == null || Waiters.size()==0)
			open = false;
		else
			open = true;
	}

	@Override
	public Role generateCustomerRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role generateWaiterRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getHostRole() {
		// TODO Auto-generated method stub
		return null;
	}
}
