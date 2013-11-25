package city.transportation;

import gui.WorldView;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.Place;
import city.market.Market;
import city.market.MarketCashierRole;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCookRole;
import city.restaurant.yixin.YixinCookRole;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.TruckAgentGui;
import city.transportation.interfaces.Truck;
import city.market.Item;

public class TruckAgent extends Agent implements Truck{
	List<Package> packages = new ArrayList<Package>();
	String _name;
	Semaphore isMoving = new Semaphore(0, true);;
	Market _market;
	TruckAgentGui _gui;
	Boolean out = false;
	
	enum truckState{parkingLot, docking, drivingtoRestaurant, atRestaurant, drivingtoMarket};
	truckState trState = truckState.parkingLot;
	
	enum packageState{atMarket, inTruck, delivering, unloaded, done};

	class Package{
	    List<Item> _items;
	    Restaurant _restaurant;
	    int orderId;
	    double bill;
	    packageState pState = packageState.atMarket;
	    
	    Package(List<Item> items, Restaurant restaurant){
	    	_items=items;
	    	_restaurant = restaurant;
	    	
	    }
	}
	
	//Constructor
	public TruckAgent(Market market, WorldView worldView){
		_name = market.getName() + "'s Truck";
		_market = market;
		_gui = new TruckAgentGui(this, _market);
		worldView.addGui(_gui);
	}
	
	public void setTruckAgentGui(TruckAgentGui gui){
		_gui = gui;
	}
	
	//----------------------------------------------Messages------------------------------------------
	public void msgDeliverToCook(List<Item> items, Restaurant restaurant){
	    packages.add(new Package(items, restaurant));
	    print("Package at market");
	    stateChanged();
	}
	
	public void msgAtDestination(){
	    isMoving.release();
	}

	public void msgAtMarket(){
	    isMoving.release();
	}
	
	//----------------------------------------------Scheduler------------------------------------------
	public boolean pickAndExecuteAnAction(){
		for(Package temp: packages){
			if(temp.pState == packageState.inTruck && trState == truckState.docking){
				DeliverToDestination(temp);
				return true;
			}
			if(temp.pState == packageState.atMarket && trState == truckState.parkingLot){
				PickFromDockFromParkingLot(temp);
				return true;
			}
			if(temp.pState == packageState.atMarket && trState == truckState.atRestaurant){
				PickFromDock(temp);
				return true;
			}
		}
		
		if(packages.isEmpty() && out == true){
			GoBackToMarket();
			return true;
		}
		return false;
	}
	
	//----------------------------------------------Actions------------------------------------------
	public void PickFromDockFromParkingLot(Package aPackage){
		trState = truckState.docking;
		out = true;
		_gui.goToDockFromParkingLot(_market);
		print("Going to dock");
		try {
			isMoving.acquire();
			//_market.msgPickUpItems();
			print("Picked up");
			aPackage.pState = packageState.inTruck;
			stateChanged();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void PickFromDock(Package aPackage){
		trState = truckState.docking;
		out = true;
		_gui.goToDockFrom(_market);
		print("Going to dock");
		try {
			isMoving.acquire();
			//_market.msgPickUpItems();
			print("Picked up");
			aPackage.pState = packageState.inTruck;
			stateChanged();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DeliverToDestination(Package aPackage){
		trState = truckState.drivingtoRestaurant;
		_gui.goToDestination(aPackage._restaurant);
		try {
			isMoving.acquire();
			//aPackage._restaurant.cook.msgOrderFulfillment(_market, aPackage._items); //Make sure GUI shows that it's dropped off !important!
			trState = truckState.atRestaurant;
			packages.remove(aPackage);
			stateChanged();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void GoBackToMarket(){
	    out = false;
	    _gui.goToMarketParkingLot(_market);
	    try {
			isMoving.acquire();
			trState = truckState.parkingLot;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public String getName(){
		return _name;
	}
}
