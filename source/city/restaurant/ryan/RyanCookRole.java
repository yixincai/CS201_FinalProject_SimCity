package city.restaurant.ryan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCookRole;
import city.restaurant.ryan.gui.RyanCookGui;
import agent.Agent;

public class RyanCookRole extends RestaurantCookRole{
	String name;
	List<Order> Orders = new ArrayList<Order>();
	List<Food> Foods = new ArrayList<Food>();
	List<MarketAgent> Markets = new ArrayList<MarketAgent>();
	List<Grill> grills = new ArrayList<Grill>();
	List<Plate> plates = new ArrayList<Plate>();
	Timer timer = new Timer();
	Map<String, Integer> cookTimes = new HashMap<String, Integer>();
	enum DishState{check, grabSupplies, gotofridge, cook, cooking, cooked, plating, plated, waiting, taken};
	enum InventoryState{fine, ordering, restocking, ordered};
	
	Semaphore isCMoving = new Semaphore(0, true);
	
	RyanCookGui gui;
	public RyanCashierRole cashier;
	
	
	//Constructor
	public RyanCookRole(String name, PersonAgent p, RyanRestaurant r){
		super(p);
		this.name = name;
		
		cookTimes.put("Steak", 2500);
		cookTimes.put("Chicken", 1000);
		cookTimes.put("Salad", 500);
		cookTimes.put("Pizza", 750);
		
		Foods.add(new Food("Steak", 20, 5, 2000));
		Foods.add(new Food("Chicken", 20, 5, 1000));
		Foods.add(new Food("Pizza", 20, 5, 1000));
		Foods.add(new Food("Salad", 20, 5, 500));
		
		grills.add(new Grill(1));
		grills.add(new Grill(2));
		grills.add(new Grill(3));
		
		plates.add(new Plate(1));
		plates.add(new Plate(2));
		plates.add(new Plate(3));
	}

	public RyanCookRole(PersonAgent p, RyanRestaurant ryanRestaurant) {
		super(p);
		this.name = "TestCook";
		
		cookTimes.put("Steak", 2500);
		cookTimes.put("Chicken", 1000);
		cookTimes.put("Salad", 500);
		cookTimes.put("Pizza", 750);
		
		Foods.add(new Food("Steak", 20, 5, 2000));
		Foods.add(new Food("Chicken", 20, 5, 1000));
		Foods.add(new Food("Pizza", 20, 5, 1000));
		Foods.add(new Food("Salad", 20, 5, 500));
		
		grills.add(new Grill(1));
		grills.add(new Grill(2));
		grills.add(new Grill(3));
		
		plates.add(new Plate(1));
		plates.add(new Plate(2));
		plates.add(new Plate(3));
	}

	public void setGui(RyanCookGui gui){
		this.gui = gui;
	}
	
	//Messages******************************************************************************************************************************************************************************************************************
	
	public void msgGivenStock(String choice, int given, int amount){
		Food temp = getFood(choice);
		print("Given " + temp.given + " of food " + choice);
		temp.given = given;
		temp.state = InventoryState.restocking;
		temp.left = amount - given;
		stateChanged();
	}
	
	public void msgTryToCookOrder(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
		Orders.add(new Order(waiter, customer, choice));
		print("Adding order for " + customer.getName());
		stateChanged();
	}
	
	public void msgCanCookOrder(){
		stateChanged();
	}
	
	public void msgFoodPutOnGrill(){
		isCMoving.release();
	}
	
	public void msgFinished(){
		stateChanged();
	}
	
	public void msgFoodPutOnPlate(){
		isCMoving.release();
	}
	
	public void msgGrabbedDish(RyanWaiterRole waiter, RyanCustomerRole customer){
		for(Order order: Orders){
			if(order.customer == customer){
				print("Clearing dish for " + customer.getCustomerName() + " at " + order.pNumber);
				gui.ClearPlate(order.pNumber);
				order.dishState = DishState.taken;
				stateChanged();
			}
		}
	}
	

	//Scheduler******************************************************************************************************************************************************************************************************************
	public boolean pickAndExecuteAnAction() {
		synchronized(Orders){
			for(Order temp: Orders){
				if(temp.dishState == DishState.taken){
					ClearPlate(temp);
					return true;
				}
				if(temp.dishState == DishState.check){
					CheckInventory(temp);
					return true;
				}
				for(Grill grill: grills){
					if(temp.dishState == DishState.grabSupplies && grill.occupied == false){
						GrabSupplies(temp, grill);
						return true;
					}
				}
				if(temp.dishState == DishState.cook){
					CookFood(temp);
					return true;
				}
				for(Plate plate: plates){
					if(temp.dishState == DishState.cooked && plate.occupied == false){
						PlateFood(temp, plate);
						return true;
					}
				}
			}
		}
		synchronized(Foods){
			for(Food temp: Foods){
				if(temp.amount <= temp.lowLevel && temp.state == InventoryState.fine && temp.market < Markets.size()){
					Restock(temp);
				}
				if(temp.state == InventoryState.restocking){
					print("Order received of " + temp.type);
					fillInventory(temp);
				}
			}
		}
		gui.gotoHome();
		return false;
	}
	
