package gameObjects;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import geometry.Vector;

public class GameSprite {
    private BufferedImage sprite;
    private BufferedImage reverseSprite;
    public GameSprite(String imagePath) {
        try {
        	sprite = ImageIO.read(GameSprite.class.getResourceAsStream(imagePath));
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
        reverseSprite = new BufferedImage(sprite.getWidth(),sprite.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)reverseSprite.getGraphics();
        g.translate(sprite.getWidth(),0);
        g.scale(-1.0,1.0);
        g.drawImage(sprite,0,0,null);
        g.dispose();
    }
    public Vector getCenter() {
    	return new Vector(sprite.getWidth() / 2,sprite.getHeight() / 2);
    }
    public int getWidth() {
    	return sprite.getWidth();
    }
    public int getHeight() {
    	return sprite.getHeight();
    }
    public void drawReverse(Graphics2D g,int locationX,int locationY) {
    	g.drawImage(reverseSprite,locationX,locationY,null);
    }
    public void draw(Graphics2D g,int locationX,int locationY) {
    	g.drawImage(sprite,locationX,locationY,null);
    }
    public void draw(Graphics2D g) {
    	g.drawImage(sprite,0,0,null);
    }
    public void drawReverse(Graphics2D g) {
    	g.drawImage(reverseSprite,0,0,null);
    }
    public BufferedImage getSprite() {
    	return sprite;
    }
    public BufferedImage getReverseSprite() {
    	return reverseSprite;
    }
}
