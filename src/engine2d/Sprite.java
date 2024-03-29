package engine2d;

import java.awt.Image;
import java.awt.*;
import java.awt.geom.*;

import game.Collision;
import game.Driver;

/**
 * This class provides the functionality for a moving animated image or Sprite.
 * 
 * @author David Cairns
 *
 */
public class Sprite implements RenderedSprite, UpdateableSprite{

	// The current Animation to use for this sprite
    private Animation anim;		

    // Position (pixels)
    private float x;
    private float y;

    // Velocity (pixels per millisecond)
    private float dx;
    private float dy;

    // Dimensions of the sprite
    private float height;
    private float width;
    private float radius;

    // The scale to draw the sprite at where 1 equals normal size
    private double scalex;
    private double scaley;
    // The rotation to apply to the sprite image
    private double rotation;

    // If render is 'true', the sprite will be drawn when requested
    private boolean render;
    
    // The draw offset associated with this sprite. Used to draw it
    // relative to specific on screen position (usually the player)
    protected int xoff=0;
    protected int yoff=0;
    
    //The parent sprite, there wont be any collision interactions with this
    //Used for things like spawning bullets
    public final Sprite parent;
    
    public boolean left = false;
    
    //Determines whether or not this sprite is affected by gravity
    protected boolean hasMass = true;

    /**
     *  Creates a new Sprite object with the specified Animation.
     *  
     * @param a The animation to use for the sprite.
     * 
     */
    public Sprite(Animation anim, Sprite parent) {
        this.anim = anim;
        render = true;
        scalex = 1.0f;
        scaley = 1.0f;
        rotation = 0.0f;
        
        left = false;
        this.parent = parent;
    }
    
    public Sprite(Animation anim) {
    	this(anim, null);
    }

    /**
     * Change the animation for the sprite to 'a'.
     *
     * @param a The animation to use for the sprite.
     */
    public void setAnimation(Animation a)
    {
    		anim = a;
    }
    
    /**
     * Set the current animation to the given 'frame'
     * 
     * @param frame The frame to set the animation to
     */
    public void setAnimationFrame(int frame)
    {
    	anim.setAnimationFrame(frame);
    }
    
    /**
     * Pauses the animation at its current frame. Note that the 
     * sprite will continue to move, it just won't animate
     */
    public void pauseAnimation()
    {
    	anim.pause();
    }
    
    /**
     * Pause the animation when it reaches frame 'f'. 
     * 
     * @param f The frame to stop the animation at
     */
    public void pauseAnimationAtFrame(int f)
    {
    	anim.pauseAt(f);
    }
    
    /**
     * Change the speed at which the current animation runs. A
     * speed of 1 will result in a normal animation,
     * 0.5 will be half the normal rate and 2 will double it.
     * 
     * Note that if you change animation, it will run at whatever
     * speed it was previously set to.
     * 
     * @param speed	The speed to set the current animation to.
     */
    public void setAnimationSpeed(float speed)
    {
    	anim.setAnimationSpeed(speed);
    }
    
    /**
     * Starts an animation playing if it has been paused.
     */
    public void playAnimation()
    {
    	anim.play();
    }
    
    /**
     * Returns a reference to the current animation
     * assigned to this sprite.
     * 
     * @return A reference to the current animation
     */
    public Animation getAnimation()
    {
    	return anim;
    }
    
    public float getXOffset() {
    	return xoff;
    }
    
    public float getYOffset() {
    	return yoff;
    }

    /**
        Updates this Sprite's Animation and its position based
        on the elapsedTime.
        
        @param The time that has elapsed since the last call to update
    */
    public void update(long elapsedTime) {
    	if (!render) return;
        
        if(!Collision.checkLowerTileCollision(this) && hasMass) {
			dy = (float) (this.dy+(0.001f*elapsedTime));
		} else {
			dy=0;
		}
        
        Collision.handleScreenEdge(this);
        
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        
        anim.update(elapsedTime);
        
        width = anim.getImage().getWidth(null);
        height = anim.getImage().getHeight(null);
        
        if (width > height)
        	radius = width / 2.0f;
        else
        	radius = height / 2.0f;
    }

    /**
        Gets this Sprite's current x position.
    */
    public float getX() {
        return x;
    }

