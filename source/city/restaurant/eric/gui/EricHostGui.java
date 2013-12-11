package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.restaurant.eric.EricHostRole;
import gui.Gui;

public class EricHostGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.FRONTDESK_X, EricAnimationConstants.FRONTDESK_Y);
	private EricHostRole _role;
	
	
	
	// ------------------------------ CONSTRUCTOR & PROPERTIES -----------------------------
	public EricHostGui(EricHostRole role) {
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
    	g.drawString("Host",
    			_position.x,
    			_position.y + EricAnimationConstants.PERSON_HEIGHT
    			);
	}
}
