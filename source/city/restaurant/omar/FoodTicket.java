package city.restaurant.omar;

public class FoodTicket {
	OmarCustomerRole c;
	OmarWaiterRole w;
	public FoodTicket(OmarWaiterRole w, OmarCustomerRole c){
		this.w = w;
		this.c = c;
	}
	
	public OmarCustomerRole getC(){
		return c;
	}
	
	public OmarWaiterRole getW(){
		return w;
	}
}
