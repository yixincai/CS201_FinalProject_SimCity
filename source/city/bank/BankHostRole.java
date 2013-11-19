package city.bank;

import java.util.*;

import city.bank.gui.BankHostRoleGui;

public class BankHostRole {

	//Data
	List<BankTellerRole> tellers;
	List<BankCustomerRole> waitingCustomers;
	BankHostRoleGui gui;
	
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
		gui.DoCallTeller(teller);   
	    c.msgCalledToDesk(teller);
	    teller.setOccupied(true);
	}

}
