package city.restaurant.eric;

import java.util.ArrayList;
import java.util.List;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.eric.gui.EricAnimationPanel;
import city.restaurant.eric.gui.EricCashierGui;
import city.restaurant.eric.gui.EricCookGui;
import city.restaurant.eric.gui.EricCustomerGui;
import city.restaurant.eric.gui.EricHostGui;
import city.restaurant.eric.gui.EricWaiterGui;
import city.restaurant.eric.interfaces.EricCashier;
import city.restaurant.eric.interfaces.EricWaiter;

public class EricRestaurant extends Restaurant {
	
	private List<EricWaiterRole> _waiters = new ArrayList<EricWaiterRole>();
	private int _businessAccountNumber = -1;
	private EricAnimationPanel _animationPanel;
	private EricHostRole _host;
	private EricRevolvingStand _revolvingStand = new EricRevolvingStand();
	
	
	
	// ---------------------------------------- CONSTRUCTOR & PROPERTIES ----------------------------------------------

	public EricRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel) {
		super(name, worldViewBuilding);
		_animationPanel = (EricAnimationPanel)animationPanel.getBuildingAnimation();
		
		_cashier = new EricCashierRole(null, this);
		((EricCashierRole)_cashier).setHost(_host);
		_host = new EricHostRole (null, this);
		_host.setCashier((EricCashierRole)_cashier);
		_cook = new EricCookRole(null, this);
		((EricCookRole)_cook).setCashier((EricCashierRole)_cashier);
	}
	public Role getHost() { return _host; }
	public void setAccountNumber(int newAccountNumber) { _businessAccountNumber = newAccountNumber; }
	public int getAccountNumber() { return _businessAccountNumber; }
	public boolean existActiveWaiter() {
		for(EricWaiter w : _waiters) {
			if(w.active()) return true;
		}
		return false;
	}
	public EricRevolvingStand revolvingStand() { return _revolvingStand; }
	public EricAnimationPanel animationPanel() { return _animationPanel; }
	
	
	
	// ----------- GUI COMMANDS -------------

	/** Clears the cook's inventory.  Called by the restaurant control panel. */
	public void clearInventory() {
		_cook.clearInventory();
	}

	
	
	// ---------------------------------------- FACTORIES & ROLE ACQUIRES -----------------------------------------------
	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		EricCustomerRole newCustomer = new EricCustomerRole(person, this, ""); // leaving hacks string blank for now
		EricCustomerGui gui = new EricCustomerGui(newCustomer);
		newCustomer.setGui(gui);
		_animationPanel.addGui(gui);
		return newCustomer;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person, boolean isSharedDataWaiter) {
		EricWaiterRole newWaiter;
		
		// Shared data:
		if(isSharedDataWaiter) {
			newWaiter = new EricSharedDataWaiterRole(person, this);
		}
		else {
			newWaiter = new EricNormalWaiterRole(person, this);
		}
		
		// Correspondence:
		newWaiter.setCashier((EricCashier)_cashier);
		newWaiter.setCook((EricCookRole)_cook);
		newWaiter.setHost(_host);

		// Gui:
		EricWaiterGui gui = new EricWaiterGui(newWaiter);
		newWaiter.setGui(gui);
		_animationPanel.addGui(gui); //TODO this is questionable.  Might change when job switching is implemented.
		
		_waiters.add(newWaiter);
		return newWaiter;
	}
	
	
	
	// ----------------------------------------- UTILITIES ---------------------------------------

	@Override
	public void generateCashierGui() {
		EricCashierGui gui = new EricCashierGui((EricCashierRole)_cashier);
		((EricCashierRole)_cashier).setGui(gui);
		animationPanel().addGui(gui);
	}

	@Override
	public void generateCookGui() {
		EricCookGui gui = new EricCookGui((EricCookRole)_cook);
		((EricCookRole)_cook).setGui(gui);
		animationPanel().addGui(gui);
	}

	@Override
	public void generateHostGui() {
		EricHostGui gui = new EricHostGui((EricHostRole)_host);
		((EricHostRole)_host).setGui(gui);
		animationPanel().addGui(gui);
	}
	@Override
	protected void cmdTimeToClose() {
		getHost().cmdFinishAndLeave();
		getCook().cmdFinishAndLeave();
		getCashier().cmdFinishAndLeave();
		for(EricWaiterRole waiter : _waiters)
			waiter.cmdFinishAndLeave();
	}

}
