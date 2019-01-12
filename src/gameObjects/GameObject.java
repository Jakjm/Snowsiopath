package gameObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import geometry.Vector;

/**
 * Class for all objects within the game.
 * @author jordan
 * @version April 4th 2018
 */
public abstract class GameObject {
	public Vector location;
	public double angle;
	public Vector center;
	public GameObject(Vector location,Vector center) {
		this.location = new Vector(location.x,location.y);
		this.center = center;
		angle = 0;
	}
	public int locationX() {
		return (int)location.x;
	}
	public int locationY() {
		return (int)location.y;
	}
	/**
	 * Draws the object at the 'angle' in radians.
	 * @param g - graphics object for the current container.
	 */
	public void draw(Graphics2D g,Vector offset) {
		AffineTransform priorState = g.getTransform();
		AffineTransform newState = new AffineTransform();
	    if(angle != 0) {
			double translationX = location.x+center.x-offset.x;
			double translationY = location.y+center.y-offset.y;
			newState.rotate(angle,translationX,translationY);
	    }
	    newState.translate(locationX()-offset.x,locationY()-offset.y);
		g.setTransform(newState);
		this.drawObject(g);
		g.setTransform(priorState);
	}
	/**
	 
	 * @param g
	 */
	public abstract void drawObject(Graphics2D g);
}
