package gameObjects.snowsiopath;

import java.awt.Graphics;
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
    public static final Vector SHOULDER_RIGHT = new Vector(40,40);
    public static final Vector SHOULDER_LEFT = new Vector(28,40);
    public static final double ARM_LENGTH = 30;
    public double weaponAngle = 0;
    
    public double flipStart;
    public double flipRange;
    public boolean flipping;
    
    /**The location of the weapon hand. **/
    public Vector handLocation;
    public Weapon weapon;
    public ArrayList<Weapon> weaponList;
    public int weaponIndex = 0;
	public Player(Vector location) {
		super(location,sprite.getCenter(),playerShape());
		weaponList = new ArrayList<Weapon>();
		weaponList.add(new Hand(location.add(SHOULDER_RIGHT)));
		weaponList.add(new Shotgun(location));
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
	public void getKeys(KeyHandler keys, Map map) {
		if(keys.rightKey || keys.dKey) {
			facingRight = true;
			velocity.x += MOVE_ACCEL;
			if(velocity.x > MAX_SPEED)velocity.x = MAX_SPEED;
		}
		else if(keys.leftKey || keys.aKey) {
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
		else if(keys.xKey) {
			half();
		}
		else if(keys.qKey) {
			backflip();
		}
		else if(keys.zKey) {
			halfBack();
		}
		if(keys.rKey) {
			if(weapon != null && weapon instanceof ProjectileLauncher) {
				((ProjectileLauncher<?>)weapon).reload();
			}
		}
		if(keys.fKey) {
			weapon.shoot(this.velocity,map);
		}
	}
	public void jump() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH;
		
		flipping = true;
	}
	public void halfBack() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH;
		angularVelocity = -0.13;
		
		
		
		flipping = true;
		flipRange = Math.PI;
		flipStart = this.angle;
	}
	public void half() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH;
		angularVelocity = 0.13;
		
		
		
		flipping = true;
		flipRange = Math.PI;
		flipStart = this.angle;
	}
	public void flip() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH*1.25;
		angularVelocity = 0.2;
		
		flipping = true;
		flipRange = 2 * Math.PI;
		flipStart = this.angle;
	}
	public void backflip() {
		if(!onGround) {
			return;
		}
		velocity.y -= JUMP_STRENGTH*1.25;
		angularVelocity = -0.2;
		
		flipping = true;
		flipRange = 2 * Math.PI;
		flipStart = this.angle;
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
    			this.subtractOverlap(data);
    			
    			//If not already on ground, check if data suggests that we're on the ground.
    			if(!this.onGround) {
    				//On ground is when the vertical normal force is greater than the horizontal.
    				this.onGround = applyNormalForce2(data);
    			}
    			super.updateShape();
    		}
    	}
	}
	public void updateWeapon(Map map) {
		//The hand starts back to allow maximum punching distance.
		if(this.weapon instanceof Hand) {
			this.weapon.setEnabled(true);
			if(facingRight) {
				this.weapon.update(map,location.add(SHOULDER_RIGHT),facingRight);
			}
			else {
				this.weapon.update(map,location.add(SHOULDER_LEFT),facingRight);
			}
		}
		
		//Otherwise, for other weapons...
		else {
			handLocation = Vector.angleVector(weaponAngle,ARM_LENGTH);
			handLocation.plusEquals(location);
			if(facingRight) {
    			if(Math.abs(weaponAngle) < 0.005) {
    				this.weaponAngle = 0;
    				this.weapon.setEnabled(true);
    			}
    			else {
    				this.weaponAngle -= Math.PI / 24;
    			}
    			handLocation.plusEquals(SHOULDER_RIGHT);
			}
    		else {
    			if(Math.abs(weaponAngle - Math.PI) < 0.005) {
					this.weaponAngle = Math.PI;
					this.weapon.setEnabled(true);
				}
				else {
					this.weaponAngle += Math.PI / 24;
				}
				handLocation.plusEquals(SHOULDER_LEFT);
    		}
    		this.weapon.update(map,handLocation,facingRight);
		}
	}
    public void update(Map map) {
    	if(!this.onGround)this.velocity.y += Map.GRAVITY;
    	super.update();
    	super.updateShape();
    	updateWalls(map);
    	updateWeapon(map);
    	
    	
    	if(onGround) {
	    	if(angle > flipStart + flipRange) {
	    		angle = flipStart + flipRange;
	    		angularVelocity = 0;
	    	}
	    	else if(angle < flipStart - flipRange) {
	    		angle = flipStart - flipRange;
	    		angularVelocity = 0;
	    	}
    	}
    }
    public void draw(Graphics2D g,Vector offset) {
    	super.draw(g,offset);
	    weapon.draw(g,offset);
	    weapon.interfaceDraw(g);
    }
    public void cycleWeapon(Map map) {
    	weapon.setEnabled(false);
    	weaponIndex++;
    	if(weaponIndex == weaponList.size()) {
    		weaponIndex = 0;
    	}
    	weapon = weaponList.get(weaponIndex);
    	if(!(weapon instanceof Hand)) {
    		weaponAngle = Math.PI / 2;
    	}
    	this.updateWeapon(map);
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
