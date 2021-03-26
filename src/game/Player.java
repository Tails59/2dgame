package game;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import engine2d.Animation;
import engine2d.RenderedSprite;
import engine2d.Sound;
import engine2d.Sprite;
import engine2d.UpdateableSprite;

public class Player extends Sprite implements UpdateableSprite, RenderedSprite {
	/*
	 * Attack cooldown time in milliseconds
	 */
	private static final long ATTACK_COOLDOWN = 500;
	private static final Timer timer = new Timer();

	public static final int MAX_HEALTH = 10;

	private Animation walkAnim;
	private Animation attackAnim;
	private Animation idleAnim;
	private Animation dieAnim;

	private Sound attackSound;

	private long lastAttack = 0;
	private int healthPoints = MAX_HEALTH;
	private boolean frozen = false;

	public static Player create() {
		Animation walkAnim = new Animation("images/player/running.gif", 1000l);
		Animation attackAnim = new Animation("images/player/idle-shooting.gif", 1000l);
		Animation idleAnim = new Animation("images/player/idle.gif", 1000l);
		Animation dieAnim = new Animation("images/player/die.gif", 200l);
		
		return new Player(walkAnim, attackAnim, idleAnim, dieAnim);
	}

	private Player(Animation walk, Animation attack, Animation idle, Animation die) {
		super(idle);

		this.idleAnim = idle;
		this.walkAnim = walk;
		this.dieAnim = die;
		this.attackAnim = attack;

		Driver.dr.getRender().register(this);

		this.setPosition(50, 50);
	}

	/**
	 * Detect when the player has hit the screen edge
	 */
	public void offscreen(int side) {
		if (side == Collision.RIGHT) {
			if (Driver.dr.currentLevel() == 3) {
				return;
			}

			Driver.dr.changeLevel(Driver.dr.currentLevel() + 1); // When the player reaches the end of the map, go to
																	// the next level
		}
	}

	/**
	 * Calculate the current X-offset, used to center the player at all times
	 * 
	 * @return
	 */
	private float calculateXOffset() {
		float x = super.getX();
		int w = super.getWidth();

		if (x >= (Driver.dr.getTileMap().getPixelWidth() - Driver.dr.getWidth() / 2)) {
			return (Driver.dr.getTileMap().getPixelWidth() - Driver.dr.getWidth());
		}

		if ((int) (x + (w / 2)) >= Driver.dr.getWidth() / 2) {
			return (x + (w / 2) - (Driver.dr.getWidth() / 2));
		}

		return 0;
	}

	@Override
	public void update(long elapsedTime) {
		this.xoff = (int) calculateXOffset();

		super.update(elapsedTime);

		if (Collision.checkRightTileCollision(this)) {
			this.setVelocityX(0);
		}
		if (Collision.checkLeftTileCollision(this)) {
			this.setVelocityX(0);
		}
		if (Collision.checkLowerTileCollision(this)) {
			this.setVelocityY(0);
		}
	}

	/**
	 * Moves the player left/right
	 * 
	 * @param left [boolean] true if the player should move to the left, false if to
	 *             the right
	 */
	public void move(boolean moveleft) {
		if (this.frozen) {
			return;
		}

		this.setAnimation(walkAnim);

		if (moveleft) {
			if (moveleft && Collision.checkLeftTileCollision(this)) {
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
		if (this.frozen) {
			return;
		}

		if (Collision.checkLowerTileCollision(this)) {
			this.setY(this.getY() - 15);
			this.setVelocityY(-0.5f);
		}
	}

	/**
	 * Respawn the player at the given co-ordinates
	 * 
	 * @param x [int] X coordinate to respawn the player at
	 * @param y [int] Y coordinate to respawn the player at
	 */
	public void respawn(int x, int y) {
		this.healthPoints = MAX_HEALTH;
		this.lastAttack = 0;
		this.left = false;
		this.frozen = false;
		this.setVelocity(0, 0);
		this.setPosition(x, y);
	}

	/**
	 * Fire a projectile (rocket)
	 */
	public void attack() {
		if (!(System.currentTimeMillis() >= this.lastAttack + ATTACK_COOLDOWN)) {
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

		Projectile.create(this);

		this.lastAttack = System.currentTimeMillis();
	}

	public void stop() {
		this.setVelocityX(0f);
		this.setAnimation(idleAnim);
	}

	/**
	 * Called when two sprites touch
	 * 
	 * @param other [Sprite] The sprite that touched this one
	 */
	public void touch(Sprite other) {
		if (this.getHealth() <= 0) {
			return;
		}

		if (other instanceof Projectile) {
			this.healthPoints -= 1;

			if (this.healthPoints <= 0) {
				this.die();
			}
		}
	}

	/**
	 * Kills the player, sets their animation to the deathAnim
	 */
	public void die() {
		this.freeze();
		this.setAnimation(dieAnim);

		Player ply = this;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ply.setAnimation(walkAnim);
				Driver.dr.changeLevel(3);
			}
		}, 2000);

	}

	/**
	 * Toggles whether the player is frozen (accepts keyboard input)
	 */
	private void freeze() {
		this.frozen = !this.frozen;
	}


	/**
	 * Get the player's current health points
	 * 
	 * @return
	 */
	public int getHealth() {
		return this.healthPoints;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.left) {
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
		return true;
	}
}
