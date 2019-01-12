package gameObjects;
import java.util.LinkedList;

import geometry.Vector;
public abstract class Map {
	public LinkedList<Wall>wallList;
	public static final double GRAVITY = 1;
	public Map() {
		wallList = new LinkedList<Wall>();
	}
	public Map(int mapWidth,int mapHeight) {
		this();
		Wall leftWall = new Wall(new Vector(0,0),50,mapHeight);
		wallList.add(leftWall);
		Wall bottomWall = new Wall(new Vector(0,mapHeight-50),mapWidth,50);
		wallList.add(bottomWall);
		Wall rightWall = new Wall(new Vector(mapWidth,50),50,mapHeight-50);
		wallList.add(rightWall);
		Wall topWall = new Wall(new Vector(50,0),mapWidth,50);
		wallList.add(topWall);
	}
}
