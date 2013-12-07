package city.restaurant.eric;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.eric.gui.EricAnimationPanel;
import city.restaurant.eric.gui.EricCustomerGui;
import city.restaurant.eric.gui.EricWaiterGui;
import city.restaurant.eric.interfaces.EricCashier;

public class EricRestaurant extends Restaurant {
	
	private EricHostRole _host;
	EricAnimationPanel _animationPanel;
	
	
	
	// ---------------------------------- CONSTRUCTOR & PROPERTIES --------------------------------------
	public EricRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel) {
		super(name, worldViewBuilding);
		_animationPanel = (EricAnimationPanel)animationPanel.getBuildingAnimation();
		
		_cashier = new EricCashierRole(null, this);
		_host = new EricHostRole(null, this);
		_cook = new EricCookRole(null, this);
		((EricCookRole)_cook).setCashier((EricCashier)_cashier);
	}
	@Override
	public Role getHost() { return _host; }
	
	
	
	// ----------------------------------------- FACTORIES -------------------------------------------

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		EricCustomerRole newCustomer = new EricCustomerRole(person, this, ""); // leaving hacks string blank for now
		EricCustomerGui gui = new EricCustomerGui(newCustomer);
		newCustomer.setGui(gui);
		_animationPanel.addGui(gui);
		return newCustomer;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person) {
		// TODO add random (or nonrandom) choice of normal vs shared-data waiter roles
		EricWaiterRole newWaiter = new EricWaiterRole(person,this);
		
		newWaiter.setCashier((EricCashier)_cashier);
		newWaiter.setCook((EricCookRole)_cook);
		newWaiter.setHost(_host);
		
		EricWaiterGui gui = new EricWaiterGui(newWaiter);
		newWaiter.setGui(gui);
		_animationPanel.addGui(gui);
		
		return newWaiter;
	}
	
	
	
	// ----------------------------------------- UTILITIES ---------------------------------------

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
