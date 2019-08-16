package gameObjects;

import geometry.Vector;

public class MapOne extends Map{
    public MapOne() {
    	super(4000,1000);
    	this.wallList.add(0,new Wall(new Vector(1000,850),400,10,Math.PI / 10));
    	this.wallList.add(0,new Wall(new Vector(800,850),400,10,-Math.PI / 10));
    	this.wallList.add(0,new Wall(new Vector(1000,800),200,10));
    	
    }
}
