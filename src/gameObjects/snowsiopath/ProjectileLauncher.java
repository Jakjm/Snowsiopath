package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.ListIterator;

import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.Weapon;
import geometry.Shape;
import geometry.Vector;

public abstract class ProjectileLauncher <P extends Projectile> extends Weapon{
	private final int RELOAD_TIME;
	private final int MAX_AMMO;
	public int ammo;
	public ProjectileLauncher(Vector location, Vector center, Shape hitShape, int FIRE_DELAY,int MAX_AMMO,int RELOAD_TIME) {
		super(location, center, hitShape, FIRE_DELAY);
		this.MAX_AMMO = MAX_AMMO;
		ammo = MAX_AMMO;
		this.RELOAD_TIME = RELOAD_TIME;
	}
	public Vector holdLocation(GameSprite sprite,GameSprite handSprite,Vector handLocation) {
		if(!facingRight) {
			return handLocation.add(new Vector(handSprite.getWidth() - sprite.getWidth(),0));
		}
		else {
			return handLocation;
		}
	}
	public boolean reload() {
		if(this.ammo == MAX_AMMO || this.fireTimer < 0)return false;
		this.fireTimer = -RELOAD_TIME;
		this.ammo = MAX_AMMO;
		return true;
	}
	public void drawAmmoBar(Graphics g,String extraText,Color color) {
    	g.setColor(Color.red);
		g.fillRect(10,40,AMMO_BAR_WIDTH,12);
		g.setColor(color);
		g.fillRect(10,40,(int)(AMMO_BAR_WIDTH *((double)ammo / MAX_AMMO)),12);
		g.setColor(Color.black);
		g.drawString(String.format("%s %d/%d",extraText,ammo,MAX_AMMO),12,50);
    }
	public boolean update(Map map,Vector location,boolean facingRight) {
		boolean touchingWall = super.update(map,location,facingRight);
		if(!touchingWall) {
			this.location.setTo(this.regularLocation);
			this.updateShape();
		}
		return touchingWall;
	}
	public boolean fireBullet() {
		if(ammo <= 0) {
			return false;
		}
		ammo--;
		return true;
	}
}
