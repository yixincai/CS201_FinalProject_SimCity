package city.bank;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.Place;
import city.bank.gui.BankTellerRoleGui;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankHost;
import city.bank.interfaces.BankTeller;
import city.market.Market;
import city.restaurant.Restaurant;

public class BankTellerRole extends Role implements BankTeller {
	
	//Data
	public List<MyCustomer> myCustomers;
	public List<MyBusinessCustomer> myBusinessCustomers;
	BankHost host;
	boolean occupied;
	String name;
	private int tellerNum;
	public static AccountDatabase database = new AccountDatabase();
	
	enum Command{None, Leave};
	Command command;
	Semaphore tellerSem = new Semaphore(0,true);
	
	PersonAgent person;
	
	BankTellerRoleGui gui;
	Bank bank;
	 
	public BankTellerRole(PersonAgent person, Bank bank, int tellerNum){
		super(person);
		this.bank = bank;
		command = Command.None;
		this.myBusinessCustomers = new ArrayList<MyBusinessCustomer>();
		this.myCustomers = new ArrayList<MyCustomer>();		
		this.tellerNum = tellerNum;
	}
	public void makeDatabase(){
		database = new AccountDatabase();
	}
	
	public static class AccountDatabase{
	      public Hashtable<Integer, Double> funds;
	      public Hashtable<Integer, Double> amountOwed;
	      
	      public AccountDatabase(){
	    	  funds = new Hashtable();
	    	  amountOwed = new Hashtable();
	      }
	//given Account Number, gets funds or amount owed
	}
	private class MyCustomer{
		  MyCustomer(BankCustomer c){
			  this.customer = c;
			  customerState = CustomerState.None;
			  accountNumber = -1;
		  }
		  
	      BankCustomer customer;
	      int accountNumber;
	      String request;
	      double amount;
	      
	      CustomerState customerState;
	}
	
	private class MyBusinessCustomer{
		Place place;
		int accountNumber;
		double amount;
		String request;
		
		MyBusinessCustomer(Place place, int accountNumber, double amount, String request){
			this.place = place;
			this.accountNumber = accountNumber;
			this.amount = amount;
			this.request = request;
		}
	}
	
	enum CustomerState { None, Arrived, GivingRequest, GivenRequest, Done};
	
