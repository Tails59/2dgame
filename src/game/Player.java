package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import engine2d.Animation;
import engine2d.Sprite;

class Player extends Sprite{
	private static Animation walkAnim;
	private static Animation attackAnim;
	private static Animation idleAnim;
	
	// Whether the sprite is facing left or not
	private static boolean left;
	
	static {
		Image img1 = new ImageIcon("images/player/idle.gif").getImage();
		Image img2 = new ImageIcon("images/player/attack.gif").getImage();
		Image img3 = new ImageIcon("images/player/walk.gif").getImage();

		walkAnim = new Animation();
		walkAnim.addFrame(img3, (long) 50);
		
		attackAnim = new Animation();
		attackAnim.addFrame(img2, (long) 50);
		
		idleAnim = new Animation();
		idleAnim.addFrame(img1, (long) 50);
	}
	
	public Player() {
		super(idleAnim);
		this.setPosition(50, 50);
	}
	
	/**
	 * Moves the player left/right
	 * 
	 * @param left [boolean] true if the player should move to the left, false if to the right
	 */
	public void move(boolean moveleft) {
		this.setAnimation(walkAnim);
		
		if(moveleft) {
			this.left = true;
			this.setVelocityX(0.5f);
		} else {
			this.left = false;
			this.setVelocityX(-0.5f);
		}
		
	}
	
	public void attack() {
		this.setAnimation(attackAnim);
	}
	
	public void stop() {
		this.setVelocityX(0f);
		this.setAnimation(idleAnim);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(!this.left) {
			AffineTransform afn = new AffineTransform();
			afn.translate(Math.round(getX()) + getWidth(), Math.round(getY()));
			afn.scale(-1.0, 1.0);
			
			//g.drawImage(getImage(), afn, null);
			g.drawImage(getImage(), afn, null);
		}else {
			super.draw(g);
		}
		
		drawBoundingBox(g);
		
	}
}
