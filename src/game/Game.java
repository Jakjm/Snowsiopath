package game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import game.backgroundMusic.BackgroundMusic;
import gameObjects.Map;
import gameObjects.MapOne;
import gameObjects.Wall;
import gameObjects.snowsiopath.Shotgun;
import gameObjects.snowsiopath.Player;
import gameObjects.snowsiopath.Projectile;
import gameObjects.snowsiopath.Rock;
import geometry.Vector;

public class Game extends JPanel {
	Map map;
	JFrame gameFrame;
	KeyHandler keyHandler;
	Player player;
	public Vector offset;
	/** The maximum number of times per second the game is drawn **/
	private static final double MAX_FR = 61.00; 
	/** The maximum number of microseconds between draws **/
	private static final double DRAW_DELAY = (Math.pow(10, 6) / MAX_FR);
	/** The font of the game panel **/
	private static final Font FPS_FONT = new Font(Font.SERIF,Font.PLAIN,16);
	/** The current framerate **/
	private double frameRate;
	public static final int SCREEN_SIZE_X = 800;
	public static final int SCREEN_SIZE_Y = 600;
	public BufferedImage gameImage;
	public Graphics2D gameGraphics;
	public ReentrantLock imageLock = new ReentrantLock(true);
	BackgroundMusic gameMusic;
	
	public JPanel backPanel;
	public CardLayout panelLayout = new CardLayout();
	public static final String MENU = "MENU";
	public static final String LOADING = "LOADING"; 
	public static final String GAME = "GAME";
	
	public static void main(String [] args) throws InterruptedException {
		new Game();
	}
	public Game() throws InterruptedException {
		super();
		super.setSize(SCREEN_SIZE_X,SCREEN_SIZE_Y);
		gameFrame = new JFrame("Game");
		
		gameFrame.setResizable(false);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameFrame.setLocation(20, 20);
		
		//Setting up back panel.
		backPanel = new JPanel();
		panelLayout = new CardLayout();
		backPanel.setLayout(panelLayout);
		backPanel.add(new TitlePanel(),MENU);
		backPanel.add(this,GAME);
		backPanel.add(new LoadingPanel(),LOADING);
		gameFrame.setContentPane(backPanel);
		switchToMenu();
		gameFrame.setVisible(true);
		
		keyHandler = new KeyManager();
		gameFrame.addKeyListener(keyHandler);
	}
	public void switchToGame() {
		gameFrame.setSize(SCREEN_SIZE_X,SCREEN_SIZE_Y);
		panelLayout.show(backPanel,GAME);
	}
	public void switchToMenu() {
		gameFrame.setSize(400,200);
		panelLayout.show(backPanel,MENU);
	}
	public void switchToLoading() {
		panelLayout.show(backPanel,LOADING);
		gameFrame.pack();
	}
	public class LoadTask implements Runnable{
		public void run() {
			initGame();
			switchToGame();
			Thread playThread = new Thread(new PlayTask());
			playThread.start();
		}
	}
	public class PlayTask implements Runnable{
		public void run() {
			loop();
		}
	}
	public class TitlePanel extends JPanel implements ActionListener{
		public JLabel title = new JLabel("Snowsiopath");
		public JButton playButton = new JButton("Play");
		public JButton controlsButton = new JButton("Controls");
		public static final int numButtons = 3;
		public TitlePanel() {
			super();
			title.setFont(new Font(Font.SERIF,Font.BOLD,24));
			this.setLayout(new GridLayout(numButtons,1));
			this.add(title);
			title.setHorizontalAlignment(JLabel.CENTER);
			
			this.add(controlsButton);
			controlsButton.addActionListener(this);
			controlsButton.setFocusable(false);			
			this.add(playButton);
			playButton.addActionListener(this);
			playButton.setFocusable(false);
			
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == playButton) {
				switchToLoading();
				Thread loadThread = new Thread(new LoadTask());
				loadThread.start();
			}
			else {
				String msg = "Use the directional keys or WSAD to move\n" 
						+ "Use Space to jump, and Q and E to flip!\n"
						+ "Press F to fire, and R to reload. You can switch weapons with the W key!";
				JOptionPane.showMessageDialog(null,msg);
				
			}
		}
	}
	public class LoadingPanel extends JPanel{
		public JLabel label = new JLabel("Loading Game");
		public LoadingPanel() {
			super();
			this.setLayout(new GridLayout(2,1));
			this.add(label);
			this.add(new JLabel("Just be patient!"));
		}
	}
	public void initGame() {
		gameMusic = new BackgroundMusic("rockSmash.wav","Klaww.wav","Stadium Ruins.wav");
		Rock.loadRocks();
		Shotgun.loadGun();
		map = new MapOne();
		player = new Player(new Vector(400,300));
		offset=new Vector();
		updateOffset();
		gameImage = new BufferedImage(SCREEN_SIZE_X,SCREEN_SIZE_Y,BufferedImage.TYPE_INT_ARGB);
		gameGraphics = (Graphics2D)gameImage.getGraphics();
		setRenderingVals(gameGraphics);
		
	}
	public void loop() {
		gameMusic.play();
		long lastPaintTime = microseconds();
		while(true) {
			long currentTime = microseconds();
			if(currentTime - lastPaintTime >= DRAW_DELAY) {
				updateGame();
				drawGame();
				this.repaint();
				frameRate = 1000000.0 /(currentTime-lastPaintTime);
				lastPaintTime = currentTime;
			}
		}
	}
	public void drawFps(Graphics g) {
		g.setFont(FPS_FONT);
		g.setColor(Color.red);
		String fpsString = String.format("FPS: %.2f",frameRate);
		g.drawString(fpsString,this.getWidth()-90,this.getHeight() - 10);
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
		map.updatePlatforms();
		player.getKeys(keyHandler,map);
		player.update(map);
		map.updateProjectiles();
	}
	public void drawGame() {
		imageLock.lock();
		
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
		
		imageLock.unlock();
	}
	public void paintComponent(Graphics g) {
		imageLock.lock();
		g.drawImage(gameImage,0,0,null);
		imageLock.unlock();
	}
	public class KeyManager extends KeyHandler {
		public void onWKeyPressed() {
			player.cycleWeapon(map);
		}
	}
	
}
