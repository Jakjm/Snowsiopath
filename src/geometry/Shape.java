package geometry;
import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Shape class for the outlines of all shapes within the game.
 * @author jordan
 * @version January 2nd 2018 1.00
 * <h1>
 *  Contains an implementation of the seperating axis theorem.
 * </h1>
 * TODO
 */
public abstract class Shape {
	protected Vector [] vertexList; /** The list of each vertex of the shape **/
	public Vector centroid; /** The center of the shape **/
	protected ArrayList<Vector> checkAxes; /** The axes to check with SAT **/
	protected final Vector POINTS []; /** The points of the shape when at an angle of zero. **/
	
	/**
	 * Creates a shape based on each of the points from the 
	 * @param SHAPE_POINTS
	 */
	public Shape(Vector [] SHAPE_POINTS) {
		this(SHAPE_POINTS,constructCentroid(SHAPE_POINTS));
	}
	public Vector [] getPoints() {
		return this.vertexList;
	}
	public Shape(Vector [] SHAPE_POINTS,Vector centeroid) {
		vertexList = new Vector[SHAPE_POINTS.length];
		POINTS = new Vector[SHAPE_POINTS.length];
		for(int i = 0;i < SHAPE_POINTS.length;i++) {
			POINTS[i] = SHAPE_POINTS[i];
		}
		checkAxes = new ArrayList<Vector>();
		this.centroid = centeroid;
	}
	/**
	 * Updates each point of the shape, assuming the shape has an angle of zero.
	 * @param location - the current location of the shape.
	 */
	public void updatePoints(Vector location) {
		for(int i = 0;i < vertexList.length;i++) {
			vertexList[i] = POINTS[i].add(location);
		}
		this.updateAxes();
	}
	/**
	 * Updates the points for the shape, with the angle and location given as well.
	 * @param location - the current location of the shape.
	 * @param angle - the current angle of the shape.
	 */
	public void updatePoints(Vector location,double angle) {
		for(int i = 0; i < vertexList.length;i++) {
			Vector centerToPoint = POINTS[i].minus(centroid);
			
			//Computes the x and y of that point.
			double sine = Math.sin(angle);
			double cosine = Math.cos(angle);
			double newX = cosine * centerToPoint.x - sine * centerToPoint.y;
			double newY = sine * centerToPoint.x + cosine * centerToPoint.y;
			
			//Creating the vector of the newly adjusted point.
			//The new X and Y get us the location of the point
			Vector newPoint = new Vector(newX,newY); 
			
			//The center and location get us the point relative to the location.
			newPoint.plusEquals(location);
			newPoint.plusEquals(centroid);
			
			vertexList[i] = newPoint;
		}
		this.updateAxes();
	}

	/**
	 * Constructs the centroid point of the shape.
	 */
	public static Vector constructCentroid(Vector [] POINTS) {
		//Getting the max and minimum y distances of the points
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		
		for(int i = 0;i < POINTS.length;i++) {
			if(POINTS[i].x > maxX)maxX = POINTS[i].x;
			if(POINTS[i].x < minX)minX = POINTS[i].x;
			if(POINTS[i].y < minY)minY = POINTS[i].y;
			if(POINTS[i].y > maxY)maxY = POINTS[i].y;
		}
		double centerX = minX + (maxX - minX)/2;
		double centerY = minY + (maxY - minY)/2;
		Vector centroid = new Vector(centerX,centerY);
		return centroid;
	}
	/**
	 * Check for collision without generating collision data. 
	 * @param otherShape - the shape we may have collided with.
	 * @return whether we have collided with the other shape or not.
	 */
	public boolean checkCollision(Shape otherShape) {
		checkAxes.addAll(otherShape.checkAxes);
		for(Vector axis : checkAxes) {
			Projection thisP = new Projection(this,axis);
			Projection otherP = new Projection(otherShape,axis);
			
			double maxOverMin = thisP.max - otherP.min;
			double minUnderMax = thisP.min - otherP.max;
			if(maxOverMin <= 0 || minUnderMax >= 0) {
				return false;
			}
		}
		return true;
	}
	//Performs a seperating axis theorem collision check on the shape. 
	public CollisionData checkCollisionData(Shape otherShape) {
		double leastOverlap = Double.POSITIVE_INFINITY;
		Vector leastOverlapAxis = null;
		Vector collisionPoint = null;
		
		//Getting the axes we have to check for our shape, and the other shape.
		LinkedList<Vector> axes = new LinkedList<Vector>();
		axes.addAll(checkAxes);
		axes.addAll(otherShape.checkAxes);
		
		//Eliminate perpendicular axes here?
		for(Vector axis : axes) {
			Projection thisP = new Projection(this,axis);
			Projection otherP = new Projection(otherShape,axis);
			
			double maxOverMin = thisP.max - otherP.min;
			double minUnderMax = thisP.min - otherP.max;
			if(maxOverMin >= 0 && minUnderMax <= 0) {
				if(Math.abs(maxOverMin) < Math.abs(leastOverlap)) {
					leastOverlap = maxOverMin;
					leastOverlapAxis = axis;
					collisionPoint = thisP.maxPoint;
				}
				if(Math.abs(minUnderMax) < Math.abs(leastOverlap)) {
					leastOverlap = minUnderMax;
					leastOverlapAxis = axis;
					collisionPoint = thisP.minPoint;
				}
			}
			else {
				return null;
			}
		}
		return new CollisionData(leastOverlap,leastOverlapAxis,collisionPoint);
	}
	
