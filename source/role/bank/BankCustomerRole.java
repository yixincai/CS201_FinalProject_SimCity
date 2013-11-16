package role.bank;

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
	//Semaphore bankCustSem;
	 
	enum State {Robber, Robbing, DoingNothing, Waiting, CalledToDesk, GaveRequest, 
		TransactionComplete, WantsAnotherRequest, TransactionDenied };
	
	public void msgCalledToDesk(BankTellerRole teller){
		  state = State.CalledToDesk;
		  this.teller = teller;
		// DoGoToTeller(teller.tellerNum);
		 // stateChanged();
	}
	public void msgHereIsInfoPickARequest(double funds, double amountOwed){
		  this.accountFunds = funds;
		  this.amountOwed = amountOwed;
		 // state = State.GiveRequest;
		  //stateChanged();
	}
	public void msgTransactionComplete(){
		  state = State.TransactionComplete;
		  //or state = WantsAnotherRequest; //send another request
		  //stateChanged(); //may give info
		}
	public void msgTransactionDenied(){
		  state = State.TransactionDenied;
		  //stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
	/* 	if state = Robber, robBank();
		if state = doingNothing, goToLine();
		if state = calledToDesk, goToTeller ();
		if state = giveRequest, giveRequest();
		if state = TransactionDenied || WantsAnotherRequest, giveNewRequest(String request);
		if state = TransactionComplete, leaveBank();
	*/
		return false;
	}
	
	private void goToLine(){
		  //DoGoToLine();
		  bankHost.msgWaiting(this);
		  state = State.Waiting;
	}
	private void goToTellerAndGiveRequest(){
		//DoGoToTeller(this.teller.getTellerNum());
		  teller.msgIAmHere(this);
	}
	private void giveRequest(){
		teller.msgHereIsMyRequest(this, request, accountNumber);
		state = State.GaveRequest;
	}
	private void giveNewRequest(){
		//may trigger robbery
		//pick new request using logic tied to accountFunds and amountOwed
		  //teller.msgHereIsMyRequest(String newRequest, int accountNumber);
		  state = State.GaveRequest;
	}
	private void leaveBank(){
		 // DoLeaveBank();
		  bankHost.msgLeavingBank(teller);
		  teller.msgLeavingBank(this);
		  state = State.DoingNothing;
	}
	private void robBank(){
		//teller.msgGiveMeAllYourMoney();
		state = State.Robbing;
	}
}
