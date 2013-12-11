package city.restaurant.eric.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import city.restaurant.eric.EricHostRole;
import gui.Gui;

public class EricHostGui implements Gui {
	
	// ---------------------------------- DATA ----------------------------------
	private RestDim _position = new RestDim(EricAnimationConstants.FRONTDESK_X, EricAnimationConstants.FRONTDESK_Y);
	private EricHostRole _role;
	
	private ImageIcon i = new ImageIcon(this.getClass().getResource("/image/restaurant/Host.png"));
	private Image image = i.getImage();
	public static int xGap = 21;
	public static int yGap = 30;
	
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
        g.drawImage(image, _position.x, _position.y, xGap, yGap, null);
	}
}
