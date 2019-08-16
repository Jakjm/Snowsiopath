package game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Class for streaming music within a Java program.
 * Works for .wav formats only.
 * @author jordan
 * @version January 12th 2019
 */
public class MusicPlayer{
	private SourceDataLine line; 
	private AudioInputStream ais;
	private AudioFormat format;
	/**Buffer size in bytes of the program **/
	private int bufferSize; 
	
	
	/**Current playback mode **/
	private int mode;
	/**Mode to play the track only once. **/
	public static final int PLAY_ONCE = 0;
	/**Mode to loop the track over and over. **/
	public static final int LOOP = 1;
	
	
	/**The state of the player**/
	private volatile int state;
	/**The state of the player when not playing anything. **/
	private static final int STOPPED = 0;
	/**The state of the player when playing something **/
	private static final int PLAYING = 1;
	
	/**The rough position of the music player in the track in frames**/
	private volatile long position;
	/**The thread being used during playback**/
	private Thread playThread;
	
	
	/**
	 * Constructs the MusicPlayer using an <b>external</b> file.
	 * @param file - the audio file to be played back.
	 */
	public MusicPlayer(File file) {
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setupMusicPlayer(stream);
	}
	/**
	 * Constructs the MusicPlayer using an <b>internal</b> path.<br>
	 * Note that the relative path starts from the package of this class.
	 * @param path - the path to the audio file to be played back.
	 */
	public MusicPlayer(String path){
		try {
			InputStream stream = new BufferedInputStream(MusicPlayer.class.getResourceAsStream(path));
			setupMusicPlayer(stream);
		}
		catch(Exception e) {
			System.out.println("Broken for" + path);
		}
	}
	private void setupMusicPlayer(InputStream stream){
		
			position = 0;
			state = STOPPED;
			mode = PLAY_ONCE;
			
			try {
				ais = AudioSystem.getAudioInputStream(stream);
			}
			catch(UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			} 
			
			ais.mark(Integer.MAX_VALUE);
			format = ais.getFormat();
			
			//Trying to find an available mixer. 
			Mixer.Info [] mixers = AudioSystem.getMixerInfo();
			for(int i = 0;i < mixers.length;i++) {
				try {
					line = AudioSystem.getSourceDataLine(format,mixers[i]);
					line.open(format);
				}
				catch (LineUnavailableException | IllegalArgumentException e) {
					continue;
				}
				break;
			}
			if(line == null) {
				System.out.println("Broken! " + format);
			}
			bufferSize = line.getBufferSize();
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	/**
	 * Skips the given time in minutes and seconds within the track.
	 * @param minutes - the minutes within the track.
	 * @param seconds - the seconds within the track.
	 */
	public void skip(int minutes,int seconds) {
		long byteSkip = (long)((seconds + minutes * 60) * format.getFrameRate() * format.getFrameSize());
		if(byteSkip >= ais.getFrameLength()*format.getFrameSize()) {
			throw new IllegalArgumentException("Time exceeds length of track.");
		}
		position += byteSkip / format.getFrameSize();
		try {
			ais.skip(byteSkip);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Skips the amount of time specified by the given string.
	 * Does nothing if the amount of time exceeds the length of the track.
	 * @param time
	 */
	public void skip(String time) {
		int colonPosition = time.indexOf(':');
		if(colonPosition == -1) {
			throw new IllegalArgumentException("Time formatted improperly.");
		}
		int mins = 0;
		int seconds = 0;
		try {
	    mins = Integer.parseInt(time.substring(0,colonPosition));
		seconds = Integer.parseInt(time.substring(colonPosition+1,time.length()));
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
		}
		skip(mins,seconds);
	}
	/**
	 * Stops the music player if it is still playing,
	 * then resets the music player to begin from the beginning of the track.
	 */
	public void reset() {
		if(state == PLAYING) {
			stop();
		}
		line.flush();
		try {
			ais.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		position = 0;
	}
	/**
	 * Stops the playback of the music player.
	 */
	public void stop() {
		state = STOPPED;
		line.stop();
	}
	
	/**
	 * Begins playback of the music player.
	 */
	public void play() {
		if(state == PLAYING)return;
		line.start();
		state = PLAYING;
		
		if(playThread == null || playThread.getState() == Thread.State.TERMINATED) {
			//Thread to pump the byte data from the audio input stream into the source data line.
			playThread = new Thread(new MusicTask());
			playThread.start();
		}
		
	}
	/**
	 * Task for the MusicPlayer's thread that is used to load data during
	 * playback and facilitate looping/stop and start.
	 * @author jordan
	 */
	private class MusicTask implements Runnable{
		/**
		 * Run method for the Music Player's thread.
		 */
		@Override
		public void run() {
			byte [] musicBuffer = new byte[bufferSize];
			while(true) {
				if(state != PLAYING) {
					line.drain();
					return;
				}
				try {
					int bytesRead = ais.read(musicBuffer,0,bufferSize);
					if(bytesRead > 0) {
						int bytesWritten = line.write(musicBuffer,0,bytesRead);
						position += bytesWritten / format.getFrameSize();
					}
					else {
						if(mode == PLAY_ONCE) {
							line.drain();
							stop();
							ais.reset();
							return;
						}
						else if(mode == LOOP) {
							ais.reset();
							continue;
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isPlaying() {
		return this.state == MusicPlayer.PLAYING;
	}
	public long framePosition() {
		return position % ais.getFrameLength();
	}
	public String currentTrackPosition() {
		return framesToTime(framePosition());
	}
	/**
	 * Converts the number of frames specified into a string time.
	 * @param frames - the number of frames to be converted into time.
	 * @return time, a string formatted into minutes:seconds.
	 */
	public String framesToTime(long frames) {
		long length = frames / (int)format.getFrameRate();
		int minutes = (int)(length / 60);
		int seconds = (int) (length % 60);
		return String.format("%d:%02d",minutes,seconds);
	}
	/**
	 * @return the length of the track being played.
	 */
	public String getTrackLength() {
		return framesToTime(ais.getFrameLength());
	}
	
	/**
	 * For loading sound effects in advance.
	 * @author jordan
	 */
	public static class BufferedSFX {
		MusicPlayer [] effects;
		private int clipNumber;
		public BufferedSFX(String path,int numClips) {
			clipNumber = 0;
			effects = new MusicPlayer[numClips];
			for(int i = 0;i < effects.length;i++) {
				effects[i] = new MusicPlayer(path);
			}
		}
		public void play() {
			effects[clipNumber].play();
			clipNumber++;
			if(clipNumber == effects.length)clipNumber = 0;
		}
	}
}
