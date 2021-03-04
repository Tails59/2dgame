package game;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import engine2d.Animation;
import engine2d.Sound;
import engine2d.Sprite;

public class Player extends Sprite {
	private static Animation walkAnim;
	private static Animation attackAnim;
	private static Animation idleAnim;
	private static Sound attackSound;
	
	// Whether the sprite is facing left or not
	private boolean left;
	
	// Load our static player fields so that they can be used by player instances
	static {
		Image img1 = new ImageIcon("images/player/idle.gif").getImage();
		Image img2 = new ImageIcon("images/player/attack.gif").getImage();
		Image img3 = new ImageIcon("images/player/walk.gif").getImage();

		walkAnim = new Animation();
		walkAnim.addFrame(img3, (long) 50);
		
		attackAnim = new Animation();
		attackAnim.addFrame(img2, (long) 50);
		
		idleAnim = new Animation();
		idleAnim.addFrame(img1, (long) 1);
	}
	
	public Player() {
		super(idleAnim);
		
		Driver.dr.getRender().register(this, 0);
		
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
			if(moveleft && Collision.checkLeftTileCollision(this)) {
				this.stop();
				return;
			}
			
			this.left = true;
			this.setVelocityX(-0.5f);
		} else {
			if (Collision.checkRightTileCollision(this)) {
				this.stop();
				return;
			}
			
			this.left = false;
			this.setVelocityX(0.5f);
		}
	}
	
	/**
	 * Makes the player jump
	 */
	public void jump() {
		if(Collision.checkLowerTileCollision(this)) {
			this.setY(this.getY()-15);
			this.setVelocityY(-0.5f);
		}
	}
	
	public void attack() {
		attackSound = new Sound("audio/player_attack.wav", false);
		attackSound.start();
		this.setAnimation(attackAnim);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setAnimation(idleAnim);
	}
	
	public void stop() {
		this.setVelocityX(0f);
		this.setAnimation(idleAnim);
	}
	
	@Override
	public float getX() {
		return super.getX();
	}
	
	public float getXOffset() {	
		float x = super.getX();
		
		if((int) x >= Driver.dr.getWidth()/2) {
			return x - (Driver.dr.getWidth()/2);
		}
		
		return 0;
	}
	
	@Override
	public void draw(Graphics2D g) {	
		float xo = this.getXOffset();
		
		this.setOffsets(0, 0);
		if(this.left) {
			this.setOffsets((int) (getWidth() - xo), 0);
			this.setScale(-1.0f, 1.0f);
			
			super.drawTransformed(g);
		} else {
			super.setOffsets((int) -xo, 0);
			super.draw(g);
		}
		
		drawBoundingBox(g);
	}
}
