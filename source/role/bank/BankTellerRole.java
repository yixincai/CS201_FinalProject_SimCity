package role.bank;

import java.util.Hashtable;
import java.util.List;

public class BankTellerRole {
	
	//Data
	List<MyCustomer> myCustomers;
	BankHostRole host;
	boolean occupied;
	String name;
	private int tellerNum;
	AccountDatabase database;
	 
	private static class AccountDatabase{
	      Hashtable<Integer, Double> funds;
	      Hashtable<Integer, Double> amountOwed;
	//given Account Number, gets funds or amount owed
	}
	private class MyCustomer{
		  MyCustomer(BankCustomerRole c){
			  this.customer = c;
		  }
		  
	      BankCustomerRole customer;
	      int accountNumber;
	      String request;
	      int number;
	}
	
	enum customerState { none, arrived, givingRequest, givenRequest};
	
	//Messages
	public void msgIAmHere(BankCustomerRole c){
		  MyCustomer m = new MyCustomer(c);
		//  m.state = arrived;
		  myCustomers.add(m);
		}
	public void msgHereIsMyRequest(BankCustomerRole c, String request, int number){
		  for(MyCustomer m: myCustomers){
			if(m.customer == c){
		  	//m.customerState = givenRequest;
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
		  //	m.customerState = leaving;
		  	myCustomers.remove(m);
		  	//stateChanged();
		  	return;
			}
		  }
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		//if there exists a MyCustomer m in myCustomers such that m.state == arrived, askForARequest(m);
		//if there exists a MyCustomer m in myCustomers such that m.state == givenRequest, processRequest(m);
		return false;
	}
	
	//Actions
	private void askForARequest(MyCustomer m){
		/*if(m.c.accountNumber == null){
		   int newAccntNum = (int)(Math.random()*10000);//open account
		   database.funds.put(newAccntNum, 0.0);
		   database.amountOwed.put(newAccntNum, 0.0);
		} m.customer.msgHereIsInfoPickARequest(database.funds.get(m.accountNumber), database.amountOwed.get(m.accountNumber));
		m.state = givingRequest; */
	}
	
	private void processRequest(MyCustomer m){  //handle mult. requests
		/*  if(m.request == deposit){
			funds.set(m.accountNumber, funds.get(m.accountNumber) + m.number);
			m.customer.funds-= m.number;
		} else if(m.request == withdraw){
		    if(amountOwed.get(m.accountNumber) >= 0){
		  	m.customer.msgTransactionDenied();}
			else {
		      funds.set(m.accountNumber,   funds.get(m.accountNumber) - m.number);
		m.customer.msgTransactionCompleted();
			}
		}
		} else if(m.request == requestloan){
		if(amountOwed.get(m.accountNumber) >= 0){
		      m.customer.msgTransactionDenied();}
		else { amountOwed.set(m.accountNumber, amountOwed.get(m.accountNumber) + m.number);
		m.customer.msgTransactionCompleted();
		} else if(m.request == payLoan){
		amountOwed.set(m.accountNumber,   amountOwed.get(m.accountNumber) - m.number);
		m.customer.msgTransactionCompleted();
		} else{ //robber
		  DoCallSecurity();
		}
		} */
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
