package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import game.MusicPlayer;
import game.MusicPlayer.BufferedSFX;
import gameObjects.Map;
import gameObjects.SolidObject;
import geometry.CollisionData;
import geometry.Shape;
import geometry.Vector;

public class Rock extends Projectile {
	public Polygon drawShape;
	int numBreaks;
	public static final int NUM_POINTS = 14;
	public static final int BASE_RADIUS = 25;
	private static final double RANGE = 1200;
	private static final double DEBRIS_RANGE = 800;
	public static final int BULLET_SPEED = 32;
	public static final int COLOR_CODE = 0x522208;
	public static final Color ROCK_COLOR = new Color(COLOR_CODE);
	public Map map;
	public static int effectNum = 0;
	public static BufferedSFX effect;
	public static void loadRocks() {
		effect = new BufferedSFX("rockSmash.wav",10);
	}
	/**
	 * Constructor for rock projectiles fired by the magic wand. 
	 * @param location
	 * @param velocity
	 * @param angle
	 */
	public Rock(Vector location,Vector velocity,double angle) {
		super(location,null,new RockShape(),RANGE);
		//this.drawHitshape = true;
		this.center = new Vector(this.hitShape.centroid);
		drawShape = ((RockShape)this.hitShape).toPolygon();
		this.angle = angle;
		this.velocity = Vector.angleVector(angle,BULLET_SPEED);
		this.angularVelocity = Math.signum(this.velocity.x)*0.06;
		this.updateShape();
		numBreaks = 2;
		
		
	}
	/**
	 * Constructor for Rocks in the form of Debris. 
	 * @param location
	 * @param angle
	 * @param shape
	 * @param numBreaks
	 * @param normalLength
	 */
	public Rock(Vector location,double angle,Shape shape, int numBreaks,double normalLength) {
		super(location,shape.centroid,shape,DEBRIS_RANGE);
		//this.drawHitshape = true;
		drawShape = ((RockShape)this.hitShape).toPolygon();
		this.velocity = Vector.angleVector(angle,normalLength);
		this.angularVelocity = Math.signum(angle) * 0.06;
		this.updateShape();
		this.numBreaks = numBreaks - 1;
	}
	public String toString() {
		return this.hitShape.toString();
	}
	/**
	 * Method for disecting rocks into debris. 
	 * 
	 * @param map - the map, used for adding the debris rocks to the projectile queue. 
	 * @param normal - the normal force of collision for the rock that has crashed. 
	 */
	public void splitRocks(Map map, Vector normal) {
		
		//Declaring variables 
		int currentPoint = 0;
		int newShapePoints = 2 + (int)(Math.random() * 4); //Number of points for the new shape, not including the old shape's center. 
		int debrisPoint;
		
		normal = normal.scalarMultiply(0.2);
		double normalLength = normal.length();
		
		Vector[] newArr;
		Vector[] points = this.hitShape.getPoints();
		while(currentPoint + newShapePoints < points.length - 2) {
			debrisPoint = 0;
			
			//Adding the points to our array. 
			newArr = new Vector[newShapePoints + 1];
			while(newShapePoints > 0) {
				newArr[debrisPoint] = points[currentPoint].minus(this.location);
				currentPoint++;
				debrisPoint++;
				newShapePoints--;
			}
			
			//Creating a rock from the array and adding it to our projectile queue. 
			newArr[debrisPoint] = this.hitShape.centroid;
			Vector newCenter = Shape.constructCentroid(newArr);
			double angle = newCenter.minus(this.hitShape.centroid).angle();
			Rock newRock = new Rock(location,angle,new RockShape(RockShape.addMidPoints(RockShape.addMidPoints(newArr))),numBreaks,normalLength);
			newRock.velocity.minusEquals(normal);
			this.map.projectileQueue.add(newRock);
			//Make the new rock. 
			
			currentPoint--;
			newShapePoints = 2 + (int)(Math.random() * 4); //Generating another random  set of points for the next new shape. 
		}
		
		//Building a rock from the leftover points. 
		debrisPoint = 0;
		newShapePoints = (points.length - currentPoint);
		
		//Adding the points to our array. 
		newArr = new Vector[newShapePoints + 2];
		while(newShapePoints >= 0) {
			if(newShapePoints == 0)currentPoint = 0;
			newArr[debrisPoint] = points[currentPoint].minus(location);
			currentPoint++;
			debrisPoint++;
			newShapePoints--;
		}
		
		//Creating a rock from the array and adding it to the queue. 
		newArr[debrisPoint] = this.hitShape.centroid;
		Vector newCenter = Shape.constructCentroid(newArr);
		double angle = newCenter.minus(this.hitShape.centroid).angle();
		Rock newRock = new Rock(location,angle,new RockShape(RockShape.addMidPoints(RockShape.addMidPoints(newArr))),numBreaks,normalLength);
		newRock.velocity.minusEquals(normal);
		this.map.projectileQueue.add(newRock);
	}
	public boolean hit(CollisionData data,Map map) {
		super.hit(data,map);
		Vector normalForce = data.normalForce(this.velocity);
		
		if(numBreaks > 0) {
			splitRocks(map,normalForce);
			if(numBreaks == 2)effect.play();
			//this.location.plusEquals(new Vector(0,-200));
		}
		return true;	
	}
	public boolean updateProjectile(Map map) {
		if(this.map == null)this.map = map; 
		this.velocity.y += Map.GRAVITY;
		return super.updateProjectile(map);
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g, offset);
	}
	@Override
	public void drawObject(Graphics2D g) {
		g.setColor(ROCK_COLOR);
		g.fillPolygon(drawShape);
		g.setColor(Color.red);
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
		public static Vector [] addMidPoints(Vector[] points) {
			Vector newPoints [] = new Vector[points.length * 2];
			for(int i = 0;i < points.length;i++) {
				newPoints[i*2] = points[i];
			}
			for(int i = 1;i < newPoints.length;i += 2) {
				Vector halfway = newPoints[i+1 != newPoints.length ? i+1:0].minus(newPoints[i-1]).scalarMultiply(0.5);
				newPoints[i] = newPoints[i-1].add(halfway); 
			}
			return newPoints;
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
