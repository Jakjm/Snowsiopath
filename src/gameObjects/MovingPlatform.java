package gameObjects;

import java.awt.Color;

import geometry.Vector;

public class MovingPlatform extends Wall{
	Vector baseLocation;
	public MovingPlatform(Vector location,int width,int height,Color c) {
		super(location,width,height,c);
		baseLocation = new Vector(location);
		velocity.x = 2.5f;
	}
	public void update() {
		if(this.location.x - this.baseLocation.x > 100) {
			this.velocity.x = -this.velocity.x;
		}
		else if(this.baseLocation.x - this.location.x > 100) {
			this.velocity.x = -this.velocity.x;
		}
		this.applyVelocity();
		this.updateShape();
	}
}
