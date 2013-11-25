package city.bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.Place;
import city.bank.gui.BankHostRoleGui;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankHost;
import city.bank.interfaces.BankTeller;

public class BankHostRole extends Role implements BankHost {

	public BankHostRole(PersonAgent person, Bank bank, List<BankTeller> tellers) {
		super(person);
		this.bank = bank;
		this.tellers = tellers;
		command = Command.None;
	}

	//Data
	Bank bank;
	List<BankTeller> tellers;
	public List<BankCustomer> waitingCustomers = new ArrayList<BankCustomer>();
	BankHostRoleGui gui;
	Semaphore hostSem = new Semaphore(0,true);
	
	Command command;
	
	enum Command{None, Leave};
	
	//Messages
	public void msgWaiting(BankCustomer c){
	 if(command == Command.None){
		 waitingCustomers.add(c);
	 } else{
		 c.msgWeAreClosed();
	 }
	 	stateChanged();
	}
	public void msgLeavingBank(BankTeller teller){
	  teller.setOccupied(false);
	  stateChanged();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		for(BankTeller t: tellers){
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
	private void actCallTeller(BankCustomer c, BankTeller t){
		//gui.DoCallTeller(t);  the host doesn't move anywhere in the bank   
	    c.msgCalledToDesk(t);
	    t.setOccupied(true);
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
	@Override
	public Place place() {
		return bank;
	}
	public void setGui(BankHostRoleGui g) {
		gui = g;
	}
}
