package geometry;

import java.util.ArrayList;

public class ComboShape extends Shape{
	public ComboShape(Portion [] pairs,Vector center) {
		super(combinePortions(pairs),center);
	}
	public ComboShape(Portion [] pairs) {
		super(combinePortions(pairs));
	}
	public static Vector [] combinePortions(Portion [] pairs) {
		ArrayList <Vector> pointsList = new ArrayList<Vector>();
		for(Portion p : pairs) {
			pointsList.addAll(p.generatePoints());
		}
		Vector [] list = new Vector[pointsList.size()];
		list = pointsList.toArray(list);
		return list;
	}
}
