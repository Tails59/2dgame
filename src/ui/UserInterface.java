package ui;

import java.awt.Graphics2D;

import game.Driver;

public final class UserInterface {
	private static Healthbar healthbar;
	
	public static void init() {
		healthbar = new Healthbar(Driver.dr.getPlayer().getMaxHealth());
	}
	
	public static void update(long elapsed) {
		healthbar.update(elapsed);
	}
	
	public static void draw(Graphics2D graphics) {
		healthbar.draw(graphics);
	}
}
