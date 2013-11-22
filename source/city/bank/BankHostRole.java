package city.bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.bank.gui.BankHostRoleGui;

public class BankHostRole extends Role {

	public BankHostRole(PersonAgent person, Bank bank, List<BankTellerRole> tellers) {
		super(person);
		this.bank = bank;
		this.tellers = tellers;
		command = Command.None;
	}

	//Data
	Bank bank;
	List<BankTellerRole> tellers;
	private List<BankCustomerRole> waitingCustomers;
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
					actCallTeller(waitingCustomers.remove(0), t);
					return true;
				}
			}
		}
		
		if(tellers.isEmpty()){  //host is last to leave bank
			actLeaveBank();
			return true;
		}
		return false;
	}
	
	//Actions
	private void actCallTeller(BankCustomerRole c, BankTellerRole teller){
		gui.DoCallTeller(teller);   
	    c.msgCalledToDesk(teller);
	    teller.setOccupied(true);
	}
	
	private void actLeaveBank(){
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
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		command = Command.Leave;
		stateChanged();
	}

	//Utilities
	public boolean isWaitingCustomersEmpty(){
		return waitingCustomers.isEmpty();
	}
}
