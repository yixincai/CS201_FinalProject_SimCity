package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.restaurant.eric.EricCashierRole;
import gui.Gui;

public class EricCashierGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.CASHIER_POSX, EricAnimationConstants.CASHIER_POSY);
	private EricCashierRole _role;
	
	
	
	// ------------------------------ CONSTRUCTOR & PROPERTIES -----------------------------
	public EricCashierGui(EricCashierRole role) {
		_role = role;
	}
	@Override
	public boolean isPresent() {
		return _role.active;
	}
    
    
    
    // -------------------------------------- METHODS -------------------------------------
	
	@Override
	public void updatePosition() {
		// do nothing.
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(_position.x, _position.y, EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.PERSON_HEIGHT);
        
    	g.setFont(EricAnimationConstants.FONT);
    	g.setColor(Color.BLACK);
    	g.drawString("Cashier",
    			_position.x,
    			_position.y + EricAnimationConstants.PERSON_HEIGHT
    			);
	}
}
