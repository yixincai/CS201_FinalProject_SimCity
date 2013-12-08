package city.bank;

import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.util.concurrent.Semaphore;

import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.bank.gui.BankCustomerRoleGui;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankHost;
import city.bank.interfaces.BankTeller;

public class BankCustomerRole extends Role implements BankCustomer {

	// --------------------------------------- DATA -------------------------------------------
	BankHost _bankHost;
	BankTeller _teller;
	public String _request; //TODO HEY OMAR I recommend you change these fields to be private, even though I see that you only use them for tests; Just make some getters.
	public double _amount;
	public double _accountFunds;
	public double _amountOwed;
	public int _accountNumber;
	
	public State _state;
	public Event _event;
	Semaphore _bankCustSem; //TODO this name is ambiguous; what is it for?
	public BankCustomerRoleGui gui; //TODO it won't let me add an underscore, but I can't figure out why
	Bank bank;
	 
	public enum State {Dead, Robber, DoingNothing, Waiting, AtTeller, GaveRequest, 
		TransactionComplete, TransactionDenied, LeaveBank };
	public enum Event {None, CalledToDesk, GivenRequestPermission, WantsAnotherRequest, ApprovedTransaction, DeniedTransaction};
	
	
	
	// ------------------------------------------- CONSTRUCTORS & PROPERTIES --------------------------------------------
	
	public BankCustomerRole(PersonAgent person, Bank bank){
		super(person);
		//person.getAccountNumber() = accountNumber;
		_accountNumber = -1;
		this.bank = bank;
		//set values above through personAgent, possible
		
		this._bankHost = bank.host();
		_state = State.DoingNothing;
		_event = Event.None;
		_bankCustSem = new Semaphore(0, true);
		
		this.gui = new BankCustomerRoleGui(this);
		if(bank.animationPanel()!= null){
			bank.animationPanel().addGui(gui);
		}
	}
	
	public double accountFunds() { return _accountFunds; }
	public double amountOwed() { return _amountOwed; }
	@Override
	public Place place() { return bank; }
	
	
	
