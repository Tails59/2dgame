package game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

class Input {
	public void keyPressed(KeyEvent e) {
		// Game controls
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Driver.dr.stop();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F1) {
			System.out.println(Driver.dr.getPlayer().getX() +", "+ Driver.dr.getPlayer().getY());
		}
			
		// Player Control
		switch(e.getKeyCode()) {
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
		System.out.println("test");
	}
	
	public void mouseReleased(MouseEvent e) { 
		
	}
	
	public void mouseExited(MouseEvent e) { 
		
	}
	
	public void mouseEntered(MouseEvent e) { 
		
	}
}
