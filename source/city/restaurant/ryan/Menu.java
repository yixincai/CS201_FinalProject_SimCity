package city.restaurant.ryan;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Menu {
	List<Option> options = new ArrayList<Option>();
	private String option;
	private double price;
	boolean stock = true;
	
	public Menu(){
		options.add(new Option("Steak", 15.00));
		options.add(new Option("Chicken", 10.00));
		options.add(new Option("Pizza", 8.00));
		options.add(new Option("Salad", 5.00));
	}
	
	public Menu(String option, double price){
		this.option = option;
		this.price = price;
	}
	
	public String getOption(){
		return option;
		
	}
	
	public double getPrice(String choice){
		for(Option temp: options){
			if(temp.choice.equals(choice)){
				return temp.price;
			}
		}
		return 0;
	}
	
	public void setStockEmpty(){
		stock = false;
	}
	
	public void setStockFull(){
		stock = true;
	}
	
	class Option{
		String choice;
		double price;
		boolean stock;
		
		Option(String choice, double price){
			this.choice = choice;
			this.price = price;
		}
		
	}
}
