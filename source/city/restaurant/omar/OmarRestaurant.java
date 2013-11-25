package city.restaurant.omar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.Role;
import city.PersonAgent;
import city.interfaces.PlaceWithAnimation;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.omar.gui.OmarRestaurantAnimationPanel;

public class OmarRestaurant extends Restaurant implements PlaceWithAnimation {

	public RevolvingStand revolving_stand = new RevolvingStand();
	//count stands for the number of waiting list
	int count = -1;
	int waiter_count = -1;
	boolean open;
	public OmarHostRole host;
	public OmarCookRole cook;
	private int businessAccountNumber = -1;
	public List<OmarWaiterRole> Waiters = new ArrayList<OmarWaiterRole>();
	private OmarRestaurantAnimationPanel _animationPanel;
	
	// ------------- CONSTRUCTOR & PROPERTIES
	
	public OmarRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);

		this._animationPanel = (OmarRestaurantAnimationPanel)animationPanel.getBuildingAnimation();

		// The animation object for these will be instantiated when a person enters the building and takes the role.
		cashier = new OmarCashierRole(null,this);
		host = new OmarHostRole(null,this,"Host");
		cook = new OmarCookRole(null,null, this);
		((OmarCookRole)cook).cashier = (OmarCashierRole)cashier;
	}

	//default constructor for unit testing DO NOT DELETE
	public OmarRestaurant(){
		super("Omar's Restaurant");    
		cashier = new OmarCashierRole(null,this);
		host = new OmarHostRole(null,this,"Host");
		cook = new OmarCookRole(null,null, this);
		((OmarCookRole)cook).cashier = (OmarCashierRole)cashier;
	}

	public boolean isOpen(){
		if (cashier.active && host.active && cook.active && Waiters.size()!=0)
			return true;
		else
			return false;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		return (new OmarCustomerRole(person, this, person.getName()));
	}

	@Override
	public Role generateWaiterRole() {
		int i = (new Random()).nextInt(2);
		OmarWaiterRole newWaiter;
		if (i == 0)
			newWaiter = new OmarWaiterRole(null, this,cook, host, null);
		else
			newWaiter = new OmarWaiterRole(null, this, cook, host, null);
		newWaiter.setCashier((OmarCashierRole)cashier);
		waiter_count++;
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
	
	public OmarRestaurantAnimationPanel getAnimationPanel() {
		return this._animationPanel;
	}
}
