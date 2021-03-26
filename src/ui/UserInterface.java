package ui;

import java.awt.Graphics2D;

import game.Player;

public final class UserInterface {
	private static Healthbar healthbar;
	private static GameOver gameOver;
	
	private static boolean drawEnd;

	public static void init() {
		healthbar = new Healthbar(Player.MAX_HEALTH);
		gameOver = new GameOver();
	}
	
	public static void draw(Graphics2D graphics) {
		healthbar.draw(graphics);
		
		if(drawEnd) {
			gameOver.draw(graphics);
		}
	}
	
	public static void toggleDrawEndscreen(boolean win_) {
		drawEnd = !drawEnd;
		gameOver.win = win_;
	}
}
