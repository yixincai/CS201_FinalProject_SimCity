package city.restaurant.eric.gui;

import java.awt.Font;

public class EricAnimationConstants
{
	// This class is HACKS! so that we can change the constants in one place
    
    // Constants for animation objects' size
    public static final int TABLE_WIDTH = 50;
    public static final int TABLE_HEIGHT = 50;
    public static final int PERSON_WIDTH = 20;
    public static final int PERSON_HEIGHT = 20;
	
    // Constants for table positions
	public static final int NUMBER_OF_TABLES = 3;
    public static final int TABLE0_POSX = 325;
    public static final int TABLE0_POSY = 20;
    public static final int TABLE1_POSX = 325;
    public static final int TABLE1_POSY = 110;
    public static final int TABLE2_POSX = 325;
    public static final int TABLE2_POSY = 200;
    
    // Constant for outside position
    public static final int OUTSIDE_X = -20;
    public static final int OUTSIDE_Y = -20;
    
    // Constant for front desk position
    public static final int FRONTDESK_X = 90;
    public static final int FRONTDESK_Y = 10;
    
    // Non-constant for waiters' idle position
    public static int NEXT_WAITER_X = 10;
    public static int NEXT_WAITER_Y = 40;
    public static void updateNextWaiter()
    {
    	// NEXT_WAITER_X unchanged
    	NEXT_WAITER_Y += PERSON_HEIGHT + 5;
    }
    
    // Non-constant for customers' waiting position
    public static int NEXT_CUSTOMER_X = 10 + PERSON_WIDTH + 5;
    public static int NEXT_CUSTOMER_Y = 40;
    public static void updateNextCustomer()
    {
    	// NEXT_CUSTOMER_X unchanged
    	NEXT_CUSTOMER_Y += PERSON_HEIGHT + 5;
    }
    
    // Constants for cook and revolving stand positions
    public static final int COOK_POSX = 233;
    public static final int COOK_POSY = 257;
	public static final int REVOLVING_STAND_POSX = 200; //TODO
	public static final int REVOLVING_STAND_POSY = 250; //TODO
    
    // Constant for cashier position
    public static final int CASHIER_POSX = 70;
    public static final int CASHIER_POSY = 200;
    
    // Constants for timers
    public static final int TIMER_PERIOD = 5; // in ms
    
    // Font constant
    public static final String FONT_NAME = "Consolas";
    public static final Font FONT = new Font(FONT_NAME, Font.BOLD, 16);
}
