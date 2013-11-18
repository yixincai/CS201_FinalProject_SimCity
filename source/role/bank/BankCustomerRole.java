package role.bank;

import gui.BankCustomerRoleGui;

public class BankCustomerRole {

	//Data
	BankHostRole bankHost;
	BankTellerRole teller;
	String request;
	String name;
	double money;
	double accountFunds;
	double amountOwed;
	int accountNumber;
	
	State state;
	Event event;
	//Semaphore bankCustSem;
	BankCustomerRoleGui gui;
	 
	enum State {Robber, DoingNothing, Waiting, AtTeller, GaveRequest, 
		TransactionComplete, TransactionDenied };
	enum Event {None, CalledToDesk, GivenRequestPermission, WantsAnotherRequest, ApprovedTransaction, DeniedTransaction};
	
	public BankCustomerRole(String name, int accountNumber, double money){
		this.name = name;
		this.accountNumber = accountNumber;
		this.money = money;
		//set values above through personAgent
		
		state = State.DoingNothing;
		event = Event.None;
	}
	
	public void msgCalledToDesk(BankTellerRole teller){
		  event = Event.CalledToDesk;
		  this.teller = teller;
		 // stateChanged();
	}
	public void msgHereIsInfoPickARequest(double funds, double amountOwed){
		  this.accountFunds = funds;
		  this.amountOwed = amountOwed;
		  event = Event.GivenRequestPermission;
		  //stateChanged();
	}
	public void msgTransactionComplete(){
		  event = Event.ApprovedTransaction;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  //stateChanged(); //may give info
		}
	public void msgTransactionDenied(){
		  event = Event.DeniedTransaction;
		  //or event = WantsAnotherRequest; && state = giveNewRequest; //send another request
		  //stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		if(state == State.Robber && event == Event.DeniedTransaction){
			robBank();
			return true;
		}
		if(state == State.DoingNothing && event == Event.None){
			goToLine();
			return true;
		}
		if(state == State.Waiting && event == Event.CalledToDesk){
			goToTeller();	
			return true;
		}
		if(state == State.AtTeller && event == Event.GivenRequestPermission){
			giveRequest();
			return true;
		}
		if(state == State.AtTeller && event == Event.WantsAnotherRequest){
			giveNewRequest();
			return true;
		}
		if(state == State.GaveRequest && (event == Event.ApprovedTransaction || event == Event.DeniedTransaction)){
			leaveBank();
			return true;
		}
	/*   maybe redesign these
		if state = giveRequest, giveRequest();
		if state = TransactionDenied || WantsAnotherRequest, giveNewRequest(String request);
		if state = TransactionComplete, leaveBank();
	*/
		return false;
	}
	
	private void goToLine(){
		  gui.DoGoToLine();
		  bankHost.msgWaiting(this);
		  state = State.Waiting;
		  // stateChanged();
	}
	private void goToTeller(){
		  gui.DoGoToTeller(this.teller.getTellerNum());
		  teller.msgIAmHere(this);
		  state = State.AtTeller;
		  // stateChanged();
	}
	private void giveRequest(){
		teller.msgHereIsMyRequest(this, request, accountNumber);
		state = State.GaveRequest;
		 // stateChanged();
	}
	private void giveNewRequest(){
		//may trigger robbery
		//pick new request using logic tied to accountFunds and amountOwed
		  //teller.msgHereIsMyRequest(String newRequest, int accountNumber);
		  state = State.GaveRequest;
		  // stateChanged();
	}
	private void leaveBank(){
		  gui.DoLeaveBank();
		  bankHost.msgLeavingBank(teller);
		  teller.msgLeavingBank(this);
		  state = State.DoingNothing;
		  //send message to person agent to set role inactive, DO NOT SET EVENT TO NONE, THIS WILL RESTART PROCESS
		  // stateChanged();
	}
	private void robBank(){
		gui.DoRobBank();
		//teller.msgGiveMeAllYourMoney();
		state = State.Robber;
		 // stateChanged();
	}
}
