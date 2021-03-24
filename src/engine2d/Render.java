package engine2d;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

/**
 * A class for registering sprites to have their draw method called,
 * rather than having to hard-code each sprites draw method into the driver
 * @author Taylor Paige
 *
 */
public class Render {
	private Vector<RenderedSprite> sprites = new Vector<>(15, 5);
	
	public void draw(Graphics2D graphics) {
		Iterator<RenderedSprite> iterator = sprites.iterator();
		
		while(iterator.hasNext()) {
			Sprite sprite = (Sprite) iterator.next();
			
			if(sprite.shouldDraw()) {
				sprite.draw(graphics);
			}
		}
	}
	
	/*
	 *  Register any object that implements RenderedSprite to have their
	 *  draw method called when necessary.
	 *  
	 *  @param obj [RenderedSprite] Object that will have its draw() method called
	 *  @param level [int] Priority this object will have over being drawn before others,
	 */
	public void register(RenderedSprite obj) {
		sprites.add(obj);
	}
	
	/**
	 * Unregister an object that was previously added using the register() method
	 * This will prevent its draw() method from being called. You should use this when the sprite
	 * is being removed, or when the sprite wont need to be drawn for a long time, or when the
	 * sprite's draw priority needs to be changed (re-register it with a new index straight after)
	 * 
	 * Failing to de-register a sprite when necessary may cause exceptions or memory leaks
	 * 
	 * @param obj [RenderedSprite] object to unregister
	 */
	public void unregister(RenderedSprite obj) {
		int index = sprites.indexOf(obj);
		
		if(index != -1) {
			sprites.remove(index);
		}
	}
}