	/**
	 * Returns a list of the perpendicular axes to the side lengths of the shape.
	 * Used for the seperating axis theorem collision detection.
	 * @return the list of perpendicular axes of the side lengths of the object.
	 */
	private void updateAxes() {
		checkAxes.clear();
		for(int i = 0;i < vertexList.length;i++) {
			Vector thisPoint = vertexList[i];
			/*
			 * Getting the next vertex, or vertex 0 if at the last one, to get the edge between this point and the next.
			 * Then taking the perpendicular vector to that edge for our collision algorithm.
			 */
			Vector nextPoint = vertexList[(i == vertexList.length - 1)?0:i+1];
			Vector newAxis = thisPoint.minus(nextPoint).perp().unitVector();
			/*
			 * If we already have an axis that is parallel to this one...
			 */
			boolean haveParallel = false;
			for(Vector v : checkAxes) {
				if(v.sameDirection(newAxis,0.005) || v.negative().sameDirection(newAxis,0.005)) {
					haveParallel = true;
					break;
				}
			}
			if(haveParallel) {
				continue;
			}
			checkAxes.add(newAxis); //Adds the perpendicular vector to our list of axes to check.
		}
	}
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("Shape: %d points, Center: %s\n",this.vertexList.length, this.centroid));
		for(int i = 0;i < POINTS.length;i++) {
			str.append(String.format("%s\n",POINTS[i]));
		}
		return str.toString();
	}
	/**
	 * Draws the points of the shape.
	 * @param g
	 */
	public void drawShape(Graphics g,Vector offset) {
		//Drawing the expected points
		g.setColor(Color.black);
		g.translate(-(int)offset.x,-(int)offset.y);
		for(int i = 0;i < this.vertexList.length;i++) {
			g.fillRect((int)this.vertexList[i].x-2,(int)this.vertexList[i].y-2,4,4);
		}
		g.translate((int)offset.x,(int)offset.y);
	}
	/**
	 * Class for the projection of A SHAPE, not just a single point, onto an axis.
	 * Gets the maximum and minimum values for the shape along the line, as if the shape is casting a shadow. 
	 * @author jordan
	 * @version April 4th
	 */
	private class Projection{
		double min;
		double max;
		Vector minPoint;
		Vector maxPoint;
		public Projection(Shape shape,Vector axis) {
			projectShape(shape,axis);
		}
		/**
		 * Projects the shape onto the given axis.
		 * Finds the maximum and minimum point of this shape along the axis.
		 * @param shape - the shape to be projected onto the axis.
		 * @param axis - the axis to project along.
		 */
		public void projectShape(Shape shape,Vector axis) {
			this.max = Double.NEGATIVE_INFINITY;
			this.min = Double.POSITIVE_INFINITY;
			for(Vector vertex : shape.vertexList) {
				//Projecting a point onto the axis and seeing whether it is a max or min point along the axis.
				double value = vertex.projectionScalar(axis);
				if(value > max) {
					max = value;
					maxPoint = vertex;
				}
				if(value < min) {
					min = value;
					minPoint = vertex;
				}
			}
		}
		public String toString() {
			return "MAX: " + max + " MIN: " + min;
		}
	}
	
	
}
