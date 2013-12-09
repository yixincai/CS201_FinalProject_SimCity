package city.transportation;

import gui.WorldView;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.market.*;
import city.restaurant.*;
import city.restaurant.omar.OmarRestaurant;
import city.restaurant.yixin.*;
import city.transportation.gui.*;
import city.transportation.interfaces.Truck;

public class TruckAgent extends Agent implements Truck{
	List<Package> packages = new ArrayList<Package>();
	String _name;
	Semaphore isMoving = new Semaphore(0, true);;
	Market _market;
	TruckAgentGui _gui;
	Boolean out = false;
	private Timer _loadingDelay = new Timer();
	public enum truckState{parkingLot, docking, drivingtoRestaurant, atRestaurant, drivingtoMarket};
	public truckState trState = truckState.parkingLot;
	
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
		_name = market.name() + "'s Truck";
		_market = market;
		_gui = new TruckAgentGui(this, _market);
		worldView.addGui(_gui);
	}
	
	//Testing
	public TruckAgent(Market market){
		_market = market;
		_gui = new TruckAgentGui(this, _market);
	}
	
	//Dummy constructor for Yixin's Market tests -- DO NOT CHANGE
	public TruckAgent(Market market, int dummy){
		_market = market;
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
			DeliverToDestination(temp);
			GoBackToMarket();
			return true;
		}
//		for(Package temp: packages){
//			if(temp.pState == packageState.inTruck && trState == truckState.docking){
//				DeliverToDestination(temp);
//				return true;
//			}
//			if(temp.pState == packageState.atMarket && trState == truckState.parkingLot){
//				PickFromDockFromParkingLot(temp);
//				return true;
//			}
//			if(temp.pState == packageState.atMarket && trState == truckState.atRestaurant){
//				PickFromDock(temp);
//				return true;
//			}
//		}
//		
//		if(packages.isEmpty() && out == true){
//			GoBackToMarket();
//			return true;
//		}
		return false;
	}
	
	//----------------------------------------------Actions------------------------------------------
//	public void PickFromDockFromParkingLot(Package aPackage){
//		trState = truckState.docking;
//		out = true;
//		_gui.goToDockFromParkingLot(_market);
//		print("Going to dock");
//		try {
//			isMoving.acquire();
//			//_market.msgPickUpItems();
//			print("Picked up");
//			aPackage.pState = packageState.inTruck;
//			stateChanged();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void PickFromDock(Package aPackage){
//		trState = truckState.docking;
//		out = true;
//		_gui.goToDock(aPackage._restaurant);
//		print("Going to dock");
//		try {
//			isMoving.acquire();
//			//_market.msgPickUpItems();
//			print("Picked up");
//			aPackage.pState = packageState.inTruck;
//			stateChanged();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void DeliverToDestination(Package aPackage){
		_gui.goToDestination(aPackage._restaurant);
		if(aPackage._restaurant instanceof OmarRestaurant) { 
			aPackage._restaurant.getCook().msgOrderFulfillment(_market, aPackage._items); } //Make sure GUI shows that it's dropped off !important!
		else if (aPackage._restaurant instanceof YixinRestaurant)
			aPackage._restaurant.getCook().msgOrderFulfillment(_market, aPackage._items); 
		print("Delivered to restaurant " + aPackage._restaurant.name());
		//trState = truckState.atRestaurant;
		packages.remove(aPackage);
		_loadingDelay.schedule(new TimerTask(){
			@Override
			public void run() {
				isMoving.release();
			}
		}, 5000);
		try{
			isMoving.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void GoBackToMarket(){
	    //out = false;
	    _gui.goToMarketParkingLot(_market);
	}
	
	public String name(){
		return _name;
	}
	
	public int getPackageListSize(){
		return packages.size();
	}
}
