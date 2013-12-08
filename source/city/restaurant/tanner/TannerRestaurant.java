package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.tanner.gui.TannerRestaurantAnimationPanel;
import city.restaurant.tanner.gui.TannerRestaurantCashierRoleGui;
import city.restaurant.tanner.gui.TannerRestaurantCookRoleGui;
import city.restaurant.tanner.gui.TannerRestaurantCustomerRoleGui;
import city.restaurant.tanner.gui.TannerRestaurantHostRoleGui;
import city.restaurant.tanner.gui.TannerRestaurantWaiterRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantHost;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import city.restaurant.yixin.YixinCashierRole;
import city.restaurant.yixin.YixinCookRole;
import city.restaurant.yixin.YixinCustomerRole;
import city.restaurant.yixin.YixinHostRole;
import city.restaurant.yixin.YixinNormalWaiterRole;
import city.restaurant.yixin.YixinSharedDataWaiterRole;
import city.restaurant.yixin.YixinWaiterRole;
import city.restaurant.yixin.gui.YixinCashierGui;
import city.restaurant.yixin.gui.YixinCookGui;
import city.restaurant.yixin.gui.YixinCustomerGui;
import city.restaurant.yixin.gui.YixinHostGui;
import city.restaurant.yixin.gui.YixinWaiterGui;

public class TannerRestaurant extends Restaurant
{
	
//------------------------------------DATA----------------------------------------------------------------
	public ProducerConsumerMonitor revolvingStand = new ProducerConsumerMonitor();
	TannerRestaurantHostRole host;
	TannerRestaurantCashierRole cashier;
	TannerRestaurantCookRole cook;
	private int bussinessAccountNumber = -1;
	public static Map<Integer, Table> tableMap = new HashMap<Integer, Table>();
	public static Map<Integer, Dish> menu = new HashMap<Integer, Dish>();
	public static int numTables = 3;
	public static int numDishes = 4;
	TannerRestaurantAnimationPanel animationPanel;
	int count = -1;
	int waiter_count = -1;
	List<TannerRestaurantWaiter> waiters;
	public double currentFunds;
	
	
	public TannerRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel)
	{
		super(name, worldViewBuilding);
		this.animationPanel = (TannerRestaurantAnimationPanel)animationPanel.getBuildingAnimation();
		waiters = new ArrayList<TannerRestaurantWaiter>();
		host = new TannerRestaurantHostRole(null, this, "Host");
		cashier = new TannerRestaurantCashierRole(null, this, "Cashier");
		cook = new TannerRestaurantCookRole(null, this, cashier, "Money Bags");	
		currentFunds = 10000.0;
		cashier.setCook(cook);
	}

	//For Unit testing DO NOT DELETE
	public TannerRestaurant() 
	{
		super("Tanner's Restaurant");
		cashier = new TannerRestaurantCashierRole(null,this, "Cashier");
		host = new TannerRestaurantHostRole(null,this,"Host");
		cook = new TannerRestaurantCookRole(null, this, cashier, "Money Bags");
		currentFunds = 10000.0;
	}

	@Override
	public Role getHost() 
	{
		return this.host;
	}
	
	public double getMoney()
	{
		return currentFunds;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) 
	{
		count++;
		if (count > 10){
			count = -1;
		}
		TannerRestaurantCustomerRole customer = new TannerRestaurantCustomerRole(person, this, person.name(), count);
		TannerRestaurantCustomerRoleGui tannerCustomerGui = new TannerRestaurantCustomerRoleGui(customer);
		customer.setGui(tannerCustomerGui);
		this.animationPanel.addGui(tannerCustomerGui);
		return customer;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person, boolean shared)
	{
		TannerRestaurantWaiter newWaiter;
		if (!shared)
			newWaiter = new TannerRestaurantRegularWaiterRole(person, this, person.name());
		else
			newWaiter = new TannerRestaurantSharedDataWaiterRole(person, this, person.name());
		newWaiter.setCashier((TannerRestaurantCashier)cashier);
		newWaiter.setCook((TannerRestaurantCook)cook);
		newWaiter.setHost(host);
		waiters.add(newWaiter);
		waiter_count++;
		TannerRestaurantWaiterRoleGui tannerWaiterGui = new TannerRestaurantWaiterRoleGui(newWaiter, waiter_count);
		newWaiter.setGui(tannerWaiterGui);
		this.animationPanel.addGui(tannerWaiterGui);
		host.addWaiter(newWaiter);
		return (Role) newWaiter;
	}
	
	public void updateAccountNumber(int newAccNumber)
	{
		this.bussinessAccountNumber = newAccNumber;
	}
	
	public int getAccountNumber()
	{
		return this.bussinessAccountNumber;
	}
	
	public int waiterCount()
	{
		return waiter_count;
	}
	
	public TannerRestaurantAnimationPanel animationPanel()
	{
		return animationPanel;
	}
	

	@Override
	public void generateCashierGui() 
	{
		TannerRestaurantCashierRoleGui tannerCashierGui = new TannerRestaurantCashierRoleGui((TannerRestaurantCashier)cashier);
		((TannerRestaurantCashier)cashier).setGui(tannerCashierGui);
		this.animationPanel.addGui(tannerCashierGui);		
	}

	@Override
	public void generateCookGui() 
	{
		TannerRestaurantCookRoleGui tannerCookGui = new TannerRestaurantCookRoleGui((TannerRestaurantCook)cook);
		((TannerRestaurantCook)cook).setGui(tannerCookGui);
		this.animationPanel.addGui(tannerCookGui);			
	}

	@Override
	public void generateHostGui()
	{
		TannerRestaurantHostRoleGui tannerHostGui = new TannerRestaurantHostRoleGui((TannerRestaurantHost)host);
		((TannerRestaurantHost)host).setGui(tannerHostGui);
		this.animationPanel.addGui(tannerHostGui);		
	}

}
