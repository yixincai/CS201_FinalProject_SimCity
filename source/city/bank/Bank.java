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
	public List<BankTellerRole> tellers = new ArrayList<BankTellerRole>();
	BankAnimationPanel _animationPanel;
	//complete
	private BankHostRole _bankHostRole;
	private Semaphore _tellerSemaphore = new Semaphore(1, true);
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	
	public Bank(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super("Bank", wvb);
		this._animationPanel = (BankAnimationPanel)bp.getBuildingAnimation();
		List<BankTeller> tellers_for_host = new ArrayList<BankTeller>();
		BankTellerRole bankTellerRole = new BankTellerRole(null,this, 0);
		tellers.add(bankTellerRole);
		tellers_for_host.add(bankTellerRole);
		_bankHostRole = new BankHostRole(null,this, tellers_for_host);
	}
		
	public Bank() {
		super("Bank", null);
		BankTellerRole teller = new BankTellerRole(null,this, 0);
		tellers.add(teller);
	}

	public BankAnimationPanel animationPanel() {
		return _animationPanel;
	}

	public void updateBankStatus(){
		if (tellers.isEmpty() || !_bankHostRole.active)
			open = false;
		else
			open = true;
	}
	
	public BankCustomerRole generateCustomerRole(PersonAgent p){
		return (new BankCustomerRole(p, this));
	}
	
	public void addTeller(PersonAgent p){ //
		tellers.add(new BankTellerRole(p, this, tellers.size()));
	}
	
	public List<BankTellerRole> getTellers(){
		return tellers;
	}
	
	public BankHostRole getHost(){
		return _bankHostRole;
	}
	
	
	
	// -------------------- FACTORIES/TRY-ACQUIRES ------------------
	
	public BankTellerRole tryAcquireTeller(PersonAgent person){
		if (_tellerSemaphore.tryAcquire()){
			tellers.get(0).setPerson(person);
			return tellers.get(0);
		}
		return null;
	}

	public BankHostRole tryAcquireHost(PersonAgent person){
		if (_hostSemaphore.tryAcquire()){
			_bankHostRole.setPerson(person);;
			return _bankHostRole;
		}
		return null;
	}
	
	public BankCustomerRole generateBankCustomerRole(PersonAgent person){
		return new BankCustomerRole(person, this);
	}
}