    /**
        Gets this Sprite's current y position.
    */
    public float getY() {
        return y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(float y) {
        this.y = y;
    }

    /**
	    Sets this Sprite's new x and y position.
	*/
	public void setPosition(float x, float y) 
	{
	    this.x = x;
	    this.y = y;
	}
		
	/**
	 * Called when this sprite has went off the screen
	 * 
	 * @param edge [int] Which edge the sprite has fallen off
	 */
	public void offscreen(int edge) {
		if(edge == Collision.RIGHT) {
			this.stop();
			this.setX(Driver.dr.getTileMap().getPixelWidth() - this.getWidth());
		}
	}

    public void shiftX(float shift)
    {
    	this.x += shift;
    }
    
    public void shiftY(float shift)
    {
    	this.y += shift;
    }
    
    /**
        Gets this Sprite's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return anim.getImage().getWidth(null);
    }

    /**
        Gets this Sprite's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return anim.getImage().getHeight(null);
    }

    /**
    	Gets the sprites radius in pixels
    */
    public float getRadius()
    {
    	return radius;
    }

    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return dy;
    }

    public void move(boolean left) {
    	
    }
    

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    /**
    	Sets the horizontal and vertical velocity of this Sprite in pixels
    	per millisecond.
	*/
	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
		Set the scale of the sprite to 's'. If s is 1
		the sprite will be drawn at normal size. If 's'
		is 0.5 it will be drawn at half size. Note that
		scaling and rotation are only applied when
		using the drawTransformed method.
	*/
    public void setScale(float s, float ss)
    {
    	scalex = s;
    	scaley = ss;
    }

	/**
		Get the current value of the scaling attribute.
		See 'setScale' for more information.
	*/
    public double getScaleX()
    {
    	return scalex;
    }
    
    /**
		Get the current value of the scaling attribute.
		See 'setScale' for more information.
	*/
	public double getScaleY()
	{
		return scaley;
	}

	/**
		Set the rotation angle for the sprite in degrees.
		Note that scaling and rotation are only applied when
		using the drawTransformed method.
	*/
    public void setRotation(double r)
    {
    	rotation = Math.toRadians(r);
    }

	/**
		Get the current value of the rotation attribute.
		in degrees. See 'setRotation' for more information.
	*/
    public double getRotation()
    {
    	return Math.toDegrees(rotation);
    }
    
    public void touch(Sprite other) {    }

    /**
     	Stops the sprites movement at the current position
    */
    public void stop()
    {
    	dx = 0;
    	dy = 0;
    }

    /**
        Gets this Sprite's current image.
    */
    public Image getImage() {
        return anim.getImage();
    }

	/**
		Draws the sprite with the graphics object 'g' at
		the current x and y co-ordinates. Scaling and rotation
		transforms are NOT applied.
	*/
    public void draw(Graphics2D g)
    {
    	if (!render) return;

    	g.drawImage(getImage(), (int) x - xoff, (int)y, null);
    }
    
    /**
		Draws the bounding box of this sprite using the graphics object 'g' and
		the currently selected foreground colour.
	*/
    public void drawBoundingBox(Graphics2D g)
    {
    	if (!render) return;

		g.setColor(Color.black);
    	g.drawRect((int) x - xoff, (int) y, (int) getWidth(), (int) getHeight());
    }
    
    public Rectangle getBoundingBox() {
    	return new Rectangle((int) x - xoff, (int) y, (int) getWidth(), (int) getHeight());
    }
    
    /**
		Draws the bounding circle of this sprite using the graphics object 'g' and
		the currently selected foreground colour.
	*/
    public void drawBoundingCircle(Graphics2D g)
    {
    	if (!render) return;

		Image img = getImage();
		
    	g.drawArc((int)x,(int)y,img.getWidth(null),img.getHeight(null),0, 360);
    }
    

	/**
		Draws the sprite with the graphics object 'g' at
		the current x and y co-ordinates with the current scaling
		and rotation transforms applied.
		
		@param g The graphics object to draw to,
	*/
    public void drawTransformed(Graphics2D g)
    {
    	if (!render) return;

		AffineTransform transform = new AffineTransform();
		transform.translate(Math.round(x-xoff),Math.round(y));
		transform.scale(scalex,scaley);
		transform.rotate(rotation,getImage().getWidth(null)/2,getImage().getHeight(null)/2);
		// Apply transform to the image and draw it
		g.drawImage(getImage(),transform,null);
    }


	/**
		Hide the sprite.
	*/
    public void hide()  {	render = false;  }

	/**
		Show the sprite
	*/
    public void show()  {  	render = true;   }

	/**
		Check the visibility status of the sprite.
	*/
    public boolean isVisible() { return render; }

	/**
		Set an x & y offset to use when drawing the sprite.
		Note this does not affect its actual position, just
		moves the drawn position.
	*/
    public void setOffsets(int x, int y)
    {
    	xoff = x;
    	yoff = y;
    }

	@Override
	public boolean shouldDraw() {
		return false;
	}
}
