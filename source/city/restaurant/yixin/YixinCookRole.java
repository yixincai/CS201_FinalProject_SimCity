package city.restaurant.yixin;

import gui.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.*;
import city.market.*;
import city.restaurant.*;
import city.restaurant.yixin.gui.YixinCookGui;

public class YixinCookRole extends RestaurantCookRole {//implements Cook{
	public YixinRestaurant restaurant;
	public YixinCashierRole cashier;

	private String name = "TheBestCook";
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public Map<String, Food> inventory = new HashMap<String, Food>();
	Timer timer = new Timer(), timer2 = new Timer();
	public YixinCookGui cookGui = null;
	boolean lowInFood = true;

	enum CookState{ableToOrder,OrderReceived,waitingToCheckout,none};
	CookState state = CookState.ableToOrder;
	enum CheckState{notChecked,Checked};
	CheckState check_state = CheckState.notChecked;	
	private Semaphore atTable = new Semaphore(0,true);

	public List<Item> invoice;	
	int market_count = 0;//switch to the next market if one cannot fulfill
	Market current_market;

	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;

	public YixinCookRole(PersonAgent p, YixinRestaurant r) {
		super(p);
		this.restaurant = r;
		inventory.put("Steak", new Food("Steak", 5000, 4, 3, 5));
		inventory.put("Chicken", new Food("Chicken", 4000, 4, 3, 5));
		inventory.put("Salad", new Food("Salad", 1000, 4, 3, 5));
		inventory.put("Pizza", new Food("Pizza", 3000, 4, 3, 5));
	}

	public void notifyCook(){
		stateChanged();
		check_state = CheckState.notChecked;
	}

