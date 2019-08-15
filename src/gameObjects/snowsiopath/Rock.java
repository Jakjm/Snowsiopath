package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import gameObjects.Map;
import gameObjects.SolidObject;
import geometry.CollisionData;
import geometry.Shape;
import geometry.Vector;

public class Rock extends Projectile {
	public Polygon drawShape;
	public static final int NUM_POINTS = 18;
	public static final int BASE_RADIUS = 55;
	
	private static final double RANGE = 800;
	private static final double DEBRIS_RANGE = 500;
	public static final int DEBRIS_SPEED = 8;
	public static final int BULLET_SPEED = 24;
	public static final int COLOR_CODE = 0xff5213;
	public static final Color ROCK_COLOR = new Color(COLOR_CODE);
	public Map map;
	boolean split;
	public Rock(Vector location,Vector velocity,double angle) {
		super(location,null,new RockShape(),RANGE);
		this.drawHitshape = true;
		split = true;
		this.center = new Vector(this.hitShape.centroid);
		drawShape = ((RockShape)this.hitShape).toPolygon();
		this.angle = angle;
		this.velocity = Vector.angleVector(angle,BULLET_SPEED);
		this.angularVelocity = Math.signum(this.velocity.x)*0.06;
		this.updateShape();
	}
	public Rock(Vector location, double angle,Shape shape) {
		super(location,null,shape,DEBRIS_RANGE);
		this.drawHitshape = true;
		this.center = new Vector(this.hitShape.centroid);
		drawShape = ((RockShape)this.hitShape).toPolygon();
		this.angle = angle;
		this.velocity = Vector.angleVector(angle,DEBRIS_SPEED);
		this.angularVelocity = Math.signum(this.velocity.x)*0.06;
		this.updateShape();
	}
	public String toString() {
		return this.hitShape.toString();
	}
	public void splitRocks(Map map, Vector normal) {
		System.out.println("Senior\n" + this);
		Vector [] newShape;
		int currentPoint = 0;
		int debrisPoint;
		double debrisAngle;
		Vector [] points = this.hitShape.getAbsolutePoints();
		while(currentPoint < points.length) {
			int shapePoints = 3 + (int)(Math.random() * 4);
			while(currentPoint + shapePoints < points.length - 5) {
				debrisPoint = 0;
				newShape = new Vector[shapePoints + 1];
				while(shapePoints > 0) {
					newShape[debrisPoint] = points[currentPoint];
					debrisPoint++;
					currentPoint++;
					shapePoints--;
				}
				newShape[debrisPoint] = this.hitShape.centroid;
				debrisAngle = this.angle + Shape.constructCentroid(newShape).minus(this.hitShape.centroid).angle();
				Rock newRock = new Rock(location,debrisAngle,new RockShape(newShape));
				System.out.println("Child\n" + newRock);
				newRock.velocity.minusEquals(normal);
				this.map.projectileQueue.add(newRock);
				shapePoints = 3 + (int)(Math.random() * 4);
			}
			shapePoints = points.length - currentPoint;
			debrisPoint = 0;
			newShape = new Vector[shapePoints + 1];
			
			while(shapePoints > 0) {
				newShape[debrisPoint] = points[currentPoint];
				debrisPoint++;
				currentPoint++;
				shapePoints--;
			}
			
			newShape[debrisPoint] = this.hitShape.centroid;
			debrisAngle = this.angle + Shape.constructCentroid(newShape).minus(this.hitShape.centroid).angle();
			
			Rock newRock = new Rock(location,debrisAngle,new RockShape(newShape));
			System.out.println("Child\n" + newRock);
			newRock.velocity.minusEquals(normal);
			this.map.projectileQueue.add(newRock);
		}
		
	}
	public boolean hit(CollisionData data,Map map) {
		super.hit(data,map);
		Vector normalForce = data.normalForce(this.velocity);
		if(split) {
			splitRocks(map,normalForce);
			return true;
		}
		else {
			return false;
		}
					
	}
	public boolean updateProjectile(Map map) {
		if(this.map == null)this.map = map; 
		boolean removeBullet = super.updateProjectile(this.map);
		this.velocity.y += Map.GRAVITY;
		return removeBullet;
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g, offset);
	}
	@Override
	public void drawObject(Graphics2D g) {
		g.setColor(ROCK_COLOR);
		g.fillPolygon(drawShape);
	}
	public static class RockShape extends Shape{
		public RockShape() {
			super(generateShape());
		}
		public RockShape(Vector [] points) {
			super(points);
		}
		public Polygon toPolygon() {
			Polygon p = new Polygon();
			for(Vector v : this.POINTS) {
				p.addPoint((int)v.x,(int)v.y);
			}
			return p;
		}
	
		public static Vector [] generateShape() {
			Vector roughCenter = new Vector(BASE_RADIUS,BASE_RADIUS);
			Vector [] pointList = new Vector [NUM_POINTS];
			double angleIncrement = (Math.PI * 2) / NUM_POINTS;
			for(int i = 0;i < pointList.length;i++) {
				double adjustedRadius = BASE_RADIUS * 3/4f + (Math.random() * (BASE_RADIUS / 2f));
				double currentAngle = angleIncrement * i + ((Math.random() * (1.0/2))-0.25) * angleIncrement;
				Vector newPoint = new Vector(Math.cos(currentAngle),Math.sin(currentAngle)).scalarMultiply(adjustedRadius).add(roughCenter);
				pointList[i] = newPoint;
			}
			return pointList;
		}
	}
}
