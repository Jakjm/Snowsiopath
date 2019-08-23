package gameObjects.snowsiopath;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Stroke;

import gameObjects.GameSprite;
import gameObjects.Map;
import geometry.Box;
import geometry.Vector;

public class Wand extends ProjectileLauncher<Rock>{
    public static GameSprite sprite = new GameSprite("/sprites/Wand.png");
    public static final double LIGHTNING_LENGTH = 30;
    public static final Vector fireRight = new Vector(sprite.getWidth(),- Rock.BASE_RADIUS - sprite.getHeight());
    public static final Vector fireLeft = new Vector(-sprite.getWidth() + Hand.sprite.getWidth() - 2 * Rock.BASE_RADIUS,-Rock.BASE_RADIUS-sprite.getHeight());
    public static final Color manaColor = new Color(0x6666ff);
    public int circleSize = 0;
	public Wand(Vector location) {
		super(location, sprite.getCenter(),new Box(sprite.getWidth(),sprite.getHeight()),32,20,120);
	}
	
	public boolean update(Map map,Vector location,boolean facingRight) {
		location = holdLocation(sprite,Hand.sprite,location);
		
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
		for(int i = 3;i <= 3;i++) {
			createRock(map,velocity,i/32.0 * Math.PI);
		}
		
		if(this.facingRight) this.angle = -Math.PI / 4;
		else this.angle = Math.PI / 4;
	}
	public void createRock(Map map,Vector velocity, double angleVariation) {
		Rock rock;
		if(facingRight) {
			rock = new Rock(this.location.add(fireRight),velocity,-angleVariation);
		}
		else {
			rock = new Rock(this.location.add(fireLeft),velocity,Math.PI  + angleVariation);
		}
		map.projectileQueue.add(rock);
	}
	public void drawRight(Graphics2D g) {
		sprite.draw(g);
		Hand.sprite.draw(g,-6,-5);
	}
	public void drawSpellcast(Graphics2D g) {
		Stroke previousStroke = g.getStroke();
		g.setColor(Color.blue);
		g.setStroke(new BasicStroke(4));
		g.drawOval(sprite.getWidth(),-2,10,10);
		g.setStroke(previousStroke);
	}
    public void drawLeft(Graphics2D g) {
    	sprite.drawReverse(g);
    	Hand.sprite.draw(g,18,-5);
    }
}
