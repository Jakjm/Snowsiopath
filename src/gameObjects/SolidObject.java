package gameObjects;

import java.awt.Graphics2D;
import geometry.CollisionData;
import geometry.Shape;
import geometry.Vector;

public abstract class SolidObject extends MovingObject {
	protected Shape hitShape;
	public boolean drawHitshape = false;
	public SolidObject(Vector location,Vector center,Shape hitShape) {
		super(location,center);
		this.hitShape = hitShape;
		this.hitShape.updatePoints(location);
	}
	public void subtractOverlap(CollisionData colData) {
		this.location = this.location.minus(colData.axis.scalarMultiply(colData.overlap));
	}
	public void applyDoubleNormal(CollisionData colData) {
		Vector normalForce = colData.normalForce(this.velocity);
		this.velocity.minusEquals(normalForce.scalarMultiply(2));
	}
	/**
	 * Applies the normal force but also checks if the player is feeling a significant force from the ground.
	 * @param colData - collision data from a collision.
	 * @return whether the player is likely on the ground.
	 */
	public boolean applyNormalForce2(CollisionData colData) {
		Vector normalForce = colData.normalForce(this.velocity);
		this.velocity.minusEquals(normalForce);
		return normalForce.y / Math.abs(normalForce.x) > 1;
	}
	public void applyNormalForce(CollisionData colData) {
		Vector normalForce = colData.normalForce(this.velocity);
		this.velocity.minusEquals(normalForce);
	}
	public boolean checkCollision(SolidObject other) {
		return this.hitShape.checkCollision(other.hitShape);
	}
	public CollisionData collisionWith(SolidObject other) {
		return this.hitShape.checkCollisionData(other.hitShape);
	}
	public void updateShape() {
		this.hitShape.updatePoints(location,angle);
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g,offset);
		if(drawHitshape) {
			hitShape.drawShape(g,offset);
		}
	}

}
