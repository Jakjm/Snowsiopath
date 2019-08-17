package gameObjects.snowsiopath;

import java.awt.Graphics2D;
import gameObjects.GameSprite;
import gameObjects.Map;
import gameObjects.SolidObject;
import geometry.Circle;
import geometry.Vector;

public class Bullet extends Projectile{
	static GameSprite sprite = new GameSprite("/sprites/Bullet.png");
	public static final double BULLET_SPEED = 16.5;
	private static final double RANGE = 700;
	public Bullet(Vector location,Vector velocity,double angle) {
		super(location,sprite.getCenter(),new Circle(16,8),RANGE);
		this.velocity = Vector.angleVector(angle,BULLET_SPEED);
		this.velocity.x += velocity.x;
	}
	@Override
	public void drawObject(Graphics2D g) {
		sprite.draw(g);
	}
}
