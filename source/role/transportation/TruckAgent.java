package role.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import role.market.MarketCashierRole;
import agent.PersonAgent;

public class TruckAgent {
	List<Package> packages;
	Semaphore isMoving;
	MarketCashierRole marketCashier;
	Boolean out = false;

	enum packageState{inTruck, delivering, unloaded, done};

	class Package{
	    List<Item> items;
	    CookAgent cook;
	    int orderId;
	    double bill;
	    packageState pState = packageState.inTruck;
	}

	class Item{
	    String stock;
	    int amount;
	}
	
	TruckAgent(MarketCashierRole marketCashier){
		this.marketCashier = marketCashier;
	}
	
	public void msgHereAreGoodsForDelivery(int order_id, List<Item> items, CookAgent cook, double bill){
	    items.add(new Package(items, cook, order_id, bill));
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
	
	public boolean pickAndExecuteAnAction(){
		for(Package temp: packages){
			if(temp.pState == packageState.inTruck){
				DeliverToDestination(temp);
			}
			
			if(temp.pState == packageState.unloaded){
				RemoveFromList(temp);
			}
			
		}
		
		if(packages.isEmpty() && out == true){
			GoBackToMarket();
		}
        
	}
	
	public void DeliverToDestination(Package aPackage){
		out = true;
		aPackage.pState = packageState.delivering;
		Gui.goToDestination(cook);
		isMoving.acquire();
		aPackage.cook.msgOrderDelivered(aPackage.items, aPackage.orderId);
	}

	public void RemoveFromList(Package aPackage){
		packages.remove(aPackage);
	}

	public void GoBackToMarket(){
	    out = false;
	    Gui.goToMarket();
	    isMoving.acquire();
	    marketCashier.msgBackFromRun(this);
	}
}
