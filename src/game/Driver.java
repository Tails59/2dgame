package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import engine2d.GameCore;
import engine2d.Sprite;
import engine2d.TileMap;

@SuppressWarnings("serial")
public class Driver extends GameCore {
	public static Driver dr;

	private static Image parallaxTree = new ImageIcon("images/background/jungle_bg_parallax.png").getImage();
	private static Image jungleTrees = new ImageIcon("images/background/jungle_bg_trees.png").getImage();

	BufferedImage backgroundBuffer;
	Color bgcolour = new Color(52, 110, 235);

	private TileMap tmap;
	private Input userInput;
	private ArrayList<Sprite> backgroundTrees = new ArrayList<>();

	private Player ply;

	// Cached fields for optimising background images
	private int mapw, ptw, jtw;

	public static void main(String[] args) {
		dr = new Driver();
		dr.init();
		dr.setResizable(false);
		dr.run(false, 1500, 768);
	}

	private void init() {
		Enemy.init();

		ply = new Player();
		ply.setPosition(47, 414);

		userInput = new Input();
		tmap = new TileMap();

		tmap.loadMap("maps", "level1.txt");
		setVisible(true);
		setSize(tmap.getPixelWidth(), tmap.getPixelHeight());

		ui.UserInterface.init();

		mapw = dr.tmap.getPixelWidth();
		ptw = parallaxTree.getWidth(this);
		jtw = jungleTrees.getWidth(this);
	}

	public void update(long elapsed) {
		Driver.dr.getSpriteUpdater().update(elapsed);
		ply.update(elapsed);
		Enemy.updateAll(elapsed);

		ui.UserInterface.update(elapsed);
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

		drawBackground(graphics);
		for (Sprite sprite : backgroundTrees) {
			sprite.setOffsets((int) (-offset * 0.2), 0);
			sprite.draw(graphics);
		}

		tmap.setXOffset(offset);
		tmap.draw(graphics, 0, 0);

		ui.UserInterface.draw(graphics);

		g.drawImage(buffer, null, null);
	}

	int x = 0;
	int len;

	private void drawBackground(Graphics2D graphics) {
		x = 0;
		len = mapw / ptw + 1;
		for (int i = 0; i < len; i++) {
			graphics.drawImage(parallaxTree, (int) (x - ply.getXOffset() * 0.2), 100, null);

			x += ptw;
		}

		x = 0;
		len = mapw / jtw + 1;
		for (int i = 0; i < len; i++) {
			graphics.drawImage(jungleTrees, (int) (x - 100 - ply.getXOffset() * 0.5), 400, null);

			x += jtw;
		}

		graphics.drawImage(backgroundBuffer, null, null);
	}
}
