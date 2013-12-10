package city.restaurant.ryan;

import agent.Agent;
import gui.trace.AlertTag;

import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import city.PersonAgent;
import city.Place;
import city.restaurant.RestaurantCustomerRole;
import city.restaurant.ryan.gui.RyanCustomerGui;
import city.restaurant.yixin.YixinRestaurant;

/**
 * Restaurant customer agent.
 */
public class RyanCustomerRole extends RestaurantCustomerRole {
	private String name;
	private int hungerLevel = 100;        // determines length of meal
	private int money;
	private double payment;
	Timer timer = new Timer();
	private RyanCustomerGui customerGui;
	String choice;
	boolean patient;
	boolean bad;
	Menu Menu = new Menu();
	List<Menu> cMenu = new ArrayList<Menu>();
	int tableNumber;
	int xTable;
	int yTable;
	int xSeat;
	int ySeat;
	Random generator = new Random();
	
	Dimension CashierPos = new Dimension(200, 65);

	// agent correspondents
	private RyanHostRole host;
	
	RyanRestaurant _restaurant;
	private RyanWaiterRole waiter;
	private RyanCashierRole cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, AtWaitArea, SeatedWaitArea, BeingSeated, Ordering, Seated, Served, GotFood, Eating, Done, Paying, Leaving, Gone};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, tableFull, waitArea, seatedWaitArea, followHost, seated, order, served, doneEating, gotCheck, atCashier, doneLeaving};
	AgentEvent event = AgentEvent.none;
	

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RyanCustomerRole(PersonAgent p, RyanRestaurant r, String name){
		super(p);
		this.name = name;
		_restaurant = r;
		host = r.host;
		patient = true;
		bad = false;
		money = generator.nextInt(20) + 10;
		cmdGotHungry();
		
		if(name.equalsIgnoreCase("poor")){
			money = 0;
		}
		if(name.equalsIgnoreCase("cheap")){
			money = 6;
		}
		if(name.equalsIgnoreCase("rich")){
			money = 1000000000;
		}
		if(name.equalsIgnoreCase("imp")){
			patient = false;
		}
		if(name.equalsIgnoreCase("bad")){
			bad = true;
		}
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(RyanHostRole host) {
		this.host = host;
	}
	
	public void setCashier(RyanCashierRole cashier){
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	// Messages	
	@Override
	public void cmdGotHungry() {
		// TODO Auto-generated method stub
		print(AlertTag.RYAN_RESTAURANT,"I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgTablesAreFull(){
		print(AlertTag.RYAN_RESTAURANT,"Leaving");
		event = AgentEvent.tableFull;
	}
	
	public void msgSitAtWaitArea(int x, int y){
		xSeat = x;
		ySeat = y;
		event = AgentEvent.waitArea;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToWaitArea(){
		event = AgentEvent.seatedWaitArea;
		stateChanged();
	}

	public void msgFollowMe(int tablenumber, RyanWaiterRole waiter, List<Menu> wMenu) { //include menu later
		this.waiter = waiter;
		tableNumber = tablenumber;
		cMenu = wMenu;
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		print(AlertTag.RYAN_RESTAURANT,"At table " + tableNumber);
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatsYourOrder(){
		print(AlertTag.RYAN_RESTAURANT,"I am ordering");
		event = AgentEvent.order;
		stateChanged();
	}
	
	public void msgPleaseReorder(List<Menu> menu){
		cMenu = menu;
		customerGui.setState(0);
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgHeresYourFood(){
		print(AlertTag.RYAN_RESTAURANT,"Got food from " + waiter.getName());
		event = AgentEvent.served;
		stateChanged();
	}
	
	public void msgHeresYourCheck(double payment){
		print(AlertTag.RYAN_RESTAURANT,"Thank you, " + waiter.getName());
		this.payment = payment;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier(){
		print(AlertTag.RYAN_RESTAURANT,"At cashier");
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void msgPaymentDone(double payment){
		print(AlertTag.RYAN_RESTAURANT,"Done paying and leaving");
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		state = AgentState.DoingNothing;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.tableFull ){
			state = AgentState.AtWaitArea;
			LeaveRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.waitArea ){
			state = AgentState.AtWaitArea;
			SitDownAtWaitArea();
			return true;
		}
		if (state == AgentState.AtWaitArea && event == AgentEvent.seatedWaitArea ){
			state = AgentState.SeatedWaitArea;
			AtWaitArea();
			return true;
		}
		if (state == AgentState.SeatedWaitArea && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if(state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Ordering;
			Choose();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.order){
			state = AgentState.Served;
			Order();
			return true;
		}
		if (state == AgentState.Served && event == AgentEvent.served){
			state = AgentState.GotFood;
			EatFood();
			return true;
		}
		if (state == AgentState.GotFood && event == AgentEvent.gotCheck){
			state = AgentState.Done;
			PayOrRun();
			return true;
		}
		if (state == AgentState.Done && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			PayCashier();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.doneLeaving){
			state = AgentState.Gone;
			leaveTable();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneLeaving){
			state = AgentState.Leaving;
			//no action
			return true;
		}
		return false;
	}

	// Actions
	private void goToRestaurant() {
		host = _restaurant.host;
		print(AlertTag.RYAN_RESTAURANT,"Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
	
	private void LeaveRestaurant(){
		host.msgGone(this);
		customerGui.setState(0);
		customerGui.DoExitRestaurant();
	}
	
	private void SitDownAtWaitArea(){
		customerGui.DoGoToWaitArea(xSeat, ySeat);
	}
	
	private void AtWaitArea(){
		host.msgImHere(this);
	}

	private void SitDown() {
		customerGui.DoGoToSeat(tableNumber);
		print(AlertTag.RYAN_RESTAURANT,"Following Waiter " + waiter.getName() + " to table " + tableNumber);
		//choice = Choose();
		//customerGui.DoGoToSeat(xTable, yTable);//hack; only one table
	}
	
	private void Choose(){
		//add timer;
		print(AlertTag.RYAN_RESTAURANT,"Choosing");
		customerGui.setState(6);
		timer.schedule(new TimerTask() {
			public void run() {
				print(AlertTag.RYAN_RESTAURANT,"Ready to order, " + waiter.getName());
				Chosen();
			}
		},
		(generator.nextInt(2500)+1000));
	}
	
	private void Chosen(){
		/*int value = generator.nextInt(size);
		choice = cMenu.get(value).getOption();
		customerGui.setState(1);
		waiter.msgReadytoOrder(this);*/
		
		boolean chosen = false;
		if(bad) money = 1000;
		while(!chosen){
			if(cMenu.isEmpty()){
				print(AlertTag.RYAN_RESTAURANT,"Too poor");
				leaveTable();
				chosen = true;
			}
			else if(!cMenu.isEmpty()){
				int size = cMenu.size();
				int random = generator.nextInt(size);
				String temp = cMenu.get(random).getOption();
				if(Menu.getPrice(temp) < money){
					choice = cMenu.get(random).getOption();
					customerGui.setState(1);
					waiter.msgReadytoOrder(this);
					if(bad) money = 0;
					chosen = true;
				}
				else if(Menu.getPrice(temp) > money){
					cMenu.remove(random);
				}
			}
		}
	}
	
	private void Order(){
		print(AlertTag.RYAN_RESTAURANT,"I'll order " + choice);
		customerGui.setChoice(choice);
		customerGui.setState(2);
		waiter.msgMyOrder(choice, this);
	}

	private void EatFood() {
		print(AlertTag.RYAN_RESTAURANT,"Eating Food " + choice);
		customerGui.setState(3);
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				//print(AlertTag.RYAN_RESTAURANT,"Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				print(AlertTag.RYAN_RESTAURANT,"Finshed Eating");
				sendDoneEatingMessage();
				//isHungry = false;
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void sendDoneEatingMessage(){
		customerGui.setState(0);
		waiter.msgDoneEating(this);
		//msgLeavingRestaurant();
	}
	
	private void PayOrRun(){ // Add run function here
		if(money > payment){
			print(AlertTag.RYAN_RESTAURANT,"Going to cashier");
			customerGui.setState(4);
			customerGui.DoGoToCashier();
		}
		else if(money < payment){
			print(AlertTag.RYAN_RESTAURANT,"Running Away");
			leaveTable();
		}
	}
	
	private void PayCashier(){
		print(AlertTag.RYAN_RESTAURANT,"Paying Cashier");
		cashier = (RyanCashierRole)_restaurant.getCashier();
		money -= payment;
		_person.cmdChangeMoney(-payment);
		cashier.msgHeresMoney(this, payment);
		payment = 0;
	}

	private void leaveTable() {
		if(bad){
			customerGui.setState(7);
			waiter.msgLeaving(this);
		}
		else if(!bad){
			print(AlertTag.RYAN_RESTAURANT,"Leaving.");
			waiter.msgLeaving(this);
			customerGui.setState(5);	
		}
		customerGui.DoExitRestaurant();
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
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public void setGui(RyanCustomerGui g) {
		customerGui = g;
	}

	public RyanCustomerGui getGui() {
		return customerGui;
	}

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return _restaurant;
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
	
}