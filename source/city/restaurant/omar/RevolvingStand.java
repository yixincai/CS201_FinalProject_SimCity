package city.restaurant.omar;

import java.util.Vector;

public class RevolvingStand extends Object {
    private final int N = 50000;
    private int count = 0;
    private Vector<FoodTicket> theData = new Vector<FoodTicket>();
    
    public int getSize(){
    	return count;
    }
    
    synchronized public void insert(FoodTicket data) {
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
            notify();                     //Not empty
        }
    }
    
    synchronized public FoodTicket remove() {
        FoodTicket data;
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
    
    private void insert_item(FoodTicket data){
        theData.addElement(data);
    }
    
    private FoodTicket remove_item(){
        FoodTicket data = (FoodTicket) theData.firstElement();
        theData.removeElementAt(0);
        return data;
    }
    
    public RevolvingStand(){
        theData = new Vector<FoodTicket>();
    }
}