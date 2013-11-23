package city.restaurant.yixin;

import gui.WorldViewBuilding;

import java.util.*;

import agent.Role;
import city.PersonAgent;
import city.restaurant.*;
import city.restaurant.yixin.gui.YixinAnimationPanel;

public class YixinRestaurant extends Restaurant{
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	int count = -1;
	boolean open;
	public YixinHostRole Host;
	private int businessAccountNumber = -1;
	public List<YixinWaiterRole> Waiters = new ArrayList<YixinWaiterRole>();
	private YixinAnimationPanel _animationPanel;
	
	
	
	public YixinRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);
		
		this._animationPanel = (YixinAnimationPanel)animationPanel.getBuildingAnimation();
		
		// The animation object for these will be instantiated when a person enters the building and takes the role.
		Cashier = new YixinCashierRole(null,this);
		Host = new YixinHostRole(null,this,"Host");
		Cook = new YixinCookRole(null,this);
        /*
        Cook.addMarket(market1);
        Cook.addMarket(market2);
        Cook.addMarket(market3);
        */
		((YixinCookRole)Cook).cashier = (YixinCashierRole)Cashier;
	}
		
	public void updateMarketStatus(){
		if (Cashier == null || Host == null || Cook == null || Waiters.size()==0)
			open = false;
		else
			open = true;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		//TODO make a new customer that is initialized with a PersonAgent of person
		count++;
		if (count > 10){
			count = 1;
		}
		return (new YixinCustomerRole(person, this, person.getName(), count-1));
	}

	@Override
	public Role generateWaiterRole() {
		int i = (new Random()).nextInt(2);
		if (i == 0)
			return (new YixinNormalWaiterRole(null, this, ""));
		else
			return (new YixinSharedDataWaiterRole(null, this, ""));
	}
	
	public void updateAccountNumber(int newAccountNumber){
		this.businessAccountNumber = newAccountNumber;
	}
	
	public int getAccountNumber(){
		return this.businessAccountNumber;
	}

	@Override
	public Role getHostRole() {
		return Host;
	}
	
}
