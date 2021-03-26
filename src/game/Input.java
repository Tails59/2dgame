package game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * A class to handle all input for the Game.
 * Helps to keep the Driver class tidied up a bit,
 * making it more readable.
 * 
 * @author Taylor Paige
 *
 */
class Input {
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// Game controls
		if (keyCode == KeyEvent.VK_ESCAPE) {
			Driver.dr.stop();
		}
		
		//Restart the game when you have completed
		if(keyCode == KeyEvent.VK_E) {
			Driver.dr.restart();
		}
		
		//Print the player's current coordinates
		if(keyCode == KeyEvent.VK_F1) {
			System.out.println(Driver.dr.getPlayer().getX() +", "+ Driver.dr.getPlayer().getY());
		}
			
		// Player Control
		switch(keyCode) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				Driver.dr.getPlayer().move(false);
				break;

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				Driver.dr.getPlayer().move(true);
				break;

			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				Driver.dr.getPlayer().jump();
				break;
		
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		// Player Movement
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
			Driver.dr.getPlayer().attack();
			break;
			
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				Driver.dr.getPlayer().stop();
				break;		
		}
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void mouseClicked(MouseEvent e) { 
		
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) { 
		int x = e.getX();
		
		if ((int) Driver.dr.getPlayer().getX() >= Driver.dr.getWidth() / 2 && x <= Driver.dr.getWidth() / 2) {
            x = (int) Driver.dr.getPlayer().getX() - (Driver.dr.getWidth() / 2 - x);
        } else if ((int) Driver.dr.getPlayer().getX() >= Driver.dr.getWidth() / 2 && x > Driver.dr.getWidth() / 2) {
            x = (int) Driver.dr.getPlayer().getX() + (x - Driver.dr.getWidth() / 2);
        }
		
		Driver.dr.getPlayer().setPosition(x, e.getY());
	}
	
	public void mouseExited(MouseEvent e) { 
		
	}
	
	public void mouseEntered(MouseEvent e) { 
		
	}
}
