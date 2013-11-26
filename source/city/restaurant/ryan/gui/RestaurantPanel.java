package city.restaurant.ryan.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.MarketAgent;
import restaurant.CashierAgent;
import restaurant.BankAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
    private RestaurantGui gui; //reference to main gui
	
    //Host, cook, waiters and customers
    public RyanHostRole host = new RyanHostRole("Host 1");
    private HostGui hostGui = new HostGui(host);
    public RyanCashierRole cashier = new RyanCashierRole("Cashier 1");
    private CashierGui cashierGui = new CashierGui(cashier, gui, new Dimension(200,20));
    private RyanCookRole cook = new RyanCookRole("Cook 1");
    private CookGui cookGui = new CookGui(cook, gui, new Dimension(550, 130));
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");
    private BankAgent bank = new BankAgent("Bank");

    private Vector<RyanCustomerRole> customers = new Vector<RyanCustomerRole>();
    private Vector<RyanWaiterRole> waiters = new Vector<RyanWaiterRole>();
    
    static int waiterPosition = 80;
    

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        host.setGui(hostGui);

        gui.animationPanel.addGui(hostGui);
        host.startThread();
        
        bank.startThread();
        
        cashier.setBank(bank);
        cashier.setGui(cashierGui);
        gui.animationPanel.addGui(cashierGui);
        cashier.startThread();
        
        gui.animationPanel.addGui(cookGui);
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        cook.setGui(cookGui);
        cook.startThread();
        
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        market1.setCook(cook);
        market2.setCook(cook);
        market3.setCook(cook);
        
        market1.startThread();
        market2.startThread();
        market3.startThread();

        setLayout(new GridLayout(1, 1, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        
        //initRestLabel();
        //add(restLabel);
        add(group);
        
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                RyanCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RyanCustomerRole c = new RyanCustomerRole(name, cashier);	
    		RyanCustomerGui cg = new RyanCustomerGui(c, gui);

    		gui.animationPanel.addGui(cg);// dw
    		c.setHost(host);
    		c.setGui(cg);
    		customers.add(c);
    		c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		waiterPosition += 25;
    		RyanWaiterRole w = new RyanWaiterRole(name, cook, cashier);	
    		WaiterGui wg = new WaiterGui(w, gui, 10, waiterPosition);

    		gui.animationPanel.addGui(wg);// dw
    		w.setGui(wg);
    		waiters.add(w);
    		w.setHost(host);
    		w.startThread();
    	}
    	
    }

    public void SetStockToZero(String choice){
    	cook.setStockToZero(choice);
    }
    
    public void AddCashToCashier(){
    	cashier.addCash(100);
    }
    
	public RyanCustomerRole matchCPerson( String name) {
		 RyanCustomerRole nobody = null;
	   	 for (int i = 0; i < customers.size(); i++) {
	         RyanCustomerRole temp = customers.get(i);
	         if (temp.getName() == name)
	             return temp;
	   	 }
		return nobody;
	}
	
	public RyanWaiterRole matchWPerson(String name){
		RyanWaiterRole nobody = null;
		for(int i = 0; i < waiters.size(); i++){
			RyanWaiterRole temp = waiters.get(i);
			if(temp.getName().equals(name)){
				return temp;
			}
		}
		return nobody;
	}

	public void middleCMan(String customerName){
		customerPanel.CReenable(customerName);
	}
	
	public void middleWMan(String waiterName){
		customerPanel.WReenable(waiterName);
	}
	
	public void pause(){
		host.pause();
		cook.pause();
		bank.pause();
		cashier.pause();
		market1.pause();
		market2.pause();
		market3.pause();
		for (int i = 0; i < customers.size(); i++) {
	         RyanCustomerRole temp = customers.get(i);
	         temp.pause();
	   	 }
		for (int i = 0; i < waiters.size(); i++) {
	         RyanWaiterRole temp = waiters.get(i);
	         temp.pause();
	   	 }
	}
	
	public void restart(){
		host.restart();
		cook.restart();
		bank.restart();
		cashier.restart();
		market1.restart();
		market2.restart();
		market3.restart();
		for (int i = 0; i < customers.size(); i++) {
	         RyanCustomerRole temp = customers.get(i);
	         temp.restart();
	   	 }
		for (int i = 0; i < waiters.size(); i++) {
	         RyanWaiterRole temp = waiters.get(i);
	         temp.restart();
	   	 }
	}
	
	public String getHostName(){
		return host.getName();
	}
}