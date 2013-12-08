package city.restaurant.ryan;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.Directory;
import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCookRole;
import city.restaurant.ryan.gui.RyanCookGui;
import agent.Agent;

public class RyanCookRole extends RestaurantCookRole{
	String name;
	public RyanRestaurant _restaurant;
	List<RyanOrder> orders = new ArrayList<RyanOrder>();
	List<Food> foods = new ArrayList<Food>();

	List<Grill> grills = new ArrayList<Grill>();
	List<Plate> plates = new ArrayList<Plate>();
	
	Timer timer = new Timer();
	Timer timer1 = new Timer();
	Map<String, Integer> cookTimes = new HashMap<String, Integer>();
	enum DishState{check, grabSupplies, gotofridge, cook, cooking, cooked, plating, plated, waiting, taken};
	
	enum OrderState{ableToOrder,OrderReceived,waitingToCheckout,none};
	OrderState orderState = OrderState.ableToOrder;
	
	enum CheckState{notChecked,Checked};
	CheckState checkState = CheckState.notChecked;	
	
	enum RoleState{WantToLeave,none}
	RoleState roleState = RoleState.none;
	
	Semaphore isCMoving = new Semaphore(0, true);
	
	RyanCookGui gui;
	public RyanCashierRole cashier;
	
	public List<Item> invoice;	
	int market_count = 0;//switch to the next market if one cannot fulfill
	Market currentMarket;
	
	
	//Constructor
	public RyanCookRole(String name, PersonAgent p, RyanRestaurant r){
		super(p);
		this.name = name;
		_restaurant = r;
		currentMarket = Directory.markets().get(market_count);
		
		cookTimes.put("Steak", 2500);
		cookTimes.put("Chicken", 1000);
		cookTimes.put("Salad", 500);
		cookTimes.put("Pizza", 750);
		
		foods.add(new Food("Steak", 0, 5, 2000));
		foods.add(new Food("Chicken", 20, 5, 1000));
		foods.add(new Food("Pizza", 20, 5, 1000));
		foods.add(new Food("Salad", 20, 5, 500));
		
		grills.add(new Grill(1));
		grills.add(new Grill(2));
		grills.add(new Grill(3));
		
		plates.add(new Plate(1));
		plates.add(new Plate(2));
		plates.add(new Plate(3));
	}

