package game;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import engine2d.Animation;
import engine2d.RenderedSprite;
import engine2d.Sprite;
import engine2d.UpdateableSprite;

public class Projectile extends Sprite implements UpdateableSprite, RenderedSprite {
	private static final Animation SHOOT_ANIM;
	
	private final Timer timer = new Timer();
	private static final float PROJECTILE_SPEED = 0.8f;
	
	private long spawnTime;
	private final boolean left;
	private final Animation DIE_ANIM;
	private boolean destroyed = false;
	
	static {
		SHOOT_ANIM = new Animation();
		SHOOT_ANIM.addFrame(new ImageIcon("images/bullet.gif").getImage(), (long) 5);
	}
	
	public Projectile(Sprite parent) {
		super(SHOOT_ANIM, parent);
		this.hasMass = false;
		this.left = parent.left;
		
		DIE_ANIM = new Animation();
		DIE_ANIM.addFrame(new ImageIcon("images/explosion.gif").getImage(), (long) 5);
		
		if(this.left) {
			this.setPosition(parent.getX(), parent.getY() + (parent.getHeight()/2) - this.getHeight()/2);
			this.setVelocityX(-PROJECTILE_SPEED);
		} else {
			this.setPosition(parent.getX(), parent.getY() + (parent.getHeight()/2) - this.getHeight()/2);
			this.setVelocityX(PROJECTILE_SPEED);
		}
		
		spawnTime = System.currentTimeMillis();
		Driver.dr.getRender().register(this);
		Driver.dr.getSpriteUpdater().register(this);
	}
	
	public Projectile(Enemy parent) {
		this((Sprite) parent);
	}
	
	public Projectile(Player parent) {
		this((Sprite) parent);
	}
	
	public void update(long elapsed) {
		if(this.destroyed) {
			return;
		}
		
		super.update(elapsed);
		if(System.currentTimeMillis() > spawnTime + 1000) {
			this.destroy();
		}
		
		if(this.hasHit()) {
			this.destroy();
		}
	}
	
	/**
	 * Check whether this projectile has hit a tile or enemy sprite
	 * 
	 * @return True if the projectile has hit any tile or Enemy class sprite
	 */
	private boolean hasHit() {
		return (this.left && Collision.checkLeftTileCollision(this))
				|| (Collision.checkRightTileCollision(this))
				|| (Collision.checkSpriteCollision(this, Enemy.getEnemies()))
				|| (Collision.checkSpriteCollision(this, Driver.dr.getPlayer()));
	}
	
	/**
	 * Destroy the projectile, do an small explosion animation
	 * then unrender itself
	 */
	private void destroy() {
		this.setAnimation(DIE_ANIM);
		this.destroyed = true;
		
		Projectile sprite = this;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Driver.dr.getRender().unregister(sprite);
			}
		}, 850);
	}
	
	public void draw(Graphics2D graphics) {	
		this.setOffsets((int) Driver.dr.getPlayer().getXOffset(), 0);
		
		if(this.left) {
			this.setOffsets((int) (this.getXOffset() - this.getWidth()), 0);
			this.setScale(-1.0f, 1.0f);
			super.drawTransformed(graphics);
			
			return;
		}
		super.draw(graphics);
	}

	@Override
	public boolean shouldDraw() {
		return true;
	}
}
