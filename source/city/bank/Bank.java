package city.bank;
import gui.WorldViewBuilding;
import gui.BuildingInteriorAnimationPanel;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.bank.gui.BankAnimationPanel;
import city.bank.interfaces.*;
import city.interfaces.PlaceWithAnimation;

public class Bank extends Place implements PlaceWithAnimation {

	boolean open;
	public List<BankTellerRole> tellers;
	public BankHostRole host;
	BankAnimationPanel _animationPanel;
	
	private BankTellerRole _bankTellerRole;
	private BankHostRole _bankHostRole;
	private Semaphore _tellerSemaphore = new Semaphore(1, true);
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	
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

	public BankAnimationPanel getAnimationPanel() {
		return _animationPanel;
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
	
	
	
	// -------------------- FACTORIES/TRY-ACQUIRES ------------------
	
	public BankTellerRole tryAcquireTeller(){
		if (_tellerSemaphore.tryAcquire()){
			return _bankTellerRole;
		}
		return null;
	}

	public BankHostRole tryAcquireHost(){
		if (_hostSemaphore.tryAcquire()){
			return _bankHostRole;
		}
		return null;
	}
	
	public BankCustomerRole generateBankCustomerRole(PersonAgent person){
		return new BankCustomerRole(person, -1, this);
	}
}
