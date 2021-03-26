package game;

import java.awt.Rectangle;
import java.util.Collection;

import engine2d.Sprite;
import engine2d.TileMap;

/**
 * A class used for all collision in my game
 * 
 * The collision detection is done by creating a
 * Rectangle object around both sprites/tiles to be
 * checked, and then using the Rectangle.intersects()
 * method to determine whether they are colliding.
 * 
 * @author Taylor Paige
 *
 */
public class Collision {
	public static final int TOP = 1;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	public static final int RIGHT = 2;
	
	/**
     * Checks and handles collisions with the edge of the screen
     * 
     * @param s			The Sprite to check collisions for
     */
    public static void handleScreenEdge(Sprite s)
    {
    	TileMap tmap = Driver.dr.getTileMap();
    	
    	//Check if half of the sprite has fallen off the screen bottom
    	if (s.getY() + (s.getHeight()/2) > tmap.getPixelHeight()) {
    		s.offscreen(BOTTOM);
    	}
    	
    	//Check if half of the sprite has went above the top edge of the screen
    	if (s.getY() + (s.getHeight()/2) <= 0) {
    		s.offscreen(TOP);
    	}
    	
    	if (s.getX() + (s.getWidth()/2) > tmap.getPixelWidth()){
    		s.offscreen(RIGHT);
    	}
    	
    	if (s.getX() <= 0){
    		s.setX(s.getX() + 1);
    		s.stop();
    		s.offscreen(LEFT);
    	}
    }
    
    /**
     * Check collision between two Sprites
     * 
     * Both sprites will have their touch() method called if colliding
     * 
     * @param s1 [Sprite]
     * @param s2 [Sprite]
     * @return colliding [boolean] True if the sprites are colliding
     */
    public static boolean checkSpriteCollision(Sprite s1, Sprite s2) {
    	if((s1.parent != s2 && s2.parent != s1) && s1.getBoundingBox().intersects(s2.getBoundingBox())) {
    		s1.touch(s2);
    		s2.touch(s1);
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Check collision between a Sprite and a Collection of many sprites
     * 
     * If the sprites are touching, both will have their touch() method called
     * with the other sprite
     * 
     * @param s1 [Sprite] Sprite to check collision for, usually the player
     * @param sprites [Collection<Sprite>] A collection of sprites, to check if s1 collides with
     * 			any of them.
     * 
     * @return collides [boolean] True if s1 collides with any of the Sprites in the collection
     */
    public static boolean checkSpriteCollision(Sprite s1, Collection<Sprite> sprites) {
    	for(Object s2 : sprites) {
    		if(s2 instanceof Sprite) {
    			Sprite sprite2 = (Sprite) s2;
    			
    			if(checkSpriteCollision(s1, (Sprite) s2)) {
    				sprite2.touch(s1);
    				s1.touch(sprite2);
        			return true;
        		}
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Check tile collision for a Sprite based on co-ordinates
     * You shouldn't use this directly - use the other methods below, which
     * check for tile collision on all 4 sides of a Sprite
     * 
     * @param sprite [Sprite] Sprite to check collision for
     * @param pos1 [float] X-coordinate for a tile check
     * @param pos2 [float] Y-coordinate for a tile check
     * @return colliding [boolean] True if there is a Tile at (pos1, pos2) 
     * 			and the Sprite collides with it
     */
    private static boolean checkTileCollision(Sprite sprite, float pos1, float pos2) {   	
    	TileMap tmap = Driver.dr.getTileMap();
    	Rectangle spriteBox = sprite.getBoundingBox();

    	int xtile = (int) (pos1/tmap.getTileWidth());
    	int ytile = (int) (pos2/tmap.getTileHeight());
    	
    	int tx = (int) tmap.getTileXC(xtile, ytile);
    	int ty = (int) tmap.getTileYC(xtile, ytile);
    	
    	Rectangle tileBox = new Rectangle(tx, ty, (int) tx+tmap.getTileWidth(), (int) ty+tmap.getTileHeight());
    	
    	return (tileBox.intersects(spriteBox) && tmap.getTileChar(xtile, ytile) != '.');
    }
    
    /**
     * Check tile collision above the Sprite
     * 
     * @param sprite [Sprite] Collision will be checked above this Sprite
     * @return colliding [boolean] True if there is a Tile directly above the Sprite
     */
    public static boolean checkUpperTileCollision(Sprite sprite) {
		return checkTileCollision(sprite, sprite.getX(), sprite.getY())
				|| checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY());
    }
    
    /**
     * Check tile collision underneath of the Sprite
     * 
     * @param sprite [Sprite] Collision will be checked underneath this Sprite
     * @return colliding [boolean] True if there is a Tile directly underneath the Sprite
     */
    public static boolean checkLowerTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX(), sprite.getY() + sprite.getHeight())
    			|| checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight());
    }
    
    /**
     * Check tile collision on the right side of the Sprite
     * 
     * @param sprite [Sprite] Collision will be checked on the right side of this Sprite
     * @return colliding [boolean] True if there is a Tile directly to the right of the Sprite
     */
    public static boolean checkRightTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY()) 
    			|| checkTileCollision(sprite, sprite.getX()+sprite.getWidth(), sprite.getY() + sprite.getHeight() - 15);
    }

    /**
     * Check tile collision on the left side of the Sprite
     * 
     * @param sprite [Sprite] Collision will be checked on the left side of this Sprite
     * @return colliding [boolean] True if there is a Tile directly to the left of the Sprite
     */
    public static boolean checkLeftTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX(), sprite.getY())
    			|| checkTileCollision(sprite, sprite.getX()-5, sprite.getY() + sprite.getHeight()-15);
    }
}
