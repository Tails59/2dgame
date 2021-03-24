package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import engine2d.Animation;
import engine2d.RenderedSprite;
import engine2d.Sprite;

public class Enemy extends Sprite implements RenderedSprite {
	public static final int DETECTION_RANGE = 500;
	public static final int ATTACK_RANGE = 250;
	public static long ATTACK_COOLDOWN = 400;
	
	private static ArrayList<Sprite> enemySprites = new ArrayList<>();
	private static final Timer timer = new Timer();
	
	private static Animation idleAnim;
	private static Animation hitAnim;
	private static Animation deathAnim;
	private static Animation runAnim;
	
	private int healthPts = 5;
	private long lastAttack = 0;
	
	static {
		Image img1 = new ImageIcon("images/grunt/idle.gif").getImage();
		Image img2 = new ImageIcon("images/grunt/hit.gif").getImage();
		Image img3 = new ImageIcon("images/grunt/die.gif").getImage();
		Image img4 = new ImageIcon("images/grunt/run.gif").getImage();
		
		deathAnim = new Animation();
		deathAnim.addFrame(img3, (long) 1);
		
		idleAnim = new Animation();
		idleAnim.addFrame(img1, (long) 1);
		
		hitAnim = new Animation();
		hitAnim.addFrame(img2, (long) 1);
		
		runAnim = new Animation();
		runAnim.addFrame(img4, (long) 1);
	}
	
	public static void init() {
		new Enemy(710, 614);
		new Enemy(1681, 419);
	}
	
	public static void updateAll(long elapsedTime) {
		for(Sprite sprite : enemySprites) {
			sprite.update(elapsedTime);
		}
	}
	
	public static ArrayList<Sprite> getEnemies(){
		return enemySprites;
	}
	
	public Enemy(int x, int y) {
		super(idleAnim);
		this.setPosition(x, y);
		enemySprites.add(this);
		Driver.dr.getRender().register(this);
	}
	
	@Override
	public void update(long elapsedTime) {
		Player ply = Driver.dr.getPlayer();

		if(shouldDraw()) {
			if(!Collision.checkLowerTileCollision(this)) {
				this.setVelocityY((float) (this.getVelocityY()+(0.001f*elapsedTime)));
			} else {
				this.setVelocityY(0);
			}
			
			//Prevent sprites being stuck inside tiles
			if(Collision.checkLowerTileCollision(this) && Collision.checkLeftTileCollision(this) && Collision.checkRightTileCollision(this)) {
				this.setY(this.getY() - 5);
			}
			
			//AI Movement control
			if(this.getY() >= ply.getY() - 30 || this.getY() <= ply.getY() + 30) {				
				if(this.getX() >= ply.getX() - DETECTION_RANGE && this.getX() <= ply.getX() + DETECTION_RANGE) {
					if(this.getX() >=  ply.getX() - ATTACK_RANGE && this.getX() <= ply.getX() + ATTACK_RANGE) {
						this.shoot();
						this.stop();
					}else{
						if(this.getX() < ply.getX()) {
							this.move(false);
						} else {
							this.move(true);
						}
					}
				}else {
					this.stop();
				}
			}
				
			super.update(elapsedTime);
		}
	}
	
	public void takeHit() {
		//this.setAnimation(hitAnim);
		
		Enemy sprite = this;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sprite.setAnimation(idleAnim);
			}
		}, 950);
		
		
		this.healthPts -= 1;
		
		if(this.healthPts <= 0) {
			this.kill();		}
	}
	
	public void touch(Sprite s1) {
		if(s1 instanceof Projectile) {
			this.takeHit();
		}
	}
	
	public void move(boolean moveleft, float speed) {
		this.setAnimation(runAnim);
		
		if(moveleft) {
			if(moveleft && Collision.checkLeftTileCollision(this)) {
				this.stop();
				return;
			}
			
			this.left = true;
			this.setVelocityX(-0.1f);
		} else {
			if (Collision.checkRightTileCollision(this)) {
				this.setVelocityX(0);
				return;
			}
			
			this.left = false;
			this.setVelocityX(0.1f);
		}
	}
	
	@Override
	public void move(boolean moveleft) {
		this.move(moveleft, 0.1f);
	}
	
	/**
	 * Called when the sprite should die, the animation is changed to the
	 * death animation and the sprite is unrendered.
	 */
	public void kill() {
		this.setAnimation(deathAnim);
		
		Enemy sprite = this;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Driver.dr.getRender().unregister(sprite);
			}
		}, 650);
		
		enemySprites.remove(this);
	}
	
	/**
	 * Fire a Projectile (bullet) in whatever direction the sprite is facing
	 */
	private void shoot() {
		if(!(System.currentTimeMillis() >= this.lastAttack + ATTACK_COOLDOWN)) {
			return;
		}
		
		this.lastAttack = System.currentTimeMillis();
		new Projectile(this);
		
	}
	
	/**
	 * Set the Sprite's animation to Idle and stop movement
	 */
	public void stop() {
		this.setAnimation(idleAnim);
		super.stop();
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		int plyOffset = (int) Driver.dr.getPlayer().getXOffset();
		this.setOffsets(plyOffset, 0);
		
		if(this.left) {
			this.setOffsets((int) (this.getXOffset() - this.getWidth()), 0);
			this.setScale(-1.0f, 1.0f);
			
			super.drawTransformed(graphics);
			
			this.setOffsets((int) (this.getXOffset() + this.getWidth()), 0);
		} else {
			super.draw(graphics);
		}
		
		this.drawBoundingBox(graphics);
	}

	@Override
	public boolean shouldDraw() {
		//Return whether or not the sprite is on screen
		return this.getX()+this.getWidth() >= Driver.dr.getPlayer().getX() - (Driver.dr.getWidth()/2) 
				&& this.getX() <= Driver.dr.getPlayer().getX() + (Driver.dr.getWidth()/2);
	}

}
