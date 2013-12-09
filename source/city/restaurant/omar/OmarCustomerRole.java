package city.restaurant.omar;

import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import city.PersonAgent;
import city.Place;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.omar.gui.OmarCustomerGui;

public class OmarCustomerRole extends RestaurantCustomerRole {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	double money = 40;
	double check = 0;
	double change = 0;
	
	private static int chooseTime = 4000;
	
	private OmarCustomerGui customerGui;
	private OmarRestaurant restaurant;
	
	int tableNum = -1;

	// agent correspondents
	private OmarHostRole host;
	OmarWaiterRole waiter;
	OmarCashierRole cashier;
	Menu menu;
	
	String choice;
	Timer chooseTimer;
	Timer eatTimer;
	Timer waitTimer;
	
	Semaphore custSem = new Semaphore(0, true);

	//change event in message
	//change state in action
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, ReadingMenu, 
		HailingWaiter, Ordering, Eating, Paying, Paid, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state
	
	
	public enum AgentEvent 
	{None, GotHungry, FollowWaiter, Seated, ReadyToOrder, 
		TellWaiterOrder, FoodArrived, DoneEating, WaitingForCheck, GotCheck, 
		GotChange, AwaitingDecision, Die, DoneLeaving};
	AgentEvent event = AgentEvent.None;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public OmarCustomerRole(PersonAgent p, OmarRestaurant r, String name){
		super(p);
		this.restaurant = r;
		this.name = name;
		
		if(name.equals("Cheap Earl")){
			this.money = 0;
		}
		
		if(name.equals("Crazy Steve")){
			this.money = 5;
		}
		
		if(name.equals("Darth Vader")){
			this.money = 13;
		}
		cmdGotHungry();
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(OmarHostRole host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages
	public void gotHungry() {//from animation  //A CUSTOMER GETS HUNGRY
		print(AlertTag.OMAR_RESTAURANT, "I'm hungry");
		event = AgentEvent.GotHungry;
		stateChanged();
	}

	public void msgFollowToTable(Menu menu, OmarWaiterRole w, int tableNum) {
		event = AgentEvent.FollowWaiter;
		
		this.menu = menu;
		this.waiter = w;
		this.tableNum = tableNum;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.Seated;
		customerGui.setCurrentStatus("Seated");
		custSem.release();
		//from animation
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		event = AgentEvent.TellWaiterOrder;
		stateChanged();
	}
	
	public void msgNeedReorder(){
		int randomLeaveOrReorder = (int)(Math.random() * 2);
		if(randomLeaveOrReorder == 1){
			state = AgentState.BeingSeated;
			event = AgentEvent.Seated;
		} else {
			state = AgentState.Paid;
			event = AgentEvent.GotChange;
		}
		stateChanged();
	}
	
	public void msgHereIsYourFood(String choice){
		event = AgentEvent.FoodArrived;
		stateChanged();
	}
	
	public void msgDoneEatingAndLeaving(){
		event = AgentEvent.DoneEating;
		stateChanged();
	}
	
	public void msgHereIsCheck(OmarCashierRole cashier, double check){
		//System.out.println("Customer " +this.name+ " payment due: " + this.check);
		print(AlertTag.OMAR_RESTAURANT, "Customer " +this.name+ " payment due: " + this.check);
		this.cashier = cashier;
		
		event = AgentEvent.GotCheck;
		stateChanged();
	}
	
	public void msgHereIsYourChange(double change) {
		event = AgentEvent.GotChange;
		state = AgentState.Paid;
		System.out.println(this.name + " paid");
		this.change = change;
		stateChanged();
	}
	
	public void msgGoDie(){
		event = AgentEvent.Die;
		state = AgentState.Paid;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() { 
		event = AgentEvent.DoneLeaving;
		custSem.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.GotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.FollowWaiter){
			state = AgentState.BeingSeated;
			sitDown();
			return true;
		}
		if(state == AgentState.BeingSeated && event == AgentEvent.Seated){
			state = AgentState.ReadingMenu;
			chooseFood();
			return true;
		}
		if(state == AgentState.ReadingMenu && event == AgentEvent.ReadyToOrder){
			state = AgentState.HailingWaiter;
			notifyWaiter();
			return true;
		}
		if(state == AgentState.HailingWaiter && event == AgentEvent.TellWaiterOrder){
			state = AgentState.Ordering;
			tellWaiterOrder();
			return true;
		}
		if(state == AgentState.Ordering && event == AgentEvent.FoodArrived){
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if(state == AgentState.Paying && event == AgentEvent.DoneEating){
			doneEatingAndReadyToPay();
			return true;
		}
		if(state == AgentState.Paying && event == AgentEvent.GotCheck){
			payCheck();
			return true;
		}
		if(state == AgentState.Paid && event == AgentEvent.GotChange){
			leaveTable();
			return true;
		} else if(state == AgentState.Paid && event == AgentEvent.Die){
			goDie();
			return true;
		}

		if (state == AgentState.Leaving && event == AgentEvent.DoneLeaving){
			state = AgentState.DoingNothing;
			event = AgentEvent.None;
			//no action
			return true;
		}
		return false;
	}

	// Actions
	private void goToRestaurant() {	//GOES TO RESTAURANT
		print(AlertTag.OMAR_RESTAURANT, "Going to restaurant");
		restaurant.host.msgIWantFood(this);//send our instance, so he can respond to us
		//he tells the host he's hungry
		
		if(this.name.equals("Crazy Steve")){
			print(AlertTag.OMAR_RESTAURANT,"Crazy Steve can't afford anything on the menu, so he left");
			host.msgLeavingWaitList(OmarCustomerRole.this);
			state = AgentState.DoingNothing;
			event = AgentEvent.DoneLeaving;
			customerGui.leftWaitingList();
		}
		waitTimer = new Timer(20000, new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				if(state == AgentState.WaitingInRestaurant && event == AgentEvent.GotHungry){
					restaurant.host.msgLeavingWaitList(OmarCustomerRole.this);
					state = AgentState.DoingNothing;
					event = AgentEvent.DoneLeaving;
					customerGui.leftWaitingList();
				}
				stateChanged();}});
		waitTimer.start();
	}

	private void sitDown() {	//GETS SEATED
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Being seated. Going to table " + tableNum);
		customerGui.DoGoToSeat(1, tableNum);
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void chooseFood(){
		chooseTimer = new Timer(chooseTime, new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				event = AgentEvent.ReadyToOrder;
				stateChanged();}});
		chooseTimer.start();
	}
	
