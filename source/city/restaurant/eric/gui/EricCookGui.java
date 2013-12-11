package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.restaurant.eric.EricCookRole;
import gui.Gui;

public class EricCookGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.COOK_POSX, EricAnimationConstants.COOK_POSY);
	private EricCookRole _role;
	
	
	
	// ------------------------------ CONSTRUCTOR & PROPERTIES -----------------------------
	public EricCookGui(EricCookRole role) {
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
    	g.drawString("Cook",
    			_position.x,
    			_position.y + EricAnimationConstants.PERSON_HEIGHT
    			);
	}
}
