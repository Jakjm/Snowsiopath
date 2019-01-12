package gameObjects;

import gameObjects.GameObject;
import geometry.Vector;

/**
 * Class for moving objects.
 * @author jordan
 * @version April 9th 2018
 */
public abstract class MovingObject extends GameObject{
	/**
	 * The translational velocity of the object.
	 */
	public Vector velocity;
	/**
	 * The angular velocity of the object.
	 */
	public double angularVelocity;
	public MovingObject(Vector location,Vector center) {
		super(location,center);
		velocity = new Vector(0,0);
		angularVelocity = 0;
	}
	public void applyVelocity() {
		this.location.plusEquals(this.velocity);
	}
	public void applyRotation() {
		this.angle += this.angularVelocity;
	}
	/**
	 * Updates the moving object with
	 * @param wallList
	 */
	public void update() {
		applyVelocity();
		applyRotation();
	}
	
}
