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
    public static final Vector fireRight = new Vector(sprite.getWidth(),- Rock.BASE_RADIUS - sprite.getHeight());
    public static final Vector fireLeft = new Vector(-1.6 * Rock.BASE_RADIUS,-Rock.BASE_RADIUS-sprite.getHeight());
    public static final Color manaColor = new Color(0x6666ff);
	public Wand(Vector location) {
		super(location, sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()),32,20,120);
	}
	public boolean update(Map map,Vector location,boolean facingRight) {
		boolean touchingWall = super.update(map,location,facingRight);
		if(Math.abs(angle) >= 0.0005) {
			angle -= Math.signum(angle)*(Math.PI / 32);
		}
		else angle = 0;
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
			createRockRight(map,velocity,-4 / 32f * Math.PI);
			createRockRight(map,velocity,-2 / 32f * Math.PI);
			createRockRight(map,velocity,0);
			this.angle =- Math.PI / 4;
		}
		else {
			createRockLeft(map,velocity,36/32f * Math.PI);
			createRockLeft(map,velocity,Math.PI);
			createRockLeft(map,velocity,34/32f*Math.PI);
			this.angle = Math.PI / 4;
		}
	}
	public void createRockRight(Map map, Vector velocity, double angle) {
		Rock rock;
		rock = new Rock(this.location.add(fireRight),velocity,angle);
		map.projectileQueue.add(rock);
	}
	public void createRockLeft(Map map, Vector velocity, double angle) {
		Rock rock;
		rock = new Rock(this.location.add(fireLeft),velocity,angle);
		map.projectileQueue.add(rock);
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
