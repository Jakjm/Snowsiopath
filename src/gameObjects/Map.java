package gameObjects;
import java.awt.Color;
import java.util.LinkedList;
import java.util.ListIterator;

import game.KeyHandler;
import gameObjects.snowsiopath.Projectile;
import geometry.Vector;
public abstract class Map {
	public LinkedList<Wall>wallList;
	public LinkedList<Projectile> bulletList; 
	public LinkedList<Projectile> projectileQueue;
	public static final double GRAVITY = 1;
	public static final int WALL_THICKNESS = 90;
	public Map() {
		wallList = new LinkedList<Wall>();
		bulletList = new LinkedList<Projectile>();
		projectileQueue = new LinkedList<Projectile>();
	}
	public Map(int mapWidth,int mapHeight) {
		this();
		Wall leftWall = new Wall(new Vector(0,0),WALL_THICKNESS,mapHeight,Color.BLACK);
		wallList.add(leftWall);
		Wall bottomWall = new Wall(new Vector(0,mapHeight-WALL_THICKNESS),mapWidth,WALL_THICKNESS,Color.magenta);
		wallList.add(bottomWall);
		Wall rightWall = new Wall(new Vector(mapWidth,WALL_THICKNESS),WALL_THICKNESS,mapHeight-WALL_THICKNESS,Color.red);
		wallList.add(rightWall);
		Wall topWall = new Wall(new Vector(WALL_THICKNESS,0),mapWidth,WALL_THICKNESS,Color.green);
		wallList.add(topWall);
	}
	public void updateProjectiles() {
		ListIterator<Projectile> bulletIterator = bulletList.listIterator();
		while(bulletIterator.hasNext())
		{
			Projectile p = bulletIterator.next();
			if(p.updateProjectile(this)) {
				bulletIterator.remove();
			}
		}
		while(projectileQueue.size() > 0) {
			bulletList.add(projectileQueue.remove());
		}
	}
}