	//Actions*******************************************************************************************************************************************************************************
	//Cooking Actions
	public void CheckInventory(Order currentOrder){
		for(int i = 0; i < Foods.size(); i++){
			Food temp = Foods.get(i);
			if(currentOrder.choice.equals(temp.type)){
				if(temp.amount <= 0){
					if(temp.state == InventoryState.fine && temp.market < Markets.size()){
						Restock(temp);
					}
					currentOrder.waiter.msgOutofOrder(currentOrder.choice, currentOrder.customer);
					Orders.remove(currentOrder);
				}
				else if(temp.amount > 0){
					currentOrder.dishState = DishState.grabSupplies;
				}
			}
				
		}
	}
	
	public void GrabSupplies(Order order, Grill grill){
		try{
			print("Going to fridge to get supplies for " + order.choice + " by " + order.customer.getName());
			order.dishState = DishState.gotofridge;
			grill.occupied = true;
			order.gNumber = grill.number;
			gui.getIngredients(order.choice, grill.number);
			isCMoving.acquire();
			order.dishState = DishState.cook;
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print("Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public void CookFood(Order currentOrder){
		print("Cooking food " + currentOrder.choice + " for " + currentOrder.customer.getName());
		currentOrder.dishState = DishState.cooking;
		currentOrder.cookTimer(cookTimes.get(currentOrder.choice));
		for(Food temp: Foods){
			if(currentOrder.choice.equals(temp.type)){
				temp.amount--;
				print("Stock of " + temp.type + " at: " + temp.amount);
			}
		}
	}
	
	public void PlateFood(Order order, Plate plate){
		try{
			print("Plating " + order.choice + " for " + order.customer.getName());
			order.dishState = DishState.plating;
			order.pNumber = plate.number;
			plate.occupied = true;
			Grill grill = getGrill(order.gNumber);
			grill.occupied = false;
			gui.Plating(order.gNumber, order.pNumber);
			isCMoving.acquire();
			order.dishState = DishState.waiting;
			print(order.choice + " for " + order.customer.getName() + " is done");
			order.waiter.msgOrderDone(order.choice, order.customer);
		}catch(InterruptedException a){
    		
    	} catch(Exception a){
    		print("Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public void ClearPlate(Order order){
		print("Clearing");
		Plate plate = getPlate(order.pNumber);
		plate.occupied = false;
		Orders.remove(order);
	}
	
	//Resupply Actions
	public void Restock(Food food){
		food.state = InventoryState.ordering;
		print("Restocking " + food.type);
		print("Inventory at " + food.amount);
		if(food.market < Markets.size()){
			Markets.get(food.market).msgPlaceOrder(food.type, (int) (food.capacity - food.amount));
		}
		
	}
	
	public void fillInventory(Food food){
		food.amount += food.given;
		food.given = 0;
		print("New stock for " + food.type + " is " + food.amount);
		food.state = InventoryState.fine;
		if(food.left > 0){
			food.market++;
			if(food.market < Markets.size()){
				food.state = InventoryState.ordering;
				print("Order not fulfilled completely: ");
				print("Ordering from " + Markets.get(food.market).getName() + ": " + food.left + " " + food.type);
				Markets.get(food.market).msgPlaceOrder(food.type, food.left);	
			}
		}
	}
	
	//Utilities*******************************************************************************************************************************************************************************
	public void setStockToZero(String choice){
		for(Food food: Foods){
			if(food.type == choice){
				food.amount = 0;
			}
		}
		stateChanged();
	}
	
	public void addMarket(MarketAgent market){
		Markets.add(market);
	}
	
	public Grill getGrill(int number){
		for(Grill temp: grills){
			if(temp.number == number){
				return temp;
			}
		}
		return null;
	}
	
	public Plate getPlate(int number){
		for(Plate temp: plates){
			if(temp.number == number){
				return temp;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public Food getFood(String type){
		for(Food temp: Foods){
			if(temp.type.equals(type)){
				return temp;
			}
		}
		return null;
	}
	
	public class Order{
		RyanWaiterRole waiter;
		RyanCustomerRole customer;
		String choice;
		int gNumber;
		int pNumber;
		DishState dishState = DishState.check;
		
		public Order(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
		}
		
		public void cookTimer(int time){
			print("Timer on");
			timer.schedule(new TimerTask() {
				public void run() {
					print("Timer off");
					dishState = DishState.cooked;
					msgFinished();
				}
			},
			time);
		}
		
	}
	
	public class Food{
		String type;
		int amount;
		int lowLevel;
		InventoryState state;
		int given;
		int left;
		int capacity;
		int cookingTime;
		int market = 0;
		
		public Food(String type, int amount, int level, int cookingTime){
			this.type = type;
			this.amount = amount;
			this.cookingTime = cookingTime;
			given = 0;
			lowLevel = level;
			if(level == 0){
				level = 5;
			}
			capacity = level*5;
			state = InventoryState.fine;
		}
		
		public int foodAmount(){
			return amount;
		}
	
	}
	
	public class Grill{
		int number;
		boolean occupied = false;
		
		Grill(int number){
			this.number = number;
		}
	}
	
	public class Plate{
		int number;
		boolean occupied = false;
		
		Plate(int number){
			this.number = number;
		}
	}

	@Override
	public void msgOrderFulfillment(Market m, List<Item> order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
	
}
