package geometry;
/**
 * Generic class for 2 Dimensional lines.
 * @author jordan
 * @version 1.00 January 2018
 * Patch 1.02 should include: 
 * 
 */
public class Vector {
	public double x;
	public double y;
	/**
	 * Constructs a zero vector.
	 */
	public Vector() {
		x = 0;
		y = 0;
	}
	/**
	 * Constructs a unit vector at the given angle. 
	 * Uses the Math cos and sine functions for x and y respectively.
	 * @param angle - the angle at which the vector should be facing.
	 */
	public Vector(double angle) {
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
	}
	/**
	 * Deep Copy Constructor.
	 * Constructs a vector to be identical 
	 * @param v
	 */
	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
	}
	/**
	 * Returns a new vector with the dimensions of x,y
	 * @param x - the horizontal component
	 * @param y - the vertical component
	 */
	public Vector(double x,double y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Returns the length of the vector.
	 * Use sparingly - Math.sqrt is pretty inefficient.
	 * @return the length of the vector.
	 */
	public double length() {
		double aSquared = this.x * this.x;
		double bSquared = this.y * this.y;
		return Math.sqrt(aSquared + bSquared);
	}
	/**
	 * Calculates the length of this vector squared.
	 * Much more efficient than using the length method, 
	 * so use this where possible as an alternative.
	 * @return the length of the vector, squared.
	 */
	public double lengthSquared() {
		double lengthSq = this.x * this.x + this.y * this.y;
		return lengthSq;
	}
	public Vector negative() {
		return new Vector(-this.x,-this.y);
	}
	public double slope() {
		if(this.x == 0)return Double.NaN;
		return this.y / this.x;
	}
	/**
	 * Returns the angle compared with 0 radians being EAST, and rotating counterclockwise considered positive.
	 * @return the angle of the vector.
	 */
	public double angle() {
		return Math.atan2(this.y,this.x);
	}
	/**
	 * Computes the scalar multiplication of the scalar onto this vector.
	 * DOES NOT modify the vector, rather returns a scalar multiplied one.
	 * @return the multiplied vector.
	 */
	public Vector scalarMultiply(double scalar) {
		return new Vector(scalar * this.x,scalar * this.y);
	}
	public void timesEquals(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	/**
	 * Computes the projection of THIS VECTOR onto vector b.
	 * Does not modify this vector or b.
	 * @param b - the vector to project this vector onto.
	 * @return the projection from this vector to b.
	 */
	public Vector projection(Vector b) {
		double scalar = this.dotProduct(b) / b.lengthSquared();
		return b.scalarMultiply(scalar);
		
	}
	/**
	 * Returns a unit vector - a vector with the same proportions and a length of one
	 * @return a vector with the same direction but with a length of one.
	 */
	public Vector unitVector() {
		double length = Math.sqrt((this.x * this.x + this.y * this.y));
		double unitX = this.x / length;
		double unitY = this.y / length;
		return new Vector(unitX,unitY);
	}
	/**
	 * Returns a newly initialized zero vector.
	 */
	public static Vector ZERO() {
		return new Vector();
	}
	public static Vector angleVector(double angle) {
		return new Vector(angle);
	}
	public static Vector angleVector(double angle,double length) {
		Vector newVector = new Vector(angle);
		newVector.timesEquals(length);
		return newVector;
	}
	/**
	 * Returns how many multiples of the axis are contained within this vector.
	 * @param axis
	 * @return the scalar multiple of the unit vector.
	 */
	public double computeScalar(Vector axis) {
		if(axis.x !=  0){
			return this.x / axis.x;
		}
		else {
			return this.y / axis.y;
		}
	}
	/**
	 * Returns the dot product between the two vectors.
	 * @param b
	 * @return - The dot product between the two vectors.
	 */
	public double dotProduct(Vector b) {
		return (this.x * b.x + this.y * b.y);
	}
	/**
	 * Constructs a new vector equal to this vector plus another vector.
	 * The vectors used to construct the new one are NOT modified.
	 * @param b - the vector to add to this one.
	 * @return - the sum of the vectors.
	 */
	public Vector add(Vector b) {
		return new Vector(this.x + b.x,this.y + b.y);
	}
	public void plusEquals(Vector b) {
		this.x += b.x;
		this.y += b.y;
	}
	/**
	 * Constructs a new vector equal to this vector minus another vector.
	 * The vectors used to construct the vector returned are not modified.
	 * @param b - the vector that is being subtracted.
	 */
	public Vector minus(Vector b) {
		return new Vector(this.x - b.x,this.y - b.y);
	}
	public void minusEquals(Vector b) {
		this.x -= b.x;
		this.y -= b.y;
	}
	public boolean sameDirection(Vector other) {
		double tolerance = 5e-3;
		if(Math.abs(this.x - other.x) < tolerance && Math.abs(this.y - other.y) < tolerance) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean equals(Object other) {
		if(!(other instanceof Vector)) {
			return false;
		}
		else {
			Vector otherVector = (Vector)other;
			return otherVector.x == this.x && otherVector.y == this.y;
		}
	}
	public String toString() {
		return String.format("X:%.2f Y:%.2f", this.x,this.y);
	}
	/**
	 * Constructs and returns a perpendicular vector.
	 * The vector used to construct it is not modified.
	 * @return a perpendicular vector.
	 */
	public Vector perp() {
		return new Vector(this.y,-this.x);
	}
}
