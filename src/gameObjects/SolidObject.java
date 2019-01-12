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
	public CollisionData collisionWith(SolidObject other) {
		return this.hitShape.sat(other.hitShape);
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
