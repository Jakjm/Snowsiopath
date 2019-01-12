package gameObjects;
import java.awt.Graphics2D;

import geometry.Shape;
import geometry.Vector;

public abstract class ReversibleObject extends SolidObject{
	public boolean facingRight = true;
	public ReversibleObject(Vector location,Vector center,Shape hitShape) {
		super(location,center,hitShape);
	}
	public void drawObject(Graphics2D g) {
		if(facingRight) {
			drawRight(g);
		}
		else {
			drawLeft(g);
		}
	}
	public abstract void drawRight(Graphics2D g);
	public abstract void drawLeft(Graphics2D g);
}
