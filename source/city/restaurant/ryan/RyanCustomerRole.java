package city.restaurant.ryan;

import restaurant.gui.CustomerGui;
import restaurant.gui.Gui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.*;
import agent.Agent;
import restaurant.Menu;

import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import city.Place;
import city.restaurant.RestaurantCustomerRole;

/**
 * Restaurant customer agent.
 */
public class RyanCustomerRole extends RestaurantCustomerRole {
	private String name;
	private int hungerLevel = 100;        // determines length of meal
	private int money;
	private double payment;
	Timer timer = new Timer();
	private CustomerGui customerGui;
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
	
	private Waiter waiter;
	private Cashier cashier;

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
	public RyanCustomerRole(String name, RyanCashierRole cashier){
		super();
		this.name = name;
		this.cashier = cashier;
		patient = true;
		bad = false;
		money = generator.nextInt(20) + 10;
		
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
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgTablesAreFull(){
		Do("Leaving");
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

	public void msgFollowMe(int tablenumber, Waiter waiter, List<Menu> wMenu) { //include menu later
		this.waiter = waiter;
		tableNumber = tablenumber;
		cMenu = wMenu;
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		Do("At table " + tableNumber);
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatsYourOrder(){
		Do("I am ordering");
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
		Do("Got food from " + waiter.getName());
		event = AgentEvent.served;
		stateChanged();
	}
	
	public void msgHeresYourCheck(double payment){
		Do("Thank you, " + waiter.getName());
		this.payment = payment;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier(){
		Do("At cashier");
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void msgPaymentDone(double payment){
		Do("Done paying and leaving");
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
	protected boolean pickAndExecuteAnAction() {
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
		Do("Going to restaurant");
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
		print("Following Waiter " + waiter.getName() + " to table " + tableNumber);
		//choice = Choose();
		//customerGui.DoGoToSeat(xTable, yTable);//hack; only one table
	}
	
	private void Choose(){
		//add timer;
		Do("Choosing");
		customerGui.setState(6);
		timer.schedule(new TimerTask() {
			public void run() {
				Do("Ready to order, " + waiter.getName());
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
				Do("Too poor");
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
		Do("I'll order " + choice);
		customerGui.setChoice(choice);
		customerGui.setState(2);
		waiter.msgMyOrder(choice, this);
	}

	private void EatFood() {
		Do("Eating Food " + choice);
		customerGui.setState(3);
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				//print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				Do("Finshed Eating");
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
			Do("Going to cashier");
			customerGui.setState(4);
			customerGui.DoGoToCashier();
		}
		else if(money < payment){
			Do("Running Away");
			leaveTable();
		}
	}
	
	private void PayCashier(){
		Do("Paying Cashier");
		money -= payment;
		cashier.msgHeresMoney(this, payment);
		payment = 0;
	}

	private void leaveTable() {
		if(bad){
			customerGui.setState(7);
			waiter.msgLeaving(this);
		}
		else if(!bad){
			Do("Leaving.");
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

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	public void cmdGotHungry() {
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