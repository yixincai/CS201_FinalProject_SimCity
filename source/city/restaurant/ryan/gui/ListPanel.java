package city.restaurant.ryan.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.HostAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {
	
    public JScrollPane paneW =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private List<JCheckBox> wlist = new ArrayList<JCheckBox>();
    private JPanel viewW = new JPanel();
	private JPanel customerPanel = new JPanel();
	private JPanel waiterPanel = new JPanel();
	private JPanel addNewInfoW = new JPanel();
	private JPanel addNewNameW = new JPanel();
    private JTextField nameW = new JTextField(100);
    private JButton addPersonW = new JButton("Add");
    private JCheckBox currentWBox = new JCheckBox();
    
    public JScrollPane paneC =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel viewC = new JPanel();
    private List<JCheckBox> clist = new ArrayList<JCheckBox>();
    private JButton addPersonC = new JButton("Add");
    private JPanel addNewInfoC = new JPanel();
    private JPanel addNewNameC = new JPanel();
    private JTextField nameC = new JTextField(100);
    JCheckBox optionH = new JCheckBox("Hungry? ");
    private JCheckBox currentCBox = new JCheckBox();    
    
    private RyanWaiterRole currentWaiter;
    private RyanCustomerRole currentPerson;
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        currentPerson = null;
        
        setLayout(new GridLayout(1, 2, 20, 20));
        
        //CUSTOMERS
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        addNewInfoC.setLayout(new BoxLayout(addNewInfoC, BoxLayout.X_AXIS));
        addNewInfoC.setMaximumSize(new Dimension(200,100));
        
        JLabel askC = new JLabel("Name: ");
        addNewNameC.setLayout(new BoxLayout(addNewNameC, BoxLayout.X_AXIS));
        addNewNameC.setMaximumSize(new Dimension (300, 100));
        addNewNameC.add(askC);
        addNewNameC.add(nameC);
        
        addNewInfoC.add(addNewNameC);
        addPersonC.addActionListener(this);
        addNewInfoC.add(addPersonC);
        customerPanel.add(addNewInfoC);
        
        viewC.setLayout(new BoxLayout((Container) viewC, BoxLayout.Y_AXIS));
        paneC.setViewportView(viewC);
        customerPanel.add(paneC);
        
        add(customerPanel);
        
        //WAITERS
        waiterPanel.setLayout(new BoxLayout(waiterPanel, BoxLayout.Y_AXIS));
        waiterPanel.add(new JLabel("<html><pre> <u>" + "Waiter" + "</u><br></pre></html>"));
        
        addNewInfoW.setLayout(new BoxLayout(addNewInfoW, BoxLayout.X_AXIS));
        addNewInfoW.setMaximumSize(new Dimension(200,100));
        
        JLabel askW = new JLabel("Name: ");
        addNewNameW.setLayout(new BoxLayout(addNewNameW, BoxLayout.X_AXIS));
        addNewNameW.setMaximumSize(new Dimension (300, 100));
        addNewNameW.add(askW);
        addNewNameW.add(nameW);
        
        addNewInfoW.add(addNewNameW);
        addPersonW.addActionListener(this);
        addNewInfoW.add(addPersonW);
        waiterPanel.add(addNewInfoW);
        
        viewW.setLayout(new BoxLayout((Container) viewW, BoxLayout.Y_AXIS));
        paneW.setViewportView(viewW);
        waiterPanel.add(paneW);
        
        add(waiterPanel);
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonC) {
        	if(!nameC.getText().isEmpty()){
        		addCPerson(nameC.getText());
            	nameC.setText(null);
        	}
        }
        else if(e.getSource() == addPersonW){
        	if(!nameW.getText().isEmpty()){
        		addWPerson(nameW.getText());
        		nameW.setText(null);
        	}
        }
        else {
        	for (JCheckBox temp:clist){
                if (e.getSource() == temp) {
                		currentPerson = restPanel.matchCPerson( temp.getText());
                        currentPerson.getGui().setHungry();
                        temp.setEnabled(false);
                    }
            }
        	for (JCheckBox temp:wlist){
        		if(e.getSource() == temp){
        			currentWaiter = restPanel.matchWPerson(temp.getText());
        			currentWaiter.WantToGoOnBreak();
        			temp.setEnabled(false);
        		}
        	}
        }
    }
    
    public void CReenable(String customerName){
    	for (int i = 0; i < clist.size(); i++) {
            JCheckBox temp = clist.get(i);
            if (customerName.equals(temp.getText())){
            	currentCBox = temp;
            	currentCBox.setEnabled(true);
            	currentCBox.setSelected(false);
            }
    	}
    }
    
    public void WReenable(String waiterName){
    	for(int i = 0; i < wlist.size(); i++){
    		JCheckBox temp = wlist.get(i);
    		if(waiterName.equals(temp.getText())){
    			currentWBox = temp;
    			currentWBox.setEnabled(true);
    			currentWBox.setSelected(false);
    		}
    	}
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addWPerson(String name){
    	if (name != null || name != " " || name != "") {
    		JPanel PersonPanel = new JPanel();
            PersonPanel.setLayout(new FlowLayout());
            PersonPanel.setMaximumSize(new Dimension(300, 30));
            PersonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            
            JCheckBox WaiterBreak = new JCheckBox(name);
            WaiterBreak.addActionListener(this);
            PersonPanel.add(WaiterBreak);
            
            restPanel.addPerson("Waiters", name);
            wlist.add(WaiterBreak);
            viewW.add(PersonPanel);
            
            validate();
            /*
            restPanel.addPerson(type, name);
            //restPanel.showInfo(type, name);
            clist.add(PersonHungry);
            viewC.add(PersonPanel);
            */
    	}
    }
    
    public void addCPerson(String name) {
        if (name != null || name != " " || name != "") {
            JPanel PersonPanel = new JPanel();
            PersonPanel.setLayout(new FlowLayout());
            PersonPanel.setMaximumSize(new Dimension(300, 30));
            PersonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            
            JCheckBox PersonHungry = new JCheckBox(name);
            PersonHungry.addActionListener(this);
            PersonPanel.add(PersonHungry);
            
            restPanel.addPerson(type, name);
            //restPanel.showInfo(type, name);
            clist.add(PersonHungry);
            viewC.add(PersonPanel);
            
            validate();
            
            /*
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel*/
            
        }
    }
}
