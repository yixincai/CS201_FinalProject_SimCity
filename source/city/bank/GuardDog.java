package city.bank;

import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.PersonAgent;
import city.Place;
import city.bank.BankHostRole.Command;
import city.bank.gui.BankHostRoleGui;
import city.bank.gui.GuardDogGui;
import city.bank.interfaces.BankCustomer;
import city.bank.interfaces.BankTeller;

public class GuardDog extends Agent {

	public GuardDog(Bank bank) {
		hitList = new ArrayList<BankCustomerRole>();
		this.bank = bank;
		command = Command.None;
		this.gui = new GuardDogGui(this);
		bank.animationPanel().addGui(gui);
	}

	//Data
	Bank bank;
	List<BankCustomerRole> hitList;
	GuardDogGui gui;
	
	Semaphore guardDogSem = new Semaphore(0,true);
	
	Command command;
	
	enum Command{None, Kill};
	
	//Messages
	public void sicEm(List<BankCustomerRole> robbers){
		AlertLog.getInstance().logMessage(AlertTag.BANK, "Riley The Guard Dog", "Woof");
		for(int i = 0; i < robbers.size(); i++){
			this.hitList.add(robbers.get(i));
		}
		
		command = Command.Kill;
		stateChanged();
	}

	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		if(command == Command.Kill){
			if(hitList.size() >= 0){
				attack(hitList.get(0));
				return true;
			}
		}
		return false;
	}
	
	private void attack(BankCustomerRole hit){
		gui.DoAttack(hit);
		try{
			guardDogSem.acquire();
		} catch (Exception e){
			e.printStackTrace();
		} 
		hitList.remove(0);
		if(hitList.size() == 0) { command = Command.None; }
	}
	
	//---------- Commands --------
	public void setGui(GuardDogGui g) {
		gui = g;
	}
	
	public void releaseSem(){
		guardDogSem.release();
	}
}
