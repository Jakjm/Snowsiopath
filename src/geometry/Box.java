package geometry;

/**
 * Collision box extension of the shape class.
 * @author jordan
 */
public class Box extends Shape {
	public Box(int sizeX,int sizeY) {
		super(createPoints(sizeX,sizeY));	
	}
	/**
	 * Creates the points for a new box.
	 * @param sizeX - the horizontal size of the box.
	 * @param sizeY - the vertical size of the box.
	 * @return the array of points of this box.
	 */
	private static Vector [] createPoints(int sizeX,int sizeY) {
		Vector [] POINTS = new Vector[4];
		POINTS[0] = new Vector(0,0);
		POINTS[1] = new Vector(sizeX,0);
		POINTS[2] = new Vector(sizeX,sizeY);
		POINTS[3] = new Vector(0,sizeY);
		return POINTS;
	}
}
