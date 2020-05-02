package gameObjects;

import java.awt.Color;

import geometry.Vector;

public class MapOne extends Map{
    public MapOne() {
    	super(4000,1000);
    	this.wallList.add(0,new Wall(new Vector(1000,850),400,10,Math.PI / 10));
    	this.wallList.add(0,new Wall(new Vector(800,850),400,10,-Math.PI / 10));
    	this.wallList.add(0,new Wall(new Vector(1000,800),200,10));
    	this.movingPlatforms.add(new MovingPlatform(new Vector(315,600),200,10,Color.red));
    	this.movingPlatforms.add(new MovingPlatform(new Vector(465,650),200,10,Color.blue));
    	this.movingPlatforms.add(new MovingPlatform(new Vector(615,700),200,10,Color.green));
    	this.wallList.addAll(this.movingPlatforms);
    }
}
