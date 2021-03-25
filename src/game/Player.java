package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import engine2d.Animation;
import engine2d.RenderedSprite;
import engine2d.Sound;
import engine2d.Sprite;
import engine2d.UpdateableSprite;

public class Player extends Sprite implements UpdateableSprite, RenderedSprite{
	/*
	 * Attack cooldown time in milliseconds
	 */
	private static final long ATTACK_COOLDOWN = 500;
	private static final Timer timer = new Timer();
	
	private final int MAX_HEALTH = 10; 
	
	private static Animation walkAnim;
	private static Animation attackAnim;
	private static Animation idleAnim;
	private static Sound attackSound;
	
	private long lastAttack = 0;
	private int healthPoints = MAX_HEALTH;
	
	// Load our static player fields so that they can be used by player instances
	static {
		Image img1 = new ImageIcon("images/player/idle.gif").getImage();
		Image img2 = new ImageIcon("images/player/idle-shooting.gif").getImage();
		Image img3 = new ImageIcon("images/player/running.gif").getImage();

		walkAnim = new Animation();
		walkAnim.addFrame(img3, (long) 1000);
		
		attackAnim = new Animation();
		attackAnim.addFrame(img2, (long) 1000);
		
		idleAnim = new Animation();
		idleAnim.addFrame(img1, (long) 1000);
	}
	
	public Player() {
		super(idleAnim);
		
		Driver.dr.getRender().register(this);
		
		this.setPosition(50, 50);
	}
	
	/**
	 * Detect when the player has hit the screen edge
	 */
	public void offscreen(int side) {
		if(side == Collision.RIGHT) {
			if(Driver.dr.currentLevel() == 3) {
				return;
			}
			
			Driver.dr.changeLevel(Driver.dr.currentLevel() + 1); //When the player reaches the end of the map, go to the next level
		}
	}
	
	private float calculateXOffset() {
    	float x = super.getX();
		int w = super.getWidth();
		
		if(x >= (Driver.dr.getTileMap().getPixelWidth() - Driver.dr.getWidth()/2)) {
			return (Driver.dr.getTileMap().getPixelWidth() - Driver.dr.getWidth());
		}
		
		if((int) (x + (w/2)) >= Driver.dr.getWidth()/2) {
			return (x + (w/2) - (Driver.dr.getWidth()/2));
		}
		
		return 0;
    }
	
    @Override
    public void update(long elapsedTime) {
    	this.xoff = (int) calculateXOffset();
    	
    	super.update(elapsedTime);
    	
    	if(Collision.checkRightTileCollision(this)) {
			this.setVelocityX(0);
		}
		if(Collision.checkLeftTileCollision(this)) {
			this.setVelocityX(0);
		}
		if(Collision.checkLowerTileCollision(this)) {
			this.setVelocityY(0);
		}
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
				this.setVelocityX(0);
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
	
	public void respawn(int x, int y) {
		this.healthPoints = MAX_HEALTH;
		this.lastAttack = 0;
		this.left = false;
		this.setVelocity(0, 0);
		this.setPosition(x, y);
	}
	
	public void attack() {
		if(!(System.currentTimeMillis() >= this.lastAttack + ATTACK_COOLDOWN)) {
			return;
		}
		
		attackSound = new Sound("audio/gunshot.wav", false);
		attackSound.start();
		
		this.setAnimation(attackAnim);
		
		Player ply = this;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ply.setAnimation(idleAnim);
			}
		}, 650);
		
		new Projectile(this);
		this.lastAttack = System.currentTimeMillis();
	}
	
	public void stop() {
		this.setVelocityX(0f);
		this.setAnimation(idleAnim);
	}
	
	public void touch(Sprite other) {
		if(other instanceof Projectile) {
			this.healthPoints -= 1;
			
			if(this.healthPoints <= 0) {
				this.die();
			}
		}
	}
	
	public void die() {
		
	}
	
	public int getMaxHealth() {
		return this.MAX_HEALTH;
	}
	
	public int getHealth() {
		return this.healthPoints;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(this.left) {
			this.setOffsets((int) (this.getXOffset() - this.getWidth()), 0);
			this.setScale(-1.0f, 1.0f);
			
			super.drawTransformed(g);
			
			this.setOffsets((int) (this.getXOffset() + this.getWidth()), 0);
		} else {
			super.draw(g);
		}
	}

	@Override
	public boolean shouldDraw() {
		// TODO Auto-generated method stub
		return true;
	}
}