	//Messages
	public void msgIAmHere(BankCustomer c){
		  MyCustomer m = new MyCustomer(c);
		  m.customerState = CustomerState.Arrived;
		  myCustomers.add(m);
		  stateChanged();
	}
	public void msgHereIsMyRequest(BankCustomer c, String request, double amount){
		   print("Customer " + c.toString() + " requested: " + request);
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
		  	m.customerState = CustomerState.GivenRequest;
		  	m.request = request;
		  	m.amount = amount; //
		  	stateChanged();
		  	return;
			}
		  }
	}
	public void msgLeavingBank(BankCustomer c){
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
				m.customerState = CustomerState.None;
				myCustomers.remove(m);
				stateChanged();
				return;
			}
		  }
	}
	
	//FOR CASHIERS OF RESTAURANTS AND CASHIERS OF MARKETS
	public void msgWiredTransaction(Place place, int accountNumber, double amount, String request){
		int newAccntNum;
		System.out.println("Wired Transaction Requested.  Fulfilling Now");
		if(accountNumber == -1){  //means it doesn't exist yet
			   newAccntNum = (int)(Math.random()*20000) + 10000; //open account from 20k to 10k for businesses
			   while(database.funds.containsKey(newAccntNum)){
				   newAccntNum = (int)(Math.random()*20000) + 10000;
			   }
		} else{
				newAccntNum = accountNumber;		//existing account
		}
		
		database.funds.put(newAccntNum, 0.0);
		database.amountOwed.put(newAccntNum, 0.0);
		MyBusinessCustomer businessCustomer = new MyBusinessCustomer(place, newAccntNum, amount, request);
		myBusinessCustomers.add(businessCustomer);
		stateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		for(MyBusinessCustomer mb: myBusinessCustomers){
			actProcessWireRequest(mb);
			return true;
		}
		for(MyCustomer m: myCustomers){
			if(m.customerState == CustomerState.Arrived){
				actAskForARequest(m);
				return true;
			}
		}
		for(MyCustomer m: myCustomers){
			if(m.customerState == CustomerState.GivenRequest){
				actProcessRequest(m);
				return true;
			}
		}
		
		if(myCustomers.isEmpty() && command == Command.Leave && host.isWaitingCustomersEmpty()){
			actLeaveBank();
		}
		return false;
	}
	
	//Actions
	private void actAskForARequest(MyCustomer m){
		int newAccntNum = m.accountNumber;
		if(m.accountNumber == -1){  //means it doesn't exist yet
		   newAccntNum = (int)(Math.random()*10000); //open account
		   while(database.funds.containsKey(newAccntNum)){
			   newAccntNum = (int)(Math.random()*10000);
		   }
		   print("Creating account " + newAccntNum + " for customer " + m.customer.toString());
		   m.accountNumber = newAccntNum;
		   database.funds.put(newAccntNum, 0.0);
		   database.amountOwed.put(newAccntNum, 0.0);
		} 
		m.customer.msgHereIsInfoPickARequest(database.funds.get(newAccntNum), database.amountOwed.get(newAccntNum), newAccntNum);
		m.customerState = CustomerState.GivingRequest;
	}
	
	private void actProcessRequest(MyCustomer m){  //handle multiple requests, MAKE SURE TO ADD CHECK TO SEE IF THEY CAN DO ACTION
		if(m.request.equalsIgnoreCase("Deposit")){ //not checked
			double currentFunds = database.funds.remove(m.accountNumber);
			database.funds.put(m.accountNumber, currentFunds + m.amount);
			m.customer.msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber)); // negative m.amount because I'm taking money from the customer
		} else if(m.request.equalsIgnoreCase("Withdraw")){ //checked
		    if(database.amountOwed.get(m.accountNumber) > 0){
		    	m.customer.msgTransactionDenied();
		  	}
			else {
				double currentFunds = database.funds.remove(m.accountNumber);
				database.funds.put(m.accountNumber, currentFunds - m.amount);
				m.customer.msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
			}
		} else if(m.request.equalsIgnoreCase("Withdraw Loan")){ // checked a bit, add robber
			if(database.amountOwed.get(m.accountNumber) > 0){
			      m.customer.msgTransactionDenied();
			}
			else { 
				double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
				database.amountOwed.put(m.accountNumber, currentAmountOwed + m.amount);
				m.customer.msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
			}
		} else if(m.request.equalsIgnoreCase("Pay Loan")){ // not checked
			double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
			database.amountOwed.put(m.accountNumber, currentAmountOwed - m.amount);
			m.customer.msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
		} else{ //robber
			gui.DoCallSecurity();
			try{
				tellerSem.acquire();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		m.customerState = CustomerState.None;
	}
	
	private void actProcessWireRequest(MyBusinessCustomer m){
		if(m.place instanceof Market){
			if(m.request.equalsIgnoreCase("Deposit")){ //not checked
				double currentFunds = database.funds.remove(m.accountNumber);
				database.funds.put(m.accountNumber, currentFunds + m.amount);
				((Market)(m.place)).getCashier().msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber); // negative m.amount because I'm taking money from the customer
			} else if(m.request.equalsIgnoreCase("Withdraw")){ //checked
					double currentFunds = database.funds.remove(m.accountNumber);
					database.funds.put(m.accountNumber, currentFunds - m.amount);
					((Market)(m.place)).getCashier().msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} else if(m.request.equalsIgnoreCase("Withdraw Loan")){ // checked a bit
					double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
					database.amountOwed.put(m.accountNumber, currentAmountOwed + m.amount);
					((Market)(m.place)).getCashier().msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} else if(m.request.equalsIgnoreCase("Pay Loan")){ // not checked
				double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
				database.amountOwed.put(m.accountNumber, currentAmountOwed - m.amount);
				((Market)m.place).getCashier().msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} 
		} else if(m.place instanceof Restaurant){
			if(m.request.equalsIgnoreCase("Deposit")){ //not checked
				double currentFunds = database.funds.remove(m.accountNumber);
				database.funds.put(m.accountNumber, currentFunds + m.amount);
				((Restaurant)m.place).getCashier().msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber); // negative m.amount because I'm taking money from the customer
			} else if(m.request.equalsIgnoreCase("Withdraw")){ //checked
					double currentFunds = database.funds.remove(m.accountNumber);
					database.funds.put(m.accountNumber, currentFunds - m.amount);
					((Restaurant)(m.place)).getCashier().msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} else if(m.request.equalsIgnoreCase("Withdraw Loan")){ // checked a bit
					double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
					database.amountOwed.put(m.accountNumber, currentAmountOwed + m.amount);
					((Restaurant)(m.place)).getCashier().msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} else if(m.request.equalsIgnoreCase("Pay Loan")){ // not checked
				double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
				database.amountOwed.put(m.accountNumber, currentAmountOwed - m.amount);
				((Restaurant)(m.place)).getCashier().msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber), m.accountNumber);
			} 
		}
		print("Transaction complete for wired customer with account number " + m.accountNumber);
		myBusinessCustomers.remove(m);
		stateChanged();
	}
	
	private void actLeaveBank(){
		gui.DoLeaveBank();
		try{
			tellerSem.acquire();
		} catch (Exception e){
			e.printStackTrace();
		}
		active = false;
	}

	//Utilities
	
	public void releaseSemaphore(){
		tellerSem.release();
	}
	public int getTellerNum(){
		return tellerNum;
	}
	
	public boolean isOccupied(){
		return occupied;
	}
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	public void setGui(BankTellerRoleGui g){
		gui = g;
	}
	
	
	
	//-------commands--------
	@Override
	public void cmdFinishAndLeave() {
		command = Command.Leave;
		stateChanged();
	}

	@Override
	public Place place() {
		return bank;
	}
}
