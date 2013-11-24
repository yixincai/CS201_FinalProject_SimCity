package city.home;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.Place;
import city.bank.gui.BankAnimationPanel;
import city.home.gui.HomeAnimationPanel;

public class House extends Place implements Home {
	
	// ------------------------- CONSTRUCTOR & PROPERTIES -----------------------------
	public House(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}
	public Place place() {
		return this;
	}
	
	HomeAnimationPanel _animationPanel;
	
	public House(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super("Home", wvb);
		this._animationPanel = (HomeAnimationPanel)bp.getBuildingAnimation();
	}
	
}
