package city.restaurant.ryan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import utilities.LoggedEvent;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.bank.Bank;
import city.market.Item;
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
	Menu menu;
	public RyanCookRole cook;
	
	enum ReceiptState{Calculate, Ready, Paying, Done}
	//enum BillState{Received, Paying, AskforLoan, Loaned, Paid};
	
	enum MoneyState{OrderedFromBank, none}
	MoneyState moneyState = MoneyState.none;
	
	enum RoleState{WantToLeave,none}
	RoleState roleState = RoleState.none;
	
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
	
	@Override
	public void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list){
		print("Market bill received with amount of " + bill);
		log.add(new LoggedEvent("Received HereIsTheBill from market. Bill = "+ bill));
		bills.add(new Bill(m, bill, price_list));
		stateChanged();
	}

	public void msgHereIsTheChange(Market m, double change){
		print("Market change received with amount of " + change);
		register += change;
		for (Bill bill : bills){
			if (bill.market == m)
				bill.state = Bill.BillState.changeReceived;
		}
		stateChanged();
	}

	public void msgHereIsTheInvoice(Market m, List<Item> invoice) {
		for (Bill bill : bills){
			if (bill.market == m){
				bill.state = Bill.BillState.invoiceReceived;
				bill.invoice = invoice;
			}
		}
		stateChanged();		
	}
	
	public void msgTransactionComplete(double amount, Double balance, Double debt, int newAccountNumber){
		_restaurant.updateAccountNumber(newAccountNumber);
		moneyState = MoneyState.none;
		register = amount;
		bankBalance = balance;
		bankDebt = debt;
	}
	
//	public void msgHeresBill(Market market, String choice, double price){
//		print("Received bill for " + choice + " of amount " + price + " from " + market.getName());
//		bills.add(new Bill(market, choice, price));
//		stateChanged();
//	}
//	
//	public void msgHereIsLoan(double amount){
//		print("Received loan of " + amount + " from the bank");
//		register += amount;
//		loans += amount;
//		Bill bill = searchLoan(amount);
//		bill.bState = BillState.Loaned;
//		stateChanged();
//	}
	
	//Scheduler***************************************************************************************************************************************
	public boolean pickAndExecuteAnAction() {
		try{
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
			
			if (register > 0){
				for (Bill bill : bills){
					if (bill.state == Bill.BillState.invoiceReceived){
						payMarketBill(bills.get(0));
						return true;
					}
				}
			}
			for (Bill bill : bills){
				if (bill.state == Bill.BillState.changeReceived){
					cook.msgOrderFinished();
					bills.remove(bill);
					return true;
				}
			}
			
			if (moneyState == MoneyState.none){
				if (register > 200 && bankDebt > 0){
					PayLoan();
					return true;
				}
				else if (register > 200){
					Deposit();
					return true;
				}
				else if (register < 0 && bankBalance > 0){
					Withdraw();
					return true;
				}
				else if (register < 0 && bankBalance <= 0){
					AskForLoan();
					return true;
				}
			}
			
			if (bills.size() == 0 && receipts.size() == 0 && roleState == RoleState.WantToLeave){
				LeaveRestaurant();
				roleState = RoleState.none;
				active = false;
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			return false;
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
	
	//Market
	private void payMarketBill(Bill bill){
		double amount = 0;
		for (Item item : bill.invoice)
			amount += (item.amount * bill.priceList.get(item.name));
		if (Math.abs(bill.cost - amount) > 0.02)
			print("Incorrect bill calculation by market");
		else 
			print("Correct bill calculation by market. Paying Market Bill");
		if (register >= bill.cost){
			register -= bill.cost;
			print("Remaining money is " + register);
			bill.market.MarketCashier.msgHereIsPayment(_restaurant, bill.cost);
			bill.state = Bill.BillState.none;
		}
		else {
			bills.get(0).cost -= register;
			bill.market.MarketCashier.msgHereIsPayment(_restaurant, register);
			register = 0;
			print("Do not have enough money with " + bill.cost +" debt");
		}
	}
	
	//Bank
	private void PayLoan(){
		double amount = Math.min(register-150, bankDebt);
		Directory.banks().get(0).tellers.get(0).msgWiredTransaction(_restaurant, _restaurant.getAccountNumber(), amount, "Pay Loan");
		moneyState = MoneyState.OrderedFromBank;
	}
	
	private void Withdraw(){
		double amount = Math.min(50-register, bankBalance);
		Directory.banks().get(0).tellers.get(0).msgWiredTransaction(_restaurant, _restaurant.getAccountNumber(), amount, "Withdraw");
		moneyState = MoneyState.OrderedFromBank;		
	}
	
	private void AskForLoan(){
		Directory.banks().get(0).tellers.get(0).msgWiredTransaction(_restaurant, _restaurant.getAccountNumber(), 50-register, "Withdraw Loan");
		moneyState = MoneyState.OrderedFromBank;
	}
	
	private void Deposit(){
		Directory.banks().get(0).tellers.get(0).msgWiredTransaction(_restaurant, _restaurant.getAccountNumber(), register/2, "Deposit");
		moneyState = MoneyState.OrderedFromBank;
	}
	
	
	public void LeaveRestaurant(){
		gui.LeaveRestaurant();
	}
	
//	public void startPaying(Bill bill){
//		synchronized(bills){
//			print("register has " + register);
//			bill.bState = BillState.Paying;
//			if(bill.cost <= register){
//				bill.bState = BillState.Paid;
//				register -= bill.cost;
//				print("Paying bill for " + bill.choice + " of amount " + bill.cost + " to " + bill.market.getName());
//				bill.market.msgHeresPayment(bill.choice, bill.cost);
//				bills.remove(bill);
//			}
//			else if(bill.cost > register){
//				print("Not enough money to pay for " + bill.choice + " asking for loan");
//				bill.bState = BillState.AskforLoan;
//				bank.msgAskForLoan(this, bill.cost);
//			}
//		}
//	}
//	
//	public void payWithLoan(Bill bill){
//		synchronized(bills){
//			print("register has " + register);
//			bill.bState = BillState.Paying;
//			if(bill.cost <= register){
//				bill.bState = BillState.Paid;
//				register -= bill.cost;
//				print("Paying bill for " + bill.choice + " of amount " + bill.cost + " to " + bill.market.getName() + " with loan from Bank");
//				bill.market.msgHeresPayment(bill.choice, bill.cost);
//				bills.remove(bill);
//			}
//		}
//	}
//	
//	public void PayBackLoans(){
//		register -= loans;
//		print("Register now has " + register);
//		bank.msgPayBackForLoan(this, loans);
//		loans = 0;
//	}
	
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

	@Override
	public Place place() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cmdFinishAndLeave() {
		roleState = RoleState.WantToLeave;
		stateChanged();
	}
	
	public static class Bill{
		Market market;
		String choice;
		double cost;
		enum BillState{none, invoiceReceived, changeReceived}
		public BillState state;
		public Map<String, Double> priceList;
		public List<Item> invoice;
		
		Bill(Market market, double cost, Map<String, Double> priceList){
			this.market = market;
			this.cost = cost;
			this.priceList = priceList;
			state = BillState.none;
		}
		
		public double getCost(){
			return cost;
		}
		
		public String getChoice(){
			return choice;
		}
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
}
