package gameObjects.snowsiopath;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.ListIterator;

import gameObjects.Map;
import gameObjects.Weapon;
import geometry.Shape;
import geometry.Vector;

public abstract class ProjectileLauncher <P extends Projectile> extends Weapon{
	public LinkedList<P> bulletList;
	private final int RELOAD_TIME;
	private final int MAX_AMMO;
	public int ammo;
	public ProjectileLauncher(Vector location, Vector center, Shape hitShape, int FIRE_DELAY,int MAX_AMMO,int RELOAD_TIME) {
		super(location, center, hitShape, FIRE_DELAY);
		bulletList = new LinkedList<P>();
		this.MAX_AMMO = MAX_AMMO;
		ammo = MAX_AMMO;
		this.RELOAD_TIME = RELOAD_TIME;
	}
	public boolean reload() {
		if(this.ammo == MAX_AMMO || this.fireTimer < 0)return false;
		this.fireTimer = -RELOAD_TIME;
		this.ammo = MAX_AMMO;
		return true;
	}
	public void draw(Graphics2D g,Vector offset) {
		super.draw(g,offset);
		for(P p : bulletList) {
			p.draw(g,offset);
		}
	}
	public void drawAmmoBar(Graphics g,String extraText,Color color) {
    	g.setColor(Color.red);
		g.fillRect(10,40,AMMO_BAR_WIDTH,12);
		g.setColor(color);
		g.fillRect(10,40,(int)(AMMO_BAR_WIDTH *((double)ammo / MAX_AMMO)),12);
		g.setColor(Color.black);
		g.drawString(String.format("%s %d/%d",extraText,ammo,MAX_AMMO),12,50);
    }
	public boolean fireBullet() {
		if(ammo <= 0) {
			return false;
		}
		ammo--;
		return true;
	}
	public void update(Map map,Vector location,boolean facingRight) {
		super.update(map,location,facingRight);
		ListIterator<P> bulletIterator = bulletList.listIterator();
		while(bulletIterator.hasNext())
		{
			P p = bulletIterator.next();
			if(p.updateProjectile(map)) {
				bulletIterator.remove();
			}
		}
	}
}
