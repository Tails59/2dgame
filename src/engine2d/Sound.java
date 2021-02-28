package engine2d;

import java.io.*;
import javax.sound.sampled.*;

public class Sound extends Thread {

	String filename;		// The name of the file to play
	boolean finished;		// A flag showing that the thread has finished
	private boolean paused = false; //A flag showing that the audio is paused;
	
	private Clip clip;
	
	private long pos;
	
	/**
	 * 
	 * @param fname
	 * @param startPaused [boolean] Whether the audio should play as soon as its created
	 */
	public Sound(String fname, boolean startPaused) {
		filename = fname;
		finished = false;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void pause() {
		if(paused)
			return;

		pos = clip.getLongFramePosition();
		clip.stop();
	}
	
	public void play() {
		if(!paused)
			return;

		clip.start();
		clip.setFramePosition((int) pos);
	}

	/**
	 * run will play the actual sound but you should not call it directly.
	 * You need to call the 'start' method of your sound object (inherited
	 * from Thread, you do not need to declare your own). 'run' will
	 * eventually be called by 'start' when it has been scheduled by
	 * the process scheduler.
	 */
	public void run() {
		try {
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat	format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			// note to self: doesn't support 24bit audio
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(stream);
			
			if(!paused) {
				clip.start();
			}
			
			Thread.sleep(100);
			while (clip.isRunning()) { Thread.sleep(100); }
			clip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finished = true;
	}
}
