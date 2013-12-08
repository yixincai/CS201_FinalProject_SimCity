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

	// --------------------------------------- DATA -------------------------------------------

	boolean _open;
	public List<BankTellerRole> _tellers = new ArrayList<BankTellerRole>();
	BankAnimationPanel _animationPanel;
	//complete
	private BankHostRole _bankHostRole;
	private Semaphore _tellerSemaphore = new Semaphore(1, true);
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	private GuardDog guardDog;
	

	// ------------------------------------------- CONSTRUCTORS & PROPERTIES --------------------------------------------
	
	public Bank(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super("Bank", wvb);
		this._animationPanel = (BankAnimationPanel)bp.getBuildingAnimation();
		List<BankTeller> tellers_for_host = new ArrayList<BankTeller>();
		BankTellerRole bankTellerRole = new BankTellerRole(null,this, 0);
		_tellers.add(bankTellerRole);
		tellers_for_host.add(bankTellerRole);
		_bankHostRole = new BankHostRole(null,this, tellers_for_host);
		guardDog = new GuardDog(this);
		guardDog.startThread();
	}
		
	public Bank() {
		super("Bank", null);
		BankTellerRole teller = new BankTellerRole(null,this, 0);
		_tellers.add(teller);
	}

	public BankAnimationPanel animationPanel() {
		return _animationPanel;
	}
	
	public BankHostRole host(){
		return _bankHostRole;
	}
	
	public void addTeller(PersonAgent p){ //
		_tellers.add(new BankTellerRole(p, this, _tellers.size()));
	}
	
	public List<BankTellerRole> tellers(){
		return _tellers;
	}
	
	
	
	// ----------------------------------------- UTILITIES --------------------------------------------

	public void updateBankStatus(){
		if (_tellers.isEmpty() || !_bankHostRole.active)
			_open = false;
		else
			_open = true;
	}
	
	
	
	// ----------------------------------- ROLE FACTORIES & ACQUIRES ---------------------------------------
	public void setClosed(){
		_open = false;
		for(int i = 0; i < _tellers.size(); i++){
			_tellers.get(i).cmdFinishAndLeave();
		}
		_bankHostRole.cmdFinishAndLeave();
	}
	
	public BankTellerRole tryAcquireTeller(PersonAgent person){
		if (_tellerSemaphore.tryAcquire()){
			_tellers.get(0).setPerson(person);
			return _tellers.get(0);
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
	
	public BankCustomerRole generateCustomerRole(PersonAgent person){
		return new BankCustomerRole(person, this);
	}
	
	public GuardDog getGuardDog(){
		return guardDog;
	}
}
