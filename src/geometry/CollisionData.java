package geometry;

/**
 * Collision information between two different shapes.
 * @author jordan
 *
 */
public class CollisionData {
	//The overlap amount of the collision
	public double overlap;
	//The normal axis to the collision.
	public Vector axis;
	//The point where the objects collided.
	public Vector collisionPoint;
	public CollisionData(double overlap,Vector axis,Vector collisionPoint) {
		this.overlap = overlap;
		this.axis = axis;
		this.collisionPoint = collisionPoint;
	}
	/**
	 * Computes the normal force from the axis of collision
	 * @param velocity
	 * @return
	 */
	public Vector normalForce(Vector velocity) {
		return velocity.projection(this.axis);
	}
}
