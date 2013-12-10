package city.restaurant.eric;

import java.util.Vector;

import city.restaurant.eric.interfaces.EricWaiter;

public class EricRevolvingStand extends Object {
	private final int N = 50000;
	private Vector<Order> _theData = new Vector<Order>();
	
	public class Order {
		EricWaiter waiter;
		String choice;
		int table;
	}
	
	public int size() {
		return _theData.size();
	}
	
	public void addOrder(EricWaiter waiter, String choice, int table) {
		Order o = new Order();
		o.waiter = waiter;
		o.choice = choice;
		o.table = table;
		insert(o);
	}
	
	synchronized public void insert(Order data) {
		while (_theData.size() == N) {
			try{ 
				System.out.println("\tFull, waiting");
				wait(5000);						 // Full, wait to add
			} catch (InterruptedException e) {};
		}
			
		insert_item(data);
		if(_theData.size() == 1) {
			System.out.println("\tNot Empty, notify");
			notify();							   // Not empty, notify a 
													// waiting consumer
		}
	}
	
	synchronized public Order remove() {
		Order data;
		if(_theData.size() == 0){
			return null;
		}

		data = remove_item();
		if(_theData.size() == N-1){
			System.out.println("\tNot full, notify");
			notify(); // Not full, notify a waiting producer
		}
		return data;
	}
	
	private void insert_item(Order data){
		_theData.addElement(data);
	}
	
	private Order remove_item(){
		Order data = (Order) _theData.firstElement();
		_theData.removeElementAt(0);
		return data;
	}
}
