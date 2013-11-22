package city.restaurant.yixin;

import java.util.*;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.*;
import city.market.*;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.yixin.gui.YixinCashierGui;

public class YixinCashierRole extends RestaurantCashierRole{// implements Cashier{
	public YixinRestaurant restaurant;
	public YixinCookRole cook;

	public EventLog log = new EventLog();
    private String name = "Cashier";
	public List<CustomerBill> bills = Collections.synchronizedList(new ArrayList<CustomerBill>());
	public List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public static Menu menu = new Menu();
	public YixinCashierGui cashierGui = null;
	public double money;
	
	public YixinCashierRole(PersonAgent p, YixinRestaurant r) {
		super(p);
		this.restaurant = r;
		money = 130.0;
	}
	
	public void setGui(YixinCashierGui g) {
		cashierGui = g;
	}
	
	public String getName() {
		return name;
	}
	
	// Messages
	public void msgComputeBill(YixinWaiterRole w, YixinCustomerRole c, String choice) {
		log.add(new LoggedEvent("Received ComputeBill from waiter. Choice = "+ choice));
		print("Bill Request received");
		bills.add(new CustomerBill(w,c,choice));
		stateChanged();
	}
	
	public void msgHereIsThePayment(YixinCustomerRole c, double check, double cash) {
		log.add(new LoggedEvent("Received HereIsTheCheck from customer. Check = "+ check + " Payment = "+ cash));
		print("Payment received");
		for (CustomerBill bill : bills)
			if (bill.customer == c){
				bill.cash = cash;
				bill.price  = check;
				bill.state = CustomerBill.BillState.ReturnedFromCustomer;
				stateChanged();
			}
	}
	
	public void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list){
		log.add(new LoggedEvent("Received HereIsTheBill from market. Bill = "+ bill));
		print("Market bill received with amount of " + bill);
		marketBills.add(new MarketBill(m, bill, price_list));
		stateChanged();
	}
	
	public void msgHereIsTheChange(Market m, double change){
		log.add(new LoggedEvent("Received HereIsTheChange from market. Bill = "+ change));
		print("Market change received with amount of " + change);
		money += change;
		cook.msgOrderFinished();
		stateChanged();
	}
	
	public void msgHereIsTheInvoice(Market m, List<Item> invoice) {
		for (MarketBill bill : marketBills){
			if (bill.market == m)
				bill.invoice_received = true;
		}
		stateChanged();		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			for (CustomerBill bill : bills){
				if (bill.state == CustomerBill.BillState.NotComputed){
					computeBill(bill);
					return true;
				}
			}
			for (CustomerBill bill : bills){
				if (bill.state == CustomerBill.BillState.ReturnedFromCustomer){
					makeChange(bill);
					bills.remove(bill);
					return true;
				}
			}
			if (money > 0){
				for (MarketBill bill : marketBills)
					if (bill.invoice_received){
						payMarketBill(marketBills.get(0));
						return true;
					}
			}
		}
		catch(ConcurrentModificationException e){
			return false;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void computeBill(CustomerBill bill) {
		print("The Bills is computed.");
		bill.state = CustomerBill.BillState.None;
		bill.waiter.msgHereIsTheCheck(bill.price, bill.customer);
	}

	private void makeChange(CustomerBill bill) {
		if(bill.cash - bill.price < 0){
			print("Customer DO NOT HAVE ENOUGH MONEY.");
			bill.customer.msgYouDoNotHaveEnoughMoney(bill.price - bill.cash);
			money += bill.cash;
			print("Remaining money is " + money);
			return;
		}
		print("Giving change to customer");
		money += bill.price;
		print("Remaining money is " + money);
		bill.customer.msgHereIsTheChange(bill.cash - bill.price);
	}
	
	private void payMarketBill(MarketBill bill){
		print("Paying Market Bill");
		if (money >= bill.balance){
			money -= bill.balance;
			print("Remaining money is " + money);
			bill.market.MarketCashier.msgHereIsPayment(restaurant, bill.balance);
			marketBills.remove(0);
		}
		else {
			marketBills.get(0).balance -= money;
			bill.market.MarketCashier.msgHereIsPayment(restaurant, money);
			money = 0;
			print("Do not have enough money with " + bill.balance +" debt");
		}
	}

	//utilities

	public static class CustomerBill {
		public YixinWaiterRole waiter;
		public YixinCustomerRole customer;
		public String choice;
		public double price;
		public double cash;
		public double change;
		public enum BillState
		{None, NotComputed, ReturnedFromCustomer};
		public BillState state = BillState.None;
		
		CustomerBill(YixinWaiterRole waiter, YixinCustomerRole customer, String choice){
			this.choice = choice;
			this.waiter = waiter;
			this.customer = customer;
			this.price = menu.getPrice(this.choice);
			state = BillState.NotComputed;
		}
	}
	
	public static class MarketBill {
		public double balance;
		public Market market;
		public boolean invoice_received;
		public Map<String, Double> price_list;
		MarketBill(Market market, double money, Map<String, Double> price_list){
			this.balance = money;
			this.market = market;
			invoice_received = false;
			this.price_list = price_list;
		}
	}

	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		
	}
}