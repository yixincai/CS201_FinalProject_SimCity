package city.restaurant.omar;

public class Food {
		String type;
		double cookTime;
		int price;
		int inventoryAmount;
		
		Food(String type, double cookTime, int inventoryAmount){
			this.type = type;
			this.cookTime = cookTime;
			this.inventoryAmount = inventoryAmount;
		}
		
		public void decrementInventory(){
			inventoryAmount--;
		}
}
