package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import city.restaurant.eric.EricCookRole;
import gui.Gui;

public class EricCookGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.COOK_POSX, EricAnimationConstants.COOK_POSY);
	private EricCookRole _role;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Chef.png"));
    Image cook = a.getImage();
    int xGap = 18;
    int yGap = 32;
	
	
	
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
        g.drawImage(cook, _position.x-20, _position.y+30, xGap, yGap, null);
        
    	g.setFont(EricAnimationConstants.FONT);
    	g.setColor(Color.BLACK);
	}
}
