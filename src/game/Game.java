package game;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameObjects.Map;
import gameObjects.MapOne;
import gameObjects.Wall;
import gameObjects.snowsiopath.Player;
import gameObjects.snowsiopath.Projectile;
import geometry.Vector;

public class Game extends JPanel {
	Map map;
	JFrame gameFrame;
	KeyHandler keyHandler;
	Player player;
	public Vector offset;
	/** The maximum number of times per second the game is drawn **/
	private static final double MAX_FR = 60.00; 
	/** The maximum number of microseconds between draws **/
	private static final double DRAW_DELAY = (Math.pow(10, 6) / MAX_FR);
	/** The font of the game panel **/
	private static final Font FPS_FONT = new Font(Font.SERIF,Font.PLAIN,16);
	/** The current framerate **/
	private double frameRate;
	public static final int SCREEN_SIZE_X = 1000;
	public static final int SCREEN_SIZE_Y = 700;
	public BufferedImage gameImage;
	public Graphics2D gameGraphics;
	public volatile boolean drawComplete = true;
	
	public static void main(String [] args) throws InterruptedException {
		new Game();
	}
	public Game() throws InterruptedException {
		super();
		super.setSize(SCREEN_SIZE_X,SCREEN_SIZE_Y);
		gameFrame = new JFrame("Game");
		gameFrame.setSize(SCREEN_SIZE_X,SCREEN_SIZE_Y);
		gameFrame.setResizable(false);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
		gameFrame.setLocation(20, 20);
		initGame();
		
		
		keyHandler = new KeyManager();
		gameFrame.addKeyListener(keyHandler);
		
		gameFrame.setContentPane(this);
		
		loop();
	}
	public void initGame() {
		map = new MapOne();
		player = new Player(new Vector(400,300));
		offset=new Vector();
		updateOffset();
		gameImage = new BufferedImage(SCREEN_SIZE_X,SCREEN_SIZE_Y,BufferedImage.TYPE_INT_ARGB);
		gameGraphics = (Graphics2D)gameImage.getGraphics();
		setRenderingVals(gameGraphics);
	}
	public void loop() {
		long lastPaintTime = microseconds();
		while(true) {
			long currentTime = microseconds();
			if(currentTime - lastPaintTime >= DRAW_DELAY) {
				updateGame();
				this.repaint();
				frameRate = Math.pow(10, 6)/(currentTime-lastPaintTime);
				lastPaintTime = currentTime;
			}
		}
	}
	public void drawFps(Graphics g) {
		g.setFont(FPS_FONT);
		g.setColor(Color.red);
		String fpsString = String.format("FPS: %.2f",frameRate);
		g.drawString(fpsString,this.getWidth()-100,this.getHeight() - 40);
	}
	public static long microseconds() {
		return System.nanoTime() / 1000;
	}
	public void updateOffset() {
		offset.x = player.location.x - SCREEN_SIZE_X / 2 + 30;
		offset.y = player.location.y - SCREEN_SIZE_Y / 2 - 40;
	}
	public void setRenderingVals(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	}
	public void updateGame() {
		player.getKeys(keyHandler,map);
		player.update(map);
		map.updateProjectiles();
		drawGame();
	}
	public void drawGame() {
		drawComplete = false;
		updateOffset();
		gameGraphics.setColor(Color.white);
		gameGraphics.fillRect(0, 0,SCREEN_SIZE_X,SCREEN_SIZE_Y);
		for(Wall w : map.wallList) {
			w.draw(gameGraphics,offset);
		}
		for(Projectile p : map.bulletList) {
			p.draw(gameGraphics,offset);
		}
		player.draw(gameGraphics,offset);
		
		drawFps(gameGraphics);
		drawComplete = true;
	}
	public void paintComponent(Graphics g) {
		if(drawComplete)g.drawImage(gameImage,0,0,null);
	}
	public class KeyManager extends KeyHandler {
		public void onWKeyPressed() {
			player.cycleWeapon();
		}
	}
}
