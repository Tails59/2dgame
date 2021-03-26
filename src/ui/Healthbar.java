package ui;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Driver;

/**
 * The healthbar shown at the top of the player's screen,
 * created by the UserInterface class
 * 
 * @author Taylor Paige
 *
 */
class Healthbar {
	public final int MAX_HEALTH;
	
	public Healthbar(int maxHealth) {		
		this.MAX_HEALTH = maxHealth;
	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect((Driver.dr.getWidth() / 2) - (MAX_HEALTH * 100/2), 25, MAX_HEALTH * 100, 35);
		graphics.setColor(Color.RED);
		graphics.fillRect((Driver.dr.getWidth() / 2) - (MAX_HEALTH * 100/2), 25, Driver.dr.getPlayer().getHealth()* 100, 35);
	}
}
