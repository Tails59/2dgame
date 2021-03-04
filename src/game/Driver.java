package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import engine2d.GameCore;
import engine2d.TileMap;

@SuppressWarnings("serial")
public class Driver extends GameCore{
	public static Driver dr;

	private TileMap tmap;
	private Input userInput;

	private Player ply;
	
	public static void main(String[] args) {
		dr = new Driver();
		dr.init();
		dr.run(false, 1080, 768);
	}
		
	private void init() {        
		ply = new Player();
		ply.setPosition(100, 50);
		userInput = new Input();
		tmap = new TileMap();
		
		tmap.loadMap("maps", "level1.txt");
		setVisible(true);
        setSize(tmap.getPixelWidth(), tmap.getPixelHeight());
	}
	
	public void update(long elapsed)
    {  
		ply.update(elapsed);
		
		if(Collision.checkRightTileCollision(ply)) {
			ply.setVelocityX(0);
		}
		if(Collision.checkLeftTileCollision(ply)) {
			ply.setVelocityX(0);
		}
		if(Collision.checkLowerTileCollision(ply)) {
			ply.setVelocityY(0);
		}
    }
	
	public TileMap getTileMap() {
		return this.tmap;
	}
	
	public Player getPlayer() {
		return this.ply;
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
		userInput.mouseClicked(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) { 
		userInput.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) { 
		userInput.mouseReleased(e);
	}
	
	@Override
	public void mouseExited(MouseEvent e) { 
		userInput.mouseExited(e);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) { 
		userInput.mouseEntered(e);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, Driver.dr.getWidth(), Driver.dr.getHeight());

		tmap.draw(g, (int) -ply.getX(), 0);
	}
}
