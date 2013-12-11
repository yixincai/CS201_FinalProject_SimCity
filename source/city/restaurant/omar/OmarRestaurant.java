package city.restaurant.omar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.Role;
import city.PersonAgent;
import city.interfaces.PlaceWithAnimation;
import city.restaurant.*;
import city.restaurant.omar.gui.*;

public class OmarRestaurant extends Restaurant implements PlaceWithAnimation {

	public RevolvingStand revolving_stand = new RevolvingStand();
	//count stands for the number of waiting list
	int count = -1;
	public OmarHostRole host;
	private List<Table> tables;
	private int businessAccountNumber = -1;
	public List<OmarWaiterRole> waiters = new ArrayList<OmarWaiterRole>();
	private OmarRestaurantAnimationPanel _animationPanel;
	
	// ------------- CONSTRUCTOR & PROPERTIES
	
	public OmarRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);

		this._animationPanel = (OmarRestaurantAnimationPanel)animationPanel.getBuildingAnimation();

		// The animation object for these will be instantiated when a person enters the building and takes the role.
		_cashier = new OmarCashierRole(null,this);
		host = new OmarHostRole(null,this,"Host");
		_animationPanel.setHost(host);
		tables = host.getTableList();
		_cook = new OmarCookRole(null,null, this);
		((OmarCookRole)_cook).cashier = (OmarCashierRole)_cashier;
	}

	// Default constructor for unit testing
	public OmarRestaurant(){
		super("Omar's Restaurant");    
		_cashier = new OmarCashierRole(null,this);
		host = new OmarHostRole(null,this,"Host");
		_cook = new OmarCookRole(null,null, this);
		((OmarCookRole)_cook).cashier = (OmarCashierRole)_cashier;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		OmarCustomerRole customer = new OmarCustomerRole(person, this, person.name());
		OmarCustomerGui customerGui = new OmarCustomerGui(customer, _animationPanel);
		customer.setGui(customerGui);
		animationPanel().addGui(customerGui);
		return customer;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person, boolean shared) {
		OmarWaiterRole newWaiter;
		if (!shared)
			newWaiter = new OmarNormalWaiterRole(person, this,(OmarCookRole)_cook, host, null);
		else
			newWaiter = new OmarSharedDataWaiterRole(person, this, (OmarCookRole)_cook, host, null);
		newWaiter.setCashier((OmarCashierRole)_cashier);
		OmarWaiterGui waiterGui = new OmarWaiterGui(newWaiter, _animationPanel);
		newWaiter.setGui(waiterGui);
		waiterGui.setHomePosition(waiters.size() * 50, 70);
		animationPanel().addGui(waiterGui);
		waiters.add(newWaiter);
		host.waiters.add(newWaiter);
		return newWaiter;
	}

	public void updateAccountNumber(int newAccountNumber){
		this.businessAccountNumber = newAccountNumber;
	}

	public int getAccountNumber(){
		return this.businessAccountNumber;
	}

	@Override
	public Role getHost() {
		return host;
	}
	
	public OmarRestaurantAnimationPanel animationPanel() {
		return this._animationPanel;
	}

	@Override
	public void generateCashierGui() {
		//TODO create cashier gui later
	}

	@Override
	public void generateCookGui() {
		OmarCookGui cashierGui = new OmarCookGui((OmarCookRole)_cook);
		((OmarCookRole)_cook).setGui(cashierGui);
		animationPanel().addGui(cashierGui);
	}

	@Override
	public void generateHostGui() {
		//TODO create cashier gui later	
	}

	@Override
	public void clearInventory() {
		_cook.clearInventory();
	}

	@Override
	public boolean existActiveWaiter() {
		for (OmarWaiterRole waiter : waiters)
			if (waiter.active)
				return true;
		return false;
	}
}
