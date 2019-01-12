package gameObjects.snowsiopath;

import gameObjects.Map;
import gameObjects.SolidObject;
import geometry.Shape;
import geometry.Vector;

public abstract class Projectile extends SolidObject{
	private Vector distanceTravelled;
	private final double RANGE;
	public Projectile(Vector location,Vector center,Shape hitshape,double range) {
		super(location,center,hitshape);
		distanceTravelled = new Vector();
		this.RANGE = range;
	}
	public boolean updateProjectile(Map map) {
		super.update();
		distanceTravelled.plusEquals(velocity);
		if(distanceTravelled.lengthSquared() > RANGE * RANGE) {
			return true;
		}
		return false;
	}
}
