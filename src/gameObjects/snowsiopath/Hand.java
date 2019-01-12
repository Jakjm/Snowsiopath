package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.Weapon;
import geometry.Circle;
import geometry.Vector;

public class Hand extends Weapon{
	public static GameSprite sprite = new GameSprite("/sprites/Hand.png");
	public static final double PUNCH_SPEED = 7;
	public static final int PUNCH_FRAMES = 8;
	public int punchFrame;
	public static final int IDLE = 0;
	public static final int PUNCHING = 1;
	public static final int RETURNING = 2;
	public FlameCluster flames;
	private int state;
	public Hand(Vector location) {
		super(location,sprite.getCenter(),new Circle(sprite.getWidth(),8),20);
		state = IDLE;
		flames = new FlameCluster(this.location.add(this.center));
	}
	@Override
	public void fire(Vector velocity) {
	    if(state != IDLE) {
	    	return;
	    }
	    else {
	    	state = PUNCHING;
	    	if(facingRight) {
	    		this.velocity.x = PUNCH_SPEED;
	    	}
	    	else {
	    		this.velocity.x =-PUNCH_SPEED;
	    	}
	    }
	}
	public void resetPunch() {
		this.velocity.x = 0;
		state = IDLE;
	}
	public void updatePunch() {
		if(state != IDLE) {
			punchFrame++;
			if(punchFrame >= PUNCH_FRAMES) {
				punchFrame = 0;
				if(state == PUNCHING) {
					state = RETURNING;
					this.velocity.x =- this.velocity.x;
				}
				else {
					resetPunch();
				}
			}
		}
	}
	public void update(Map map,Vector location,boolean facingRight) {
		super.update(map,location,facingRight);
		flames.update(location.add(center),Vector.ZERO());
		updatePunch();
	}
	public void interfaceDraw(Graphics2D g) {
		super.interfaceDraw(g);
		sprite.draw(g,10,25);
	}
	public String toString() {
		return "Hand";
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g, offset);
		flames.draw(g, offset);
	}
	@Override
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
	}
	@Override
	public void drawLeft(Graphics2D g) {
		drawRight(g);
	}
}
