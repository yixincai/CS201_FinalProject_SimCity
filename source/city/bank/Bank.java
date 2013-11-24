package city.bank;
import gui.WorldViewBuilding;
import gui.BuildingInteriorAnimationPanel;

import java.util.*;

import city.PersonAgent;
import city.Place;
import city.bank.gui.BankAnimationPanel;
import city.bank.interfaces.BankTeller;
import city.restaurant.yixin.gui.YixinAnimationPanel;

public class Bank extends Place {

	boolean open;
	public List<BankTellerRole> tellers;
	public BankHostRole host;
	BankAnimationPanel _animationPanel;
	
	public Bank(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super("Bank", wvb);
		this._animationPanel = (BankAnimationPanel)bp.getBuildingAnimation();
		List<BankTeller> tellers_for_host = new ArrayList<BankTeller>();
		BankTellerRole teller = new BankTellerRole(null,this, 0);
		tellers.add(teller);
		tellers_for_host.add(teller);
		host = new BankHostRole(null,this, tellers_for_host);
	}
		
	public Bank() {
		super("Bank", null);
	}

	public void updateBankStatus(){
		if (tellers.isEmpty() || !host.active)
			open = false;
		else
			open = true;
	}
	
	public BankCustomerRole generateCustomerRole(PersonAgent p){
		return (new BankCustomerRole(p, p.getAccountNumber(), this));
	}
	
	public void addTeller(PersonAgent p){ //
		tellers.add(new BankTellerRole(p, this, tellers.size()));
	}
	
	public List<BankTellerRole> getTellers(){
		return tellers;
	}
}
