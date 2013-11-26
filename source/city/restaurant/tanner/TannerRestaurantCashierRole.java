package city.restaurant.tanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utilities.LoggedEvent;
import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.market.interfaces.MarketCashier;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.tanner.Bill.BillState;
import city.restaurant.tanner.MarketBill.MarketBillState;
import city.restaurant.tanner.gui.TannerRestaurantCashierRoleGui;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;

public class TannerRestaurantCashierRole extends RestaurantCashierRole implements TannerRestaurantCashier
{
	
//---------------------------------------------Data-----------------------------------------------------------
	
	TannerRestaurantCashierRoleGui myGui;
	String name_;
	public double myMoney;
	public double bankBalance;
	public double bankDebt;
	public List<Bill> bills;
	public List<MarketBill> marketBills;
	public utilities.EventLog log = new utilities.EventLog();
	public TannerRestaurantCook cook;
	TannerRestaurant restaurant;
	public enum MoneyState {OrderedFromBank, none};
	public MoneyState money_state;
	
	
//------------------------------------------Constructor-------------------------------------------------------
	
	public TannerRestaurantCashierRole(PersonAgent person, TannerRestaurant rest, String name) 
	{
		super(person);
		this.restaurant = rest;
		myMoney = restaurant.getMoney();
		bills = new ArrayList<Bill>();
		marketBills = new ArrayList<MarketBill>();
		name_ = name;
	}
	
	
//------------------------------------------Accessors-----------------------------------------------------------
	
	@Override
	public Place place() 
	{
		return this.restaurant;
	}

	public void setGui(TannerRestaurantCashierRoleGui g)
	{
		this.myGui = g;
	}
	
	public void setCook(TannerRestaurantCook cook) 
	{
		this.cook = cook;
	}

//------------------------------------------Messages-----------------------------------------------------------
	
	
	@Override
	public void msgHereIsMyPayment(double billAmount, double myMoney, TannerRestaurantCustomer c) 
	{
		synchronized(bills)
		{
			for(int i = 0; i < bills.size(); i++)
			{
				if(bills.get(i).customer == c)
				{
					bills.get(i).changeDue = myMoney - billAmount;
					bills.get(i).state = city.restaurant.tanner.Bill.BillState.paid;
					bills.get(i).paidInFull = true;
					break;
				}
			}
		}
		stateChanged();		
	}
	
	
	@Override
	public void msgIDontHaveEnoughMoney(double billAmount, double myMoney, TannerRestaurantCustomer c) 
	{
		synchronized(bills)
		{
			for(int i = 0; i < bills.size(); i++)
			{
				if(bills.get(i).customer == c)
				{
					bills.get(i).state = city.restaurant.tanner.Bill.BillState.paid;
					bills.get(i).paidInFull = false;
					bills.get(i).amountShort = billAmount - myMoney;
					break;
				}
			}
		}
		stateChanged();		
	}
	@Override
	public void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list) 
	{
		print("Market bill received with amount of " + bill);
		log.add(new LoggedEvent("Received HereIsTheBill from market. Bill = "+ bill));
		marketBills.add(new MarketBill(bill, m, price_list));
		stateChanged();
	}	

	public void msgHereIsTheInvoice(Market m, List<Item> invoice) {
		for (MarketBill bill : marketBills){
			if (bill.market == m){
				bill.billState = MarketBill.MarketBillState.invoiceReceived;
				bill.invoice = invoice;
			}
		}
		stateChanged();		
	}
	
	public void msgHereIsTheChange(Market m, double change){
		print("Market change received with amount of " + change);
		myMoney += change;
		for (MarketBill bill : marketBills){
			if (bill.market == m)
				bill.billState = MarketBill.MarketBillState.changeReceived;
		}
		stateChanged();
	}


	@Override
	public void msgTransactionComplete(double amount, Double balance, Double debt, int newAccountNumber) 
	{
		restaurant.updateAccountNumber(newAccountNumber);
		money_state = MoneyState.none;
		myMoney = amount;
		bankBalance = balance;
		bankDebt = debt;
	}

	@Override
	public void msgComputeBill(int choice, TannerRestaurantCustomer customer, TannerRestaurantWaiter w) 
	{
		bills.add(new Bill(choice, customer, w, restaurant));
		stateChanged();		
	}
	
	@Override
	public void msgHereIsAMarketBill(double amount, MarketCashier m) 
	{
		//Irrelevant now
	}


	@Override
	public void msgHereIsYourChange(double change, MarketCashier m) 
	{
		//Irrelevant now		
	}




	
