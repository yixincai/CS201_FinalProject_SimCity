package city.restaurant.omar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import city.restaurant.RestaurantCustomerRole;

public class OmarCustomerRole extends RestaurantCustomerRole {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	double money = 40;
	double check = 0;
	double change = 0;
	
	private static int chooseTime = 4000;
	
	private OmarCustomerGui customerGui;
	
	int tableNum = -1;

	// agent correspondents
	private HostAgent host;
	WaiterAgent waiter;
	CashierAgent cashier;
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
	public CustomerAgent(String name){
		super();
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
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages
	public void gotHungry() {//from animation  //A CUSTOMER GETS HUNGRY
		print("I'm hungry");
		event = AgentEvent.GotHungry;
		stateChanged();
	}

	public void msgFollowToTable(Menu menu, WaiterAgent w, int tableNum) {
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
	
	public void msgHereIsCheck(CashierAgent cashier, double check){
		System.out.println("Customer " +this.name+ " payment due: " + this.check);
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
	protected boolean pickAndExecuteAnAction() {
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
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		//he tells the host he's hungry
		
		if(this.name.equals("Crazy Steve")){
			System.out.println("Crazy Steve can't afford anything on the menu, so he left");
			host.msgLeavingWaitList(CustomerAgent.this);
			state = AgentState.DoingNothing;
			event = AgentEvent.DoneLeaving;
			customerGui.leftWaitingList();
		}
		waitTimer = new Timer(10000, new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				if(state == AgentState.WaitingInRestaurant && event == AgentEvent.GotHungry){
					host.msgLeavingWaitList(CustomerAgent.this);
					state = AgentState.DoingNothing;
					event = AgentEvent.DoneLeaving;
					customerGui.leftWaitingList();
				}
				stateChanged();}});
		waitTimer.start();
	}

	private void sitDown() {	//GETS SEATED
		Do("Being seated. Going to table " + tableNum);
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
		Do("Chose Food.  Called waiter " + waiter.toString() + " over.");
		waiter.msgReadyToOrder(this);
	}
	
	private void tellWaiterOrder(){
		customerGui.setCurrentStatus("Waiting");
		Do("Told Waiter order");
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
		Do("Eating Food");
	
		eatTimer = new Timer(5000, new ActionListener() { 
			public void actionPerformed(ActionEvent event){
				doneEating();
				state = AgentState.Paying;
				eatTimer.stop();}});
		eatTimer.start();
	}

	private void doneEating() {
		System.out.println(this.toString() + " done eating.");
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
			System.out.println("Customer's change for next time: " + money);
		}
	}
	
	private void goDie(){
		Do("Can't pay for food.  Guess I have to die now. :(");
		customerGui.DoDie();
		try {
			custSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void leaveTable() {	//LEAVES
		customerGui.setCurrentStatus("Leaving");
		Do("Leaving.");
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

	public String toString() {
		return "customer " + getName();
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
}