	private void notifyWaiter(){
		customerGui.setCurrentStatus("Ready");
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Chose Food.  Called waiter " + waiter.getName() + " over.");
		waiter.msgReadyToOrder(this);
	}
	
	private void tellWaiterOrder(){
		customerGui.setCurrentStatus("Waiting");
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Told Waiter order");
		String choice;
		int randomChoice = (int)(Math.random() * 4);
		if(randomChoice == 0){
			choice = "Pizza";
		} else if(randomChoice == 1){
			choice = "Hot Dog";
		} else if(randomChoice == 2){
			choice = "Burger";
		} else{
			choice = "Filet Mignon";
		}
		if(this.name.equals("Darth Vader")){
			choice = "Pizza";
		}
		this.choice = choice;
		waiter.msgHereIsMyChoice(this, choice);
	}

	private void eatFood() {	//EATS HIS FOOD
		customerGui.setCurrentStatus("Eating");
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Eating Food");
	
		eatTimer = new Timer(5000, new ActionListener() { 
			public void actionPerformed(ActionEvent event){
				doneEating();
				state = AgentState.Paying;
				eatTimer.stop();}});
		eatTimer.start();
	}

	private void doneEating() {
		print(AlertTag.OMAR_RESTAURANT, "Done eating.");
		event = AgentEvent.DoneEating;
		stateChanged();
	}
	
	private void doneEatingAndReadyToPay(){
		waiter.msgDoneEatingAndReadyToPay(this);
		event = AgentEvent.WaitingForCheck;
		stateChanged();
	}
	private void payCheck(){
		money -= check;
		event = AgentEvent.AwaitingDecision;
		
		customerGui.DoGoToCashier();
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(money < 0){
			cashier.msgICantAffordMyMeal(this);
		} else{
			cashier.msgTakeMoney(this, (int)check);
			print(AlertTag.OMAR_RESTAURANT, "Customer's change for next time: " + money);
		}
	}
	
	private void goDie(){
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Can't pay for food.  Guess I have to die now. :(");
		customerGui.DoDie();
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void leaveTable() {	//LEAVES
		customerGui.setCurrentStatus("Leaving");
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Leaving.");
		restaurant.host.msgLeavingTable(this);
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		event = AgentEvent.DoneLeaving;
		state = AgentState.DoingNothing;
		active = false;
		stateChanged();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public void setGui(OmarCustomerGui g) {
		customerGui = g;
	}

	public OmarCustomerGui getGui() {
		return customerGui;
	}
	
	public void msgArrived() {
		custSem.release();
	}

	@Override
	public void cmdGotHungry() {
		print(AlertTag.OMAR_RESTAURANT, "I'm hungry");
		event = AgentEvent.GotHungry;
		stateChanged();
	}

	@Override
	public Place place() {
		return restaurant;
	}

	@Override
	public void cmdFinishAndLeave() {
		customerGui.setCurrentStatus("Leaving");
		print(AlertTag.OMAR_RESTAURANT, this.name + ": Leaving.");
		host.msgLeavingTable(this);
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		event = AgentEvent.DoneLeaving;
		state = AgentState.DoingNothing;
		active = false;
		stateChanged();
	}
}