	public RyanCookRole(PersonAgent p, RyanRestaurant r) {
		super(p);
		this.name = "TestCook";
		_restaurant = r;
		currentMarket = Directory.markets().get(market_count);
		
		cookTimes.put("Steak", 2500);
		cookTimes.put("Chicken", 1000);
		cookTimes.put("Salad", 500);
		cookTimes.put("Pizza", 750);
		
		foods.add(new Food("Steak", 0, 5, 2000));
		foods.add(new Food("Chicken", 20, 5, 1000));
		foods.add(new Food("Pizza", 20, 5, 1000));
		foods.add(new Food("Salad", 20, 5, 500));
		
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
	
	public void msgOrderFulfillment(Market m, List<Item> order) {
		// TODO Auto-generated method stub
		print("Market response received");
		orderState = OrderState.OrderReceived;
		currentMarket = m;
		invoice = order;
		boolean fulfilled = true;
		for (Item item : order){
			getFood(item.name).amount += item.amount;
			if (getFood(item.name).amount < getFood(item.name).capacity)
				fulfilled = false;
		}
		//switch to another market
		if (!fulfilled){
			market_count++;
			if (market_count == Directory.markets().size())
				market_count = 0;
		}
	}
	
	public void msgOrderFinished(){
		orderState = OrderState.ableToOrder;
	}
	
	public void notifyCook(){
		stateChanged();
		checkState = CheckState.notChecked;
	}
	
	public void msgTryToCookOrder(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
		orders.add(new RyanOrder(waiter, customer, choice));
		print("Adding order for " + customer.getName());
		stateChanged();
	}
	
	public void msgCanCookOrder(){
		stateChanged();
	}
	
	public void msgAtStand(){
		isCMoving.release();
	}
	
	public void msgFoodPutOnGrill(){
		isCMoving.release();
	}
	
	public void msgFinishedOrder(RyanOrder order){
		getOrder(order).dishState = DishState.cooked;
		stateChanged();
	}
	
	public void msgFoodPutOnPlate(){
		isCMoving.release();
	}
	
	public void msgGrabbedDish(RyanWaiterRole waiter, RyanCustomerRole customer){
		for(RyanOrder order: orders){
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
		synchronized(foods){
			for(Food temp: foods){
				if(temp.amount <= temp.lowLevel && orderState == OrderState.ableToOrder){
					Restock();
					return true;
				}
			}			
		}
		
		synchronized(orders){
			for(RyanOrder temp: orders){
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
		RyanOrder order = _restaurant.revolvingStand.remove();
		if (order!=null){
			DoGoToRevolvingStand(); 
			orders.add(order);
			return true;
		}
		
		if(checkState == CheckState.notChecked){
			timer1.schedule(new TimerTask() {
				public void run() {
					print("Notify the cook to check revolving stand");
					notifyCook();
				}
			}, 10000);
			checkState = CheckState.Checked;
			return true;
		}
		
		if(orderState == OrderState.OrderReceived){
			giveInvoice();
			return true;
		}
		
		if (orders.size() == 0 && orderState == OrderState.none && roleState == RoleState.WantToLeave){
			gui.LeaveRestaurant();
			roleState = RoleState.none;
			active = false;
			return true;
		}
		
		gui.gotoHome();
		return false;
	}
	
	//Actions*******************************************************************************************************************************************************************************
	//Cooking Actions
	private void DoGoToRevolvingStand() {
		print("Going to revolving stand");
		gui.goToRevolvingStand();
		try{
			isCMoving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void CheckInventory(RyanOrder currentOrder){
		for(int i = 0; i < foods.size(); i++){
			Food temp = foods.get(i);
			if(currentOrder.choice.equals(temp.type)){
				if(temp.amount <= 0){
					currentOrder.waiter.msgOutofOrder(currentOrder.choice, currentOrder.customer);
					orders.remove(currentOrder);
				}
				else if(temp.amount > 0){
					currentOrder.dishState = DishState.grabSupplies;
				}
			}
				
		}
	}
	
	public void GrabSupplies(RyanOrder order, Grill grill){
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
    		print(AlertTag.RYAN_RESTAURANT,"Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public void CookFood(final RyanOrder currentOrder){
		print("Cooking food " + currentOrder.choice + " for " + currentOrder.customer.getName());
		currentOrder.dishState = DishState.cooking;
		//currentOrder.cookTimer(cookTimes.get(currentOrder.choice));
		
		System.out.println("Timer on");
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("Timer off");
				msgFinishedOrder(currentOrder);
			}
		},
		cookTimes.get(currentOrder.choice));
		
		for(Food temp: foods){
			if(currentOrder.choice.equals(temp.type)){
				temp.amount--;
				print("Stock of " + temp.type + " at: " + temp.amount);
			}
		}
	}
	
	public void PlateFood(RyanOrder order, Plate plate){
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
    		print(AlertTag.RYAN_RESTAURANT,"Unexpected exception caught in Agent thread:", a);
    	}
	}
	
	public void ClearPlate(RyanOrder order){
		print("Clearing");
		Plate plate = getPlate(order.pNumber);
		plate.occupied = false;
		orders.remove(order);
	}
	
	//Resupply Actions	
	public void Restock(){
		print("Restocking on Food");
		List<Item> order = new ArrayList<Item>();
		for(Food temp: foods){
			if(temp.amount <= temp.lowLevel){
				print("Restocking " + temp.type);
				order.add(new Item(temp.type, (int) (temp.capacity - temp.amount)));
			}
		}
		currentMarket.MarketCashier.msgPlaceOrder(_restaurant, order);
		orderState =  OrderState.none;
	}
	
	public void giveInvoice(){
		orderState =  OrderState.waitingToCheckout;
		cashier.msgHereIsTheInvoice(currentMarket, invoice);
		print("Giving invoice to cashier");
	}
	
	//Utilities*******************************************************************************************************************************************************************************
	public void setStockToZero(String choice){
		for(Food food: foods){
			if(food.type == choice){
				food.amount = 0;
			}
		}
		stateChanged();
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
		for(Food temp: foods){
			if(temp.type.equals(type)){
				return temp;
			}
		}
		return null;
	}
	
	public RyanOrder getOrder(RyanOrder order){
		for(RyanOrder temp: orders){
			if(temp == order){
				return temp;
			}
		}
		return null;
	}
	
	public class Food{
		String type;
		int amount;
		int lowLevel;
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
	public Place place() {
		// TODO Auto-generated method stub
		return _restaurant;
	}

	@Override
	public void cmdFinishAndLeave() {
		roleState = RoleState.WantToLeave;
		stateChanged();		
	}
	
}
