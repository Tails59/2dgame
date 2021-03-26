package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import engine2d.FadingSound;
import engine2d.GameCore;
import engine2d.Sound;
import engine2d.TileMap;

@SuppressWarnings("serial")
public class Driver extends GameCore {
	public static Driver dr;

	private static Image parallaxTree = new ImageIcon("images/background/jungle_bg_parallax.png").getImage();
	private static Image jungleTrees = new ImageIcon("images/background/jungle_bg_trees.png").getImage();
	private static Image caveBg = new ImageIcon("images/background/cave_bg.png").getImage();

	BufferedImage backgroundBuffer;
	Color bgcolour = new Color(52, 110, 235);

	private TileMap tmap;
	private Input userInput;
	private Player ply;
	
	private int level = 0;

	// Cached fields for optimising background images
	private int mapw, ptw, jtw;

	public static void main(String[] args) {
		dr = new Driver();
		dr.init();
		dr.setResizable(false);
		dr.run(false, 1500, 768);
	}

	private void init() {
		ply = Player.create();
		tmap = new TileMap();
		userInput = new Input();
		
		changeLevel(1);

		setVisible(true);
		setSize(tmap.getPixelWidth(), tmap.getPixelHeight());

		ui.UserInterface.init();
		mapw = dr.tmap.getPixelWidth();
		ptw = parallaxTree.getWidth(this);
		jtw = jungleTrees.getWidth(this);
		
		Sound backgroundSound = new Sound("audio/bg_music.wav", true);
		backgroundSound.start();
	}
	
	public void changeLevel(int level) {
		if(level == 1) {
			Enemy.setup(1);
			
			ply.respawn(47, 414);
			tmap.loadMap("maps", "level1.txt");
		}else if(level == 2) {
			Enemy.setup(2);
			
			ply.respawn(47, 610);
			tmap.loadMap("maps", "level2.txt");
		}else if(level == 3) {
			ui.UserInterface.toggleDrawEndscreen(!(ply.getHealth() <= 0));
			Enemy.setup(0);
			ply.respawn(750, 400);
			tmap.loadMap("maps", "level3.txt");
			
			FadingSound snd = new FadingSound("audio/victory.wav");
			snd.start();
		}
		
		this.level = level;
	}

	public void update(long elapsed) {		
		Driver.dr.getSpriteUpdater().update(elapsed);
		ply.update(elapsed);
		Enemy.updateAll(elapsed);
	}
	
	public int currentLevel() {
		return this.level;
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
		BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = buffer.createGraphics();
		graphics.setClip(0, 0, getWidth(), getHeight());

		int offset = (int) -ply.getXOffset();

		graphics.setColor(bgcolour);
		graphics.fillRect(0 + offset, 0, tmap.getPixelWidth(), Driver.dr.getHeight());

		if(level == 1) {
			if(ply.getX() >= 3406) { //Draw a different background in the cave area of the level
				drawCaveBackground(graphics);
			}else {
				drawJungleBackground(graphics);
			}
		}else if(level == 2) {
			drawCaveBackground(graphics);
		}else {
			drawJungleBackground(graphics);
		}

		tmap.setXOffset(offset);
		tmap.draw(graphics, 0, 0);

		ui.UserInterface.draw(graphics);

		g.drawImage(buffer, null, null);
	}
	
	public void restart() {
		if(level == 3) {
			dr.changeLevel(1);
			ui.UserInterface.toggleDrawEndscreen(false);
		}
	}

	int x = 0;
	int len;

	private void drawJungleBackground(Graphics2D graphics) {
		x = 0;
		len = mapw / ptw + 1;
		for (int i = 0; i < len; i++) {
			graphics.drawImage(parallaxTree, (int) (x - ply.getXOffset() * 0.2), 100, null);

			x += ptw;
		}

		x = 0;
		len = mapw / jtw + 1;
		for (int i = 0; i < len; i++) {
			graphics.drawImage(jungleTrees, (int) (x - 100 - ply.getXOffset() * 0.5), 300, null);

			x += jtw;
		}

		graphics.drawImage(backgroundBuffer, null, null);
	}
	
	private void drawCaveBackground(Graphics2D g) {
		BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = buffer.createGraphics();
		
		x = 0;
		
		for(int i = 0; i < 2; i++) {
			graphics.drawImage(caveBg, (int) (x - ply.getXOffset() * 0.1), 0, null);
			x+= 1200;
		}
		
		g.drawImage(buffer, null, null);
	}
}
