package city.bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.bank.gui.BankHostRoleGui;

public class BankHostRole extends Role {

	public BankHostRole(PersonAgent person) {
		super(person);
		command = Command.None;
	}

	//Data
	List<BankTellerRole> tellers;
	List<BankCustomerRole> waitingCustomers;
	BankHostRoleGui gui;
	Semaphore hostSem = new Semaphore(0,true);
	
	Command command;
	
	enum Command{None, Leave};
	
	//Messages
	public void msgWaiting(BankCustomerRole c){
	 if(command == Command.None){
		 waitingCustomers.add(c);
	 } else{
		 c.msgWeAreClosed();
	 }
	 	stateChanged();
	}
	public void msgLeavingBank(BankTellerRole teller){
	  teller.setOccupied(false);
	  stateChanged();
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
		
		if(tellers.isEmpty()){  //host is last to leave bank
			leaveBank();
			return true;
		}
		return false;
	}
	
	//Actions
	private void callTeller(BankCustomerRole c, BankTellerRole teller){
		gui.DoCallTeller(teller);   
	    c.msgCalledToDesk(teller);
	    teller.setOccupied(true);
	}
	
	private void leaveBank(){
		gui.DoLeaveBank();
		try{
			hostSem.acquire();
		} catch (Exception e){
			e.printStackTrace();
		}
		active = false;
		stateChanged();
	}
	
	//---------- Commands --------
	@Override
	protected void finishCommandAndLeave() {
		// TODO Auto-generated method stub
		
	}

}