	public void setGui(YixinCookGui gui){
		cookGui = gui;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void releaseSemaphore(){
		atTable.release();
	}

	public void msgHereIsTheOrder(YixinWaiterRole w, String choice, int table) {
		print(AlertTag.YIXIN_RESTAURANT,"Order received");
		orders.add(new Order(w,choice,table,Order.OrderState.NotCooked));
		stateChanged();
	}

	public void msgDone(Order o){
		o.state = Order.OrderState.Cooked;
		stateChanged();
	}

	public void msgOrderFinished(){
		state = CookState.ableToOrder;
	}

	public void msgOrderFulfillment(Market m, List<Item> orderFulfillment){
		print(AlertTag.YIXIN_RESTAURANT,"Market response received");
		state = CookState.OrderReceived;
		current_market = m;
		invoice = orderFulfillment;
		boolean fulfilled = true;
		for (Item item : orderFulfillment){
			inventory.get(item.name).amount += item.amount;
			if (inventory.get(item.name).amount < inventory.get(item.name).capacity)
				fulfilled = false;
		}
		//switch to another market
		if (!fulfilled){
			market_count++;
			if (market_count == Directory.markets().size())
				market_count = 0;
		}
		//check inventory
		if (inventory.get("Steak").amount < inventory.get("Steak").threshold)
			lowInFood = true;
		if (inventory.get("Chicken").amount < inventory.get("Chicken").threshold)
			lowInFood = true;
		if (inventory.get("Pizza").amount < inventory.get("Pizza").threshold)
			lowInFood = true;
		if (inventory.get("Salad").amount < inventory.get("Salad").threshold)
			lowInFood = true;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			for (Order order : orders){
				if (order.state == Order.OrderState.Cooked){
					returnOrder(order);
					return true;
				}
			}
			for (Order order : orders){
				if (order.state == Order.OrderState.NotCooked){
					cookOrder(order);
					return true;
				}
			}
			Order order = restaurant.revolving_stand.remove();
			if (order!=null){
				DoGoToRevolvingStand();
				orders.add(order);
				cookOrder(order);
				return true;
			}
			if(state == CookState.OrderReceived){
				giveInvoice();
				state =  CookState.waitingToCheckout;
				return true;
			}
			if(lowInFood && state == CookState.ableToOrder){
				askForSupply(Directory.markets().get(market_count));
				state =  CookState.none;
				lowInFood = false;
				return true;
			}
			if (orders.size() == 0 && state == CookState.none && role_state == RoleState.WantToLeave){
				//LeaveRestaurant();
				role_state = RoleState.none;
				active = false;
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			return false;
		}
		if (check_state == CheckState.notChecked){
			timer2.schedule(new TimerTask() {
				public void run() {
					print(AlertTag.YIXIN_RESTAURANT,"Notify the cook to check revolving stand");
					notifyCook();
				}
			}, 10000);
			check_state = CheckState.Checked;
		}
		GoHome();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	public void LeaveRestaurant(){
		cookGui.LeaveRestaurant();
		try{
			atTable.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void askForSupply(Market market){
		print(AlertTag.YIXIN_RESTAURANT,"Buy food from market.");
		List<Item> order = new ArrayList<Item>();
		if (inventory.get("Steak").amount <= inventory.get("Steak").threshold)
			order.add(new Item("Steak", inventory.get("Steak").capacity - inventory.get("Steak").amount));
		if (inventory.get("Chicken").amount <= inventory.get("Chicken").threshold)
			order.add(new Item("Chicken", inventory.get("Chicken").capacity - inventory.get("Chicken").amount));
		if (inventory.get("Salad").amount <= inventory.get("Salad").threshold)
			order.add(new Item("Salad", inventory.get("Salad").capacity - inventory.get("Salad").amount));
		if (inventory.get("Pizza").amount <= inventory.get("Pizza").threshold)
			order.add(new Item("Pizza", inventory.get("Pizza").capacity - inventory.get("Pizza").amount));
		market.MarketCashier.msgPlaceOrder(restaurant, order);
	}

	private void cookOrder(final Order order) {
		Food f = inventory.get(order.choice);
		if (f.amount < f.threshold){
			lowInFood = true;
		}		
		if (f.amount == 0){
			print(AlertTag.YIXIN_RESTAURANT,f.choice + " is running out");
			order.w.msgFoodRunsOut(order.choice, order.tableNumber);
			orders.remove(order);
			return;
		}
		DoGoToFridge();
		order.state = Order.OrderState.Cooking;
		final long time = f.cookingTime;
		f.amount--;
		DoGoToCookingPlace();
		DoCookFood(order.choice);
		timer.schedule(new TimerTask() {
			public void run() {
				print(AlertTag.YIXIN_RESTAURANT,"Cooking " + order.choice + " with time of " + time);
				msgDone(order);
				cookGui.DoFinishFood();
			}
		}, time);
	}

	private void returnOrder(Order order) {
		print(AlertTag.YIXIN_RESTAURANT,"The order is ready");
		DoGoToPlate();
		order.w.msgOrderIsReady(order.choice, order.tableNumber);
		orders.remove(order);
	}

	private void giveInvoice(){
		print(AlertTag.YIXIN_RESTAURANT,"Giving invoice to cashier");
		cashier.msgHereIsTheInvoice(current_market, invoice);
	}

	private void GoHome(){
		cookGui.DoGoHome();
	}

	// The animation DoXYZ() routines
	private void DoGoToCookingPlace() {
		print(AlertTag.YIXIN_RESTAURANT,"Go Cooking.");
		cookGui.DoGoCookFood();
		try{
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoCookFood(String choice){
		print(AlertTag.YIXIN_RESTAURANT,"Cooking " + choice);
		cookGui.DoCookFood(choice);
	}

	private void DoGoToFridge() {
		print(AlertTag.YIXIN_RESTAURANT,"Going to fridge");
		cookGui.DoGoToFridge();
		try{
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGoToPlate() {
		print(AlertTag.YIXIN_RESTAURANT,"Going to plating area");
		cookGui.DoPutPlate();
		try{
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGoToRevolvingStand() {
		print(AlertTag.YIXIN_RESTAURANT,"Going to revolving stand");
		cookGui.DoGoToRevolvingStand();
		try{
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//utilities



	public static class Food {
		String choice;
		long cookingTime;
		int amount;
		int threshold;
		int capacity;
		boolean ordered = false;

		Food(String choice, long time, int amount, int threshold, int capacity){
			this.choice = choice;
			this.cookingTime = time;
			this.amount = amount;
			this.threshold = threshold;
			this.capacity = capacity;
		}
	}

	@Override
	public void cmdFinishAndLeave() {
		role_state = RoleState.WantToLeave;
		stateChanged();		
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return restaurant;
	}

	@Override
	public void clearInventory() {
		inventory.remove("Steak");
		inventory.remove("Chicken");
		inventory.remove("Salad");
		inventory.remove("Pizza");
		
		inventory.put("Steak", new Food("Steak", 5000, 0, 3, 5));
		inventory.put("Chicken", new Food("Chicken", 4000, 0, 3, 5));
		inventory.put("Salad", new Food("Salad", 1000, 0, 3, 5));
		inventory.put("Pizza", new Food("Pizza", 3000, 0, 3, 5));
		lowInFood = true;
		stateChanged();
	}

}