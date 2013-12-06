package city.restaurant.eric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;
import city.restaurant.eric.interfaces.*;

public class EricCashierRole extends Agent implements Cashier
{
	// ----------------------------------------- DATA ----------------------------------------------
	
	// Personal data:
	private String _name;
	
	// Correspondence:
	private Host _host;
	
	// Agent data:
	private double _money = 140;
	// Public for TEST:
	public class Bill
	{
		public Check check;
		public BillState state;
		public Waiter waiter = null;
		public Customer customer = null;
		public double paidAmount = 0; // the amount paid, which needs to be processed.
		public double owedAmount = 0;
		public String toString() { return "owedAmount: " + this.owedAmount + "; paidAmount: " + this.paidAmount + "; waiter: " + this.waiter.getName() + "; state: " + this.state + "; customer: " + this.customer.name() + "."; }
	}
	// Public for TEST:
	public enum BillState { REQUESTED, WAITING_FOR_PAYMENT, PAID_NEEDS_CHANGE, OWED, NOTIFY_HOST_OWED, PAY_DEBT_NEEDS_CHANGE }
	private List<Bill> _bills = Collections.synchronizedList(new ArrayList<Bill>());
	// Public for TEST:
	public class MarketBill
	{
		public double amountOwed;
		public Market market;
	}
	private List<MarketBill> _marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	
	// ------------------------------------------ CONSTRUCTOR ----------------------------------------------
	public EricCashierRole(String name)
	{
		_name = name;
	}

	// ----------------------------------------- PROPERTIES ----------------------------------------------
	public String getName() { return _name; }
	public String toString() { return "cashier " + getName(); }
	public void setHost(Host host) { _host = host; }
	
	// ------------------------------------------- TEST PROPERTIES ------------------------------------------------
	public List<Bill> bills() { return _bills; }
	public double money() { return _money; }
	public void setMoney(double money) { _money = money; }
	public List<MarketBill> marketBills() { return _marketBills; }
	
	
	
	// ----------------------------------------- MESSAGES ----------------------------------------------
	
	public void msgGiveMeCheck(Waiter sender, String choice, int table)
	{
		Bill b = new Bill();
		b.check = new Check(choice, table);
		b.state = BillState.REQUESTED;
		b.waiter = sender;
		b.owedAmount = b.check.price();
		
		_bills.add(b);
		
		stateChanged();
	}
	
	public void msgHereIsMoney(Customer sender, double money, Check c)
	{
		for(Bill b : _bills)
		{
			if(b.check == c) // may want to change this to .equals(c) later, if passing copies around rather than references.
			{
				print("Received $" + money + " from " + sender.name());
				b.customer = sender;
				b.paidAmount = money;
				b.state = BillState.PAID_NEEDS_CHANGE;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgDoesCustomerOwe(Customer customer) // from Host
	{
		for(Bill b : _bills)
		{
			if(b.customer == customer)
			{
				b.state = BillState.NOTIFY_HOST_OWED;
				stateChanged();
				return; // this is important because it skips 
			}
		}
		
		// If we haven't seen this customer before, make a new check with a balance of zero
		Bill b = new Bill();
		b.customer = customer;
		b.state = BillState.NOTIFY_HOST_OWED;
		b.owedAmount = 0;
		_bills.add(b);
		stateChanged();
	}
	
	public void msgHereIsOwedMoney(Customer sender, double money)
	{
		for(Bill b : _bills)
		{
			if(b.customer == sender)
			{
				print("Received " + money + " from " + sender.name());
				b.paidAmount = money;
				b.state = BillState.PAY_DEBT_NEEDS_CHANGE;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgYouOwe(Market sender, double amount)
	{
		MarketBill b = new MarketBill();
		b.amountOwed = amount;
		b.market = sender;
		_marketBills.add(b);
		stateChanged();
	}
	
	
	
	// ----------------------------------------- SCHEDULER ----------------------------------------------
	// Note: this function is public in order to TEST
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.NOTIFY_HOST_OWED) {
					actNotifyHostCustomerOwes(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.REQUESTED) {
					actGiveWaiterCheck(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.PAID_NEEDS_CHANGE) {
					actGiveChange(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.PAY_DEBT_NEEDS_CHANGE) {
					actDebtGiveChange(b);
					return true;
				}
			}
		}
		synchronized(_marketBills) {
			if(_money > 0) {
				for(MarketBill b : _marketBills) {
					actMakeMarketPayment(b);
					return true;
				}
			}
		}
		return false;
	}
	
	

	// ----------------------------------------- ACTIONS ----------------------------------------------
	
	private void actGiveWaiterCheck(Bill b)
	{
		logThis("actGiveWaiterCheck");
		
		print("Giving check to " + b.waiter.getName());
		b.state = BillState.WAITING_FOR_PAYMENT;
		b.waiter.msgHereIsCheck(b.check);
	}
	
	private void actGiveChange(Bill b)
	{
		logThis("actGiveChange");
		
		double change = b.paidAmount - b.check.price();
		b.owedAmount = b.check.price() - b.paidAmount;
		if(b.owedAmount < 0) b.owedAmount = 0;
		if(change < 0) change = 0;
		_money += b.paidAmount - change;
		b.paidAmount = 0;
		
		print("Giving $" + change + " in change to customer " + b.customer.name());
		b.customer.msgHereIsChange(change);
		
		if(b.owedAmount > 0)
		{
			print(b.customer.name() + " still owes " + b.owedAmount);
			b.state = BillState.OWED;
			b.waiter = null; // because the customer is leaving and will no longer be assigned to a waiter
			b.check = null; // because the customer is leaving and all we care about now is his debt
		}
		else
		{
			_bills.remove(b);
		}
	}
	
	private void actNotifyHostCustomerOwes(Bill b)
	{
		logThis("actNotifyHostCustomerOwes");
		
		if(b.owedAmount == 0)
		{
			print("Notifying " + _host.getName() + " that " + b.customer.name() + " does not owe anything.");
			_bills.remove(b);
		}
		else
		{
			print("Notifying " + _host.getName() + " that " + b.customer.name() + " owes $" + b.owedAmount);
			b.state = BillState.OWED;
		}
		_host.msgCustomerOwes(b.customer, b.owedAmount);
	}
	
	private void actDebtGiveChange(Bill b)
	{
		logThis("actDebtGiveChange");
		
		double change = b.paidAmount - b.owedAmount;
		b.owedAmount = b.owedAmount - b.paidAmount;
		if(b.owedAmount < 0) b.owedAmount = 0;
		if(change < 0) change = 0;
		_money += b.paidAmount - change;
		b.paidAmount = 0;
		
		print("Giving $" + change + " in change to customer " + b.customer.name());
		b.customer.msgHereIsChange(change);
		
		if(b.owedAmount > 0)
		{
			b.state = BillState.OWED;
			// don't need to set b.waiter and b.check to null because we already did that
		}
		else
		{
			_bills.remove(b);
		}
	}
	
	private void actMakeMarketPayment(MarketBill b)
	{
		logThis("actMakeMarketPayment");
		
		// Choose the amount to pay:
		double amountToPay = b.amountOwed;
		if(_money < amountToPay) {
			amountToPay = _money;
		}

		print("Paying $" + amountToPay + " to " + b.market.getName() + ".");
		
		// Send the payment
		b.amountOwed -= amountToPay;
		_money -= amountToPay;
		if(b.amountOwed < .01) {
			_marketBills.remove(b);
		}
		b.market.msgPayment(this, amountToPay);
	}
}
