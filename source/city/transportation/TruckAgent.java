package city.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Place;
import city.market.Market;
import city.market.MarketCashierRole;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCookRole;
import city.restaurant.yixin.YixinCookRole;
import city.transportation.gui.BusAgentGui;
import city.market.Item;

public class TruckAgent {
	List<Package> packages;
	Semaphore isMoving;
	Market _market;
	BusAgentGui gui;
	Boolean out = false;

	enum packageState{inTruck, delivering, unloaded, done};

	class Package{
	    List<Item> _items;
	    Restaurant _restaurant;
	    int orderId;
	    double bill;
	    packageState pState = packageState.inTruck;
	    
	    Package(List<Item> items, Restaurant restaurant){
	    	_items=items;
	    	_restaurant = restaurant;
	    	
	    }
	}
	
	//Constructor
	TruckAgent(Market market){
		_market = market;
	}
	
	//----------------------------------------------Messages------------------------------------------
	public void msgHereAreGoodsForDelivery(List<Item> items, Restaurant restaurant){
	    packages.add(new Package(items, restaurant));
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
		gui.goToDestination(aPackage._restaurant);
		isMoving.acquire();
		aPackage.cook.msgOrderDelivered(aPackage._items, _market);
	}

	public void RemoveFromList(Package aPackage){
		packages.remove(aPackage);
	}

	public void GoBackToMarket(){
	    out = false;
	    gui.goToMarket(_market);
	    isMoving.acquire();
	    _market.MarketCashier.msgBackFromRun(this);
	}
}
