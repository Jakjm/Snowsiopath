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
	public Map() {
		wallList = new LinkedList<Wall>();
		bulletList = new LinkedList<Projectile>();
		projectileQueue = new LinkedList<Projectile>();
	}
	public Map(int mapWidth,int mapHeight) {
		this();
		Wall leftWall = new Wall(new Vector(0,0),50,mapHeight,Color.BLACK);
		wallList.add(leftWall);
		Wall bottomWall = new Wall(new Vector(0,mapHeight-50),mapWidth,50,Color.magenta);
		wallList.add(bottomWall);
		Wall rightWall = new Wall(new Vector(mapWidth,50),50,mapHeight-50,Color.red);
		wallList.add(rightWall);
		Wall topWall = new Wall(new Vector(50,0),mapWidth,50,Color.green);
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
