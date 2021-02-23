package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game2D.GameCore;

@SuppressWarnings("serial")
class Driver extends GameCore{
	public static Driver dr;
	private Input userInput = new Input();
	
	public Player ply;
	
	public static void main(String[] args) {
		dr = new Driver();
		dr.run(false, 1000, 1000);
	}
	
	private Driver() {
		ply = new Player();
	}
	
	public void update(long elapsed)
    {    	    	       	
        ply.update(elapsed);
    }

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.yellow);
        
        ply.draw(g);
	}
	
	// Move our user input (mouse and keyboard listeners) to an outside class,
	// helps to keep the code more readable
	@Override
	public void keyPressed(KeyEvent e) {
		userInput.keyPressed(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		userInput.keyReleased(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		userInput.keyTyped(e);
	}
}
