package city.restaurant.ryan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import agent.Agent;
import restaurant.*;
import restaurant.interfaces.*;

public class BankAgent extends Agent implements Bank{
	String name;
	double vault = 1000000000;
	double loaned = 0;
	List<Loan> loans = new ArrayList<Loan>();
	List<Payment> payments = new ArrayList<Payment>();
	
	enum LoanState{received, review, paid};
	
	public BankAgent(String name){
		this.name = name;
	}
	
	//Messages************************************************************************************************************************************
	public void msgAskForLoan(Cashier cashier, double amount){
		Do("Received request for a loan of " + amount + " from " + cashier.getName());
		loans.add(new Loan(cashier, amount));
		stateChanged();
	}
	
	public void msgPayBackForLoan(Cashier cashier, double payment){
		Do("Received payment of " + payment);
		payments.add(new Payment(cashier, payment));
		stateChanged();
	}
	
	//Scheduler************************************************************************************************************************************
	protected boolean pickAndExecuteAnAction(){
		synchronized(loans){
			for(Loan loan: loans){
				if(loan.lState == LoanState.received){
					AnalyzeLoan(loan);
					return true;
				}
			}
		}
		synchronized(payments){
			for(Payment payment: payments){
				if(payment.pState == LoanState.received){
					ReceivePayment(payment);
					return true;
				}
			}
		}
		return false;
	}
	
	//Actions************************************************************************************************************************************
	public void AnalyzeLoan(Loan loan){
		loan.lState = LoanState.review;
		vault -= loan.amount;
		loaned += loan.amount;
		Do("Given loan to " + loan.cashier.getName() + " for amount of " + loan.amount);
		loan.cashier.msgHereIsLoan(loan.amount);
		loans.remove(loan);
	}
	
	public void ReceivePayment(Payment payment){
		loaned -= payment.amount;
		vault += payment.amount;
		if(loaned == 0){
			Do("Cashier doesn't owe Bank any loans.");
		}
		else{
			Do("Cashier owes " + loaned + " to bank");
		}
		payments.remove(payment);
	}
	
	//Utilities************************************************************************************************************************************
	public String getName(){
		return name;
	}
	
	class Loan{
		Cashier cashier;
		double amount;
		LoanState lState = LoanState.received;
		
		Loan(Cashier cashier, double amount){
			this.cashier = cashier;
			this.amount = amount;
		}
		
	}
	
	class Payment{
		Cashier cashier;
		double amount;
		double payment = 0;
		LoanState pState = LoanState.received;
		
		Payment(Cashier cashier, double amount){
			this.cashier = cashier;
			this.amount = amount;
		}
		
	}

}
