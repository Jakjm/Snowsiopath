package geometry;

import java.util.ArrayList;

public class HalfCircle extends Portion{
	public static final int RIGHT_DOWN = 0;
	public static final int RIGHT_UP = 1;
	public static final int LEFT_DOWN = 2;
	public static final int LEFT_UP = 3;
	private final int orientation, points;
	private int diameter;
	private Vector offset;
	public HalfCircle(int diameter,int orientation,int points,Vector offset) {
		if(diameter < 0)throw new IllegalArgumentException();
		if(orientation < 0 || orientation > 3)throw new IllegalArgumentException();
		if(points <= 2)throw new IllegalArgumentException();
		this.diameter = diameter;
		this.points = points;
		this.orientation = orientation;
		this.offset = offset;
	}
	@Override
	public ArrayList<Vector> generatePoints() {
		ArrayList<Vector> list = new ArrayList<Vector>();
		double angleInterval = Math.PI / (points-1);
		double radius = diameter/2.0;
		
		if(orientation == RIGHT_DOWN) {
			for(int i = 0;i < points;i++) {
				double angle = (Math.PI / 2) - (i * angleInterval);
				Vector newPoint = new Vector(Math.cos(angle) * radius,-Math.sin(angle) * radius);
				newPoint.plusEquals(offset);
				list.add(newPoint);
			}
		}
		else if(orientation == LEFT_UP) {
			for(int i = 0;i < points;i++) {
				double angle = 3.0/2*Math.PI - (i * angleInterval);
				Vector newPoint = new Vector(Math.cos(angle) * radius,-Math.sin(angle) * radius);
				newPoint.plusEquals(offset);
				newPoint.plusEquals(new Vector(((double)diameter / 2),0));
				list.add(newPoint);
			}
		}
		return list;
	}
}
