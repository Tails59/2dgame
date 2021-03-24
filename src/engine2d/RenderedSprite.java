package engine2d;

import java.awt.Graphics2D;

public interface RenderedSprite {
	
	/**
	 *  Called to check whether a sprite should be drawn or not
	 *  Avoid doing extensive calculations in this method, as it will
	 *  cause performance issues (this method is called every update)
	 *  
	 * @return True if the sprite should be drawn, false if it should be hidden
	 */
	public abstract boolean shouldDraw();
	
	public abstract void draw(Graphics2D g);
}