//-----------------------------------------Scheduler-----------------------------------------------------------
	
	@Override
	public boolean pickAndExecuteAnAction() 
	{
		if(marketBills.size() >0)
		{
			if(myMoney > 0)
			{
				synchronized(marketBills)
				{
					for(int i = 0; i < marketBills.size(); i++)
					{
						if(marketBills.get(i).billState == MarketBillState.invoiceReceived)
						{
							PayMarketBill(marketBills.get(i));
							return true;
						}
					}
				}
				
				synchronized(marketBills)
				{
					for(int i = 0; i < marketBills.size(); i++)
					{
						if(marketBills.get(i).billState == MarketBillState.changeReceived)
						{
							
						}
					}
				}
			}
		}
		else if(bills.size() > 0)
		{
			synchronized(bills)
			{
				for(int i = 0; i < bills.size(); i++)
				{
					if(bills.get(i).state == BillState.computed)
					{
						ComputeBill(bills.get(i));
						return true;
					}
				}
			}
			
			synchronized(bills)
			{
				for(int i = 0; i < bills.size(); i++)
				{
					if(bills.get(i).state == BillState.paid)
					{
						SettleUp(bills.get(i));
						return true;
					}
				}
			}
			
			synchronized(bills)
			{
				for(int i = 0; i < bills.size(); i++)
				{
					if(bills.get(i).state == BillState.settled)
					{
						bills.remove(bills.get(i));
						return true;
					}
				}
			}
		}
	}
	
//-----------------------------------------Actions-------------------------------------------------------------
	
	private void ComputeBill(Bill b)
	{
		print("Compute bill");
		log.add(new LoggedEvent("Compute Bill"));
		b.state = BillState.pending;
		b.waiter.msgHereIsTheChek(b.amount, b.customer);
	}
	
	private void SettleUp(Bill b)
	{
		if(b.paidInFull == true)
		{
			log.add(new LoggedEvent("Giving change to customer"));
			b.customer.msgHereIsYourChange(b.changeDue);
			b.state = BillState.paid;
		}
		
		else if(b.paidInFull == false)
		{
			log.add(new LoggedEvent("Telling customer he/she owes us"));
			b.customer.msgYouOweUs(b.amountShort);
			b.state = BillState.paid;
		}
	}
	
	private void PayMarketBill(MarketBill mb)
	{
		double amount = 0;
		for (Item item : mb.invoice)
			amount += (item.amount * mb.price_list.get(item.name));
		if (Math.abs(mb.amount - amount) > 0.02)
			print("Incorrect bill calculation by market");
		else 
			print("Correct bill calculation by market. Paying Market Bill");
		if (myMoney >= mb.amount){
			myMoney -= mb.amount;
			print("Remaining money is " + myMoney);
			mb.market.MarketCashier.msgHereIsPayment(restaurant, mb.amount);
			mb.billState = MarketBillState.none;
		}
		else {
			marketBills.get(0).amount -= myMoney;
			mb.market.MarketCashier.msgHereIsPayment(restaurant, myMoney);
			myMoney = 0;
			print("Do not have enough money with " + mb.amount +" debt");
		}
	}
//-----------------------------------------Commands------------------------------------------------------------
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub

	}

}

//--------------------------------------Utilities------------------------------------------------------
class MarketBill
{
	double amount;
	Market market;
	public Map<String, Double> price_list;
	enum MarketBillState {none, invoiceReceived, changeReceived}
	MarketBillState billState;
	List<Item> invoice;
	
	public MarketBill(double a, Market m, Map<String, Double> invoice)
	{
		price_list = invoice;
		amount = a;
		market = m;
		billState = MarketBillState.none;
	}
}
