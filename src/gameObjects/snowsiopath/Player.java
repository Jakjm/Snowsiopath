package gameObjects.snowsiopath;

import java.awt.Graphics2D;
import java.util.ArrayList;
import game.KeyHandler;
import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.ReversibleObject;
import gameObjects.Wall;
import gameObjects.Weapon;
import geometry.CollisionData;
import geometry.ComboShape;
import geometry.HalfCircle;
import geometry.Line;
import geometry.Portion;
import geometry.Shape;
import geometry.Vector;

public class Player extends ReversibleObject{
	public static GameSprite sprite = new GameSprite("/sprites/Snowsiopath.png");
	public static final double MOVE_ACCEL = 0.6;
	public static final double MAX_SPEED = 4.5;
	public static final double JUMP_STRENGTH = 13;
	public boolean onGround = false;
    public static final Vector SHOULDER_RIGHT = new Vector(30,40);
    public static final Vector SHOULDER_LEFT = new Vector(28,40);
    public static final double ARM_LENGTH = 30;
    public double weaponAngle = 0;
    public Vector weaponLocation;
    public Weapon weapon;
    public ArrayList<Weapon> weaponList;
    public int weaponIndex = 0;
	public Player(Vector location) {
		super(location,sprite.getCenter(),playerShape());
		
		weaponList = new ArrayList<Weapon>();
		weaponList.add(new Hand(location.add(SHOULDER_RIGHT)));
		weaponList.add(new Gun(location));
		weaponList.add(new Wand(location));
		weapon = weaponList.get(0);
	}
	public static Shape playerShape() {
		Line faceRight = new Line(30,Line.DOWN,new Vector(60,0));
		Line faceLeft = new Line(30,Line.UP,new Vector(20,0));
		HalfCircle middleRight = new HalfCircle(38,HalfCircle.RIGHT_DOWN,5,new Vector(45,50));
		HalfCircle bottomRight = new HalfCircle(53,HalfCircle.RIGHT_DOWN,6,new Vector(50,95));
		HalfCircle bottomLeft = new HalfCircle(53,HalfCircle.LEFT_UP,6,new Vector(12,95));
		HalfCircle middleLeft = new HalfCircle(38,HalfCircle.LEFT_UP,5,new Vector(22,50));
		Portion [] pieces = new Portion [] {faceRight,middleRight,bottomRight,bottomLeft,middleLeft,faceLeft};
		return new ComboShape(pieces,sprite.getCenter());
	}
	public void getKeys(KeyHandler keys) {
		if(keys.rightKey) {
			facingRight = true;
			velocity.x += MOVE_ACCEL;
			if(velocity.x > MAX_SPEED)velocity.x = MAX_SPEED;
		}
		else if(keys.leftKey) {
			facingRight = false;
			velocity.x -= MOVE_ACCEL;
			if(velocity.x < -MAX_SPEED)velocity.x =- MAX_SPEED;
		}
		if(keys.spaceKey) {
			jump();
		}
		else if(keys.eKey) {
			flip();
		}
		else if(keys.qKey) {
			backflip();
		}
		if(keys.rKey) {
			if(weapon != null && weapon instanceof ProjectileLauncher) {
				((ProjectileLauncher<?>)weapon).reload();
			}
		}
		if(keys.fKey) {
			weapon.shoot(this.velocity);
		}
	}
	public void jump() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH;
	}
	public void flip() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH*1.25;
		angularVelocity = 0.2;
	}
	public void backflip() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH*1.25;
		angularVelocity = -0.2;
	}
	public void updateWalls(Map map) {
		/*
    	 * Checks for a collision with any walls.
    	 */
    	onGround = false;
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
    			//If not already on ground, check if data suggests that we're on the ground.
    			if(!this.onGround) {
    				//On ground is when the vertical normal force is greater than the horizontal.
    				this.onGround = normalForce.y / Math.abs(normalForce.x) > 1;
    			}
    			super.updateShape();
    		}
    	}
	}
	public void updateWeapon(Map map) {
		if(facingRight) {
    		if(this.weapon instanceof Hand) {
    			this.weapon.update(map,location.add(SHOULDER_RIGHT),facingRight);
    		}
    		else {
    			if(Math.abs(weaponAngle) < 0.005) {
    				this.weaponAngle = 0;
    				this.weapon.setEnabled(true);
    			}
    			else {
    				this.weaponAngle -= Math.PI / 24;
    			}
    			weaponLocation = Vector.angleVector(weaponAngle,ARM_LENGTH);
    			weaponLocation.plusEquals(SHOULDER_RIGHT);
    			weaponLocation.plusEquals(location);
    			this.weapon.update(map,weaponLocation,facingRight);
    		}
    	}
    	else {
    		if(this.weapon instanceof Hand) {
    			this.weapon.update(map,location.add(SHOULDER_LEFT),facingRight);
    		}
    		else {
    			if(Math.abs(weaponAngle - Math.PI) < 0.005) {
    				this.weaponAngle = Math.PI;
    				this.weapon.setEnabled(true);
    			}
    			else {
    				this.weaponAngle += Math.PI / 24;
    			}
    			weaponLocation = Vector.angleVector(weaponAngle,ARM_LENGTH);
    			weaponLocation.plusEquals(SHOULDER_LEFT);
    			weaponLocation.plusEquals(location);
    			this.weapon.update(map,weaponLocation,facingRight);
    		}
    	}
	}
    public void update(Map map) {
    	this.velocity.y += Map.GRAVITY;
    	this.angle %= 2*Math.PI;
    	super.update();
    	super.updateShape();
    	updateWalls(map);
    	updateWeapon(map);
    	if(onGround || Math.abs(angle) >= 2*Math.PI) {
    		this.angle = 0;
    		this.angularVelocity = 0;
    	}
    }
    public void draw(Graphics2D g,Vector offset) {
    	super.draw(g,offset);
	    weapon.draw(g,offset);
	    weapon.interfaceDraw(g);
    }
    public void cycleWeapon() {
    	weapon.setEnabled(false);
    	weaponIndex++;
    	if(weaponIndex == weaponList.size()) {
    		weaponIndex = 0;
    	}
    	weapon = weaponList.get(weaponIndex);
    	if(!(weapon instanceof Hand)) {
    		weaponAngle = Math.PI / 2;
    	}
    }
	@Override
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
	}

	@Override
	public void drawLeft(Graphics2D g) {
		sprite.drawReverse(g);
	}
}
