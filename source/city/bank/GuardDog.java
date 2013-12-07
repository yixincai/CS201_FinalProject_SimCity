package city.bank;

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
		this.bank = bank;
		bank.animationPanel().addGui(gui);
		command = Command.None;
		this.gui = new GuardDogGui(this);
	}

	//Data
	Bank bank;
	List<BankCustomerRole> hitList = new ArrayList<BankCustomerRole>();
	GuardDogGui gui;
	
	Semaphore guardDogSem = new Semaphore(0,true);
	
	Command command;
	
	enum Command{None, Kill};
	
	//Messages
	public void sicEm(List<BankCustomerRole> robbers){
		command = Command.Kill;
	}

	
	//Scheduler
	public boolean pickAndExecuteAnAction(){
		if(hitList.size() >= 0){
			attack(hitList.get(0));
			return true;
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
		command = Command.None;
		hitList.remove(0);
	}
	
	//---------- Commands --------
	public void setGui(GuardDogGui g) {
		gui = g;
	}
	
	public void releaseSem(){
		guardDogSem.release();
	}
}
