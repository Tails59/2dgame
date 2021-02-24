package game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

class Input {
	public void keyPressed(KeyEvent e) {
		// Game controls
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
			Driver.dr.stop();
			
		// Player Movement
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				Driver.dr.ply.move(true);
				break;

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				Driver.dr.ply.move(false);
				break;

			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				break;
		
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				break;				
		}
	}
	
	public void keyReleased(KeyEvent e) {
		// Player Movement
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				Driver.dr.ply.stop();
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
		
	}
	
	public void mouseExited(MouseEvent e) { 
		
	}
	
	public void mouseEntered(MouseEvent e) { 
		
	}
}
