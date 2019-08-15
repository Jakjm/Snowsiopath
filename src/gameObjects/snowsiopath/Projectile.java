package gameObjects.snowsiopath;

import gameObjects.Map;
import gameObjects.SolidObject;
import gameObjects.Wall;
import geometry.CollisionData;
import geometry.Shape;
import geometry.Vector;

public abstract class Projectile extends SolidObject{
	protected Vector distanceTravelled;
	private final double RANGE;
	public Projectile(Vector location,Vector center,Shape hitshape,double range) {
		super(location,center,hitshape);
		distanceTravelled = new Vector();
		this.RANGE = range;
	}
	
	public boolean hit(CollisionData colData,Map map) {
		this.subtractOverlap(colData);
		this.updateShape();
		return true;
	}
	public boolean updateProjectile(Map map) {
		super.update();
		
		boolean deadBullet = false;
		distanceTravelled.plusEqualsAbs(velocity);
		if(distanceTravelled.lengthSquared() > RANGE * RANGE) {
			deadBullet = true;
		}
		
		this.updateShape();
		
		for(Wall wall : map.wallList) {
			CollisionData colData = this.collisionWith(wall);
			if(colData != null) {
					if(hit(colData,map))deadBullet = true;
					this.updateShape();
			}
		}
		return deadBullet;
	}
}
