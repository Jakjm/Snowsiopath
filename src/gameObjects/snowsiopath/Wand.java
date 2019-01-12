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
    public static final Vector fireRight = new Vector(sprite.getWidth(),-25);
    public static final Vector fireLeft = new Vector(-sprite.getWidth()-25,-25);
    public static final Color manaColor = new Color(0x6666ff);
	public Wand(Vector location) {
		super(location, sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()),20,5,120);
	}
	public void update(Map map,Vector location,boolean facingRight) {
		super.update(map,location,facingRight);
		if(Math.abs(angle) >= 0.0005) {
			angle -= Math.signum(angle)*(Math.PI / 32);
		}
		else angle = 0;
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
	public void fire(Vector velocity) {
		if(!super.fireBullet()) {
			return;
		}
		if(!facingRight) {
			this.bulletList.add(new Rock(this.location.add(fireLeft),velocity,Math.PI));
			this.angle += Math.PI / 4;
		}
		else {
			this.bulletList.add(new Rock(this.location.add(fireRight),velocity,0));
			this.angle -= Math.PI / 4;
		}
	}
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
		Hand.sprite.draw(g,-6,-5);
	}
    public void drawLeft(Graphics2D g) {
    	sprite.drawReverse(g);
    	Hand.sprite.draw(g,18,-5);
    }
}
