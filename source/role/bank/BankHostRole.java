package role.bank;

import java.util.*;

public class BankHostRole {

	//Data
	List<BankTellerRole> tellers;
	List<BankCustomerRole> waitingCustomers;
	
	//Messages
	public void msgWaiting(BankCustomerRole c){
	  waitingCustomers.add(c);
	 // stateChanged();
	}
	public void msgLeavingBank(BankTellerRole teller){
	  teller.setOccupied(false);
	 // stateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		for(BankTellerRole t: tellers){
			if(!t.isOccupied()){
				if(!waitingCustomers.isEmpty()){
					callTeller(waitingCustomers.remove(0), t);
					return true;
				}
			}
		}
		
		return false;
	}
	
	//Actions
	private void callTeller(BankCustomerRole c, BankTellerRole teller){
		//DoCallTeller(teller);   
	    c.msgCalledToDesk(teller);
	    teller.setOccupied(true);
	}

}
