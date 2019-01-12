package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import geometry.Box;
import geometry.Vector;

public class Wall extends SolidObject{
	private int wallWidth;
	private int wallHeight;
	private Color color;
	public Wall(Vector location,int width,int height) {
		super(location,new Vector(width/2,height/2),new Box(width,height));
		this.wallWidth = width;
		this.wallHeight = height;
		this.color = Color.blue;
		this.updateShape();
	}
	public Wall(Vector location,int width,int height,Color c) {
		this(location,width,height);
		this.color = c;
		this.updateShape();
	}
	public Wall(Vector location,int width,int height,Color c,double angle) {
		this(location,width,height,c);
		this.angle = angle;
		this.updateShape();
	}
	public Wall(Vector location,int width,int height,double angle) {
		this(location,width,height);
		this.angle = angle;
		this.updateShape();
	}
	    

	@Override
	public void drawObject(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0,0,wallWidth,wallHeight);
	}

}
