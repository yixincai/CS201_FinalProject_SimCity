package city.restaurant.yixin;

import gui.trace.AlertTag;

import java.util.*;

import utilities.LoggedEvent;
import city.*;
import city.market.*;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.yixin.gui.YixinCashierGui;

public class YixinCashierRole extends RestaurantCashierRole{// implements Cashier{
	public YixinRestaurant restaurant;
	public YixinCookRole cook;

	private String name = "Cashier";
	public List<CustomerBill> bills = Collections.synchronizedList(new ArrayList<CustomerBill>());
	public List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public static Menu menu = new Menu();
	public YixinCashierGui cashierGui = null;
	public double money, bankBalance, bankDebt;
	enum MoneyState{OrderedFromBank, none}
	MoneyState money_state = MoneyState.none;
	enum RoleState{WantToLeave,none}
	RoleState role_state = RoleState.none;

	public YixinCashierRole(PersonAgent p, YixinRestaurant r) {
		super(p);
		this.restaurant = r;
		money = 10000.0;
		bankBalance = 0;
		bankDebt = 0;
		
	}

	public void setGui(YixinCashierGui g) {
		cashierGui = g;
	}

	public String getName() {
		return name;
	}

	// Messages
	public void msgComputeBill(YixinWaiterRole w, YixinCustomerRole c, String choice) {
		print(AlertTag.YIXIN_RESTAURANT, "Bill Request received");
		log.add(new LoggedEvent("Received ComputeBill from waiter. Choice = "+ choice));
		bills.add(new CustomerBill(w,c,choice));
		stateChanged();
	}

	public void msgHereIsThePayment(YixinCustomerRole c, double check, double cash) {
		print(AlertTag.YIXIN_RESTAURANT,"Payment received");
		log.add(new LoggedEvent("Received HereIsTheCheck from customer. Check = "+ check + " Payment = "+ cash));
		for (CustomerBill bill : bills)
			if (bill.customer == c){
				bill.cash = cash;
				bill.price  = check;
				bill.state = CustomerBill.BillState.ReturnedFromCustomer;
				stateChanged();
			}
	}

