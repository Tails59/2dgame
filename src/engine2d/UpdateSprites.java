package engine2d;

import java.util.Iterator;
import java.util.Vector;

/**
 * A class for registering sprites to have their update method called,
 * rather than having to hard-code each sprites update method into the driver
 * @author Taylor Paige
 */
public class UpdateSprites {
	private Vector<UpdateableSprite> sprites = new Vector<>(15, 5);
	
	public void update(long elapsedTime) {
		Iterator<UpdateableSprite> iterator = sprites.iterator();
		
		while(iterator.hasNext()) {
			UpdateableSprite sprite = iterator.next();
			
			sprite.update(elapsedTime);
		}
	}
	
	public void register(UpdateableSprite obj) {
		sprites.add(obj);
	}
	
	public void unregister(UpdateableSprite obj) {
		int index = sprites.indexOf(obj);
		
		if(index != -1) {
			sprites.remove(index);
		}
	}
}
