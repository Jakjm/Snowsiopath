package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.ListIterator;

import gameObjects.GameObject;
import gameObjects.Map;
import gameObjects.MovingObject;
import geometry.Vector;

public class FlameCluster{
	public ArrayList<Flame>flames;
	public double angle;
	public Vector location;
	public double SPREAD_RADIUS = 10;
	public double SPREAD_SPAN = Math.PI / 3;
	public FlameCluster(Vector location) {
		this.location = location;
		this.angle = -Math.PI / 2;
		flames = new ArrayList<Flame>();
	}
	public FlameCluster(Vector location,double angle) {
		this(location);
		this.angle += angle;
	}
	public void addFlames(Vector velocity) {
		//angle should start from this.angle 
		double flameAngle = this.angle + (Math.random() - (1.0/2)) * SPREAD_SPAN + velocity.angle();
		Vector rotationVector = Vector.angleVector(flameAngle,SPREAD_RADIUS);
		Vector flameLocation  = this.location.add(rotationVector);
		Flame newFlame = new Flame(flameLocation,flameAngle);
		newFlame.velocity.plusEquals(velocity);
		flames.add(0,newFlame);
		flames.add(newFlame);
	}
	public void update(Vector location,Vector velocity) {
		ListIterator<Flame> flameIter = flames.listIterator();
		this.location = new Vector(location);
		while(flameIter.hasNext()) {
			Flame currentFlame = flameIter.next();
			if(currentFlame.updateFlame()) {
				flameIter.remove();
			}
		}
		addFlames(velocity);
	}
	public void draw(Graphics2D g,Vector offset) {
		for(Flame flame : flames) {
			flame.draw(g,offset);
		}
	}
	public class Flame extends MovingObject{
		public int burnTimer = 0;
		public Color color;
		public static final int BURN_TIME = 15;
		public static final int YELLOW = 5;
		public static final int ORANGE = 10;
		public static final int FLAME_WIDTH = 6;
		public static final int FLAME_HEIGHT = 2;
		public static final double FLAME_SPEED = 0.5;
		public Flame(Vector location,double angle) {
			super(location,new Vector(FLAME_WIDTH/2,FLAME_HEIGHT/2));
			color = Color.yellow;
			this.angle = angle;
			this.velocity = new Vector(Math.cos(angle),Math.sin(angle)).scalarMultiply(FLAME_SPEED);
		}
		public boolean updateFlame() {
			super.update();
			burnTimer++;
			if(burnTimer > BURN_TIME) {
				return true;
			}
			else if(burnTimer > ORANGE) {
				color = Color.red;
			}
			else if(burnTimer > YELLOW){
				color = Color.orange;
			}
			return false;
		}
		public void drawObject(Graphics2D g) {
			g.setColor(color);
			g.fillOval(0,0,FLAME_WIDTH,FLAME_HEIGHT);
		}
	}
}
