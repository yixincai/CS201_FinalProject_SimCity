package city.restaurant.tanner;

import java.util.HashMap;
import java.util.Map;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import agent.Role;
import city.PersonAgent;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.tanner.gui.TannerRestaurantAnimationPanel;

public class TannerRestaurant extends Restaurant {
	
//------------------------------------DATA----------------------------------------------------------------
	TannerRestaurantHostRole host;
	TannerRestaurantCashierRole cashier;
	private int bussinessAccountNumber = -1;
	public static Map<Integer, Table> tableMap = new HashMap<Integer, Table>();
	public static int numTables = 3;
	TannerRestaurantAnimationPanel animationPanel;
	
	
	public TannerRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel)
	{
		super(name, worldViewBuilding);
		this.animationPanel = (TannerRestaurantAnimationPanel)animationPanel.getBuildingAnimation();
		host = new TannerRestaurantHostRole(null, this);
		cashier = new TannerRestaurantCashierRole(null, this);
		
		
	}

	//For Unit testing
	public TannerRestaurant() 
	{
		super("Tanner's Restaurant");
		
	}

	@Override
	public Role getHostRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role generateWaiterRole(PersonAgent person) {
		// TODO Auto-generated method stub
		return null;
	}

}
