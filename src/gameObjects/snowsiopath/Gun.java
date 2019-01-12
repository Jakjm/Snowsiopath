package gameObjects.snowsiopath;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;

import geometry.Box;
import geometry.Vector;
import java.util.LinkedList;
import java.util.ListIterator;

import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.Weapon;

public class Gun extends ProjectileLauncher<Bullet>{
	public static GameSprite sprite = new GameSprite("/sprites/Shotgun.png");
	public static final Vector FIRE_LOCATION = new Vector(14,-5);
	public static final Vector FIRE_LEFT = new Vector (-6,-5);
	public boolean spinning = false;
	public int spinCounter = 0;
	public static final int MAX_AMMO = 16;
	public static final int RELOAD_TIME = 60;
	public Gun(Vector location) {
		super(location,sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()+5),24,MAX_AMMO,RELOAD_TIME);
		bulletList = new LinkedList<Bullet>();
	}
	
	@Override
	public void fire(Vector velocity) {
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
		bulletList.add(b);
		
		spinning = false;
		spinCounter++;
		if(spinCounter == 4) {
			spinning = true;
			spinCounter = 0;
		}
		if(spinning) {
			if(facingRight)this.angle += 15*Math.PI/8;
			else this.angle -= 15*Math.PI / 8;
		}
		else {
			if(facingRight)this.angle -= Math.PI / 4;
			else this.angle += Math.PI / 4;
		}
	}
	public void update(Map map,Vector location,boolean facingRight) {
		super.update(map,location,facingRight);
		if(!spinning) {
			if(!(Math.abs(this.angle) < 0.0005)) {
				this.angle -= Math.signum(angle)*Math.PI/32;
			}
			else {
				this.angle = 0;
			}
		}
		else {
			if(!(Math.abs(this.angle) < 0.0005)) {
				this.angle -= Math.signum(angle)*Math.PI/8;
			}
			else {
				this.angle = 0;
			}
		}
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
}
