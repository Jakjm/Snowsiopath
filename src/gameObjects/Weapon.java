package gameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import geometry.CollisionData;
import geometry.Shape;
import geometry.Vector;

public abstract class Weapon extends ReversibleObject {
    public final int FIRE_DELAY;
    public int fireTimer = 0;
    public static Font interfaceFont = new Font(Font.SERIF,Font.PLAIN,12);
    public static final int INTERFACE_WIDTH = 100;
    public static final int INTERFACE_HEIGHT = 60;
    public static final int AMMO_BAR_WIDTH = INTERFACE_WIDTH - 10;
    private boolean enabled = false;
    private Vector relativeLocation;
	public Weapon(Vector location, Vector center, Shape hitShape,int FIRE_DELAY) {
		super(location, center, hitShape);
		this.FIRE_DELAY = FIRE_DELAY;
		relativeLocation = new Vector(location);
	}
	public abstract void fire(Vector velocity);
	public void shoot(Vector velocity) {
		if(!enabled || fireTimer < FIRE_DELAY)return;
		else {
			fire(velocity);
			fireTimer = 0;
		}
	}
	public void setEnabled(boolean e) {
		this.enabled = e;
	}
	/**
	 * Updates the weapon with:
	 * @param map - the map of the game
	 * @param location - the expected location of the weapon/hand based on the wielder at this moment.
	 * @param facingRight - whether the weapon should be facing right or not.
	 */
    public void update(Map map,Vector location,boolean facingRight) {
    	super.update();
    	this.facingRight = facingRight;
    	this.relativeLocation = location;
    	this.location = location;
    	fireTimer++;
    	/*
    	 * Checks for a collision with any walls.
    	 */
    	this.updateShape();
    	for(Wall wall : map.wallList) {
    		CollisionData data = this.collisionWith(wall);
    		if(data == null) {
    			continue;
    		}
    		else {
    			//Updating the location and velocity of the player using the overlap and normal force.
    			this.location = this.location.minus(data.axis.scalarMultiply(data.overlap));
    			Vector normalForce = data.NormalForce(this.velocity);
    			this.velocity = this.velocity.minus(normalForce);
    		}
    	}
    	this.updateShape();
    }
    public void interfaceDraw(Graphics2D g) {
    	g.setColor(Color.black);
    	g.setFont(interfaceFont);
    	g.drawRect(5,5,100,60);
    	g.drawString(this.toString(),7,20);
    }
}