	public void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list){
		print(AlertTag.YIXIN_RESTAURANT,"Market bill received with amount of " + bill);
		log.add(new LoggedEvent("Received HereIsTheBill from market. Bill = "+ bill));
		marketBills.add(new MarketBill(m, bill, price_list));
		stateChanged();
	}

	public void msgHereIsTheChange(Market m, double change){
		print(AlertTag.YIXIN_RESTAURANT,"Market change received with amount of " + change);
		money += change;
		for (MarketBill bill : marketBills){
			if (bill.market == m)
				bill.state = MarketBill.BillState.changeReceived;
		}
		stateChanged();
	}

	public void msgHereIsTheInvoice(Market m, List<Item> invoice) {
		for (MarketBill bill : marketBills){
			if (bill.market == m){
				bill.state = MarketBill.BillState.invoiceReceived;
				bill.invoice = invoice;
			}
		}
		stateChanged();		
	}
	
	public void msgTransactionComplete(double amount, Double balance, Double debt, int newAccountNumber){
		restaurant.updateAccountNumber(newAccountNumber);
		money_state = MoneyState.none;
		money -= amount;
		bankBalance = balance;
		bankDebt = debt;
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
					if (bill.state == MarketBill.BillState.invoiceReceived){
						payMarketBill(marketBills.get(0));
						return true;
					}
			}
			for (MarketBill bill : marketBills)
				if (bill.state == MarketBill.BillState.changeReceived){
					((YixinCookRole)restaurant.getCook()).msgOrderFinished();
					marketBills.remove(bill);
					return true;
				}
			if (money_state == MoneyState.none){
				if (money > 5000 && bankDebt > 0){
					PayLoan();
					return true;
				}
				else if (money > 5000){
					Deposit();
					return true;
				}
				else if (money < 0 && bankBalance > 0){
					Withdraw();
					return true;
				}
				else if (money < 0 && bankBalance <= 0){
					AskForLoan();
					return true;
				}
			}
			if (marketBills.size() == 0 && bills.size() == 0 && role_state == RoleState.WantToLeave && restaurant.getNumberOfCustomers() == 0){
				//LeaveRestaurant();
				role_state = RoleState.none;
				active = false;
				return true;
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
	//Bank
	private void PayLoan(){
		double amount = Math.min(money-150, bankDebt);
		Directory.banks().get(0)._tellers.get(0).msgWiredTransaction(restaurant, restaurant.getAccountNumber(), amount, "Pay Loan");
		money_state = MoneyState.OrderedFromBank;
	}
	
	private void Withdraw(){
		double amount = Math.min(50-money, bankBalance);
		Directory.banks().get(0)._tellers.get(0).msgWiredTransaction(restaurant, restaurant.getAccountNumber(), amount, "Withdraw");
		money_state = MoneyState.OrderedFromBank;		
	}
	
	private void AskForLoan(){
		Directory.banks().get(0)._tellers.get(0).msgWiredTransaction(restaurant, restaurant.getAccountNumber(), 50-money, "Withdraw Loan");
		money_state = MoneyState.OrderedFromBank;
	}
	
	private void Deposit(){
		Directory.banks().get(0)._tellers.get(0).msgWiredTransaction(restaurant, restaurant.getAccountNumber(), money-1000, "Deposit");
		money_state = MoneyState.OrderedFromBank;
	}
	
	//Customer Bill
	private void computeBill(CustomerBill bill) {
		print(AlertTag.YIXIN_RESTAURANT,"The Bills is computed.");
		bill.state = CustomerBill.BillState.None;
		bill.waiter.msgHereIsTheCheck(bill.price, bill.customer);
	}

	private void makeChange(CustomerBill bill) {
		if(bill.cash - bill.price < 0){
			print(AlertTag.YIXIN_RESTAURANT,"Customer DO NOT HAVE ENOUGH MONEY.");
			bill.customer.msgYouDoNotHaveEnoughMoney(bill.price - bill.cash);
			money += bill.cash;
			print(AlertTag.YIXIN_RESTAURANT,"Remaining money is " + money);
			return;
		}
		print(AlertTag.YIXIN_RESTAURANT,"Giving change to customer");
		money += bill.price;
		print(AlertTag.YIXIN_RESTAURANT,"Remaining money is " + money);
		bill.customer.msgHereIsTheChange(bill.cash - bill.price);
	}

	private void payMarketBill(MarketBill bill){
		double amount = 0;
		for (Item item : bill.invoice)
			amount += (item.amount * bill.price_list.get(item.name));
		if (Math.abs(bill.balance - amount) > 0.02)
			print(AlertTag.YIXIN_RESTAURANT,"Incorrect bill calculation by market");
		else 
			print(AlertTag.YIXIN_RESTAURANT,"Correct bill calculation by market. Paying Market Bill");
		if (money >= bill.balance){
			money -= bill.balance;
			print(AlertTag.YIXIN_RESTAURANT,"Remaining money is " + money);
			bill.market.MarketCashier.msgHereIsPayment(restaurant, bill.balance);
			bill.state = MarketBill.BillState.none;
		}
		else {
			marketBills.get(0).balance -= money;
			bill.market.MarketCashier.msgHereIsPayment(restaurant, money);
			money = 0;
			print(AlertTag.YIXIN_RESTAURANT,"Do not have enough money with " + bill.balance +" debt");
		}
	}
	
	public void LeaveRestaurant(){
		cashierGui.LeaveRestaurant();
		/*try{
			atDestination.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}*/
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
		enum BillState{none, invoiceReceived, changeReceived}
		public BillState state;
		public Map<String, Double> price_list;
		public List<Item> invoice;
		MarketBill(Market market, double money, Map<String, Double> price_list){
			this.balance = money;
			this.market = market;
			this.state = BillState.none;
			this.price_list = price_list;
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
}