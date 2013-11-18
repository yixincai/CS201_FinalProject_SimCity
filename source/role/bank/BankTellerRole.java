package role.bank;

import gui.BankTellerRoleGui;

import java.util.Hashtable;
import java.util.List;

public class BankTellerRole {
	
	//Data
	List<MyCustomer> myCustomers;
	BankHostRole host;
	boolean occupied;
	String name;
	private int tellerNum;
	static AccountDatabase database;
	
	BankTellerRoleGui gui;
	 
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
	      int number;
	      
	      CustomerState customerState;
	}
	
	enum CustomerState { None, Arrived, GivingRequest, GivenRequest};
	
	//Messages
	public void msgIAmHere(BankCustomerRole c){
		  MyCustomer m = new MyCustomer(c);
		  m.customerState = CustomerState.Arrived;
		  myCustomers.add(m);
		  //stateChanged();
	}
	public void msgHereIsMyRequest(BankCustomerRole c, String request, int number){
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
		  	m.customerState = CustomerState.GivenRequest;
		  	m.request = request;
		  	m.number = number; //
		  	//stateChanged();
		  	return;
			}
		  }
	}
	public void msgLeavingBank(BankCustomerRole c){
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
				m.customerState = CustomerState.None;
				myCustomers.remove(m);
				//stateChanged();
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
			database.funds.put(m.accountNumber, currentFunds + m.number);
			//m.customer.funds-= m.number;
		} else if(m.request.equalsIgnoreCase("Withdraw")){ //checked
		    if(database.amountOwed.get(m.accountNumber) >= 0){
		    	m.customer.msgTransactionDenied();
		  	}
			else {
				double currentFunds = database.funds.remove(m.accountNumber);
				database.funds.put(m.accountNumber, currentFunds - m.number);
				m.customer.msgTransactionComplete();
			}
		} else if(m.request.equalsIgnoreCase("Withdraw Loan")){ // checked a bit, add robber
			if(database.amountOwed.get(m.accountNumber) >= 0){
			      m.customer.msgTransactionDenied();
			}
			else { 
				double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
				database.amountOwed.put(m.accountNumber, currentAmountOwed + m.number);
				m.customer.msgTransactionComplete();
			}
		} else if(m.request.equalsIgnoreCase("Pay Loan")){ // not checked
			double currentAmountOwed = database.amountOwed.remove(m.accountNumber);
			database.amountOwed.put(m.accountNumber, currentAmountOwed - m.number);
			m.customer.msgTransactionComplete();
		} else{ //robber
			gui.DoCallSecurity();
		}
	}

	//Utilities
	public int getTellerNum(){
		return tellerNum;
	}
	
	public boolean isOccupied(){
		return occupied;
	}
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
}
