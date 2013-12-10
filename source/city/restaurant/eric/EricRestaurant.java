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
import city.restaurant.eric.interfaces.EricHost;
import city.restaurant.eric.interfaces.EricWaiter;

public class EricRestaurant extends Restaurant {
	
	private List<EricWaiter> _waiters = new ArrayList<EricWaiter>();
	private int _businessAccountNumber = -1;
	private EricAnimationPanel _animationPanel;
	private EricHostRole _host;
	private EricRevolvingStand _revolvingStand = new EricRevolvingStand();
	
	
	
	// ---------------------------------------- CONSTRUCTOR & PROPERTIES ----------------------------------------------

	public EricRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel) {
		super(name, worldViewBuilding);
		_animationPanel = (EricAnimationPanel)animationPanel.getBuildingAnimation();
		
		cashier = new EricCashierRole(null, this);
		((EricCashierRole)cashier).setHost(_host);
		_host = new EricHostRole (null, this);
		_host.setCashier((EricCashierRole)cashier);
		cook = new EricCookRole(null, this);
		((EricCookRole)cook).setCashier((EricCashierRole)cashier);
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
		cook.clearInventory();
	}

	
	
	// ---------------------------------------- FACTORIES & ROLE ACQUIRES -----------------------------------------------
	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person, boolean isSharedDataWaiter) {
		if(isSharedDataWaiter) {
			EricSharedDataWaiterRole w = new EricSharedDataWaiterRole(person, this);
			_waiters.add(w);
			return w;
		}
		else {
			EricNormalWaiterRole w = new EricNormalWaiterRole(person, this);
			_waiters.add(w);
			return w;
		}
	}

	@Override
	public void generateCashierGui() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateCookGui() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateHostGui() {
		// TODO Auto-generated method stub
		
	}

}
