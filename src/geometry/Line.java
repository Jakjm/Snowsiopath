package geometry;

import java.util.ArrayList;

public class Line extends Portion{
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	private int length,orientation;
	private Vector offset;
	public Line(int length,int orientation,Vector offset) {
		super();
		if(orientation < 0 || orientation > 3) throw new IllegalArgumentException();
		if(length < 0)throw new IllegalArgumentException();
		this.length = length;
		this.offset = offset;
		this.orientation = orientation;
	}
	private static ArrayList<Vector> generatePoints(int length,int orientation,final Vector offset){
		ArrayList<Vector> pointsList = new ArrayList<Vector>();
		Vector origin = new Vector(0,0);
		origin.plusEquals(offset);
		if(orientation == UP) {
			Vector bottom = new Vector(0,length);
			bottom.plusEquals(offset);
			
			pointsList.add(bottom);
			pointsList.add(origin);
		}
		else if(orientation == DOWN) {
			Vector bottom = new Vector(0,length);
			bottom.plusEquals(offset);
			
			pointsList.add(origin);
			pointsList.add(bottom);
		}
		else if(orientation == RIGHT) {
			Vector right = new Vector(length,0);
			right.plusEquals(offset);
			
			pointsList.add(origin);
			pointsList.add(right);
		}
		else {
			Vector right = new Vector(length,0);
			right.plusEquals(offset);
			
			pointsList.add(right);
			pointsList.add(origin);
		}
		return pointsList;
	}
	@Override
	public ArrayList<Vector> generatePoints() {
		return generatePoints(length,orientation,offset);
	}
}
