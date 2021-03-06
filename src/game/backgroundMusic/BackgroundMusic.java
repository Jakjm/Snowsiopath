package game.backgroundMusic;
import game.MusicPlayer;

/**
 * Class for managing concurrent background music during runtime. 
 * @author jordan
 */
public class BackgroundMusic{
	MusicPlayer player;
	String [] setList;
	int currentTrack;
	Thread runThread;
	
	private volatile boolean playing;
	public BackgroundMusic(String ... backgroundSounds) {
		this.setList = backgroundSounds;
		currentTrack = 0;
	}
	public void stop() {
		playing = false;
	}
	public void play() {
		if(playing == true) {
			return;
		}
		playing = true;
		runThread = new Thread(new BGMTask());
		runThread.start();
	}
	private class BGMTask implements Runnable{
		public void run() {
			if(player == null)player = new MusicPlayer(setList[currentTrack]);
			player.play();
			while(playing) {
				//If we should stop, stop playing. 
				if(!player.isPlaying()) {
					currentTrack++;
					if(currentTrack == setList.length)currentTrack = 0;
					player = new MusicPlayer(setList[currentTrack]);
					player.play();
				}
			}
			player.stop();
		}
	}
}
