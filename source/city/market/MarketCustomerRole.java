package city.market;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.market.gui.*;
import city.market.interfaces.MarketCustomer;
import agent.Role;

public class MarketCustomerRole extends Role implements MarketCustomer{
	public MarketCustomerGui gui;
	
	Market market;
	List<Item> order = new ArrayList<Item>();
	List<Item> orderFulfillment;
	Map<String, Double> price_list;
	
	double money = 500, payment, debt;
	public enum CustomerState {wantToBuy, needPay, pickUpItems, payNextTime, none}
	public CustomerState state = CustomerState.none;

	private Semaphore atDestination = new Semaphore(0,true);
	
	public MarketCustomerRole(PersonAgent person, Market m){
		super(person);
		this.market = m;
		cmdBuyFood(3);
	}

	public void setGui(MarketCustomerGui g) {
		gui = g;
	}
	
	public void msgAnimationFinished() {
		//from animation
		atDestination.release();
		stateChanged();
	}
	
	//command from person
	public void cmdBuyFood(int meals){
		order.add(new Item("Meal", meals));
		state = CustomerState.wantToBuy;
		stateChanged();
	}
	
	public void cmdBuyCar(){
		order.add(new Item("Car", 1));
		state = CustomerState.wantToBuy;
		stateChanged();
	}
	
	public void cmdFinishAndLeave() {
	}
	
	//message
	public void msgHereIsBill(double payment, Map<String, Double> price_list, List<Item> orderFulfillment){
		this.payment = payment;
		this.price_list = price_list;
		this.orderFulfillment = orderFulfillment;
		state = CustomerState.needPay;
		stateChanged();
	}
	
	public void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change){
		print("Received change from cashier.");
		this.orderFulfillment = orderFulfillment;
		money += change;
		state = CustomerState.pickUpItems;
		stateChanged();
	}
	
	public void msgHereIsGoodAndDebt(List<Item> orderFulfillment, double debt){
		this.orderFulfillment = orderFulfillment;
		this.debt = debt;
		state = CustomerState.payNextTime;
		stateChanged();
	}

	public boolean pickAndExecuteAnAction(){
		if(state == CustomerState.wantToBuy){
			buyFromMarket();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.needPay){
			payMarket();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.pickUpItems){
			pickUpItems();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.payNextTime && money>0){
			payBackMarket();
			state = CustomerState.none;
			return true;
		}
		return false;
	}
	
	public void payMarket(){
		/*double bill = 0;
		for (Item item : orderFulfillment)
			bill += item.amount * price_list.get(item.name);
		if (Math.abs(bill - payment) > 0.02)
			System.out.println("Incorrect bill calculation by market");
		*/
		print("Pay Market.");
		DoGoToCashier();
		//DoGoToWaitingArea();
		market.MarketCashier.msgPay(this, money);
		money = 0;
	}
	
	public void buyFromMarket(){
		DoGoToCashier();
		market.MarketCashier.msgPlaceOrder(this, order);
		DoGoToWaitingArea();
	}
	
	public void payBackMarket(){
		DoGoToCashier();
		market.MarketCashier.msgPay(this, money);
		DoGoToWaitingArea();
		money = 0;
	}
	
	public void pickUpItems(){
		print("Go pick up items.");
		DoGoToCashier();
		DoLeaveMarket();
		active = false;
		cmdBuyFood(3);
	}
	
	public void DoGoToCashier(){
		gui.GoToCashier();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void DoGoToWaitingArea(){
		gui.GoToWaitingArea();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoLeaveMarket(){
		gui.LeaveMarket();
		try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Place place() {
		return market;
	}
}
