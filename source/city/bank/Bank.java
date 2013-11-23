package city.bank;
import gui.Building;
import gui.BuildingPanel;

import java.util.*;

import city.PersonAgent;
import city.Place;
import city.bank.interfaces.BankTeller;

public class Bank extends Place {

	boolean open;
	public List<BankTellerRole> tellers;
	public BankHostRole host;
	
	public Bank(){
		super("Bank");
		BankTellerRole teller = new BankTellerRole(null,this);
		tellers.add(teller); //
		
		BankHostRole host = new BankHostRole(null,this, tellers);
	}
		
	public void updateBankStatus(){
		if (tellers.isEmpty() || host == null)
			open = false;
		else
			open = true;
	}
	
	public BankCustomerRole generateCustomerRole(PersonAgent p){
		return (new BankCustomerRole(p, p.accountNumber, this));
	}
	
	public void addTeller(PersonAgent p){ //
		tellers.add(new BankTellerRole(p, this));
	}
}
