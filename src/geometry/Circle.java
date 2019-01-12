package geometry;

public class Circle extends Shape{
	public Circle(int diameter,int pointCount) {
		super(constructPoints(diameter,pointCount));
	}
	public static Vector [] constructPoints(int diameter,int pointCount) {
		Vector [] points = new Vector[pointCount];
		double radius = diameter / 2;
		for(int i = 0;i < points.length;i++) {
			double currentAngle = (2*Math.PI)*((double)i/points.length);
			double x = Math.cos(currentAngle)*radius+radius; //Adds radius to center at (0,0).
			double y = Math.sin(currentAngle)*radius+radius; //Adds radius to center at (0,0).
			points[i] = new Vector(x,y);
		}
		return points;
	}
}
