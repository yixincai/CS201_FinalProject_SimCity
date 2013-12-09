package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import city.market.Item;

public class ImageAtlas {
	public static Hashtable<String, BufferedImage> mapAtlas = new Hashtable<String, BufferedImage>();
	
	public static void load() throws IOException { 
		mapAtlas.put("Bank", ImageIO.read(new File("wvimage/Bank.png")));
		mapAtlas.put("Market", ImageIO.read(new File("wvimage/Market.png")));
		mapAtlas.put("OmarRestaurant", ImageIO.read(new File("wvimage/OmarRestaurant.png")));
		mapAtlas.put("YixinRestaurant", ImageIO.read(new File("wvimage/YixinRestaurant.png")));
		mapAtlas.put("RyanRestaurant", ImageIO.read(new File("wvimage/RyanRestaurant.png")));
		mapAtlas.put("EricRestaurant", ImageIO.read(new File("wvimage/EricRestaurant.png")));
		mapAtlas.put("House", ImageIO.read(new File("wvimage/House1.png")));
		mapAtlas.put("Apartment", ImageIO.read(new File("wvimage/Apartment1.png")));
	/*	mapAtlas.put(key, value);
		mapAtlas.put(key, value);
		mapAtlas.put(key, value);
		mapAtlas.put(key, value); */
	}
}
