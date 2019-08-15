package gameObjects.snowsiopath;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;

import geometry.Box;
import geometry.Vector;
import java.util.LinkedList;
import java.util.ListIterator;

import game.MusicPlayer;
import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.Weapon;

public class Gun extends ProjectileLauncher<Bullet>{
	public static GameSprite sprite = new GameSprite("/sprites/Shotgun.png");
	public static final Vector FIRE_LOCATION = new Vector(14,-7);
	public static final Vector FIRE_LEFT = new Vector (-6,-7);
	public boolean spinning = false;
	public int spinCounter = 0;
	public static final int MAX_AMMO = 16;
	public static final int RELOAD_TIME = 60;
	
	public MusicPlayer [] gunSounds;
	public MusicPlayer reloadSound;
	public Gun(Vector location) {
		super(location,sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()+5),24,MAX_AMMO,RELOAD_TIME);
		initSounds();
	}
	public void initSounds() {
		gunSounds = new MusicPlayer[4];
		for(int i = 0; i < gunSounds.length;i++) {
			gunSounds[i] = new MusicPlayer("gunSound.wav");
		}
		reloadSound = new MusicPlayer("reload.wav");
	}
	public void playGunfire() {
		for(int i = 0; i < gunSounds.length;i++) {
			if(!gunSounds[i].isPlaying()) {
				gunSounds[i].play();
				break;
			}
		}
	}
	public boolean reload() {
		boolean sucessfulReload = super.reload();
		if(sucessfulReload)reloadSound.play();
		return sucessfulReload;
	}
	public void fire(Vector velocity, Map map) {
		if(!fireBullet()) {
			return;
		}
		double firingAngle;
		Bullet b;
		if(!facingRight) {
			firingAngle = Math.PI;
			b = new Bullet(this.location.add(FIRE_LEFT),velocity,firingAngle);
		}
		else {
			firingAngle = 0;
			b = new Bullet(this.location.add(FIRE_LOCATION),velocity,firingAngle);
		}
		map.bulletList.add(b);
		playGunfire();
		spinGun();
	}
	public void spinGun() {
		spinning = false;
		spinCounter++;
		if(spinCounter == 4) {
			spinning = true;
			spinCounter = 0;
		}
		if(spinning) {
			//Start spinning by adding/subtracting from the angle. 
			if(facingRight)this.angle += 15*Math.PI/8;
			else this.angle -= 15*Math.PI / 8;
		}
		else {
			//Start tilting backwards. 
			if(facingRight)this.angle -= Math.PI / 4;
			else this.angle += Math.PI / 4;
		}
	}
	public boolean update(Map map,Vector location,boolean facingRight) {
		boolean touchingWall = super.update(map,location,facingRight);
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
				this.angle -= Math.signum(angle)*Math.PI/32;
			}
		}
		
		if(!touchingWall) {
			this.location.setTo(this.relativeLocation);
			this.updateShape();
		}
		return touchingWall; 
	}

	public void interfaceDraw(Graphics2D g) {
		super.interfaceDraw(g);
		sprite.draw(g,10,25);
		super.drawAmmoBar(g,"Bullets:",Color.green);
	}
	@Override
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
		Hand.sprite.draw(g,-4,2);
	}
	public String toString() {
		return "Gun";
	}
	@Override
	public void drawLeft(Graphics2D g) {
		sprite.drawReverse(g);
		Hand.sprite.draw(g,10,2);
	}

	@Override
	public Projectile createProjectile(Vector location) {
		// TODO Auto-generated method stub
		return null;
	}
}
