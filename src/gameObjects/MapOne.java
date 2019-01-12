package gameObjects;

import geometry.Vector;

public class MapOne extends Map{
    public MapOne() {
    	super(1000,1000);
    	this.wallList.add(new Wall(new Vector(400,900),300,10,Math.PI / 6));
    	this.wallList.add(new Wall(new Vector(200,900),300,10,-Math.PI / 6));
    }
}
