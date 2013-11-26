package city.restaurant.yixin;

import gui.WorldViewBuilding;

import java.util.*;

import agent.Role;
import city.PersonAgent;
import city.interfaces.PlaceWithAnimation;
import city.restaurant.*;
import city.restaurant.yixin.gui.*;

public class YixinRestaurant extends Restaurant implements PlaceWithAnimation {
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	//count stands for the number of waiting list
	int count = -1;
	int waiter_count = -1;
	boolean open;
	public YixinHostRole host;
	private int businessAccountNumber = -1;
	public List<YixinWaiterRole> Waiters = new ArrayList<YixinWaiterRole>();
	private YixinAnimationPanel _animationPanel;
	
	// ------------- CONSTRUCTOR & PROPERTIES
	
	public YixinRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);

		this._animationPanel = (YixinAnimationPanel)animationPanel.getBuildingAnimation();

		// The animation object for these will be instantiated when a person enters the building and takes the role.
		cashier = new YixinCashierRole(null,this);
		host = new YixinHostRole(null,this,"Host");
		cook = new YixinCookRole(null,this);
		((YixinCookRole)cook).cashier = (YixinCashierRole)cashier;
	}

	//default constructor for unit testing DO NOT DELETE
	public YixinRestaurant(){
		super("Yixin's Restaurant");    
		cashier = new YixinCashierRole(null,this);
		host = new YixinHostRole(null,this,"Host");
		cook = new YixinCookRole(null,this);
		((YixinCookRole)cook).cashier = (YixinCashierRole)cashier;
		((YixinCashierRole)cashier).cook = (YixinCookRole)cook;
	}

	public boolean isOpen(){
		if (cashier.active && host.active && cook.active && Waiters.size()!=0)
			return true;
		else
			return false;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		//TODO make a new customer that is initialized with a PersonAgent of person
		count++;
		if (count > 10){
			count = -1;
		}
		YixinCustomerRole customer = new YixinCustomerRole(person, this, person.getName(), count);
		YixinCustomerGui yixinCustomerGui = new YixinCustomerGui(customer,count);
		customer.setGui(yixinCustomerGui);
		getAnimationPanel().addGui(yixinCustomerGui);
		return customer;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person) {
		int i = (new Random()).nextInt(2);
		YixinWaiterRole newWaiter;
		if (i == 0)
			newWaiter = new YixinNormalWaiterRole(person, this, person.getName());
		else
			newWaiter = new YixinSharedDataWaiterRole(person, this, person.getName());
		newWaiter.setCashier((YixinCashierRole)cashier);
		newWaiter.setCook((YixinCookRole)cook);
		newWaiter.setHost(host);
		Waiters.add(newWaiter);
		waiter_count++;
		YixinWaiterGui yixinWaiterGui = new YixinWaiterGui(newWaiter, waiter_count);
		newWaiter.setGui(yixinWaiterGui);
		getAnimationPanel().addGui(yixinWaiterGui);
		return newWaiter;
	}

	public void updateAccountNumber(int newAccountNumber){
		this.businessAccountNumber = newAccountNumber;
	}

	public int getAccountNumber(){
		return this.businessAccountNumber;
	}

	@Override
	public Role getHostRole() {
		return host;
	}

	public int waiterCount(){
		return waiter_count;
	}
	
	public YixinAnimationPanel getAnimationPanel() {
		return this._animationPanel;
	}

	@Override
	public void generateCashierGui() {
		YixinCashierGui yixinCashierGui = new YixinCashierGui((YixinCashierRole)cashier);
		((YixinCashierRole)cashier).setGui(yixinCashierGui);
		getAnimationPanel().addGui(yixinCashierGui);
	}

	@Override
	public void generateCookGui() {
		YixinCookGui yixinCookGui = new YixinCookGui((YixinCookRole)cook);
		((YixinCookRole)cook).setGui(yixinCookGui);
		getAnimationPanel().addGui(yixinCookGui);		
	}

	@Override
	public void generateHostGui() {
		YixinHostGui hostGui = new YixinHostGui(host);
		host.setGui(hostGui);
		getAnimationPanel().addGui(hostGui);		
	}

}
