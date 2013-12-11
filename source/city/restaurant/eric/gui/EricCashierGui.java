package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import city.restaurant.eric.EricCashierRole;
import gui.Gui;

public class EricCashierGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.CASHIER_POSX, EricAnimationConstants.CASHIER_POSY);
	private EricCashierRole _role;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Cashier.png"));
    Image cashier = a.getImage();
    int xGap = 19;
    int yGap = 25;
	
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
        g.drawImage(cashier, _position.x, _position.y, xGap, yGap, null);
	}
}
