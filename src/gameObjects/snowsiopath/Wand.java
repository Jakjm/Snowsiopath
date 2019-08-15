package gameObjects.snowsiopath;

import java.awt.Color;

import java.awt.Graphics2D;

import gameObjects.GameSprite;
import gameObjects.Map;
import geometry.Box;
import geometry.Vector;

public class Wand extends ProjectileLauncher<Rock>{
    public static GameSprite sprite = new GameSprite("/sprites/Wand.png");
    public static final double LIGHTNING_LENGTH = 30;
    public static final Vector fireRight = new Vector(sprite.getWidth(),- Rock.BASE_RADIUS);
    public static final Vector fireLeft = new Vector(-2 * Rock.BASE_RADIUS,-Rock.BASE_RADIUS);
    public static final Color manaColor = new Color(0x6666ff);
	public Wand(Vector location) {
		super(location, sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()),20,5,120);
	}
	public boolean update(Map map,Vector location,boolean facingRight) {
		boolean touchingWall = super.update(map,location,facingRight);
		if(Math.abs(angle) >= 0.0005) {
			angle -= Math.signum(angle)*(Math.PI / 32);
		}
		else angle = 0;
		if(!touchingWall) {
			this.location.setTo(this.relativeLocation);
			this.updateShape();
		}
		return touchingWall; 
	}
	public void interfaceDraw(Graphics2D g) {
		super.interfaceDraw(g);
		sprite.draw(g,10,25);
		super.drawAmmoBar(g,"Mana:",manaColor);
	}
	public String toString() {
		return "Wand";
	}
	@Override
	public void fire(Vector velocity, Map map) {
		if(!super.fireBullet()) {
			return;
		}
		Rock rock;
		if(facingRight) {
			rock = new Rock(this.location.add(fireRight),velocity,30/16f * Math.PI);	
			this.angle -= Math.PI / 4;
		}
		else {
			rock = new Rock(this.location.add(fireLeft),velocity,18/16f * Math.PI);
			this.angle += Math.PI / 4;
		}
		map.bulletList.add(rock);
	}
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
		Hand.sprite.draw(g,-6,-5);
	}
    public void drawLeft(Graphics2D g) {
    	sprite.drawReverse(g);
    	Hand.sprite.draw(g,18,-5);
    }
	@Override
	public Projectile createProjectile(Vector location) {
		// TODO Auto-generated method stub
		return null;
	}
}
