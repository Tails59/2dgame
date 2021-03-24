package ui;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Driver;


class Healthbar {
	public final int MAX_HEALTH;
	
	public Healthbar(int maxHealth) {		
		this.MAX_HEALTH = maxHealth;
	}
	
	public void update(long elapsedTime) {

	}
	
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect((Driver.dr.getWidth() / 2) - (MAX_HEALTH * 100/2), 25, MAX_HEALTH * 100, 35);
		graphics.setColor(Color.RED);
		graphics.fillRect((Driver.dr.getWidth() / 2) - (MAX_HEALTH * 100/2), 25, Driver.dr.getPlayer().getHealth()* 100, 35);
	}
}
