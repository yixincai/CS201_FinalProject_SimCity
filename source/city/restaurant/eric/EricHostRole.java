package city.restaurant.eric;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.Collection;
import java.util.List;

import city.restaurant.eric.gui.EricAnimationConstants;
import agent.Agent;
import city.restaurant.eric.interfaces.*;

public class EricHostRole extends Agent implements Host
{
	// -------------------------------- DATA ---------------------------------------
	
	// Personal data:
	private String _name;
	
	// Correspondence:
	private Cashier _cashier;
	
	// Agent data:
	// Customer:
	private class MyCustomer
	{
		public Customer agent;
		public CustomerState state;
	}
	private enum CustomerState
	{
		// Checking debt
		CONFIRM_DEBT, CONFIRMING_DEBT, HAS_DEBT, PAYING_DEBT,
		// Checking debt the second time
		CONFIRM_DEBT_AGAIN, CONFIRMING_DEBT_AGAIN, SEND_AWAY,
		// Normative
		ARRIVED, WAITING, EATING
	}
	List<MyCustomer> _customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	// Table:
	private class Table
	{
		public Table(int num) { number = num; }
		public int number;
		public MyCustomer occupant = null; // null occupant means table is empty.
	}
	List<Table> _tables = Collections.synchronizedList(new ArrayList<Table>());
	// Waiter:
	private class MyWaiter
	{
		public Waiter agent;
		public WaiterState state;
		public int numberOfCustomers = 0;
	}
	private enum WaiterState { WORKING, ON_BREAK }
	List<MyWaiter> _waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	// The purpose of this list is so that waiters that ask for a break first will have higher priority when choosing which waiters can go on break.
	List<MyWaiter> _waitersThatWantABreak = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	
	// ---------------------------------------- PROPERTIES --------------------------------------
	public String getMaitreDName() { return _name; } // Uh, okay
	public String getName() { return _name; }
	public String toString() { return "host " + getName(); }
	public void setCashier(Cashier cashier) { _cashier = cashier; }
	//public List getWaitingCustomers() { return waitingCustomers; }
	//public Collection getTables() { return tables; }
	
	// -------------------------------------- CONSTRUCTOR ------------------------------------
	
	public EricHostRole(String name)
	{
		_name = name;
		
		// Initialize the hard-coded number of tables
		for(int i = 0; i < EricAnimationConstants.NUMBER_OF_TABLES; i++)
		{
			_tables.add(new Table(i));
		}
	}
	
	
	
	// ---------------------------------------- MESSAGES ---------------------------------------------
	
	/**
	 * Register new waiter agents.
	 * @param sender The sender of this message
	 */
	public void msgImOnDuty(Waiter sender)
	{
		MyWaiter w = new MyWaiter();
		
		w.agent = sender;
		w.state = WaiterState.WORKING;
		
		print(sender.getName() + " on duty.");

		_waiters.add(w);
		
		stateChanged();
	}

	/**
	 * Register new customers that come into the restaurant.
	 * @param sender The sender of this message
	 */
	public void msgIWantFood(Customer sender)
	{
		print("Customer " + sender.getName() + " wants food");
		
		// Check if sender has already paid debt
		for(MyCustomer c : _customers)
		{
			if(c.agent == sender && c.state == CustomerState.PAYING_DEBT)
			{
				c.state = CustomerState.CONFIRM_DEBT_AGAIN;
				stateChanged();
				return;
			}
		}
		
		MyCustomer newCustomer = new MyCustomer();
		
		newCustomer.agent = sender;
		newCustomer.state = CustomerState.CONFIRM_DEBT;

		_customers.add(newCustomer);

		stateChanged();
	}
	
