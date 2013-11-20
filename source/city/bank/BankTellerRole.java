package city.bank;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.bank.gui.BankTellerRoleGui;

public class BankTellerRole extends Role {
	
	//Data
	List<MyCustomer> myCustomers;
	BankHostRole host;
	boolean occupied;
	String name;
	private int tellerNum;
	static AccountDatabase database;
	
	enum Command{None, Leave};
	Command command;
	Semaphore tellerSem = new Semaphore(0,true);
	
	PersonAgent person;
	
	BankTellerRoleGui gui;
	 
	BankTellerRole(PersonAgent person){
		super(person);
		command = Command.None;
	}
	
	private static class AccountDatabase{
	      Hashtable<Integer, Double> funds;
	      Hashtable<Integer, Double> amountOwed;
	//given Account Number, gets funds or amount owed
	}
	private class MyCustomer{
		  MyCustomer(BankCustomerRole c){
			  this.customer = c;
			  customerState = CustomerState.None;
			  accountNumber = -1;
		  }
		  
	      BankCustomerRole customer;
	      int accountNumber;
	      String request;
	      int amount;
	      
	      CustomerState customerState;
	}
	
	enum CustomerState { None, Arrived, GivingRequest, GivenRequest};
	
	//Messages
	public void msgIAmHere(BankCustomerRole c){
		  MyCustomer m = new MyCustomer(c);
		  m.customerState = CustomerState.Arrived;
		  myCustomers.add(m);
		  stateChanged();
	}
	public void msgHereIsMyRequest(BankCustomerRole c, String request, int amount){
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
	public void msgLeavingBank(BankCustomerRole c){
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
				m.customerState = CustomerState.None;
				myCustomers.remove(m);
				stateChanged();
				return;
			}
		  }
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		for(MyCustomer m: myCustomers){
			if(m.customerState == CustomerState.Arrived){
				askForARequest(m);
				return true;
			}
		}
		for(MyCustomer m: myCustomers){
			if(m.customerState == CustomerState.GivenRequest){
				processRequest(m);
				return true;
			}
		}
		
		if(myCustomers.isEmpty() && command == Command.Leave){
			leaveBank();
		}
		return false;
	}
	
	//Actions
	private void askForARequest(MyCustomer m){
		if(m.accountNumber == -1){  //means it doesn't exist yet
		   int newAccntNum = (int)(Math.random()*10000); //open account
		   while(database.funds.containsKey(newAccntNum)){
			   newAccntNum = (int)(Math.random()*10000);
		   }
		   database.funds.put(newAccntNum, 0.0);
		   database.amountOwed.put(newAccntNum, 0.0);
		} 
		m.customer.msgHereIsInfoPickARequest(database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
		m.customerState = CustomerState.GivingRequest;
	}
	
	private void processRequest(MyCustomer m){  //handle multiple requests, MAKE SURE TO ADD CHECK TO SEE IF THEY CAN DO ACTION
		if(m.request.equalsIgnoreCase("Deposit")){ //not checked
			double currentFunds = database.funds.remove(m.accountNumber);
			database.funds.put(m.accountNumber, currentFunds + m.amount);
			m.customer.msgTransactionComplete(-m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber)); // negative m.amount because I'm taking money from the customer
		} else if(m.request.equalsIgnoreCase("Withdraw")){ //checked
		    if(database.amountOwed.get(m.accountNumber) >= 0){
		    	m.customer.msgTransactionDenied();
		  	}
			else {
				double currentFunds = database.funds.remove(m.accountNumber);
				database.funds.put(m.accountNumber, currentFunds - m.amount);
				m.customer.msgTransactionComplete(m.amount, database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
			}
		} else if(m.request.equalsIgnoreCase("Withdraw Loan")){ // checked a bit, add robber
			if(database.amountOwed.get(m.accountNumber) >= 0){
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
	}
	
	private void leaveBank(){
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
	
	//-------commands--------
	@Override
	protected void cmdFinishAndLeave() {
		command = Command.Leave;
		stateChanged();
	}
}
