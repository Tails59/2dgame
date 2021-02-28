package game;

import java.awt.Rectangle;

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
     * @param tmap		The tile map to check 
     * @param elapsed	How much time has gone by since the last call
     */
    public static void handleScreenEdge(Sprite s, TileMap tmap, long elapsed)
    {
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
    	
    	if (s.getX() + (s.getWidth()/2) <= 0){
    		s.offscreen(LEFT);
    	}
    }
    
    public static void checkSpriteCollision(Sprite s1, Sprite s2) {
    	s1.getBoundingBox().intersects(s2.getBoundingBox());
    }
    
    private static boolean checkTileCollision(Sprite sprite, float pos1, float pos2) {
    	TileMap tmap = Driver.dr.getTileMap();
    	Rectangle spriteBox = sprite.getBoundingBox();

    	int xtile = (int) (pos1/tmap.getTileWidth());
    	int ytile = (int) (pos2/tmap.getTileHeight());
    	
    	int tx = (int) tmap.getTileXC(xtile, ytile);
    	int ty = (int) tmap.getTileYC(xtile, ytile);
    	
    	Rectangle tileBox = new Rectangle(tx, ty, (int) tx+tmap.getTileWidth(), (int) ty+tmap.getTileHeight());
    	
    	if(tileBox.intersects(spriteBox) && tmap.getTileChar(xtile, ytile) != '.') {
    		return true;
    	}
    	
    	return false;	
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
