package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class GameOver {
	boolean win = false;
	
	public void draw(Graphics2D graphics) {
		graphics.setFont(new Font("Helvetica", Font.BOLD, 30));
		graphics.setColor(Color.BLACK);
		graphics.drawString("GAME OVER", 700, 300);
		if(win) {
			graphics.drawString("YOU WIN!", 720, 350);
		}else {
			graphics.drawString("YOU LOSE!", 720, 350);
		}
		
		graphics.drawString("('E' to replay)", 700, 720);
	}
}
