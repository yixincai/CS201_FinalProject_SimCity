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
import city.restaurant.yixin.YixinCashierRole;
import city.restaurant.yixin.YixinCookRole;
import city.restaurant.yixin.YixinHostRole;

public class TannerRestaurant extends Restaurant
{
	
//------------------------------------DATA----------------------------------------------------------------
	TannerRestaurantHostRole host;
	TannerRestaurantCashierRole cashier;
	TannerRestaurantCookRole cook;
	private int bussinessAccountNumber = -1;
	public static Map<Integer, Table> tableMap = new HashMap<Integer, Table>();
	public static Map<Integer, Dish> menu = new HashMap<Integer, Dish>();
	public static int numTables = 3;
	public static int numDishes = 4;
	TannerRestaurantAnimationPanel animationPanel;
	
	
	public TannerRestaurant(String name, WorldViewBuilding worldViewBuilding, BuildingInteriorAnimationPanel animationPanel)
	{
		super(name, worldViewBuilding);
		this.animationPanel = (TannerRestaurantAnimationPanel)animationPanel.getBuildingAnimation();
		host = new TannerRestaurantHostRole(null, this, "Host");
		cashier = new TannerRestaurantCashierRole(null, this);
		cook = new TannerRestaurantCookRole(null, this, cashier);	
	}

	//For Unit testing DO NOT DELETE
	public TannerRestaurant() 
	{
		super("Tanner's Restaurant");
		cashier = new TannerRestaurantCashierRole(null,this);
		host = new TannerRestaurantHostRole(null,this,"Host");
		cook = new TannerRestaurantCookRole(null, this, cashier);
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
