package game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import engine2d.Animation;
import engine2d.RenderedSprite;
import engine2d.Sprite;

public class Enemy extends Sprite implements RenderedSprite {
	public static final int DETECTION_RANGE = 500;
	public static final int ATTACK_RANGE = 250;
	public static final long ATTACK_COOLDOWN = 500;
	
	private static ArrayList<Sprite> enemySprites = new ArrayList<>();
	private static final Timer timer = new Timer();
	
	private Animation idleAnim;
	private Animation deathAnim;
	private Animation runAnim;
	
	private int healthPts = 5;
	private long lastAttack = 0;
	
	public static void setup(int level) {
		resetLevel();
		if(level == 1) {
			create(710, 614);
			create(1681, 413);
			create(2466, 352);
			create(2677, 610);
			create(2884, 352);
			create(3523, 481);
			create(4281, 610);
		}else if(level == 2) {
			create(927, 488);
			create(1298, 351);
			create(1838, 416);
			create(2085, 616);
			create(1281, 616);
			create(2687, 288);
			create(3475, 418);
			create(3722, 618);
			create(4063, 484);
		}
	}
	
	/**
	 * Create a new Enemy sprite
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Enemy create(int x, int y) {	
		Animation idleAnim = new Animation("images/grunt/idle.gif", 1000l);		
		Animation deathAnim = new Animation("images/grunt/die.gif", 1000l);
		Animation runAnim = new Animation("images/grunt/run.gif", 1000l);
		
		return new Enemy(x, y, deathAnim, idleAnim, runAnim);
	}
	
	public static void resetLevel() {
		for(Sprite sprite : enemySprites) {
			Driver.dr.getRender().unregister(sprite);
			Driver.dr.getSpriteUpdater().unregister(sprite);
		}
		
		enemySprites = new ArrayList<Sprite>();
	}
	
	public static void updateAll(long elapsedTime) {
		for(Sprite sprite : enemySprites) {
			sprite.update(elapsedTime);
		}
	}
	
	public static ArrayList<Sprite> getEnemies(){
		return enemySprites;
	}
	
	private Enemy(int x, int y, Animation death, Animation idle, Animation run) {
		super(idle);
		
		this.idleAnim = idle;
		this.deathAnim = death;
		this.runAnim = run;
		
		this.setPosition(x, y);
		enemySprites.add(this);
		Driver.dr.getRender().register(this);
	}
	
	@Override
	public void update(long elapsedTime) {
		if(shouldDraw()) {
			Player ply = Driver.dr.getPlayer();
			
			if(!Collision.checkLowerTileCollision(this)) {
				this.setVelocityY((float) (this.getVelocityY()+(0.001f*elapsedTime)));
			} else {
				this.setVelocityY(0);
			}
			
			//AI Movement control
			if(this.getY() >= ply.getY() - 30 && this.getY() <= ply.getY() + 30) {				
				if(this.getX() >= ply.getX() - DETECTION_RANGE && this.getX() <= ply.getX() + DETECTION_RANGE) {
					if(this.getX() >=  ply.getX() - ATTACK_RANGE && this.getX() <= ply.getX() + ATTACK_RANGE) {
						
						this.left = !(this.getX() < ply.getX());
						
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
	
	/**
	 * Called when the sprite should take damage
	 * from a projectile
	 */
	public void takeHit() {
		this.healthPts -= 1;
		
		if(this.healthPts <= 0) {
			this.kill();		}
	}
	
	public void touch(Sprite s1) {
		if(s1 instanceof Projectile) {
			this.takeHit();
		}
	}
	
	@Override
	public void move(boolean moveleft) {
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
		Projectile.create(this);		
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
	}

	@Override
	public boolean shouldDraw() {
		Player ply = Driver.dr.getPlayer();
		//Return whether or not the sprite is on screen
		return ply.getX() + Driver.dr.getWidth() >= this.getX()
				&& ply.getX() - Driver.dr.getWidth() <= this.getX();
	}

}
