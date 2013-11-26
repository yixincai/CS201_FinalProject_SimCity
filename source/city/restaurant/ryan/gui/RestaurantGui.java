package city.restaurant.ryan.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	RyanAnimationPanel animationPanel = new RyanAnimationPanel();
	static private JPanel menuPanel = new JPanel();
	
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    static private JPanel picPanel = new JPanel();
    static private JLabel picLabel = new JLabel();
    
    static private JPanel optionPanel = new JPanel();
    static private JButton pauseButton = new JButton();
    static private JButton steakButton = new JButton();
    static private JButton chickenButton = new JButton();
    static private JButton pizzaButton = new JButton();
    static private JButton saladButton = new JButton();
    static private JButton cashButton = new JButton();
    boolean clicked = false;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    //private JPanel infoPanel;
    //private JLabel infoLabel; //part of infoPanel
    //private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1000;
        int WINDOWY = 800;

    	setBounds(50, 50, WINDOWX, WINDOWY);
    	setLayout(new GridBagLayout());
        GridBagConstraints constrain = new GridBagConstraints();
        
        Dimension picDim1 = new Dimension((int)(WINDOWX*0.2), (int) (WINDOWY*0.25));
        Dimension picDim2 = new Dimension((int)(WINDOWX*0.2), (int) (WINDOWY*0.1));
        picPanel = new JPanel();
        picPanel.setPreferredSize(picDim1);
        picPanel.setMinimumSize(picDim1);
        picPanel.setMaximumSize(picDim1);
        picPanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createRaisedBevelBorder(), "Picture of Restaurant Owner"));
        picLabel = new JLabel();
        picLabel.setSize(picDim2);
        picLabel.setText("<html><pre><i>Ryan Hsu: </i></pre?</html>");
        ImageIcon picture = new ImageIcon(this.getClass().getResource("/resources/picture.jpeg"));
        JLabel pictureLabel = new JLabel(picture);
        picPanel.add(picLabel);
        picPanel.add(pictureLabel);
        
        Dimension menuDim = new Dimension(100, (int) (500));
        menuPanel.setPreferredSize(menuDim);
        menuPanel.setMinimumSize(menuDim);
        menuPanel.setMaximumSize(menuDim);
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        menuPanel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + restPanel.host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        menuPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        menuPanel.add(label, BorderLayout.CENTER);
        menuPanel.add(new JLabel("               "), BorderLayout.EAST);
        menuPanel.add(new JLabel("               "), BorderLayout.WEST);
        
        Dimension aniDim = new Dimension((int) (WINDOWX*0.6), (int) (WINDOWY*0.5));
        animationPanel.setPreferredSize(aniDim);
        animationPanel.setMinimumSize(aniDim);
        animationPanel.setMaximumSize(aniDim);
        
        Dimension optDim = new Dimension((int) (WINDOWX*0.2), (int) (WINDOWY*0.5));
        optionPanel.setPreferredSize(optDim);
        optionPanel.setMinimumSize(optDim);
        optionPanel.setMaximumSize(optDim);
        optionPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.add(new JLabel("            Options Menu", JLabel.CENTER));
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        optionPanel.add(pauseButton);
        optionPanel.add(new JLabel("Set Inventory To 0", JLabel.CENTER));
        steakButton = new JButton("Steak");
        steakButton.addActionListener(this);
        optionPanel.add(steakButton);
        chickenButton = new JButton("Chicken");
        chickenButton.addActionListener(this);
        optionPanel.add(chickenButton);
        pizzaButton = new JButton("Pizza");
        pizzaButton.addActionListener(this);
        optionPanel.add(pizzaButton);
        saladButton = new JButton("Salad");
        saladButton.addActionListener(this);
        optionPanel.add(saladButton);
        cashButton = new JButton("Add 100 Cash to Register");
        cashButton.addActionListener(this);
        optionPanel.add(cashButton);
        
        Dimension restDim = new Dimension((int)(WINDOWX*0.6), (int) (WINDOWY * .4));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        restPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        constrain.fill = GridBagConstraints.BOTH;
        constrain.weightx = 0.5;
        constrain.weighty = 0.5;
        constrain.gridx = 0;
        constrain.gridy = 0;
        constrain.gridwidth = 1;
        add(picPanel, constrain);
        
        constrain.fill = GridBagConstraints.BOTH;
        constrain.gridx = 0;
        constrain.gridy = 1;
        add(menuPanel, constrain);
        
        constrain.fill = GridBagConstraints.BOTH;
        constrain.gridx = 1;
        constrain.gridy = 0;
        constrain.gridwidth = 1;
        constrain.gridheight = 2;
        add(animationPanel, constrain);
        
        constrain.fill = GridBagConstraints.BOTH;
        constrain.gridx = 0;
        constrain.gridy = 2;
        constrain.gridwidth = 1;
        constrain.gridheight = 1;
        add(optionPanel, constrain);
        
        constrain.fill = GridBagConstraints.BOTH;
        constrain.gridx = 1;
        constrain.gridy = 2;
        constrain.gridwidth = 2;
        constrain.gridheight = 1;
        add(restPanel, constrain);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        //stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof RyanCustomerRole) {
            RyanCustomerRole customer = (RyanCustomerRole) person;
            //stateCB.setText("Hungry?");
          //Should checkmark be there? 
            //stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            //stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            //infoLabel.setText("<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        //infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == pauseButton){
    		if(clicked == false){
    			clicked = true;
    			restPanel.pause();
    		}
    		else if(clicked == true){
    			clicked = false;
    			restPanel.restart();
    		}
    	}
    	if(e.getSource() == steakButton){
    		restPanel.SetStockToZero("Steak");
    	}
		if(e.getSource() == chickenButton){
			restPanel.SetStockToZero("Chicken");		
		}
		if(e.getSource() == pizzaButton){
			restPanel.SetStockToZero("Pizza");
		}
		if(e.getSource() == saladButton){
			restPanel.SetStockToZero("Salad");
		}
		if(e.getSource() == cashButton){
			restPanel.AddCashToCashier();
		}
        /*if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }*/
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(RyanCustomerRole c) {
    	restPanel.middleCMan(c.getCustomerName());
    }
    
    public void setWaiterEnabled(RyanWaiterRole w){
    	restPanel.middleWMan(w.getName());
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("Ryan's CSCI201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
