package game;

import java.awt.Rectangle;
import java.util.Collection;

import engine2d.Sprite;
import engine2d.TileMap;

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
    
    public static boolean checkSpriteCollision(Sprite s1, Sprite s2) {
    	if((s1.parent != s2 && s2.parent != s1) && s1.getBoundingBox().intersects(s2.getBoundingBox())) {
    		s1.touch(s2);
    		s2.touch(s1);
    		
    		return true;
    	}
    	
    	return false;
    }
    
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
    
    public static boolean checkUpperTileCollision(Sprite sprite) {
		return checkTileCollision(sprite, sprite.getX(), sprite.getY())
				|| checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY());
    }
    
    public static boolean checkLowerTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX(), sprite.getY() + sprite.getHeight())
    			|| checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight());
    }

    public static boolean checkRightTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX() + sprite.getWidth(), sprite.getY()) 
    			|| checkTileCollision(sprite, sprite.getX()+sprite.getWidth(), sprite.getY() + sprite.getHeight() - 15);
    }

    public static boolean checkLeftTileCollision(Sprite sprite) {
    	return checkTileCollision(sprite, sprite.getX(), sprite.getY())
    			|| checkTileCollision(sprite, sprite.getX()-5, sprite.getY() + sprite.getHeight()-15);
    }
}
