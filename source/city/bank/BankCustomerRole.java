package city.bank;

import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.Place;
import city.bank.gui.BankCustomerRoleGui;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankHost;
import city.bank.interfaces.BankTeller;

public class BankCustomerRole extends Role implements BankCustomer {

	//Data
	BankHost bankHost;
	BankTeller teller;
	public String request;
	public double amount;
	public double accountFunds;
	public double amountOwed;
	public int accountNumber;
	
	public State state;
	public Event event;
	Semaphore bankCustSem;
	BankCustomerRoleGui gui;
	Bank bank;
	 
	public enum State {Robber, DoingNothing, Waiting, AtTeller, GaveRequest, 
		TransactionComplete, TransactionDenied, LeaveBank };
	public enum Event {None, CalledToDesk, GivenRequestPermission, WantsAnotherRequest, ApprovedTransaction, DeniedTransaction};
	
	public BankCustomerRole(PersonAgent person, Bank bank){
		super(person);
		//person.getAccountNumber() = accountNumber;
		accountNumber = -1;
		this.bank = bank;
		//set values above through personAgent, possible
		
		this.bankHost = bank.getHost();
		state = State.DoingNothing;
		event = Event.None;
		bankCustSem = new Semaphore(0, true);
		
		this.gui = new BankCustomerRoleGui(this);
		bank.animationPanel().addGui(gui);
	}
	
	// --------------------- ACCESSORS ---------------
	public double accountFunds() { return accountFunds; }
	public double amountOwed() { return amountOwed; }
	
	
	
	// ------------------------------ COMMANDS ---------------------------------
	public void cmdRequest(String request, double amount) {
		switch(request) {
		case "Withdraw":
			cmdWithdraw(amount);
			return;
		case "Deposit":
			cmdDeposit(amount);
			return;
		case "Withdraw Loan":
			cmdWithdrawLoan(amount);
			return;
		case "Pay Loan":
			cmdPayLoan(amount);
			return;
		}
	}
	public void cmdWithdraw(double amount) {
		request = "Withdraw";
		this.amount = amount;
		stateChanged();
	}
	public void cmdDeposit(double amount) {
		request = "Deposit";
		this.amount = amount;
		stateChanged();
	}
	public void cmdWithdrawLoan(double amount) {
		request = "Withdraw Loan";
		this.amount = amount;
		stateChanged();
	}
	public void cmdPayLoan(double amount) {
		request = "Pay Loan";
		this.amount = amount;
		stateChanged();
	}
	@Override
	public void cmdFinishAndLeave() {
		//do nothing
	}
	
	
	
	// ----------------------------- MESSAGES ------------------------------------
	public void msgWeAreClosed(){
		state = State.LeaveBank;
		event = Event.DeniedTransaction;
		stateChanged();
	}
	
	public void msgCalledToDesk(BankTeller teller){
		  event = Event.CalledToDesk;
		  this.teller = teller;
		  stateChanged();
	}
	public void msgHereIsInfoPickARequest(double funds, double amountOwed, int newAccountNumber){
		  this.accountNumber = newAccountNumber;
		  this.accountFunds = funds;
		  this.amountOwed = amountOwed;
		  event = Event.GivenRequestPermission;
		  stateChanged();
	}
	/**
	 * @param amountReceived The amount of money received from the teller.  If the transaction was a deposit, this amount will be negative.
	 */
	public void msgTransactionComplete(double amountReceived, double funds, double amountOwed){
			print("Transaction complete!");
		  event = Event.ApprovedTransaction;
		  _person.cmdChangeMoney(amountReceived);
		  this.accountFunds = funds;
		  this.amountOwed = amountOwed;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  stateChanged(); //may give info
		}
	public void msgTransactionDenied(){
		  event = Event.DeniedTransaction;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		if(state == State.LeaveBank && event == Event.DeniedTransaction){
			actLeaveBankWithoutTransaction();
			return true;
		}
		if(state == State.Robber && event == Event.DeniedTransaction){
			actRobBank();
			return true;
		} 
		if(state == State.DoingNothing && event == Event.None){
			actGoToLine();
			return true;
		}
		if(state == State.Waiting && event == Event.CalledToDesk){
			actGoToTeller();	
			return true;
		}
		if(state == State.AtTeller && event == Event.GivenRequestPermission){
			actGiveRequest();
			return true;
		}
		if(state == State.AtTeller && event == Event.WantsAnotherRequest){
			actGiveNewRequest();
			return true;
		}
		if(state == State.GaveRequest && (event == Event.ApprovedTransaction || event == Event.DeniedTransaction)){
			actLeaveBank();
			return true;
		}
	/*   maybe redesign these
		if state = giveRequest, giveRequest();
		if state = TransactionDenied || WantsAnotherRequest, giveNewRequest(String request);
		if state = TransactionComplete, leaveBank();
	*/
		return false;
	}
	
	private void actGoToLine(){
		  gui.DoGoToLine();
		  try {
				bankCustSem.acquire();
		  } catch (InterruptedException e) {
				e.printStackTrace();
		  }
		  bankHost.msgWaiting(this);
		  state = State.Waiting;
		  // stateChanged();
	}
	private void actGoToTeller(){
		  gui.DoGoToTeller(this.teller.getTellerNum());
		  try {
				bankCustSem.acquire();
		  } catch (InterruptedException e) {
				e.printStackTrace();
		  }
		  teller.msgIAmHere(this);
		  state = State.AtTeller;
		 // stateChanged();
	}
	private void actGiveRequest(){
		teller.msgHereIsMyRequest(this, request, accountNumber);
		state = State.GaveRequest;
		// stateChanged();
	}
	private void actGiveNewRequest(){
		//may trigger robbery
		//pick new request using logic tied to accountFunds and amountOwed
		  //teller.msgHereIsMyRequest(String newRequest, int accountNumber);
		  state = State.GaveRequest;
		//  stateChanged();
	}
	private void actLeaveBank(){
		  bankHost.msgLeavingBank(teller);
		  teller.msgLeavingBank(this);
		  gui.DoLeaveBank();
		  try {
			  bankCustSem.acquire();
		  } catch (Exception e){
			  e.printStackTrace();
		  }
		  state = State.DoingNothing;
		  active = false;
		  //send message to person agent to set role inactive, DO NOT SET EVENT TO NONE, THIS WILL RESTART PROCESS
		//  stateChanged();
	}
	private void actLeaveBankWithoutTransaction(){
		gui.DoLeaveBank();
		try {
			 bankCustSem.acquire();
	    } catch (Exception e){
			e.printStackTrace();
		}
		state = State.DoingNothing;
		active = false;
		//stateChanged();
	}
	private void actRobBank(){
		gui.DoRobBank();
		//teller.msgGiveMeAllYourMoney();
		state = State.Robber;
		 // stateChanged();
	}
	
	public void releaseSemaphore(){
		bankCustSem.release();
		//stateChanged();
	}

	@Override
	public Place place() {
		return bank;
	}
}
