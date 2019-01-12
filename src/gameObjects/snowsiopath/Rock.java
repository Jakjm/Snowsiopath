package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import gameObjects.Map;
import gameObjects.SolidObject;
import geometry.Shape;
import geometry.Vector;

public class Rock extends Projectile {
	public Polygon drawShape;
	public static final int NUM_POINTS = 12;
	public static final int RADIUS = 25;
	private static final double RANGE = 300;
	public static final int BULLET_SPEED = 8;
	public FlameCluster centerFlames;
	public static final int COLOR_CODE = 0xca9913;
	public static final Color ROCK_COLOR = new Color(COLOR_CODE);
	public FlameCluster pointOne;
	public FlameCluster [] clusterFlames;
	int numFlames = 2;
	public Rock(Vector location,Vector velocity,double angle) {
		super(location,null,new RockShape(),RANGE);
		this.center = new Vector(this.hitShape.centroid);
		drawShape = ((RockShape)this.hitShape).toPolygon();
		//Initializing velocity.
		this.velocity = Vector.angleVector(angle,BULLET_SPEED);
		this.velocity.x += velocity.x;
		this.angularVelocity = Math.signum(this.velocity.x)*0.15;
		
		centerFlames = new FlameCluster(this.location.add(center));
		clusterFlames = new FlameCluster[numFlames];
		for(int i = 0;i < numFlames;i++) {
			clusterFlames[i] = new FlameCluster(this.hitShape.getPoints()[(NUM_POINTS / numFlames) * i]);
		}
	}
	public boolean updateProjectile(Map map) {
		boolean removeBullet = super.updateProjectile(map);
		this.hitShape.updatePoints(location,angle);
		centerFlames.update(this.location.add(center),this.velocity);
		for(int i = 0;i < numFlames;i++) {
			clusterFlames[i].update(this.hitShape.getPoints()[(NUM_POINTS / numFlames) * i],this.velocity);
		}
		return removeBullet;
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g, offset);
		centerFlames.draw(g, offset);
		for(FlameCluster c : clusterFlames) {
			c.draw(g,offset);
		}
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
		public Polygon toPolygon() {
			Polygon p = new Polygon();
			for(Vector v : this.POINTS) {
				p.addPoint((int)v.x,(int)v.y);
			}
			return p;
		}
		public static Vector [] generateShape() {
			Vector roughCenter = new Vector(25,25);
			Vector [] pointList = new Vector [NUM_POINTS];
			double angleIncrement = (Math.PI * 2) / NUM_POINTS;
			for(int i = 0;i < pointList.length;i++) {
				double adjustedRadius = RADIUS + (Math.random() * (RADIUS / 2) - RADIUS / 4);
				double currentAngle = angleIncrement * i + ((Math.random() * (1.0/2))-0.25) * angleIncrement;
				Vector newPoint = new Vector(Math.cos(currentAngle),Math.sin(currentAngle)).scalarMultiply(adjustedRadius).add(roughCenter);
				pointList[i] = newPoint;
			}
			return pointList;
		}
	}
}
