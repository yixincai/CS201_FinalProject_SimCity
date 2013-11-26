package city.restaurant.ryan;

import java.util.Vector;

public class RyanRevolvingStand {
	private final int N = 50000;
    private int count = 0;
    private Vector<RyanOrder> theData = new Vector<RyanOrder>();
    
    public int getSize(){
    	return count;
    }
    
    synchronized public void insert(RyanOrder data) {
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                         // Full, wait to add
            } catch (InterruptedException ex) {};
        }
            
        insert_item(data);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a 
                                                    // waiting consumer
        }
    }
    
    synchronized public RyanOrder remove() {
        RyanOrder data;
        if(count == 0){
        	return null;
        }

        data = remove_item();
        count--;
        if(count == N-1){ 
            System.out.println("\tNot full, notify");
            notify();                               // Not full, notify a 
                                                    // waiting producer
        }
        return data;
    }
    
    private void insert_item(RyanOrder data){
        theData.addElement(data);
    }
    
    private RyanOrder remove_item(){
        RyanOrder data = (RyanOrder) theData.firstElement();
        theData.removeElementAt(0);
        return data;
    }
    
    public RyanRevolvingStand(){
        theData = new Vector<RyanOrder>();
    }
}
