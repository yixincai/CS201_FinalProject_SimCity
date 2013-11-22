package city.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Place;
import city.market.MarketCashierRole;
import city.restaurant.RestaurantCookRole;
import city.restaurant.yixin.YixinCookRole;
import city.transportation.gui.BusAgentGui;
import agent.PersonAgent;

public class TruckAgent {
	List<Package> packages;
	Semaphore isMoving;
	MarketCashierRole marketCashier;
	Place _market;
	BusAgentGui gui;
	Boolean out = false;

	enum packageState{inTruck, delivering, unloaded, done};

	class Package{
	    List<Item> items;
	    RestaurantCookRole cook;
	    int orderId;
	    double bill;
	    packageState pState = packageState.inTruck;
	    
	    Package(List<Item> items, RestaurantCookRole cook, int id, double bill){
	    	this.orderId = id;
	    	this.items=items;
	    	this.cook = cook;
	    	this.bill = bill;
	    }
	}

	class Item{
	    String stock;
	    int amount;
	}
	
	//Constructor
	TruckAgent(MarketCashierRole marketCashier){
		this.marketCashier = marketCashier;
	}
	
	//----------------------------------------------Messages------------------------------------------
	public void msgHereAreGoodsForDelivery(int order_id, List<Item> items, RestaurantCookRole cook, double bill){
	    packages.add(new Package(items, cook, order_id, bill));
	}
	
	public void msgAtDestination(){
	    isMoving.release();
	}

	public void msgGoodsUnloaded(int order_id){
	    for(Package aPackage : packages){
	        if(aPackage.orderId == order_id){
	        aPackage.pState = packageState.unloaded;}
	    }
	}

	public void msgAtMarket(){
	    isMoving.release();
	}
	
	//----------------------------------------------Scheduler------------------------------------------
	public boolean pickAndExecuteAnAction(){
		for(Package temp: packages){
			if(temp.pState == packageState.unloaded){
				RemoveFromList(temp);
				return true;
			}
			
			if(temp.pState == packageState.inTruck){
				DeliverToDestination(temp);
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
	public void DeliverToDestination(Package aPackage){
		out = true;
		aPackage.pState = packageState.delivering;
		gui.goToDestination(cook);
		isMoving.acquire();
		aPackage.cook.msgOrderDelivered(aPackage.items, aPackage.orderId);
	}

	public void RemoveFromList(Package aPackage){
		packages.remove(aPackage);
	}

	public void GoBackToMarket(){
	    out = false;
	    gui.goToMarket(_market);
	    isMoving.acquire();
	    marketCashier.msgBackFromRun(this);
	}
}
