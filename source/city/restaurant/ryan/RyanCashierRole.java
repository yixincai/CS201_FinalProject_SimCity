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
import city.bank.Bank;
import city.market.Market;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.ryan.gui.RyanCashierGui;
import agent.Agent;

public class RyanCashierRole extends RestaurantCashierRole {
	String name;
	RyanCashierGui gui;
	RyanRestaurant _restaurant;
	Bank bank;
	public List<Receipt> receipts = new ArrayList<Receipt>();
	public List<Bill> bills = new ArrayList<Bill>();
	public double register, bankBalance, bankDebt;
	double loans;
	Menu menu;
	public RyanCookRole cook;
	
	enum ReceiptState{Calculate, Ready, Paying, Done}
	enum BillState{Received, Paying, AskforLoan, Loaned, Paid};
	
	public RyanCashierRole(PersonAgent p, RyanRestaurant r){
		super(p);
		this.name = name;
		menu = new Menu();
		
		_restaurant = r;
		register = 130.0;
		bankBalance = 0;
		bankDebt = 0;
	}
	

	public void setGui(RyanCashierGui gui){
		this.gui = gui;
	}
	
	public void setBank(Bank bank){
		this.bank = bank;
	}
	
	//Messages******************************************************************************************************************************************************************
	public void msgMakeReceipt(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
		receipts.add(new Receipt(waiter, customer, choice));
		stateChanged();
	}
	
	public void msgHeresMoney(RyanCustomerRole customer, double payment){
		print("Receiving Payment");
		for(Receipt temp: receipts){
			if(temp.customer == customer){
				temp.payment = payment;
				temp.state = ReceiptState.Paying;
				stateChanged();
			}
		}
	}
	
	public void msgHeresBill(Market market, String choice, double price){
		print("Received bill for " + choice + " of amount " + price + " from " + market.getName());
		bills.add(new Bill(market, choice, price));
		stateChanged();
	}
	
	public void msgHereIsLoan(double amount){
		print("Received loan of " + amount + " from the bank");
		register += amount;
		loans += amount;
		Bill bill = searchLoan(amount);
		bill.bState = BillState.Loaned;
		stateChanged();
	}
	
	//Scheduler***************************************************************************************************************************************
	public boolean pickAndExecuteAnAction() {
		synchronized(receipts){
			for(Receipt temp : receipts){
				if(temp.state == ReceiptState.Calculate){
					calculatePayment(temp);
					return true;
				}
				if(temp.state == ReceiptState.Paying){
					receivePayment(temp);
					return true;
				}
			}
		}
		synchronized(bills){
			for(Bill temp: bills){
				if(temp.bState == BillState.Loaned){
					payWithLoan(temp);
					return true;
				}
				if(temp.bState == BillState.Received){
					startPaying(temp);
					return true;
				}
			}
			if(bills.isEmpty()){
				if(register >= loans && loans != 0){
					PayBackLoans();
					return true;
				}
			}
		}
		return false;
	}
	
	//Actions*********************************************************************************************************************************************************************************************
	public void calculatePayment(Receipt receipt){
		print("Calculating receipt for " + receipt.customer.getName());
		receipt.state = ReceiptState.Ready;
		receipt.amount = menu.getPrice(receipt.choice);
		receipt.waiter.msgHeresReceipt(receipt.customer, receipt.amount);
	}
	
	public void receivePayment(Receipt receipt){
		if(receipt.amount == receipt.payment){
			register += receipt.amount;
			print("Payment for " + receipt.customer.getName() + " is completed");
			print("Register is at " + register);
			receipt.customer.msgPaymentDone(receipt.amount);
			receipts.remove(receipt);
		}
	}
	
	public void startPaying(Bill bill){
		synchronized(bills){
			print("register has " + register);
			bill.bState = BillState.Paying;
			if(bill.cost <= register){
				bill.bState = BillState.Paid;
				register -= bill.cost;
				print("Paying bill for " + bill.choice + " of amount " + bill.cost + " to " + bill.market.getName());
				bill.market.msgHeresPayment(bill.choice, bill.cost);
				bills.remove(bill);
			}
			else if(bill.cost > register){
				print("Not enough money to pay for " + bill.choice + " asking for loan");
				bill.bState = BillState.AskforLoan;
				bank.msgAskForLoan(this, bill.cost);
			}
		}
	}
	
	public void payWithLoan(Bill bill){
		synchronized(bills){
			print("register has " + register);
			bill.bState = BillState.Paying;
			if(bill.cost <= register){
				bill.bState = BillState.Paid;
				register -= bill.cost;
				print("Paying bill for " + bill.choice + " of amount " + bill.cost + " to " + bill.market.getName() + " with loan from Bank");
				bill.market.msgHeresPayment(bill.choice, bill.cost);
				bills.remove(bill);
			}
		}
	}
	
	public void PayBackLoans(){
		register -= loans;
		print("Register now has " + register);
		bank.msgPayBackForLoan(this, loans);
		loans = 0;
	}
	
	//Utility******************************************************************************************************************************************************************
	public String getName(){
		return name;
	}
	
	public void addCash(double money){
		register += money;
		print("Register now at " + register);
		stateChanged();
	}
	
	public double getRegister(){
		return register;
	}
	
	public double getLoan(){
		return loans;
	}
	
	Bill searchLoan(double amount){
		synchronized(bills){
			for(Bill temp: bills){
				if(temp.cost == amount){
					return temp;
				}
			}
		}
		print("Bill search returned null");
		return null;
	}
	
	public class Receipt{
		RyanCustomerRole customer;
		RyanWaiterRole waiter;
		String choice;
		double amount;
		double payment;
		ReceiptState state;
		int table; 
		
		Receipt(RyanWaiterRole waiter, RyanCustomerRole customer, String choice){
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
			state = ReceiptState.Calculate;
		}
		
		public RyanCustomerRole getCustomer(){
			return customer;
		}
		
		public RyanWaiterRole getWaiter(){
			return waiter;
		}
		
		public String getChoice(){
			return choice;
		}
		
		public double getAmount(){
			return amount;
		}
		
		public double getPayment(){
			return payment;
		}
		
	}
	
	public class Bill{
		Market market;
		String choice;
		double cost;
		BillState bState = BillState.Received; 
		
		Bill(Market market, String choice, double cost){
			this.market = market;
			this.choice = choice;
			this.cost = cost;
		}
		
		public double getCost(){
			return cost;
		}
		
		public String getChoice(){
			return choice;
		}
	}

	@Override
	public void msgHereIsTheBill(Market m, double bill,
			Map<String, Double> price_list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheChange(Market m, double change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionComplete(double amount, Double balance,
			Double debt, int newAccountNumber) {
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
