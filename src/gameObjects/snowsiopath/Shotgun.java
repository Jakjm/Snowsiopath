package gameObjects.snowsiopath;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;

import geometry.Box;
import geometry.Vector;
import java.util.LinkedList;
import java.util.ListIterator;

import game.MusicPlayer;
import game.MusicPlayer.BufferedSFX;
import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.Weapon;

public class Shotgun extends ProjectileLauncher<Bullet>{
	public static GameSprite sprite = new GameSprite("/sprites/PumplessShotgun.png");
	
	public static final GameSprite pumpSprite = new GameSprite("/sprites/pump.png");
	public static final Vector FIRE_LOCATION = new Vector(sprite.getWidth(),-7);
	public static final Vector FIRE_LEFT = new Vector (-6,-7);
	public boolean spinning = false;
	public int spinCounter = 0;
	public static final int MAX_AMMO = 6;
	public static final int FIRE_DELAY = 24;
	public static final int RELOAD_TIME = 40;
	public static BufferedSFX fireSound;
	public static BufferedSFX pumpSound;
	private int offHandLocation;
	private int offHandVelocity;
	public static void loadGun() {
		fireSound = new BufferedSFX("gunSound.wav",8);
		pumpSound = new BufferedSFX("reload.wav",3);
	}
	public Shotgun(Vector location) {
		super(location,sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()+5),FIRE_DELAY,MAX_AMMO,RELOAD_TIME);
	}
	public boolean reload() {
		boolean sucessfulReload = super.reload();
		return sucessfulReload;
	}
	@Override
	public Projectile createProjectile(Vector location) {
		// TODO Auto-generated method stub
		return null;
	}
	public void fire(Vector velocity, Map map) {
		if(!fireBullet()) {
			return;
		}
		fireBullet(map,facingRight,velocity,0);
		fireBullet(map,facingRight,velocity,0.1);
		fireBullet(map,facingRight,velocity,0.2);
		fireBullet(map,facingRight,velocity,-0.1);
		fireBullet(map,facingRight,velocity, -0.2);
		fireSound.play();
		spinGun();
	}
	public void fireBullet(Map map,boolean facingRight,Vector velocity,double angleAdjustment) {
		Bullet newBullet;
		if(facingRight) {
			newBullet = new Bullet(this.location.add(FIRE_LOCATION),velocity,0 + angleAdjustment);
		}
		else {
			newBullet = new Bullet(this.location.add(FIRE_LEFT),velocity,Math.PI + angleAdjustment);
		}
		map.projectileQueue.add(newBullet);
	}
	public void spinGun() {
		spinning = false;
		spinCounter++;
		//If the gun should spin
		if(spinCounter == 4) {
			spinning = true;
			spinCounter = 0;
			//Start spinning by adding/subtracting from the angle. 
			if(facingRight)this.angle += 15*Math.PI/8;
			else this.angle -= 15*Math.PI / 8;
		}
		//If the gun should tilt backwards...
		else {
			
			if(facingRight)this.angle -= Math.PI / 4;
			else this.angle += Math.PI / 4;
		}
	}
	public boolean update(Map map,Vector location,boolean facingRight) {
		location = holdLocation(sprite,Hand.sprite,location);
		
		//Code for tilting the gun. 
		if(spinning) {
			//If the gun has finished spinning
			if(Math.abs(this.angle) < 0.0005){
				this.angle = 0;
			}
			else {
				
				this.angle -= Math.signum(angle)*Math.PI/8;
			}
		}
		else {
			//If the gun has finished tilting
			if(Math.abs(this.angle) < 0.0005) {
				this.angle = 0;
			}
			else {
				this.angle -= Math.signum(angle)*Math.PI/64;
			}
		}
		if(fireTimer > 0.8 * FIRE_DELAY && fireTimer <= FIRE_DELAY && ammo > 0) {
			offHandVelocity = 1;
		}
		else if(fireTimer > 0.6 * FIRE_DELAY && fireTimer <= FIRE_DELAY && ammo > 0){
			if(offHandVelocity == 0) {
				pumpSound.play();
			}
			offHandVelocity = -1;
		}
		else {
			offHandVelocity = 0;
			offHandLocation = 0;
		}
		offHandLocation += offHandVelocity;
		boolean touchingWall = super.update(map,location,facingRight);
		return touchingWall; 
	}

	public void interfaceDraw(Graphics2D g) {
		super.interfaceDraw(g);
		sprite.draw(g,10,25);
		pumpSprite.draw(g,30,28);
		super.drawAmmoBar(g,"Bullets: ",Color.green);
	}
	@Override
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
		Hand.sprite.draw(g,0,2);
		pumpSprite.draw(g,20+offHandLocation,4);
		if(this.enabled) {
			Hand.sprite.draw(g,15+offHandLocation,4);
		}
	}
	public String toString() {
		return "Gun";
	}
	@Override
	public void drawLeft(Graphics2D g) {
		sprite.drawReverse(g);
		Hand.sprite.draw(g,sprite.getWidth() - Hand.sprite.getWidth(),2);
		pumpSprite.draw(g,10-offHandLocation,4);
		if(this.enabled) {
			Hand.sprite.draw(g,5-offHandLocation,4);
		}
	}

	
}
