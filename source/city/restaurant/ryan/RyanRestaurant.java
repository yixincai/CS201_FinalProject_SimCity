package city.restaurant.ryan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.Role;
import city.PersonAgent;
import city.interfaces.PlaceWithAnimation;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.yixin.ProducerConsumerMonitor;

public class RyanRestaurant extends Restaurant implements PlaceWithAnimation{
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	//count stands for the number of waiting list
	int count = -1;
	int waiter_count = -1;
	boolean open;
	public RyanHostRole host;
	private int businessAccountNumber = -1;
	public List<RyanWaiterRole> Waiters = new ArrayList<RyanWaiterRole>();
	private RyanAnimationPanel _animationPanel;
	
	// ------------- CONSTRUCTOR & PROPERTIES
	
	public RyanRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);

		this._animationPanel = (RyanAnimationPanel)animationPanel.getBuildingAnimation();

		// The animation object for these will be instantiated when a person enters the building and takes the role.
		cashier = new RyanCashierRole(null,this);
		host = new RyanHostRole(null,this,"Host");
		cook = new RyanCookRole(null,this);
		((RyanCookRole)cook).cashier = (RyanCashierRole)cashier;
	}

	//default constructor for unit testing DO NOT DELETE
	public RyanRestaurant(){
		super("Ryan's Restaurant");    
		cashier = new RyanCashierRole(null,this);
		host = new RyanHostRole(null,this,"Host");
		cook = new RyanCookRole(null,this);
		((RyanCookRole)cook).cashier = (RyanCashierRole)cashier;
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
			count = 1;
		}
		return (new RyanCustomerRole(person, this, person.getName(), count-1));
	}

	@Override
	public Role generateWaiterRole() {
		int i = (new Random()).nextInt(2);
		RyanWaiterRole newWaiter;
		if (i == 0)
			newWaiter = new RyanWaiterRole(null, this, "");
		else
			newWaiter = new RyanSharedDataWaiterRole(null, this, "");
		newWaiter.setCashier((RyanCashierRole)cashier);
		newWaiter.setCook((RyanCookRole)cook);
		newWaiter.setHost(host);
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
	
	public RyanAnimationPanel getAnimationPanel() {
		return this._animationPanel;
	}
}
