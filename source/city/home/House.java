package city.home;

import javax.swing.JPanel;

import gui.BuildingInteriorAnimationPanel;
import gui.WorldViewBuilding;
import city.Place;
import city.bank.gui.BankAnimationPanel;
import city.home.gui.HomeAnimationPanel;
import city.interfaces.PlaceWithAnimation;

public class House extends Place implements Home, PlaceWithAnimation {
	
	// ------------------------------------- DATA -----------------------------------
	HomeAnimationPanel _animationPanel;
	
	
	
	// ------------------------- CONSTRUCTOR & PROPERTIES -----------------------------
	public House(String name, WorldViewBuilding worldViewBuilding) {
		super(name, worldViewBuilding);
	}
	public House(String name, WorldViewBuilding wvb, BuildingInteriorAnimationPanel bp){
		super("Home", wvb);
		_animationPanel = (HomeAnimationPanel)bp.getBuildingAnimation();
	}
	public Place place() {
		return this;
	}
	public JPanel getAnimationPanel() {
		return _animationPanel;
	}
	
}
