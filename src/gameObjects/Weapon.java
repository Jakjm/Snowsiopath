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
    protected boolean enabled = false;
    private boolean againstWall = false;
    
    /**The position of the Weapon relative to the hand**/
    public Vector relativeHandPosition;
    
    /**The location of the weapon assuming it isn't touching a wall**/
    public Vector regularLocation;
	public Weapon(Vector location, Vector center, Shape hitShape,int FIRE_DELAY) {
		super(location, center, hitShape);
		this.FIRE_DELAY = FIRE_DELAY;
		regularLocation = new Vector(location);
	}
	public abstract void fire(Vector velocity, Map map);
	public void shoot(Vector velocity, Map map) {
		if(!enabled || fireTimer < FIRE_DELAY || againstWall)return;
		else {
			fire(velocity,map);
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
    public boolean update(Map map,Vector location,boolean facingRight) {
    	super.update();
    	this.facingRight = facingRight;
    	this.location.plusEquals(location.minus(this.regularLocation));
    	this.regularLocation = location;
    	fireTimer++;
    	/*
    	 * Checks for a collision with any walls.
    	 */
    	this.updateShape();
    	againstWall = false;
    	for(Wall wall : map.wallList) {
    		CollisionData data = this.collisionWith(wall);
    		if(data == null) {
    			continue;
    		}
    		else {
    			//Updating the location and velocity of the player using the overlap and normal force.
    			this.location = this.location.minus(data.axis.scalarMultiply(data.overlap));
    			Vector normalForce = data.normalForce(this.velocity);
    			this.velocity.x = this.velocity.x - normalForce.x;
    			againstWall = true;
    		}
    	}
    	this.updateShape();
    	return againstWall; 
    }
    public void interfaceDraw(Graphics2D g) {
    	g.setColor(Color.black);
    	g.setFont(interfaceFont);
    	g.setColor(Color.lightGray);
    	g.fillRect(5,5,100,60);
    	g.setColor(Color.black);
    	g.drawRect(5,5,100,60);
    	g.drawString(this.toString(),10,20);
    }
}
