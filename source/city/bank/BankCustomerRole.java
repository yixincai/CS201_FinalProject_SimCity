package city.bank;

import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.bank.gui.BankCustomerRoleGui;

public class BankCustomerRole extends Role {

	//Data
	BankHostRole bankHost;
	BankTellerRole teller;
	String request;
	private double amount;
	private double accountFunds;
	private double amountOwed;
	int accountNumber;
	
	State state;
	Event event;
	Semaphore bankCustSem;
	BankCustomerRoleGui gui;
	Bank bank;
	 
	enum State {Robber, DoingNothing, Waiting, AtTeller, GaveRequest, 
		TransactionComplete, TransactionDenied, LeaveBank };
	enum Event {None, CalledToDesk, GivenRequestPermission, WantsAnotherRequest, ApprovedTransaction, DeniedTransaction};
	
	public BankCustomerRole(PersonAgent person, int accountNumber, Bank bank){
		super(person);
		//person.getAccountNumber() = accountNumber;
		this.accountNumber = accountNumber;
		this.bank = bank;
		//set values above through personAgent, possible
		
		state = State.DoingNothing;
		event = Event.None;
	}
	
	// --------------------- ACCESSORS ---------------
	public double accountFunds() { return accountFunds; }
	public double amountOwed() { return amountOwed; }
	
	
	
	// ------------------------------ COMMANDS ---------------------------------
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
	
	public void msgCalledToDesk(BankTellerRole teller){
		  event = Event.CalledToDesk;
		  this.teller = teller;
		  stateChanged();
	}
	public void msgHereIsInfoPickARequest(double funds, double amountOwed){
		  this.accountFunds = funds;
		  this.amountOwed = amountOwed;
		  event = Event.GivenRequestPermission;
		  stateChanged();
	}
	/**
	 * @param amountReceived The amount of money received from the teller.  If the transaction was a deposit, this amount will be negative.
	 */
	public void msgTransactionComplete(double amountReceived, double funds, double amountOwed){
		  event = Event.ApprovedTransaction;
		  _person.changeMoney(amountReceived);
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
		  bankHost.msgWaiting(this);
		  state = State.Waiting;
		  // stateChanged();
	}
	private void actGoToTeller(){
		  gui.DoGoToTeller(this.teller.getTellerNum());
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
		  // stateChanged();
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
		  stateChanged();
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
		stateChanged();
	}
	private void actRobBank(){
		gui.DoRobBank();
		//teller.msgGiveMeAllYourMoney();
		state = State.Robber;
		 // stateChanged();
	}
}