	public void msgCustomerOwes(Customer customer, double owedAmount) // from Cashier
	{
		for(MyCustomer c : _customers)
		{
			if(c.agent == customer)
			{
				if(owedAmount > 0 && c.state == CustomerState.CONFIRMING_DEBT)
				{
					print(c.agent.getName() + " owes $" + owedAmount);
					c.state = CustomerState.HAS_DEBT;
					stateChanged();
					return;
				}
				else if(owedAmount > 0 && c.state == CustomerState.CONFIRMING_DEBT_AGAIN)
				{
					print(c.agent.getName() + " still owes $" + owedAmount + "; we will refuse service");
					c.state = CustomerState.SEND_AWAY;
					stateChanged();
					return;
				}
				else
				{
					print(c.agent.getName() + " doesn't owe anything.");
					c.state = CustomerState.ARRIVED;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgLeaving(Customer sender)
	{
		for(MyCustomer c : _customers)
		{
			if(c.agent == sender)
			{
				print(c.agent.getName() + " is leaving.");
				_customers.remove(c);
				stateChanged();
				return;
			}
		}
	}
	
	public void msgTableFree(Waiter sender, int tableNumber) // from waiter
	{
		for(MyWaiter w : _waiters)
		{
			if(w.agent == sender)
			{
				print("Waiter " + w.agent.getName() + " finished a customer");
				w.numberOfCustomers--;
				break;
			}
		}
		for(Table t : _tables)
		{
			if(t.number == tableNumber)
			{
				print("Table number " + t.number + " is free");
				_customers.remove(t.occupant);
				t.occupant = null;
				break;
			}
		}
		stateChanged();
	}

	public void msgIWantABreak(Waiter sender)
	{
		for(MyWaiter w : _waiters)
		{
			if(w.agent == sender)
			{
				print(w.agent.getName() + " wants a break.");
				_waitersThatWantABreak.add(w);
				stateChanged();
				return;
			}
		}
	}
	
	public void msgGoingBackToWork(Waiter sender)
	{
		for(MyWaiter w : _waiters)
		{
			if(w.agent == sender)
			{
				print(w.agent.getName() + " is coming back from a break.");
				w.state = WaiterState.WORKING;
				stateChanged();
				return;
			}
		}
	}
	
	
	
	// ----------------------------------------- SCHEDULER -------------------------------------------
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		synchronized(_customers)
		{
			for(MyCustomer c : _customers) {
				if(c.state == CustomerState.CONFIRM_DEBT || c.state == CustomerState.CONFIRM_DEBT_AGAIN) {
					actCheckDebt(c);
					return true;
				}
			}
		}

		synchronized(_customers) {
			for(MyCustomer c : _customers) {
				if(c.state == CustomerState.HAS_DEBT) {
					actSendCustomerToPayDebt(c);
					return true;
				}
			}
		}

		synchronized(_customers) {
			for(MyCustomer c : _customers) {
				if(c.state == CustomerState.SEND_AWAY) {
					actSendCustomerAway(c);
					return true;
				}
			}
		}

		// Must be a waiting customer, a free table, and a working waiter
		synchronized(_customers) {
			for(MyCustomer c : _customers) {
				if(c.state == CustomerState.ARRIVED || c.state == CustomerState.WAITING) {
					synchronized(_tables) {
						for(Table t : _tables) {
							if(t.occupant == null) {
								synchronized(_waiters) {
									// This is just to make sure that there is at least one working waiter (and that there are more than 0 waiters in the list)
									for(MyWaiter w : _waiters) {
										if(w.state == WaiterState.WORKING) {
											actAssignCustomer(c,t);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		synchronized(_tables) {
			int freeTables = 0;
			for(Table t : _tables) { if(t.occupant == null) freeTables++; }
			if(freeTables == 0)
			{
				for(MyCustomer c : _customers) {
					if(c.state == CustomerState.ARRIVED) {
						actTellCustomerRestaurantIsFull(c);
						return true;
					}
				}
			}
		}

		if(!_waitersThatWantABreak.isEmpty())
		{
			// Make sure there are at least 2 working waiters so that when one goes on break we'll have at least one.
			int numWorkingWaiters = 0;
			for(MyWaiter w : _waiters)
			{
				if(w.state == WaiterState.WORKING) numWorkingWaiters++;
			}
			if(numWorkingWaiters > 1)
			{
				actGiveBreak(_waitersThatWantABreak.get(0));
				return true;
			}
			else
			{
				actNoBreak(_waitersThatWantABreak.get(0));
				return true;
			}
		}
		
		return false;
	}
	
	

	// ---------------------------------------- ACTIONS -----------------------------------------
	
	private void actCheckDebt(MyCustomer c)
	{
		print("Checking if " + c.agent.getName() + " has debt");
		_cashier.msgDoesCustomerOwe(c.agent);
		if(c.state == CustomerState.CONFIRM_DEBT)
		{
			c.state = CustomerState.CONFIRMING_DEBT;
		}
		else if(c.state == CustomerState.CONFIRM_DEBT_AGAIN)
		{
			c.state = CustomerState.CONFIRMING_DEBT_AGAIN;
		}
	}
	
	private void actSendCustomerToPayDebt(MyCustomer c)
	{
		print("Sending " + c.agent.getName() + " to cashier to pay.");
		c.agent.msgGoToCashierAndPayDebt(_cashier);
		c.state = CustomerState.PAYING_DEBT;
	}
	
	private void actSendCustomerAway(MyCustomer c)
	{
		print("Sending customer " + c.agent.getName() + " away.");
		c.agent.msgWeWontServeYou();
		_customers.remove(c);
	}
	
	private void actTellCustomerRestaurantIsFull(MyCustomer c)
	{
		c.agent.msgRestaurantIsFull();
		c.state = CustomerState.WAITING;
	}
	
	private void actAssignCustomer(MyCustomer c, Table t)
	{
		print("Will seat customer " + c.agent.getName());
		
		t.occupant = c;
		c.state = CustomerState.EATING;
		
		// note: this function can only get called if there exists at least one on-duty waiter (and an empty table) so we don't have to make sure the waiterlist is full.
		
		// Find the waiter that is assigned the smallest number of customers.
		MyWaiter leastBusyWaiter = _waiters.get(0);
		int minNumberOfCustomers = Integer.MAX_VALUE;
		for(MyWaiter w : _waiters)
		{
			if(w.state == WaiterState.WORKING && w.numberOfCustomers < minNumberOfCustomers)
			{
				minNumberOfCustomers = w.numberOfCustomers;
				leastBusyWaiter = w;
			}
		}
		
		print("Assigning customer " + c.agent.getName() + " to waiter " + leastBusyWaiter.agent.getName());

		leastBusyWaiter.numberOfCustomers++;
		c.agent.msgComeToFrontDesk();
		leastBusyWaiter.agent.msgSeatCustomer(c.agent, t.number);
	}
	
	private void actNoBreak(MyWaiter w)
	{
		print("Denying a break for " + w.agent.getName());
		_waitersThatWantABreak.remove(w);
		w.agent.msgNoBreak();
	}
	
	private void actGiveBreak(MyWaiter w)
	{
		print("Giving " + w.agent.getName() + " a break");
		w.state = WaiterState.ON_BREAK;
		_waitersThatWantABreak.remove(w);
		w.agent.msgFinishCustomersGoOnBreak();
	}
}