	// ---------------------------------------------- COMMANDS ---------------------------------------------------
	public void cmdRequest(String request, double amount) {
		switch(request) {
		case "Robber":
			cmdSteal(amount);
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
	public void cmdSteal(double amount) {
		_request = "Steal";
		this._amount = amount;
		_event = Event.DeniedTransaction;
		_state = State.Robber;
		stateChanged();
	}
	public void cmdWithdraw(double amount) {
		_request = "Withdraw";
		this._amount = amount;
		stateChanged();
	}
	public void cmdDeposit(double amount) {
		_request = "Deposit";
		this._amount = amount;
		stateChanged();
	}
	public void cmdWithdrawLoan(double amount) {
		_request = "Withdraw Loan";
		this._amount = amount;
		stateChanged();
	}
	public void cmdPayLoan(double amount) {
		_request = "Pay Loan";
		this._amount = amount;
		stateChanged();
	}
	@Override
	public void cmdFinishAndLeave() {
		// do nothing
	}
	
	
	
	// --------------------------------------------- MESSAGES -----------------------------------------------------
	public void msgWeAreClosed(){
		_state = State.LeaveBank;
		_event = Event.DeniedTransaction;
		stateChanged();
	}
	
	public void msgCalledToDesk(BankTeller teller){
		  _event = Event.CalledToDesk;
		  this._teller = teller;
		  stateChanged();
	}
	public void msgHereIsInfoPickARequest(double funds, double amountOwed, int newAccountNumber){
		  this._accountNumber = newAccountNumber;
		  this._accountFunds = funds;
		  this._amountOwed = amountOwed;
		  _event = Event.GivenRequestPermission;
		  stateChanged();
	}
	/**
	 * @param amountReceived The amount of money received from the teller.  If the transaction was a deposit, this amount will be negative.
	 */
	public void msgTransactionComplete(double amountReceived, double funds, double amountOwed){
			print(AlertTag.BANK, "Transaction complete!");
		  _event = Event.ApprovedTransaction;
		  _person.cmdChangeMoney(amountReceived);
		  this._accountFunds = funds;
		  this._amountOwed = amountOwed;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  stateChanged(); //may give info
		}
	public void msgTransactionDenied(){
		  _event = Event.DeniedTransaction;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  stateChanged();
	}
	
	
	
	// ----------------------------------------------- SCHEDULER --------------------------------------------------------
	public boolean pickAndExecuteAnAction(){
		if(_state == State.LeaveBank && _event == Event.DeniedTransaction){
			actLeaveBankWithoutTransaction();
			return true;
		}
		if(_state == State.Robber && _event == Event.DeniedTransaction){
			actRobBank();
			return true;
		} 
		if(_state == State.DoingNothing && _event == Event.None){
			actGoToLine();
			return true;
		}
		if(_state == State.Waiting && _event == Event.CalledToDesk){
			actGoToTeller();	
			return true;
		}
		if(_state == State.AtTeller && _event == Event.GivenRequestPermission){
			actGiveRequest();
			return true;
		}
		if(_state == State.AtTeller && _event == Event.WantsAnotherRequest){
			actGiveNewRequest();
			return true;
		}
		if(_state == State.GaveRequest && (_event == Event.ApprovedTransaction || _event == Event.DeniedTransaction)){
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
	
	
	
	// ----------------------------------------------- ACTIONS --------------------------------------------------------
	private void actGoToLine(){
		  gui.DoGoToLine();
		  try {
				_bankCustSem.acquire();
		  } catch (InterruptedException e) {
				e.printStackTrace();
		  }
		  _bankHost.msgWaiting(this);
		  _state = State.Waiting;
		  // stateChanged();
	}
	private void actGoToTeller(){
		  gui.DoGoToTeller(this._teller.getTellerNum());
		  try {
				_bankCustSem.acquire();
		  } catch (InterruptedException e) {
				e.printStackTrace();
		  }
		  _teller.msgIAmHere(this);
		  _state = State.AtTeller;
		 // stateChanged();
	}
	private void actGiveRequest(){
		_teller.msgHereIsMyRequest(this, _request, _accountNumber);
		_state = State.GaveRequest;
		// stateChanged();
	}
	private void actGiveNewRequest(){
		//may trigger robbery
		//pick new request using logic tied to accountFunds and amountOwed
		  //teller.msgHereIsMyRequest(String newRequest, int accountNumber);
		  _state = State.GaveRequest;
		//  stateChanged();
	}
	private void actLeaveBank(){
		  _bankHost.msgLeavingBank(_teller);
		  _teller.msgLeavingBank(this);
		  gui.DoLeaveBank();
		  try {
			  _bankCustSem.acquire();
		  } catch (Exception e){
			  e.printStackTrace();
		  }
		  _state = State.DoingNothing;
		  active = false;
		  //send message to person agent to set role inactive, DO NOT SET EVENT TO NONE, THIS WILL RESTART PROCESS
		//  stateChanged();
	}
	private void actLeaveBankWithoutTransaction(){
		gui.DoLeaveBank();
		try {
			 _bankCustSem.acquire();
	    } catch (Exception e){
			e.printStackTrace();
		}
		_state = State.DoingNothing;
		active = false;
		//stateChanged();
	}
	private void actRobBank(){
		gui.DoRobBank();
		try {
			 _bankCustSem.acquire();
	    } catch (Exception e){
			e.printStackTrace();
		}
		//_person.cmdChangeMoney(_amount);
		for(int i = 0; i < Directory.banks().size(); i++){
			if(this.bank == Directory.banks().get(i)){
				_teller = Directory.banks().get(i)._tellers.get(0);
				AlertLog.getInstance().logMessage(AlertTag.BANK, "Robber", "ROBBING THE BANK");
				_teller.msgRobbery(_amount, this);
				_state = State.Robber;
			}
		}
		
		gui.DoRun();
		/*try {
			 _bankCustSem.acquire();
	    } catch (Exception e){
			e.printStackTrace();
		} */
		_state = State.Dead;
	}
	
	
	
	// -------------------------------------------- UTILITIES -----------------------------------------------
	public void dead(){
		gui.dead();
		_state = State.Dead;
	}
	public void releaseSemaphore(){
		_bankCustSem.release();
		//stateChanged();
	}
	
	public BankCustomerRoleGui getGui(){
		return gui;
	}
}
